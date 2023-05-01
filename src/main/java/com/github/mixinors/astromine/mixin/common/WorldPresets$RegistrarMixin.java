package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.registry.common.AMDimensionOptions;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(WorldPresets.Registrar.class)
public class WorldPresets$RegistrarMixin {
	@Inject(method = "createPreset", at = @At("RETURN"))
	private void astromine$addAstromineDimensions(DimensionOptions dimensionOptions, CallbackInfoReturnable<WorldPreset> cir) {
		var preset = cir.getReturnValue();
		preset.dimensions = new HashMap<>(preset.dimensions);
		
		preset.dimensions.put(AMWorlds.ROCKET_INTERIORS_DIMENSION_OPTIONS_KEY, AMDimensionOptions.ROCKET_INTERIORS);
		preset.dimensions.put(AMWorlds.MOON_DIMENSION_OPTIONS_KEY, AMDimensionOptions.MOON);
		preset.dimensions.put(AMWorlds.EARTH_ORBIT_DIMENSION_OPTIONS_KEY, AMDimensionOptions.EARTH_ORBIT);
	}
}
