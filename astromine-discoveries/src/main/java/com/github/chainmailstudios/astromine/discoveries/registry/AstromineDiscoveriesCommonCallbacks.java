package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.common.callback.ServerChunkManagerCallback;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.mars.MarsChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.moon.MoonChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.space.EarthSpaceChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.vulcan.VulcanChunkGenerator;
import com.github.chainmailstudios.astromine.registry.AstromineCommonCallbacks;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;

import net.minecraft.server.world.ServerWorld;

public class AstromineDiscoveriesCommonCallbacks extends AstromineCommonCallbacks {
	public static void initialize() {
		ServerChunkManagerCallback.EVENT.register(manager -> {
			if (manager.chunkGenerator instanceof EarthSpaceChunkGenerator) {
				manager.chunkGenerator = ((EarthSpaceChunkGenerator) manager.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			}
		});

		ServerChunkManagerCallback.EVENT.register(manager -> {
			if (manager.chunkGenerator instanceof MoonChunkGenerator) {
				manager.chunkGenerator = ((MoonChunkGenerator) manager.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			}
		});

		ServerChunkManagerCallback.EVENT.register(manager -> {
			if (manager.chunkGenerator instanceof MarsChunkGenerator) {
				manager.chunkGenerator = ((MarsChunkGenerator) manager.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			}
		});

		ServerChunkManagerCallback.EVENT.register(manager -> {
			if (manager.chunkGenerator instanceof VulcanChunkGenerator) {
				manager.chunkGenerator = ((VulcanChunkGenerator) manager.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			}
		});

		ItemComponentCallbackV2.register(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, AstromineDiscoveriesItems.SPACE_SUIT_CHESTPLATE, (useless, stack) -> {
			FluidInventoryComponent component = new SimpleFluidInventoryComponent(1);
			component.getVolume(0).setSize(Fraction.ofWhole(64));
			return component;
		});
	}
}
