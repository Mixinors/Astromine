package com.github.chainmailstudios.astromine.mixin;

import java.util.LinkedHashMap;

import com.github.chainmailstudios.astromine.world.AstromineDimensionType;
import com.github.chainmailstudios.astromine.world.gen.AstromineBiomeSource;
import com.github.chainmailstudios.astromine.world.gen.AstromineChunkGenerator;
import com.mojang.datafixers.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
	@Inject(method = "method_28517", at = @At("RETURN"), cancellable = true)
	private static void method_28517(long seed, CallbackInfoReturnable<SimpleRegistry<DimensionOptions>> cir) {
		SimpleRegistry<DimensionOptions> registry = cir.getReturnValue();
		registry.add(AstromineDimensionType.OPTIONS, new DimensionOptions(() -> AstromineDimensionType.INSTANCE, new AstromineChunkGenerator(new AstromineBiomeSource(seed), seed)));
		cir.setReturnValue(registry);
	}
}