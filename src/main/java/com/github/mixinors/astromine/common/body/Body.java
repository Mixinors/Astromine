package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.render.skybox.Skybox;
import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gravity.api.common.manager.GravityManager;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
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
					Identifier.CODEC.fieldOf("id").forGetter(Body::id),
					Type.CODEC.fieldOf("type").forGetter(Body::type),
					Position.CODEC.fieldOf("position").forGetter(Body::position),
					Size.CODEC.fieldOf("size").forGetter(Body::size),
					Orbit.CODEC.optionalFieldOf("orbit").forGetter(body -> Optional.ofNullable(body.orbit())),
					Dimension.CODEC.optionalFieldOf("surfaceDimension").forGetter(body -> Optional.ofNullable(body.surfaceDimension())),
					Dimension.CODEC.optionalFieldOf("orbitDimension").forGetter(body -> Optional.ofNullable(body.orbitDimension())),
					Texture.CODEC.fieldOf("texture").forGetter(Body::texture),
					Codecs.LITERAL_TEXT.fieldOf("name").forGetter(Body::name),
					Codecs.TRANSLATABLE_TEXT.fieldOf("description").forGetter(Body::description)
			).apply(instance, (id, type, position, size, orbit, surfaceDimension, orbitDimension, texture, name, description) -> new Body(id, type, position, size, orbit.orElse(null), surfaceDimension.orElse(null), orbitDimension.orElse(null), texture, name, description))
	);
	
	public enum Type {
		STAR(AMCommon.id("star")),
		PLANET(AMCommon.id("planet")),
		DWARF_PLANET(AMCommon.id("dwarf_planet")),
		MOON(AMCommon.id("moon")),
		ASTEROID(AMCommon.id("asteroid")),
		COMET(AMCommon.id("comet")),
		SATELLITE(AMCommon.id("satellite")),
		STATION(AMCommon.id("station"));
		
		public static final Codec<Type> CODEC = Identifier.CODEC.xmap(Type::byId, Type::id);
		
		private static final Map<Identifier, Type> BY_ID = new HashMap<>();
		
		private final Identifier id;
		
		Type(Identifier id) {
			this.id = id;
		}
		
		public Identifier id() {
			return id;
		}
		
		public static Type byId(Identifier id) {
			return BY_ID.get(id);
		}
		
		 static {
			for (var value : values()) {
				BY_ID.put(value.id(), value);
			}
		 }
	}
	
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
	
	public record Atmosphere(
			RegistryKey<Fluid> content
	) {
		public static final Codec<Atmosphere> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						RegistryKey.createCodec(Registry.FLUID_KEY).fieldOf("content").forGetter(Atmosphere::content)
				).apply(instance, Atmosphere::new)
		);
	}
	
	public record Environment(
			double temperature,
			double humidity,
			float sound,
			float gravity
	) {
		public static final Codec<Environment> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						Codec.DOUBLE.fieldOf("temperature").forGetter(Environment::temperature),
						Codec.DOUBLE.optionalFieldOf("humidity", 0.0D).forGetter(Environment::humidity),
						Codec.FLOAT.optionalFieldOf("sound", 1.0F).forGetter(Environment::sound),
						Codec.FLOAT.optionalFieldOf("gravity", 0.08F).forGetter(Environment::gravity)
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
	
	public record Dimension(
			RegistryKey<World> worldKey,
			RegistryKey<DimensionOptions> worldOptionsKey,
			RegistryKey<DimensionType> worldDimensionTypeKey,
			@Nullable Atmosphere atmosphere,
			@Nullable Environment environment,
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
		
		public static final Codec<Dimension> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						RegistryKey.createCodec(Registry.WORLD_KEY).fieldOf("worldKey").forGetter(Dimension::worldKey),
						RegistryKey.createCodec(Registry.DIMENSION_KEY).fieldOf("worldOptionsKey").forGetter(Dimension::worldOptionsKey),
						RegistryKey.createCodec(Registry.DIMENSION_TYPE_KEY).fieldOf("worldDimensionTypeKey").forGetter(Dimension::worldDimensionTypeKey),
						Atmosphere.CODEC.optionalFieldOf("atmosphere").forGetter(dimension -> Optional.ofNullable(dimension.atmosphere)),
						Environment.CODEC.optionalFieldOf("environment").forGetter(dimension -> Optional.ofNullable(dimension.environment)),
						Skybox.CODEC.optionalFieldOf("skybox").forGetter(dimension -> Optional.ofNullable(dimension.skybox)),
						Layer.CODEC.optionalFieldOf("topLayer").forGetter(dimension -> Optional.ofNullable(dimension.topLayer)),
						Layer.CODEC.optionalFieldOf("bottomLayer").forGetter(dimension -> Optional.ofNullable(dimension.bottomLayer))
				).apply(instance, (worldKey, worldOptionsKey, worldDimensionTypeKey, atmosphere, environment, skybox, topLayer, bottomLayer) -> new Dimension(worldKey, worldOptionsKey, worldDimensionTypeKey, atmosphere.orElse(null), environment.orElse(null), skybox.orElse(null), topLayer.orElse(null), bottomLayer.orElse(null)))
		);
	}
	
	private final Identifier id;
	
	private final Type type;
	
	private final Position position;
	private final Size size;
	
	@Nullable
	private final Orbit orbit;
	
	@Nullable
	Dimension surfaceDimension;
	@Nullable
	Dimension orbitDimension;
	
	private final Texture texture;
	
	private final Text name;
	private final Text description;
	
	private double angle = 0.0D;
	
	private double orbitX = 0.0D;
	private double prevOrbitX = 0.0D;
	
	private double orbitY = 0.0D;
	private double prevOrbitY = 0.0D;
	
	private double scale = 0.0D;
	private double prevScale = 1.0D;
	
	public Body(Identifier id, Type type, Position position, Size size, @Nullable Orbit orbit, @Nullable Dimension surfaceDimension, @Nullable Dimension orbitDimension, Texture texture, Text name, Text description) {
		this.id = id;
		this.type = type;
		this.position = position;
		this.size = size;
		this.orbit = orbit;
		this.surfaceDimension = surfaceDimension;
		this.orbitDimension = orbitDimension;
		this.texture = texture;
		this.name = name;
		this.description = description;
	}
	
	@ApiStatus.Internal
	public void onLoad() {
		var surface = surfaceDimension();
		
		if (surface != null) {
			if (surface.environment() != null) {
				var surfaceEnvironment = surface.environment();
				
				GravityManager.set(surface.worldKey(), surfaceEnvironment.gravity());
			}
		}
		
		var orbit = orbitDimension();
		
		if (orbit != null) {
			var orbitDimension = orbitDimension();
			
			if (orbitDimension != null) {
				var orbitEnvironment = orbitDimension.environment();
				
				GravityManager.set(orbitDimension.worldKey(), orbitEnvironment.gravity());
			}
		}
	}
	
	@ApiStatus.Internal
	public void onUnload() {
		var surface = surfaceDimension();
		
		if (surface != null) {
			if (surface.environment() != null) {
				GravityManager.reset(surface.worldKey());
			}
		}
		
		var orbit = orbitDimension();
		
		if (orbit != null) {
			var orbitDimension = orbitDimension();
			
			if (orbitDimension != null) {
				GravityManager.reset(orbitDimension.worldKey());
			}
		}
	}
	
	@ApiStatus.Internal
	public void onReload() {
		onLoad();
	}
	
	public Identifier id() {
		return id;
	}
	
	public Type type() {
		return type;
	}
	
	public Position position() {
		return position;
	}
	
	public Size size() {
		return size;
	}
	
	@Nullable
	public Orbit orbit() {
		return orbit;
	}
	
	@Nullable
	public Dimension surfaceDimension() {
		return surfaceDimension;
	}
	
	@Nullable
	public Dimension orbitDimension() {
		return orbitDimension;
	}
	
	public Texture texture() {
		return texture;
	}
	
	public Text name() {
		return name;
	}
	
	public Text description() {
		return description;
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
