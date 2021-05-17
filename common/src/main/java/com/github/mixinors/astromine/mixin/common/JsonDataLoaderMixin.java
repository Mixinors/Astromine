package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.recipe.condition.manager.ConditionalRecipeManager;
import com.google.gson.Gson;
import net.minecraft.resource.JsonDataLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JsonDataLoader.class)
public class JsonDataLoaderMixin {
	@Mutable @Shadow @Final private String dataType;
	
	@Inject(at = @At("RETURN"), method = "<init>")
	void astromine_init(Gson gson, String dataType, CallbackInfo ci) {
		if ((Object) this instanceof ConditionalRecipeManager) {
			this.dataType = "conditional";
		}
	}
}
