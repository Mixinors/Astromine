package com.github.mixinors.astromine.common.util;

import com.github.mixinors.astromine.AMCommon;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ResourceUtil {
	public static <T> Stream<T> load(ResourceManager manager, String startingPath, Codec<T> codec) {
		// TODO: IMPROVE!!!!!!
		return manager.findResources(startingPath, s -> s.getPath().endsWith(".json"))
				.keySet()
			   	.stream()
			   	.map(id -> {
					   try {
						   return manager.getResource(id);
					   } catch (Exception e) {
						   return Optional.<Resource>empty();
					   }
			   	})
			   	.filter(Optional::isPresent)
				.map(resource -> resource.orElse(null))
			   	.map(resource -> {
					   try {
						   var stream = resource.getInputStream();
						   var reader = new InputStreamReader(stream);
						   var json = AMCommon.GSON.fromJson(reader, JsonObject.class);
					
						   reader.close();
					
						   return json;
					   } catch (Exception e) {
						   e.printStackTrace();
					   }
				
					   return null;
			   	})
			   	.map(json -> codec.decode(JsonOps.INSTANCE, json))
			   	.map(DataResult::result)
			   	.filter(Optional::isPresent)
			   	.map(Optional::get)
			   	.map(Pair::getFirst);
	}
}
