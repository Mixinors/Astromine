package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.client.render.skybox.SkyboxTextures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BodyDimension(
		RegistryKey<World> worldKey,
		RegistryKey<DimensionOptions> worldOptionsKey,
		RegistryKey<DimensionType> worldDimensionTypeKey,
		@Nullable BodyAtmosphere atmosphere,
		@Nullable BodyEnvironment environment,
		@Nullable SkyboxTextures skybox,
		@Nullable Layer topLayer,
		@Nullable Layer bottomLayer
) {
	
	public record Layer(
			RegistryKey<World> worldKey,
			int worldY
	) {
		public static final Codec<Layer> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						RegistryKey.createCodec(RegistryKeys.WORLD).fieldOf("worldKey").forGetter(Layer::worldKey),
						Codec.INT.fieldOf("worldY").forGetter(Layer::worldY)
				).apply(instance, Layer::new)
		);
	}
	
	public static final Codec<BodyDimension> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					RegistryKey.createCodec(RegistryKeys.WORLD).fieldOf("worldKey").forGetter(BodyDimension::worldKey),
					RegistryKey.createCodec(RegistryKeys.DIMENSION).fieldOf("worldOptionsKey").forGetter(BodyDimension::worldOptionsKey),
					RegistryKey.createCodec(RegistryKeys.DIMENSION_TYPE).fieldOf("worldDimensionTypeKey").forGetter(BodyDimension::worldDimensionTypeKey),
					BodyAtmosphere.CODEC.optionalFieldOf("atmosphere").forGetter(dimension -> Optional.ofNullable(dimension.atmosphere)),
					BodyEnvironment.CODEC.optionalFieldOf("environment").forGetter(dimension -> Optional.ofNullable(dimension.environment)),
					SkyboxTextures.CODEC.optionalFieldOf("skybox").forGetter(dimension -> Optional.ofNullable(dimension.skybox)),
					Layer.CODEC.optionalFieldOf("topLayer").forGetter(dimension -> Optional.ofNullable(dimension.topLayer)),
					Layer.CODEC.optionalFieldOf("bottomLayer").forGetter(dimension -> Optional.ofNullable(dimension.bottomLayer))
			).apply(instance, (worldKey, worldOptionsKey, worldDimensionTypeKey, atmosphere, environment, skybox, topLayer, bottomLayer) -> new BodyDimension(worldKey, worldOptionsKey, worldDimensionTypeKey, atmosphere.orElse(null), environment.orElse(null), skybox.orElse(null), topLayer.orElse(null), bottomLayer.orElse(null)))
	);
}
