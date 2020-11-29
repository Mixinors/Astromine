/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.layer.Layer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Layer.class)
public class BiomeLayerSamplerMixin {
	@Unique
	private Registry<Biome> registry;
	@Unique
	private int storedLastBiomeId;

	@Inject(method = "get", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/worldgen/biome/Biomes;byId(I)Lnet/minecraft/resources/ResourceKey;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private void astromine_storeVariables(Registry<Biome> registry, int i, int j, CallbackInfoReturnable<Biome> cir, int k) {
		this.registry = registry;
		storedLastBiomeId = k;
	}

	@ModifyVariable(method = "get", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/worldgen/biome/Biomes;byId(I)Lnet/minecraft/resources/ResourceKey;", ordinal = 0, shift = At.Shift.BY, by = 2), ordinal = 0)
	private ResourceKey<Biome> modifyBiome(ResourceKey<Biome> original) {
		if (original != null)
			return original;
		Biome biome = registry.byId(storedLastBiomeId);
		if (biome == null)
			return original;
		return registry.getResourceKey(biome).filter(key -> key.location().getNamespace().equals(AstromineCommon.MOD_ID)).orElse(original);
	}

	@Inject(method = "get", at = @At("RETURN"))
	private void astromine_removeStoredVariables(Registry<Biome> registry, int i, int j, CallbackInfoReturnable<Biome> cir) {
		this.registry = null;
	}
}
