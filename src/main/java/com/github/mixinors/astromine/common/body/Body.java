package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.client.render.skybox.Skybox;
import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.github.mixinors.astromine.common.widget.BodyWidget;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * <p>A {@link Body} represents a three-dimensional body in space,
 * which may or may not be orbiting a point.</p>
 *
 * <p>Positions should be KM-based and fit within a <code>float</code>.</p>
 */
public class Body {
	public static final Codec<Body> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Identifier.CODEC.fieldOf("id").forGetter(Body::getId),
					Identifier.CODEC.optionalFieldOf("worldId").forGetter(body -> Optional.ofNullable(body.getWorldId())),
					Identifier.CODEC.optionalFieldOf("orbitWorldId").forGetter(body -> Optional.ofNullable(body.getOrbitWorldId())),
					Position.CODEC.fieldOf("position").forGetter(Body::getPosition),
					Size.CODEC.fieldOf("size").forGetter(Body::getSize),
					Orbit.CODEC.optionalFieldOf("orbit").forGetter(body -> Optional.ofNullable(body.getOrbit())),
					Environment.CODEC.fieldOf("environment").forGetter(Body::getEnvironment),
					Skybox.CODEC.optionalFieldOf("worldSkybox").forGetter(body -> Optional.ofNullable(body.getWorldSkybox())),
					Skybox.CODEC.optionalFieldOf("orbitWorldSkybox").forGetter(body -> Optional.ofNullable(body.getOrbitWorldSkybox())),
					Texture.CODEC.fieldOf("texture").forGetter(Body::getTexture),
					Codecs.LITERAL_TEXT.fieldOf("name").forGetter(Body::getName),
					Codecs.TRANSLATABLE_TEXT.fieldOf("description").forGetter(Body::getDescription)
			).apply(instance, (id, worldId, orbitWorldId, position, size, orbit, environment, worldSkybox, orbitWorldSkybox, texture, name, description) -> new Body(id, worldId.orElse(null), orbitWorldId.orElse(null), position, size, orbit.orElse(null), environment, worldSkybox.orElse(null), orbitWorldSkybox.orElse(null), texture, name, description))
	);
	
	public record Orbit(
			@Nullable Identifier orbitedBodyId,
			@Nullable Position orbitedBodyOffset,
			double width, double height, double speed,
			boolean tidalLocked
	) {
		public static final Codec<Orbit> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Identifier.CODEC.optionalFieldOf("orbitedBodyId").forGetter(orbit -> Optional.ofNullable(orbit.orbitedBodyId)),
						Position.CODEC.optionalFieldOf("orbitedBodyOffset").forGetter(orbit -> Optional.ofNullable(orbit.orbitedBodyOffset)),
						Codec.DOUBLE.fieldOf("width").forGetter(Orbit::width),
						Codec.DOUBLE.fieldOf("height").forGetter(Orbit::height),
						Codec.DOUBLE.fieldOf("speed").forGetter(Orbit::speed),
						Codec.BOOL.fieldOf("tidalLocked").forGetter(Orbit::tidalLocked)
				).apply(instance, ((orbitedBodyId, orbitedBodyOffset, width, height, speed, tidalLocked) -> new Orbit(orbitedBodyId.orElse(null), orbitedBodyOffset.orElse(null), width, height, speed, tidalLocked)))
		);
	}
	
	public record Environment(
			double temperature,
			double humidity
	) {
		public static final Codec<Environment> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Codec.DOUBLE.fieldOf("temperature").forGetter(Environment::temperature),
						Codec.DOUBLE.optionalFieldOf("humidity", 0.0D).forGetter(Environment::humidity)
				).apply(instance, Environment::new)
		);
	}
	
	public record Texture(
			Identifier up,
			Identifier down,
			Identifier north,
			Identifier south,
			Identifier east,
			Identifier west
	) {
		public static final Codec<Texture> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Identifier.CODEC.fieldOf("up").forGetter(Texture::up),
						Identifier.CODEC.fieldOf("down").forGetter(Texture::down),
						Identifier.CODEC.fieldOf("north").forGetter(Texture::north),
						Identifier.CODEC.fieldOf("south").forGetter(Texture::south),
						Identifier.CODEC.fieldOf("east").forGetter(Texture::east),
						Identifier.CODEC.fieldOf("west").forGetter(Texture::west)
				).apply(instance, Texture::new)
		);
	}
	
	private final Identifier id;
	
	@Nullable
	private final Identifier worldId;
	@Nullable
	private final Identifier orbitWorldId;
	
	private final Position position;
	private final Size size;
	
	@Nullable
	private final Orbit orbit;
	private final Environment environment;
	private final Skybox worldSkybox;
	private final Skybox orbitWorldSkybox;
	private final Texture texture;
	
	private final Text name;
	private final Text description;
	
	private final RegistryKey<World> worldKey;
	private final RegistryKey<DimensionOptions> worldOptionsKey;
	private final RegistryKey<DimensionType> worldDimensionTypeKey;
	
	private final RegistryKey<World> orbitWorldKey;
	private final RegistryKey<DimensionOptions> orbitWorldOptionsKey;
	private final RegistryKey<DimensionType> orbitWorldDimensionTypeKey;
	
	private double angle = 0.0D;
	
	private double orbitX = 0.0D;
	private double prevOrbitX = 0.0D;
	
	private double orbitY = 0.0D;
	private double prevOrbitY = 0.0D;
	
	private double scale = 0.0D;
	private double prevScale = 1.0D;
	
	public Body(Identifier id, @Nullable Identifier worldId, @Nullable Identifier orbitWorldId, Position position, Size size, @Nullable Orbit orbit, Environment environment, @Nullable Skybox worldSkybox, @Nullable Skybox orbitWorldSkybox, Texture texture, Text name, Text description) {
		this.id = id;
		this.worldId = worldId;
		this.orbitWorldId = orbitWorldId;
		this.position = position;
		this.size = size;
		this.orbit = orbit;
		this.environment = environment;
		this.worldSkybox = worldSkybox;
		this.orbitWorldSkybox = orbitWorldSkybox;
		this.texture = texture;
		this.name = name;
		this.description = description;
		
		this.worldKey = RegistryKey.of(Registry.WORLD_KEY, worldId);
		this.worldOptionsKey = RegistryKey.of(Registry.DIMENSION_KEY, worldId);
		this.worldDimensionTypeKey = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, worldId);
		
		this.orbitWorldKey = RegistryKey.of(Registry.WORLD_KEY, orbitWorldId);
		this.orbitWorldOptionsKey = RegistryKey.of(Registry.DIMENSION_KEY, orbitWorldId);
		this.orbitWorldDimensionTypeKey = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, orbitWorldId);
	}
	
	public Identifier getId() {
		return id;
	}
	
	@Nullable
	public Identifier getWorldId() {
		return worldId;
	}
	
	@Nullable
	public Identifier getOrbitWorldId() {
		return orbitWorldId;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public Size getSize() {
		return size;
	}
	
	@Nullable
	public Orbit getOrbit() {
		return orbit;
	}
	
	public Environment getEnvironment() {
		return environment;
	}
	
	@Nullable
	public Skybox getWorldSkybox() {
		return worldSkybox;
	}
	
	@Nullable
	public Skybox getOrbitWorldSkybox() {
		return orbitWorldSkybox;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	@Nullable
	public RegistryKey<World> getWorldKey() {
		return worldKey;
	}
	
	@Nullable
	public RegistryKey<World> getOrbitWorldKey() {
		return orbitWorldKey;
	}
	
	public Text getName() {
		return name;
	}
	
	public Text getDescription() {
		return description;
	}
	
	public RegistryKey<DimensionOptions> getWorldOptionsKey() {
		return worldOptionsKey;
	}
	
	public RegistryKey<DimensionType> getWorldDimensionTypeKey() {
		return worldDimensionTypeKey;
	}
	
	public RegistryKey<DimensionOptions> getOrbitWorldOptionsKey() {
		return orbitWorldOptionsKey;
	}
	
	public RegistryKey<DimensionType> getOrbitWorldDimensionTypeKey() {
		return orbitWorldDimensionTypeKey;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public double getOrbitX() {
		return orbitX;
	}
	
	public void setOrbitX(double orbitX) {
		this.orbitX = orbitX;
	}
	
	public double getPrevOrbitX() {
		return prevOrbitX;
	}
	
	public void setPrevOrbitX(double prevOrbitX) {
		this.prevOrbitX = prevOrbitX;
	}
	
	public double getOrbitY() {
		return orbitY;
	}
	
	public void setOrbitY(double orbitY) {
		this.orbitY = orbitY;
	}
	
	public double getPrevOrbitY() {
		return prevOrbitY;
	}
	
	public void setPrevOrbitY(double prevOrbitY) {
		this.prevOrbitY = prevOrbitY;
	}
	
	public double getScale() {
		return scale;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public double getPrevScale() {
		return prevScale;
	}
	
	public void setPrevScale(double prevScale) {
		this.prevScale = prevScale;
	}
}
