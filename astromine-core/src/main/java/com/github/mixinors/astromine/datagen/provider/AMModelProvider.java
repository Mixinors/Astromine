package com.github.mixinors.astromine.datagen.provider;

import java.util.Optional;
import java.util.Set;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.base.BlockWithEntity;
import com.github.mixinors.astromine.common.fluid.ExtendedFluid;
import com.github.mixinors.astromine.datagen.family.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.MaterialFamily;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMFluids;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.*;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockStateDefinitionProvider;

public class AMModelProvider extends FabricBlockStateDefinitionProvider {
	/**
	 * Blocks with a single cube model, with the same texture on all sides
	 */
	public static final Set<Block> SIMPLE_CUBE_ALL = Set.of(
			AMBlocks.BLAZING_ASTEROID_STONE.get()
	);

	/**
	 * Blocks with a single model, where the block model itself isn't data generated
	 */
	public static final Set<Block> SIMPLE_STATE = Set.of(
			AMBlocks.SPACE_SLIME_BLOCK.get(),
			AMBlocks.ALTAR.get(),
			AMBlocks.ALTAR_PEDESTAL.get()
	);

	/**
	 * Blocks with a single model, where neither the block model nor the item model are data generated
	 */
	public static final Set<Block> JUST_STATE = Set.of(
			AMBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.get()
	);

	public static final Set<Block> CUBE_BOTTOM_TOP = Set.of(
			AMBlocks.NUCLEAR_WARHEAD.get()
	);

	public static final Set<ExtendedFluid> FLUIDS = Set.of(
			AMFluids.OIL,
			AMFluids.FUEL,
			AMFluids.BIOMASS,
			AMFluids.OXYGEN,
			AMFluids.HYDROGEN
	);

	public static final Set<Block> MACHINES = Set.of(
			AMBlocks.PRIMITIVE_TANK.get(),
			AMBlocks.BASIC_TANK.get(),
			AMBlocks.ADVANCED_TANK.get(),
			AMBlocks.ELITE_TANK.get(),
			AMBlocks.CREATIVE_TANK.get(),

			AMBlocks.PRIMITIVE_SOLID_GENERATOR.get(),
			AMBlocks.BASIC_SOLID_GENERATOR.get(),
			AMBlocks.ADVANCED_SOLID_GENERATOR.get(),
			AMBlocks.ELITE_SOLID_GENERATOR.get(),

			AMBlocks.PRIMITIVE_LIQUID_GENERATOR.get(),
			AMBlocks.BASIC_LIQUID_GENERATOR.get(),
			AMBlocks.ADVANCED_LIQUID_GENERATOR.get(),
			AMBlocks.ELITE_LIQUID_GENERATOR.get(),

			AMBlocks.PRIMITIVE_ELECTRIC_FURNACE.get(),
			AMBlocks.BASIC_ELECTRIC_FURNACE.get(),
			AMBlocks.ADVANCED_ELECTRIC_FURNACE.get(),
			AMBlocks.ELITE_ELECTRIC_FURNACE.get(),

			AMBlocks.PRIMITIVE_ALLOY_SMELTER.get(),
			AMBlocks.BASIC_ALLOY_SMELTER.get(),
			AMBlocks.ADVANCED_ALLOY_SMELTER.get(),
			AMBlocks.ELITE_ALLOY_SMELTER.get(),

			AMBlocks.PRIMITIVE_TRITURATOR.get(),
			AMBlocks.BASIC_TRITURATOR.get(),
			AMBlocks.ADVANCED_TRITURATOR.get(),
			AMBlocks.ELITE_TRITURATOR.get(),

			AMBlocks.PRIMITIVE_PRESSER.get(),
			AMBlocks.BASIC_PRESSER.get(),
			AMBlocks.ADVANCED_PRESSER.get(),
			AMBlocks.ELITE_PRESSER.get(),

			AMBlocks.PRIMITIVE_WIREMILL.get(),
			AMBlocks.BASIC_WIREMILL.get(),
			AMBlocks.ADVANCED_WIREMILL.get(),
			AMBlocks.ELITE_WIREMILL.get(),

			AMBlocks.PRIMITIVE_ELECTROLYZER.get(),
			AMBlocks.BASIC_ELECTROLYZER.get(),
			AMBlocks.ADVANCED_ELECTROLYZER.get(),
			AMBlocks.ELITE_ELECTROLYZER.get(),

			AMBlocks.PRIMITIVE_REFINERY.get(),
			AMBlocks.BASIC_REFINERY.get(),
			AMBlocks.ADVANCED_REFINERY.get(),
			AMBlocks.ELITE_REFINERY.get(),

			AMBlocks.PRIMITIVE_FLUID_MIXER.get(),
			AMBlocks.BASIC_FLUID_MIXER.get(),
			AMBlocks.ADVANCED_FLUID_MIXER.get(),
			AMBlocks.ELITE_FLUID_MIXER.get(),

			AMBlocks.PRIMITIVE_SOLIDIFIER.get(),
			AMBlocks.BASIC_SOLIDIFIER.get(),
			AMBlocks.ADVANCED_SOLIDIFIER.get(),
			AMBlocks.ELITE_SOLIDIFIER.get(),

			AMBlocks.PRIMITIVE_MELTER.get(),
			AMBlocks.BASIC_MELTER.get(),
			AMBlocks.ADVANCED_MELTER.get(),
			AMBlocks.ELITE_MELTER.get(),

			AMBlocks.PRIMITIVE_BUFFER.get(),
			AMBlocks.BASIC_BUFFER.get(),
			AMBlocks.ADVANCED_BUFFER.get(),
			AMBlocks.ELITE_BUFFER.get(),
			AMBlocks.CREATIVE_BUFFER.get(),

			AMBlocks.PRIMITIVE_CAPACITOR.get(),
			AMBlocks.BASIC_CAPACITOR.get(),
			AMBlocks.ADVANCED_CAPACITOR.get(),
			AMBlocks.ELITE_CAPACITOR.get(),
			AMBlocks.CREATIVE_CAPACITOR.get(),

			AMBlocks.VENT.get(),

			AMBlocks.FLUID_EXTRACTOR.get(),
			AMBlocks.FLUID_INSERTER.get(),

			AMBlocks.BLOCK_BREAKER.get(),
			AMBlocks.BLOCK_PLACER.get()
	);

