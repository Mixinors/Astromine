package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.registry.common.AMWorlds;
import com.mojang.serialization.Lifecycle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.dimension.DimensionOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Shadow
	@Final
	protected SaveProperties saveProperties;
	
	@Inject(at = @At("HEAD"), method = "createWorlds")
	private void astromine$createWorlds(CallbackInfo ci) {
		// TODO: Remove; this is a horrible hack to get around the fact that the dimensions
		//  are not loaded from a datapack, and, as such, don't exist in this registry.
		var generatorOptions = saveProperties.getGeneratorOptions();
		var registry = generatorOptions.getDimensions();
		
		AMWorlds.DIMENSIONS.forEach((k, v) -> {
			if (!registry.contains(k)) {
				((SimpleRegistry<DimensionOptions>) registry).add(k, v.get(), Lifecycle.stable());
			}
		});
	}
}
