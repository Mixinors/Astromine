package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.client.render.skybox.SkyboxTextures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public record BodyDimension(
		ResourceKey<Level> worldKey,
		ResourceKey<LevelStem> worldOptionsKey,
		ResourceKey<DimensionType> worldDimensionTypeKey,
		@Nullable BodyAtmosphere atmosphere,
		@Nullable BodyEnvironment environment,
		@Nullable SkyboxTextures skybox,
		@Nullable Layer topLayer,
		@Nullable Layer bottomLayer
) {
	
	public record Layer(
			ResourceKey<Level> worldKey,
			int worldY
	) {
		public static final Codec<Layer> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						ResourceKey.codec(Registries.DIMENSION).fieldOf("worldKey").forGetter(Layer::worldKey),
						Codec.INT.fieldOf("worldY").forGetter(Layer::worldY)
				).apply(instance, Layer::new)
		);
	}
	
	public static final Codec<BodyDimension> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ResourceKey.codec(Registries.DIMENSION).fieldOf("worldKey").forGetter(BodyDimension::worldKey),
					ResourceKey.codec(Registries.LEVEL_STEM).fieldOf("worldOptionsKey").forGetter(BodyDimension::worldOptionsKey),
					ResourceKey.codec(Registries.DIMENSION_TYPE).fieldOf("worldDimensionTypeKey").forGetter(BodyDimension::worldDimensionTypeKey),
					BodyAtmosphere.CODEC.optionalFieldOf("atmosphere").forGetter(dimension -> Optional.ofNullable(dimension.atmosphere())),
					BodyEnvironment.CODEC.optionalFieldOf("environment").forGetter(dimension -> Optional.ofNullable(dimension.environment())),
					SkyboxTextures.CODEC.optionalFieldOf("skybox").forGetter(dimension -> Optional.ofNullable(dimension.skybox())),
					Layer.CODEC.optionalFieldOf("topLayer").forGetter(dimension -> Optional.ofNullable(dimension.topLayer())),
					Layer.CODEC.optionalFieldOf("bottomLayer").forGetter(dimension -> Optional.ofNullable(dimension.bottomLayer()))
			).apply(instance, (worldKey, worldOptionsKey, worldDimensionTypeKey, atmosphere, environment, skybox, topLayer, bottomLayer) -> new BodyDimension(worldKey, worldOptionsKey, worldDimensionTypeKey, atmosphere.orElse(null), environment.orElse(null), skybox.orElse(null), topLayer.orElse(null), bottomLayer.orElse(null)))
	);
}
