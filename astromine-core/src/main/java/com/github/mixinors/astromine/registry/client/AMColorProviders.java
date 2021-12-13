package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.registry.common.AMFluids;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class AMColorProviders {
	public static void init() {
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> AMFluids.OIL.getTintColor(), AMFluids.OIL.getCauldron());
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> AMFluids.FUEL.getTintColor(), AMFluids.FUEL.getCauldron());
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> AMFluids.BIOMASS.getTintColor(), AMFluids.BIOMASS.getCauldron());
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> AMFluids.OXYGEN.getTintColor(), AMFluids.OXYGEN.getCauldron());
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> AMFluids.HYDROGEN.getTintColor(), AMFluids.HYDROGEN.getCauldron());
	}
}
