package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.world.generation.BiomeRegistryCallback;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DynamicRegistryManager.class)
public class DynamicRegistryManagerMixin {
	@Inject(method = "create", at = @At("RETURN"))
	private static void method_31141(CallbackInfoReturnable<DynamicRegistryManager.Impl> cir) {
		BiomeRegistryCallback invoker = BiomeRegistryCallback.EVENT.invoker();

		MutableRegistry<Biome> biomes = cir.getReturnValue().get(Registry.BIOME_KEY);
		biomes.forEach(biome -> invoker.accept(cir.getReturnValue(), biome));
	}
}
