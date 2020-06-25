package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import com.github.chainmailstudios.astromine.common.world.generation.AstromineBiomeSource;
import com.github.chainmailstudios.astromine.common.world.generation.AstromineChunkGenerator;
import com.github.chainmailstudios.astromine.registry.AstromineDimensionTypes;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
	@Inject(method = "method_28517", at = @At("RETURN"), cancellable = true)
	private static void method_28517(long seed, CallbackInfoReturnable<SimpleRegistry<DimensionOptions>> cir) {
		SimpleRegistry<DimensionOptions> registry = cir.getReturnValue();
		registry.add(AstromineDimensionTypes.SPACE_OPTIONS, new DimensionOptions(() -> AstromineDimensionTypes.INSTANCE, new AstromineChunkGenerator(new AstromineBiomeSource(seed), seed)));
		cir.setReturnValue(registry);
	}
}