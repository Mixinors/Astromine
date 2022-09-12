package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gravity.api.common.manager.GravityManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import org.jetbrains.annotations.Nullable;
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
					BodyType.CODEC.fieldOf("type").forGetter(Body::type),
					Position.CODEC.fieldOf("position").forGetter(Body::position),
					Size.CODEC.fieldOf("size").forGetter(Body::size),
					BodyOrbit.CODEC.optionalFieldOf("orbit").forGetter(body -> Optional.ofNullable(body.orbit())),
					BodyDimension.CODEC.optionalFieldOf("surfaceDimension").forGetter(body -> Optional.ofNullable(body.surfaceDimension())),
					BodyDimension.CODEC.optionalFieldOf("orbitDimension").forGetter(body -> Optional.ofNullable(body.orbitDimension())),
					BodyTexture.CODEC.fieldOf("texture").forGetter(Body::texture),
					Codecs.LITERAL_TEXT.fieldOf("name").forGetter(Body::name),
					Codecs.TRANSLATABLE_TEXT.fieldOf("description").forGetter(Body::description)
			).apply(instance, (id, type, position, size, orbit, surfaceDimension, orbitDimension, texture, name, description) -> new Body(id, type, position, size, orbit.orElse(null), surfaceDimension.orElse(null), orbitDimension.orElse(null), texture, name, description))
	);
	
	private final Identifier id;
	
	private final BodyType type;
	
	private final Position position;
	private final Size size;
	
	@Nullable
	private final BodyOrbit orbit;
	
	@Nullable
	BodyDimension surfaceDimension;
	@Nullable
	BodyDimension orbitDimension;
	
	private final BodyTexture texture;
	
	private final Text name;
	private final Text description;
	
	private double angle = 0.0D;
	
	private double orbitX = 0.0D;
	private double prevOrbitX = 0.0D;
	
	private double orbitY = 0.0D;
	private double prevOrbitY = 0.0D;
	
	private double scale = 0.0D;
	private double prevScale = 1.0D;
	
	public Body(Identifier id, BodyType type, Position position, Size size, @Nullable BodyOrbit orbit, @Nullable BodyDimension surfaceDimension, @Nullable BodyDimension orbitDimension, BodyTexture texture, Text name, Text description) {
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
			if (orbit.environment() != null) {
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
	
	public BodyType type() {
		return type;
	}
	
	public Position position() {
		return position;
	}
	
	public Size size() {
		return size;
	}
	
	@Nullable
	public BodyOrbit orbit() {
		return orbit;
	}
	
	@Nullable
	public BodyDimension surfaceDimension() {
		return surfaceDimension;
	}
	
	@Nullable
	public BodyDimension orbitDimension() {
		return orbitDimension;
	}
	
	public BodyTexture texture() {
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
