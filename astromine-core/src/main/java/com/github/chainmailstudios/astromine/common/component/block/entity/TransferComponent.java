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

package com.github.chainmailstudios.astromine.common.component.block.entity;

import com.github.chainmailstudios.astromine.common.component.general.provider.TransferComponentProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.callback.TransferEntryCallback;
import com.github.chainmailstudios.astromine.common.component.general.base.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.DirectionUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.util.Map;

/**
 * A {@link Component} representing a {@link BlockEntity}'s
 * siding information.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #writeToNbt(CompoundTag)} and {@link #readFromNbt(CompoundTag)}.
 */
public class TransferComponent implements Component {
	private final Reference2ReferenceMap<ComponentKey<?>, TransferEntry> components = new Reference2ReferenceOpenHashMap<>();

	/** Returns this component's {@link Map} of {@link ComponentKey}s to {@link TransferEntry}-ies. */
	public static <V> TransferComponent get(V v) {
		if (v instanceof TransferComponentProvider) {
			return ((TransferComponentProvider) v).getTransferComponent();
		}

		try {
			return AstromineComponents.BLOCK_ENTITY_TRANSFER_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}

	/** Returns this component's {@link TransferEntry} for the given {@link ComponentKey}. */
	public TransferEntry get(ComponentKey<?> type) {
		return components.getOrDefault(type, null);
	}

	public Map<ComponentKey<?>, TransferEntry> getComponents() {
		return components;
	}

	/** Adds a {@link TransferEntry} to this component for the given {@link ComponentKey}. */
	public void add(ComponentKey<?> type) {
		TransferEntry entry = new TransferEntry(type);

		TransferEntryCallback.EVENT.invoker().handle(entry);

		components.put(type, entry);
	}

	/** Asserts whether this component's siding contains
	 * data for {@link ItemComponent} or not. */
	public boolean hasItem() {
		return components.containsKey(AstromineComponents.ITEM_INVENTORY_COMPONENT);
	}

	/** Asserts whether this component's siding contains
	 * data for {@link FluidComponent} or not. */
	public boolean hasFluid() {
		return components.containsKey(AstromineComponents.FLUID_INVENTORY_COMPONENT);
	}

	/** Asserts whether this component's siding contains
	 * data for {@link EnergyComponent} or not. */
	public boolean hasEnergy() {
		return components.containsKey(AstromineComponents.ENERGY_INVENTORY_COMPONENT);
	}

	/** Returns this component's {@link TransferType} for {@link ItemComponent}'s key at the given {@link Direction}. */
	public TransferType getItem(Direction direction) {
		return components.get(AstromineComponents.ITEM_INVENTORY_COMPONENT).get(direction);
	}

	/** Returns this component's {@link TransferType} for {@link FluidComponent}'s key at the given {@link Direction}. */
	public TransferType getFluid(Direction direction) {
		return components.get(AstromineComponents.FLUID_INVENTORY_COMPONENT).get(direction);
	}

	/** Returns this component's {@link TransferType} for {@link EnergyComponent}'s key at the given {@link Direction}. */
	public TransferType getEnergy(Direction direction) {
		return components.get(AstromineComponents.ENERGY_INVENTORY_COMPONENT).get(direction);
	}

	/** Serializes this {@link TransferComponent} to a {@link CompoundTag}. */
	@Override
	public void writeToNbt(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		for (Map.Entry<ComponentKey<?>, TransferEntry> entry : components.entrySet()) {
			dataTag.put(entry.getKey().getId().toString(), entry.getValue().toTag());
		}

		tag.put("data", dataTag);
	}

	/** Deserializes this {@link TransferComponent} from a {@link CompoundTag}. */
	@Override
	public void readFromNbt(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			Identifier keyId = new Identifier(key);

			TransferEntry entry = new TransferEntry(ComponentRegistry.get(keyId));

			entry.fromTag(dataTag.getCompound(key));

			components.put(ComponentRegistry.get(keyId), entry);
		}
	}

	/**
	 * A representation of a side's transfer information.
	 *
	 * Serialization and deserialization methods are provided for:
	 * - {@link CompoundTag} - through {@link #toTag()} and {@link #fromTag(CompoundTag)}.
	 */
	public static class TransferEntry {
		private final Reference2ReferenceMap<Direction, TransferType> types = new Reference2ReferenceOpenHashMap<>(6, 1);

		private final ComponentKey<?> componentKey;

		public TransferEntry(ComponentKey<?> componentKey) {
			for (Direction direction : Direction.values()) {
				this.set(direction, TransferType.NONE);
			}

			this.componentKey = componentKey;
		}

		/** Returns this entry's {@link ComponentKey}. */
		public ComponentKey<?> getComponentKey() {
			return componentKey;
		}

		/** Sets the {@link TransferType} of the given {@link Direction}
		 * to the specified value. */
		public void set(Direction direction, TransferType type) {
			types.put(direction, type);
		}

		/** Returns the {@link TransferType} of the given {@link Direction}. */
		public TransferType get(Direction origin) {
			return types.get(origin);
		}

		/** Deserializes this {@link TransferEntry} from a {@link CompoundTag}. */
		public void fromTag(CompoundTag tag) {
			for (String directionKey : tag.getKeys()) {
				if (tag.contains(directionKey)) {
					types.put(DirectionUtilities.byNameOrId(directionKey), TransferType.valueOf(tag.getString(directionKey)));
				}
			}
		}

		/** Serializes this {@link TransferEntry} to a {@link CompoundTag}. */
		public CompoundTag toTag() {
			CompoundTag tag = new CompoundTag();

			for (Map.Entry<Direction, TransferType> entry : types.entrySet()) {
				if (entry.getValue() != TransferType.NONE)
					tag.putString(String.valueOf(entry.getKey().getName()), entry.getValue().toString());
			}

			return tag;
		}
	}
}
