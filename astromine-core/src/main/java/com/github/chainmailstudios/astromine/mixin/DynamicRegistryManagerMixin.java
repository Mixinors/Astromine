package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.world.generation.BiomeRegistryCallback;
import com.github.chainmailstudios.astromine.common.world.generation.EarlyFish;
import com.google.common.collect.Sets;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(DynamicRegistryManager.class)
public class DynamicRegistryManagerMixin {
	static {
		FabricLoader.getInstance().getEntrypoints("early_fish", EarlyFish.class).forEach(EarlyFish::onEarlyFish);
	}

	@Inject(method = "create", at = @At("RETURN"))
	private static void create(CallbackInfoReturnable<DynamicRegistryManager.Impl> cir) {
		BiomeRegistryCallback invoker = BiomeRegistryCallback.EVENT.invoker();

		MutableRegistry<Biome> biomes = cir.getReturnValue().get(Registry.BIOME_KEY);
		biomes.getEntries().forEach(entry -> invoker.accept(cir.getReturnValue(), entry.getKey(), entry.getValue()));
	}

	@Unique Set<Identifier> storedLoaded;

	@Inject(method = "load(Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Lnet/minecraft/util/dynamic/RegistryOps;)V", at = @At("HEAD"))
	private static void beforeLoad(DynamicRegistryManager.Impl impl, RegistryOps<?> registryOps, CallbackInfo ci) {
		((DynamicRegistryManagerMixin) (Object) impl).storedLoaded = Sets.newHashSet(impl.get(Registry.BIOME_KEY).getIds());
	}

	@Inject(method = "load(Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Lnet/minecraft/util/dynamic/RegistryOps;)V", at = @At("RETURN"))
	private static void afterLoad(DynamicRegistryManager.Impl impl, RegistryOps<?> registryOps, CallbackInfo ci) {
		BiomeRegistryCallback invoker = BiomeRegistryCallback.EVENT.invoker();
		Set<Identifier> storedLoaded = ((DynamicRegistryManagerMixin) (Object) impl).storedLoaded;

		impl.get(Registry.BIOME_KEY).getEntries().stream()
				.filter(entry -> !storedLoaded.contains(entry.getKey().getValue()))
				.forEach(entry -> invoker.accept(impl, entry.getKey(), entry.getValue()));

		((DynamicRegistryManagerMixin) (Object) impl).storedLoaded = null;
	}
}
