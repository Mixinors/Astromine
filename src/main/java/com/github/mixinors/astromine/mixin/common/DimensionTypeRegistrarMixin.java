package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.registry.common.AMDimensionTypes;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypeRegistrar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionTypeRegistrar.class)
public class DimensionTypeRegistrarMixin {
	@Inject(at = @At("HEAD"), method = "initAndGetDefault")
	private static void astromine$initAndGetDefault(Registry<DimensionType> registry, CallbackInfoReturnable<RegistryEntry<DimensionType>> cir) {
		BuiltinRegistries.add(registry, AMWorlds.ROCKET_INTERIORS_DIMENSION_TYPE_KEY, AMDimensionTypes.ROCKET_INTERIORS);
		BuiltinRegistries.add(registry, AMWorlds.MOON_DIMENSION_TYPE_KEY, AMDimensionTypes.MOON);
		BuiltinRegistries.add(registry, AMWorlds.EARTH_ORBIT_DIMENSION_TYPE_KEY, AMDimensionTypes.EARTH_ORBIT);
	}
}
