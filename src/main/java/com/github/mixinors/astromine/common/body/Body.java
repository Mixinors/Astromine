package com.github.mixinors.astromine.common.body;

import com.github.mixinors.astromine.common.util.data.tier.Tier;
import com.github.mixinors.astromine.common.widget.BodyWidget;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * <p>A {@link Body} represents a three-dimensional body in space,
 * which may or may not be orbiting a point.</p>
 *
 * <p>Positions should be KM-based and fit within a <code>float</code>.</p>
 */
public class Body {
	private final Tier tier;
	
	private final Position position;
	
	private final Size size;
	
	@Nullable
	private final Body orbitedBody;
	
	private final double mass;
	private final double temperature;
	
	private final double orbitWidth;
	private final double orbitHeight;
	
	private final double orbitSpeed;
	
	private final boolean orbitTidalLocked;
	
	@Nullable
	private final RegistryKey<World> worldKey;
	
	@Nullable
	private final RegistryKey<World> orbitWorldKey;
	
	@Nullable
	private final Identifier texture;
	
	private final Supplier<Collection<Text>> tooltip;
	
	@Nullable
	private BodyWidget widget = null;
	
	Body(Tier tier, Position position, Size size, Body orbitedBody, double mass, double temperature, double orbitWidth, double orbitHeight, double orbitSpeed, boolean orbitTidalLocked, @Nullable RegistryKey<World> worldKey, @Nullable RegistryKey<World> orbitWorldKey, @Nullable Identifier texture, Supplier<Collection<Text>> tooltip) {
		this.tier = tier;
		
		this.position = position;
		
		this.size = size;
		
		this.orbitedBody = orbitedBody;
		
		this.mass = mass;
		this.temperature = temperature;
		
		this.orbitWidth = orbitWidth;
		this.orbitHeight = orbitHeight;
		
		this.orbitSpeed = orbitSpeed;
		
		this.orbitTidalLocked = orbitTidalLocked;
		
		this.worldKey = worldKey;
		this.orbitWorldKey = orbitWorldKey;
		
		this.texture = texture;
		
		this.tooltip = tooltip;
	}
	
	public Tier getTier() {
		return tier;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public Size getSize() {
		return size;
	}
	
	@Nullable
	public Body getOrbitedBody() {
		return orbitedBody;
	}
	
	public double getMass() {
		return mass;
	}
	
	public double getTemperature() {
		return temperature;
	}
	
	public double getOrbitWidth() {
		return orbitWidth;
	}
	
	public double getOrbitHeight() {
		return orbitHeight;
	}
	
	public double getOrbitSpeed() {
		return orbitSpeed;
	}
	
	public boolean isOrbitTidalLocked() {
		return orbitTidalLocked;
	}
	
	@Nullable
	public RegistryKey<World> getWorldKey() {
		return worldKey;
	}
	
	@Nullable
	public RegistryKey<World> getOrbitWorldKey() {
		return orbitWorldKey;
	}
	
	@Nullable
	public Identifier getTexture() {
		return texture;
	}
	
	public Collection<Text> getTooltips() {
		return tooltip.get();
	}
	
	@Nullable
	public BodyWidget getWidget() {
		return widget;
	}
	
	public void setWidget(@Nullable BodyWidget widget) {
		this.widget = widget;
	}
	
	public static class Builder {
		private Tier tier;
		
		private Position position;
		
		private Size size;
		
		@Nullable
		private Body orbitedBody;
		
		private double mass;
		private double temperature;
		
		private double orbitWidth;
		private double orbitHeight;
		
		private double orbitTime;
		
		private boolean orbitTidalLocked;
		
		private RegistryKey<World> worldKey;
		private RegistryKey<World> orbitWorldKey;
		
		private Identifier texture;
		
		private Supplier<Collection<Text>> tooltip;
		
		public Builder setTier(Tier tier) {
			this.tier = tier;
			return this;
		}
		
		public Builder setPosition(Position position) {
			this.position = position;
			return this;
		}
		
		public Builder setSize(Size size) {
			this.size = size;
			return this;
		}
		
		public Builder setOrbitedBody(Body orbitedBody) {
			this.orbitedBody = orbitedBody;
			return this;
		}
		
		public Builder setMass(double mass) {
			this.mass = mass;
			return this;
		}
		
		public Builder setTemperature(double temperature) {
			this.temperature = temperature;
			return this;
		}
		
		public Builder setOrbitWidth(double orbitWidth) {
			this.orbitWidth = orbitWidth;
			return this;
		}
		
		public Builder setOrbitHeight(double orbitHeight) {
			this.orbitHeight = orbitHeight;
			return this;
		}
		
		public Builder setOrbitTime(double orbitTime) {
			this.orbitTime = orbitTime;
			return this;
		}
		
		public Builder setOrbitTidalLocked(boolean orbitTidalLocked) {
			this.orbitTidalLocked = orbitTidalLocked;
			return this;
		}
		
		public Builder setWorldKey(RegistryKey<World> worldKey) {
			this.worldKey = worldKey;
			return this;
		}
		
		public Builder setOrbitWorldKey(RegistryKey<World> orbitWorldKey) {
			this.orbitWorldKey = orbitWorldKey;
			return this;
		}
		
		public Builder setTexture(Identifier texture) {
			this.texture = texture;
			return this;
		}
		
		public Builder setTooltip(Supplier<Collection<Text>> tooltip) {
			this.tooltip = tooltip;
			return this;
		}
		
		public Body createBody() {
			return new Body(tier, position, size, orbitedBody, mass, temperature, orbitWidth, orbitHeight, orbitTime, orbitTidalLocked, worldKey, orbitWorldKey, texture, tooltip);
		}
	}
}
