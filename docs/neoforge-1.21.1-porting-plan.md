# Astromine NeoForge 1.21.1 Porting Plan

## Verified Platform

- Target Minecraft: `1.21.1`
- Target loader/API: NeoForge `21.1.233`, artifact `net.neoforged:neoforge:21.1.233`
- Java target: 21
- Mappings: Mojang official mappings through the NeoForge/NeoGradle userdev toolchain
- Build plugin: `net.neoforged.gradle.userdev`

NeoForge 1.21.1 uses the NeoForge package/API surface (`net.neoforged.*`) and the post-1.20.2 registration, capability, and custom payload networking APIs.

## Current Fabric Surface

- Loader/build: `fabric-loom`, Yarn mappings, `fabric.mod.json`, access widener.
- Entrypoints: `AMCommon`, `AMClient`, `AMDedicated`.
- Registries: Architectury registries plus direct Fabric/vanilla `Registry.register` calls.
- Runtime APIs: Fabric events, Fabric networking, Fabric Transfer API, Team Reborn Energy, Cardinal Components, Fabric object builders, Fabric render/model hooks.
- UI/integration: Hammer GUI, REI Fabric, ModMenu, Patchouli Fabric.
- Data generation: Fabric datagen entrypoint.
- Worldgen: 1.19.2 in-code biome/structure registrations plus JSON worldgen resources.

## NeoForge Replacement Map

- Metadata: replace `fabric.mod.json` with `META-INF/neoforge.mods.toml` and `pack.mcmeta`.
- Entrypoints: replace Fabric initializers with `@Mod(AMCommon.MOD_ID)` constructor and mod/game event bus registration.
- Registries: replace Architectury/direct registration with `DeferredRegister`, `DeferredHolder`, and specialized `DeferredRegister.Blocks` / `DeferredRegister.Items` where useful.
- Blocks/items/entities/block entities/menus/recipe serializers/sounds/particles: use Forge registries and `RegisterEvent` only where `DeferredRegister` is not appropriate.
- Creative tabs: use `CreativeModeTab` registry and/or `BuildCreativeModeTabContentsEvent`.
- Events: replace Fabric/Architectury events with mod bus and `NeoForge.EVENT_BUS`.
- Networking: replace Architectury/Fabric networking with `CustomPacketPayload`, `StreamCodec`, and `RegisterPayloadHandlersEvent`.
- Menus/screens: use `MenuType`, `IForgeMenuType` when extra open data is required, and `MenuScreens.register`.
- Capabilities: expose item/fluid/energy through NeoForge `BlockCapability`, `ItemCapability`, `IItemHandler`, `IFluidHandler`, and `IEnergyStorage`.
- Fluids: replace Fabric fluid registration/rendering/transfer assumptions with Forge fluid types, source/flowing fluids, buckets, and `FluidStack`.
- Data generation: use `GatherDataEvent` providers and `runData`.
- GameTests: add `gameTestServer` run config and GameTest classes under the `astromine` namespace.
- Access widening: convert the access widener to access transformers or remove entries by using public NeoForge hooks.

## Migration Risks

- Yarn-to-MojMap rename volume is large and affects almost every source file.
- Hammer GUI/Gravity and other Fabric-only dependencies may need removal, replacement, or separate NeoForge-compatible artifacts before compilation can complete.
- Cardinal Components usages need design replacement: saved data, entity capabilities/attachments, or explicit Astromine managers depending on ownership.
- Existing transfer wrappers should not be preserved as compatibility shims unless they still simplify machine/logistics logic after converting to NeoForge capabilities.
- 1.21.1 worldgen dynamic registries should be data-driven; in-code biome/structure registration from 1.19.2 is a likely hard blocker.
- 1.21.1 recipes, tags, loot, data components, and item data must be audited because several 1.20+ data formats changed after 1.19.2.

## Implementation Slices

1. Replace build, metadata, run configs, and generated mod metadata.
2. Convert entrypoints and basic registries to NeoForge while leaving internals untouched.
3. Remap source from Yarn names to MojMap names and fix 1.19.2 -> 1.21.1 vanilla API drift.
4. Port menus/screens/network packets.
5. Replace Fabric Transfer and Team Reborn Energy with NeoForge capabilities.
6. Replace Cardinal Components and Fabric events with NeoForge-native state/events.
7. Port fluids, fluid blocks, buckets, rendering, and fluid recipes.
8. Port worldgen, dimensions, entities, and mixins/access transformers.
9. Port data generation and generated resource conventions.
10. Add and run GameTests for machines, storage, transfer, and loading sanity.
