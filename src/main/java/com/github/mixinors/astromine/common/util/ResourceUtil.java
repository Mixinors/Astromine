package com.github.mixinors.astromine.common.util;

import com.github.mixinors.astromine.AMCommon;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import net.minecraft.server.packs.resources.ResourceManager;

public class ResourceUtil {
	public static <T> Stream<T> load(ResourceManager manager, String startingPath, Codec<T> codec) {
		return manager.listResources(startingPath, s -> s.getPath().endsWith(".json"))
				.entrySet()
				.stream()
				.flatMap((entry) -> {
					try (var reader = new InputStreamReader(entry.getValue().open(), StandardCharsets.UTF_8)) {
						var json = AMCommon.GSON.fromJson(reader, JsonObject.class);
						
						return codec.decode(JsonOps.INSTANCE, json)
								.resultOrPartial((error) -> AMCommon.LOGGER.warn("Failed to decode resource {}: {}", entry.getKey(), error))
								.stream()
								.map(Pair::getFirst);
					} catch (Exception exception) {
						AMCommon.LOGGER.warn("Failed to load resource {}", entry.getKey(), exception);
						
						return Stream.empty();
					}
				});
	}
}
