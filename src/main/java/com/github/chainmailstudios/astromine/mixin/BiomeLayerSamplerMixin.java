package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BiomeLayerSampler.class)
public class BiomeLayerSamplerMixin {
	@Unique
	private Registry<Biome> registry;
	@Unique
	private int storedLastBiomeId;

	@Inject(method = "sample",
	        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biomes;fromRawId(I)Lnet/minecraft/util/registry/RegistryKey;", ordinal = 0),
	        locals = LocalCapture.CAPTURE_FAILHARD)
	private void storeVariables(Registry<Biome> registry, int i, int j, CallbackInfoReturnable<Biome> cir, int k) {
		this.registry = registry;
		storedLastBiomeId = k;
	}

	@ModifyVariable(method = "sample",
	                at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biomes;fromRawId(I)Lnet/minecraft/util/registry/RegistryKey;", ordinal = 0,
	                         shift = At.Shift.BY, by = 2), ordinal = 0)
	private RegistryKey<Biome> modifyBiome(RegistryKey<Biome> original) {
		if (original != null)
			return original;
		Biome biome = registry.get(storedLastBiomeId);
		if (biome == null)
			return original;
		return registry.getKey(biome).filter(key -> key.getValue().getNamespace().equals(AstromineCommon.MOD_ID)).orElse(original);
	}

	@Inject(method = "sample", at = @At("RETURN"))
	private void removeStoredVariables(Registry<Biome> registry, int i, int j, CallbackInfoReturnable<Biome> cir) {
		this.registry = null;
	}
}
