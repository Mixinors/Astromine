package com.github.mixinors.astromine.mixin.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Mixin(JsonUnbakedModel.class)
public interface JsonUnbakedModelAccessor {
	@Accessor
	JsonUnbakedModel getParent();
	
	@Accessor
	Identifier getParentId();
	
	@Accessor
	Map<String, Either<SpriteIdentifier, String>> getTextureMap();
	
}
