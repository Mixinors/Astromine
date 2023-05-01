package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.registry.common.AMDimensionOptions;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.WorldPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldPreset.class)
public class WorldPresetMixin {
	@Inject(at = @At("HEAD"), method = "createDimensionOptionsRegistry")
	private void astromine$createDimensionOptionsRegistry(CallbackInfoReturnable<Registry<DimensionOptions>> cir) {
		var registry = (SimpleRegistry<DimensionOptions>) cir.getReturnValue();
		
		registry.add(AMWorlds.ROCKET_INTERIORS_DIMENSION_OPTIONS_KEY, AMDimensionOptions.ROCKET_INTERIORS, Lifecycle.stable());
		registry.add(AMWorlds.MOON_DIMENSION_OPTIONS_KEY, AMDimensionOptions.MOON, Lifecycle.stable());
		registry.add(AMWorlds.EARTH_ORBIT_DIMENSION_OPTIONS_KEY, AMDimensionOptions.EARTH_ORBIT, Lifecycle.stable());
		
	}
}
