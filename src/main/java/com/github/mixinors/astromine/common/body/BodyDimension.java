package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.client.render.skybox.Skybox;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nullable;
import java.util.Optional;

public record BodyDimension(
		RegistryKey<World> worldKey,
		RegistryKey<DimensionOptions> worldOptionsKey,
		RegistryKey<DimensionType> worldDimensionTypeKey,
		@Nullable BodyAtmosphere atmosphere,
		@Nullable BodyEnvironment environment,
		@Nullable Skybox skybox,
		@Nullable Layer topLayer,
		@Nullable Layer bottomLayer
) {
	public record Layer(
			RegistryKey<World> worldKey,
			int worldY
	) {
		public static final Codec<Layer> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						RegistryKey.createCodec(Registry.WORLD_KEY).fieldOf("worldKey").forGetter(Layer::worldKey),
						Codec.INT.fieldOf("worldY").forGetter(Layer::worldY)
				).apply(instance, Layer::new)
		);
	}
	
	public static final Codec<BodyDimension> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					RegistryKey.createCodec(Registry.WORLD_KEY).fieldOf("worldKey").forGetter(BodyDimension::worldKey),
					RegistryKey.createCodec(Registry.DIMENSION_KEY).fieldOf("worldOptionsKey").forGetter(BodyDimension::worldOptionsKey),
					RegistryKey.createCodec(Registry.DIMENSION_TYPE_KEY).fieldOf("worldDimensionTypeKey").forGetter(BodyDimension::worldDimensionTypeKey),
					BodyAtmosphere.CODEC.optionalFieldOf("atmosphere").forGetter(dimension -> Optional.ofNullable(dimension.atmosphere)),
					BodyEnvironment.CODEC.optionalFieldOf("environment").forGetter(dimension -> Optional.ofNullable(dimension.environment)),
					Skybox.CODEC.optionalFieldOf("skybox").forGetter(dimension -> Optional.ofNullable(dimension.skybox)),
					Layer.CODEC.optionalFieldOf("topLayer").forGetter(dimension -> Optional.ofNullable(dimension.topLayer)),
					Layer.CODEC.optionalFieldOf("bottomLayer").forGetter(dimension -> Optional.ofNullable(dimension.bottomLayer))
			).apply(instance, (worldKey, worldOptionsKey, worldDimensionTypeKey, atmosphere, environment, skybox, topLayer, bottomLayer) -> new BodyDimension(worldKey, worldOptionsKey, worldDimensionTypeKey, atmosphere.orElse(null), environment.orElse(null), skybox.orElse(null), topLayer.orElse(null), bottomLayer.orElse(null)))
	);
}