	public static final TextureKey LEFT = TextureKey.of("left", TextureKey.EAST);
	public static final TextureKey RIGHT = TextureKey.of("right", TextureKey.WEST);

	public static final Model MACHINE = blockModel(AMCommon.id("machine"), TextureKey.TOP, TextureKey.BOTTOM, LEFT, RIGHT, TextureKey.FRONT, TextureKey.BACK);
	public static final Model MACHINE_ACTIVE = blockModel(AMCommon.id("machine_active"), "_active", TextureKey.TOP, TextureKey.BOTTOM, LEFT, RIGHT, TextureKey.FRONT, TextureKey.BACK);

	public AMModelProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	public static void registerCubeColumn(BlockStateModelGenerator blockStateModelGenerator, Block cubeColumn, Block endTexture) {
		Texture texture = Texture.sideEnd(Texture.getId(cubeColumn), Texture.getId(endTexture));
		Identifier identifier = Models.CUBE_COLUMN.upload(cubeColumn, texture, blockStateModelGenerator.modelCollector);
		blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(cubeColumn, identifier));
	}

	public static void registerAsteroidOre(BlockStateModelGenerator blockStateModelGenerator, Block asteroidOre) {
		registerCubeColumn(blockStateModelGenerator, asteroidOre, AMBlocks.ASTEROID_STONE.get());
	}

	public static void registerMeteorOre(BlockStateModelGenerator blockStateModelGenerator, Block meteorOre) {
		registerCubeColumn(blockStateModelGenerator, meteorOre, AMBlocks.METEOR_STONE.get());
	}

	public static void registerCauldron(BlockStateModelGenerator blockStateModelGenerator, Block cauldron) {
		blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(cauldron, Models.TEMPLATE_CAULDRON_FULL.upload(cauldron, Texture.cauldron(Texture.getSubId(Blocks.WATER, "_still")), blockStateModelGenerator.modelCollector)));
	}

	public static Model model(Identifier parent, String variant, TextureKey... requiredTextures) {
		return new Model(Optional.of(parent), Optional.of(variant), requiredTextures);
	}

	public static Model model(Identifier parent, TextureKey... requiredTextures) {
		return new Model(Optional.of(parent), Optional.empty(), requiredTextures);
	}

	private static Model blockModel(Identifier parent, TextureKey... requiredTextures) {
		return model(getBlockFolderId(parent), requiredTextures);
	}

	private static Model blockModel(Identifier parent, String variant, TextureKey... requiredTextures) {
		return model(getBlockFolderId(parent), variant, requiredTextures);
	}

	private static Model itemModel(Identifier parent, TextureKey... requiredTextures) {
		return model(getItemFolderId(parent), requiredTextures);
	}

	public static Identifier getBlockFolderId(Identifier id) {
		return new Identifier(id.getNamespace(), "block/" + id.getPath());
	}

	public static Identifier getItemFolderId(Identifier id) {
		return new Identifier(id.getNamespace(), "item/" + id.getPath());
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		BlockFamilies.getFamilies().filter(AMBlockFamilies::isAstromineFamily).filter(BlockFamily::shouldGenerateModels).forEach(family -> {
			blockStateModelGenerator.registerCubeAllModelTexturePool(family.getBaseBlock()).family(family);
			blockStateModelGenerator.registerParentedItemModel(family.getBaseBlock(), ModelIds.getBlockModelId(family.getBaseBlock()));
		});

		SIMPLE_CUBE_ALL.forEach((block) -> {
			blockStateModelGenerator.registerSimpleCubeAll(block);
			blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getBlockModelId(block));
		});

		SIMPLE_STATE.forEach((block) -> {
			blockStateModelGenerator.registerSimpleState(block);
			blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getBlockModelId(block));
		});

		JUST_STATE.forEach(blockStateModelGenerator::registerSimpleState);

		CUBE_BOTTOM_TOP.forEach((block) -> {
			blockStateModelGenerator.registerSingleton(block, TexturedModel.CUBE_BOTTOM_TOP);
			blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getBlockModelId(block));
		});

		FLUIDS.forEach((fluid) -> {
			blockStateModelGenerator.registerStateWithModelReference(fluid.getBlock(), Blocks.WATER);
			registerCauldron(blockStateModelGenerator, fluid.getCauldron());
		});

		MACHINES.forEach((block) -> {
			registerMachine(blockStateModelGenerator, block);
			blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getBlockModelId(block));
		});

		MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateModels).forEach((family) -> {
			family.getBlockVariants().forEach(((variant, block) -> {
				if (family.shouldGenerateModel(variant)) {
					variant.getModelRegistrar().accept(blockStateModelGenerator, block);
					blockStateModelGenerator.registerParentedItemModel(block, ModelIds.getBlockModelId(block));
				}
			}));
		});
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateModels).forEach((family) -> {
			family.getItemVariants().forEach(((variant, item) -> {
				if (family.shouldGenerateModel(variant)) variant.getModelRegistrar().accept(itemModelGenerator, item);
			}));
		});

		FLUIDS.forEach((fluid) -> {
			itemModelGenerator.register(fluid.getBucketItem(), Models.GENERATED);
		});
	}

	public final void registerMachine(BlockStateModelGenerator blockStateModelGenerator, Block machine) {
		Texture inactiveTexture = new Texture().put(TextureKey.TOP, Texture.getSubId(machine, "_top")).put(TextureKey.BOTTOM, Texture.getSubId(machine, "_bottom")).put(LEFT, Texture.getSubId(machine, "_left")).put(RIGHT, Texture.getSubId(machine, "_right")).put(TextureKey.FRONT, Texture.getSubId(machine, "_front")).put(TextureKey.BACK, Texture.getSubId(machine, "_back"));
		Texture activeTexture = new Texture().put(TextureKey.TOP, Texture.getSubId(machine, "_top_active")).put(TextureKey.BOTTOM, Texture.getSubId(machine, "_bottom_active")).put(LEFT, Texture.getSubId(machine, "_left_active")).put(RIGHT, Texture.getSubId(machine, "_right_active")).put(TextureKey.FRONT, Texture.getSubId(machine, "_front_active")).put(TextureKey.BACK, Texture.getSubId(machine, "_back_active"));
		Identifier inactiveId = MACHINE.upload(machine, inactiveTexture, blockStateModelGenerator.modelCollector);
		Identifier activeId = MACHINE_ACTIVE.upload(machine, activeTexture, blockStateModelGenerator.modelCollector);

		BlockStateVariant inactiveVariant = BlockStateVariant.create().put(VariantSettings.MODEL, inactiveId);
		BlockStateVariant activeVariant = BlockStateVariant.create().put(VariantSettings.MODEL, activeId);
		BlockStateVariant northVariant = BlockStateVariant.create();
		BlockStateVariant eastVariant = BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R90);
		BlockStateVariant southVariant = BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R180);
		BlockStateVariant westVariant = BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R270);

		blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(machine).coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, BlockWithEntity.ACTIVE)
				.register((facing, active) -> {
					BlockStateVariant facingVariant = switch (facing) {
						case EAST -> eastVariant;
						case SOUTH -> southVariant;
						case WEST -> westVariant;
						default -> northVariant;
					};
					return BlockStateVariant.union(facingVariant, active ? activeVariant : inactiveVariant);
				})));
	}
}
