/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.common.component.block.entity;

import com.github.mixinors.astromine.common.component.Component;
import com.github.mixinors.astromine.common.component.general.provider.TransferComponentProvider;
import com.github.mixinors.astromine.registry.common.AMComponents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.block.transfer.TransferType;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.component.general.base.ItemComponent;

import java.util.Map;

/**
 * A {@link Component} representing a {@link BlockEntity}'s
 * siding information.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag(CompoundTag)} and {@link #fromTag(CompoundTag)}.
 */
public interface TransferComponent extends Component {
	/** Returns this component's {@link Map} of {@link Identifier}s to {@link TransferEntry}-ies. */
	static <V> TransferComponent get(V v) {
		if (v instanceof TransferComponentProvider) {
			return ((TransferComponentProvider) v).getTransferComponent();
		}

		return null;
	}
	
	/** Returns this {@link TransferComponent}'s item {@link TransferEntry}. */
	TransferEntry getItemEntry();
	
	/** Returns this {@link TransferComponent}'s fluid {@link TransferEntry}. */
	TransferEntry getFluidEntry();
	
	/** Returns this {@link TransferComponent}'s energy {@link TransferEntry}. */
	TransferEntry getEnergyEntry();
	
	/** Adds an item {@link TransferEntry} to this component. */
	void addItem();
	
	/** Adds a fluid {@link TransferEntry} to this component. */
	void addFluid();
	
	/** Adds an energy {@link TransferEntry} to this component. */
	void addEnergy();

	/** Asserts whether this component's siding contains
	 * data for {@link ItemComponent} or not. */
	boolean hasItem();

	/** Asserts whether this component's siding contains
	 * data for {@link FluidComponent} or not. */
	boolean hasFluid();

	/** Asserts whether this component's siding contains
	 * data for {@link EnergyComponent} or not. */
	public boolean hasEnergy();

	/** Returns this component's {@link TransferType} for {@link ItemComponent}'s key at the given {@link Direction}. */
	default TransferType getItem(Direction direction) {
		return getItemEntry().get(direction);
	}

	/** Returns this component's {@link TransferType} for {@link FluidComponent}'s key at the given {@link Direction}. */
	default TransferType getFluid(Direction direction) {
		return getFluidEntry().get(direction);
	}

	/** Returns this component's {@link TransferType} for {@link EnergyComponent}'s key at the given {@link Direction}. */
	default TransferType getEnergy(Direction direction) {
		return getEnergyEntry().get(direction);
	}
	
	/** Returns this component's {@link TransferEntry} for the given component {@link Identifier}. */
	public TransferEntry get(Identifier id) {
		if (id.equals(AMComponents.ITEM_INVENTORY_COMPONENT)) return getItemEntry();
		if (id.equals(AMComponents.FLUID_INVENTORY_COMPONENT)) return getFluidEntry();
		if (id.equals(AMComponents.ENERGY_INVENTORY_COMPONENT)) return getEnergyEntry();
		return null;
	}

	/** Serializes this {@link TransferComponent} to a {@link CompoundTag}. */
	@Override
	public void toTag(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();
		
		dataTag.put("Item", itemComponentTransferEntry.toTag());
		dataTag.put("Fluid", fluidComponentTransferEntry.toTag());
		dataTag.put("Energy", energyComponentTransferEntry.toTag());

		tag.put("Data", dataTag);
	}

	/** Deserializes this {@link TransferComponent} from a {@link CompoundTag}. */
	@Override
	public void fromTag(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("Data");
		
		if (dataTag.contains("Item")) {
			itemComponentTransferEntry = new TransferEntry();
			itemComponentTransferEntry.fromTag(dataTag.getCompound("Item"));
		}
		
		if (dataTag.contains("Fluid")) {
			fluidComponentTransferEntry = new TransferEntry();
			fluidComponentTransferEntry.fromTag(dataTag.getCompound("Fluid"));
		}
		
		if (dataTag.contains("Energy")) {
			energyComponentTransferEntry = new TransferEntry();
			energyComponentTransferEntry.fromTag(dataTag.getCompound("Energy"));
		}
	}

	/**
	 * A representation of a side's transfer information.
	 *
	 * Serialization and deserialization methods are provided for:
	 * - {@link CompoundTag} - through {@link #toTag()} and {@link #fromTag(CompoundTag)}.
	 */
	public static class TransferEntry {
		private TransferType up = TransferType.NONE;
		private TransferType down = TransferType.NONE;
		private TransferType north = TransferType.NONE;
		private TransferType south = TransferType.NONE;
		private TransferType east = TransferType.NONE;
		private TransferType west = TransferType.NONE;

		/** Sets the {@link TransferType} of the given {@link Direction}
		 * to the specified value. */
		public void set(Direction direction, TransferType type) {
			switch (direction) {
				case UP: {
					up = type;
					return;
				}
				
				case DOWN: {
					down = type;
					return;
				}
				
				case NORTH: {
					north = type;
					return;
				}
				
				case SOUTH: {
					south = type;
					return;
				}
				
				case EAST: {
					east = type;
					return;
				}
				
				default: {
					west = type;
				}
			}
		}

		/** Returns the {@link TransferType} of the given {@link Direction}. */
		public TransferType get(Direction origin) {
			switch (origin) {
				case UP: return up;
				case DOWN: return down;
				case NORTH: return north;
				case SOUTH: return south;
				case EAST: return east;
				default: return west;
			}
		}

		/** Deserializes this {@link TransferEntry} from a {@link CompoundTag}. */
		public void fromTag(CompoundTag tag) {
			up = TransferType.fromString(tag.getString("Up"));
			down = TransferType.fromString(tag.getString("Down"));
			north = TransferType.fromString(tag.getString("North"));
			south = TransferType.fromString(tag.getString("South"));
			east = TransferType.fromString(tag.getString("East"));
			west = TransferType.fromString(tag.getString("West"));
		}

		/** Serializes this {@link TransferEntry} to a {@link CompoundTag}. */
		public CompoundTag toTag() {
			CompoundTag tag = new CompoundTag();
			
			tag.putString("Up", up.toString());
			tag.putString("Down", down.toString());
			tag.putString("North", north.toString());
			tag.putString("South", south.toString());
			tag.putString("East", east.toString());
			tag.putString("West", west.toString());

			return tag;
		}
	}
}
