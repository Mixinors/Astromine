package com.github.mixinors.astromine.cardinalcomponents.common.component.base;

import com.github.mixinors.astromine.cardinalcomponents.common.component.Component;
import com.github.mixinors.astromine.registry.common.AMComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface OxygenComponent extends Component {
	/** Returns the {@link OxygenComponent} of the given {@link V}. */
	@Nullable
	static <V> OxygenComponent from(V v) {
		return null;
	}
	
	/** Instantiates an {@link OxygenComponent} with automatic synchronization. */
	static OxygenComponent of(Entity entity) {
		return new OxygenComponentImpl(entity);
	}
	
	/** Instantiates an {@link OxygenComponent} with automatic synchronization. */
	static OxygenComponent ofSynced(Entity entity) {
		return new OxygenComponentSyncedImpl(entity);
	}
	
	/** Simulate behavior, damaging the entity if out of oxygen. */
	default void simulate(boolean isBreathing) {
		var entity = getEntity();
		var oxygen = getOxygen();
		
		if (entity instanceof PlayerEntity player) {
			if (player.isCreative() || player.isSpectator()) {
				return;
			}
		}
		
		oxygen = nextOxygen(isBreathing, oxygen);
		
		if (oxygen == getMinimumOxygen()) {
			entity.damage(DamageSource.DROWN, 1.0F);
		}
	}
	
	/** Returns the next oxygen level for this component,
	 * adding if positive, subtracting if negative, based
	 * on the given amount. */
	default int nextOxygen(boolean isPositive, int oxygen) {
		return isPositive ? oxygen < getMaximumOxygen() ? oxygen + 1 : getMaximumOxygen() : oxygen > getMinimumOxygen() ? oxygen - 1 : getMinimumOxygen();
	}
	
	/** Returns this component's oxygen amount. */
	int getOxygen();
	
	/** Sets this component's oxygen amount to the specified value. */
	void setOxygen(int oxygen);
	
	/** Returns this component's minimum oxygen amount. */
	int getMinimumOxygen();
	
	/** Sets this component's minimum oxygen amount to the specified value. */
	void setMinimumOxygen(int minimumOxygen);
	
	/** Returns this component's maximum oxygen amount. */
	int getMaximumOxygen();
	
	/** Sets this component's maximum oxygen amount to the specified value. */
	void setMaximumOxygen(int maximumOxygen);
	
	/** Returns this component's entity. */
	Entity getEntity();
	
	/** Sets this component's entity to the specified value. */
	void setEntity(Entity entity);
	
	/** Serializes this {@link FluidComponent} to a {@link CompoundTag}. */
	@Override
	default void toTag(CompoundTag tag) {
		tag.putInt("Oxygen", getOxygen());
	}
	
	/** Deserializes this {@link FluidComponent} from a {@link CompoundTag}. */
	@Override
	default void fromTag(CompoundTag tag) {
		this.setOxygen(tag.getInt("Oxygen"));
	}
	
	/** Returns this component's {@link Identifier}. */
	@Override
	default Identifier getId() {
		return AMComponents.OXYGEN;
	}
}
