/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.callback.TransferEntryCallback;
import com.github.chainmailstudios.astromine.common.utilities.DirectionUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockEntityTransferComponent implements Component {
	private final Reference2ReferenceMap<ComponentKey<?>, TransferEntry> components = new Reference2ReferenceOpenHashMap<>();

	@Nullable
	public static <V> BlockEntityTransferComponent get(V v) {
		try {
			return AstromineComponents.BLOCK_ENTITY_TRANSFER_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}

	public TransferEntry get(ComponentKey<?> type) {
		return components.getOrDefault(type, null);
	}

	public Map<ComponentKey<?>, TransferEntry> get() {
		return components;
	}

	public void add(ComponentKey<?> type) {
		TransferEntry entry = new TransferEntry(type);

		TransferEntryCallback.EVENT.invoker().handle(entry);

		components.put(type, entry);
	}

	public boolean hasItem() {
		return components.containsKey(AstromineComponents.ITEM_INVENTORY_COMPONENT);
	}

	public boolean hasFluid() {
		return components.containsKey(AstromineComponents.FLUID_INVENTORY_COMPONENT);
	}

	public boolean hasEnergy() {
		return components.containsKey(AstromineComponents.ENERGY_INVENTORY_COMPONENT);
	}

	public TransferType getItem(Direction direction) {
		return components.get(AstromineComponents.ITEM_INVENTORY_COMPONENT).get(direction);
	}

	public TransferType getFluid(Direction direction) {
		return components.get(AstromineComponents.FLUID_INVENTORY_COMPONENT).get(direction);
	}

	public TransferType getEnergy(Direction direction) {
		return components.get(AstromineComponents.ENERGY_INVENTORY_COMPONENT).get(direction);
	}

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

	@Override
	public void writeToNbt(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		for (Map.Entry<ComponentKey<?>, TransferEntry> entry : components.entrySet()) {
			dataTag.put(entry.getKey().getId().toString(), entry.getValue().toTag(new CompoundTag()));
		}

		tag.put("data", dataTag);
	}

	public static class TransferEntry {
		public static final Direction[] DIRECTIONS = Direction.values();
		private final Reference2ReferenceMap<Direction, TransferType> types = new Reference2ReferenceOpenHashMap<>(6, 1);

		private final ComponentKey<?> componentKey;

		public TransferEntry(ComponentKey<?> componentKey) {
			for (Direction direction : DIRECTIONS) {
				this.set(direction, TransferType.NONE);
			}

			this.componentKey = componentKey;
		}

		public void set(Direction direction, TransferType type) {
			types.put(direction, type);
		}

		public TransferType get(Direction origin) {
			return types.get(origin);
		}

		public void fromTag(CompoundTag tag) {
			for (String directionKey : tag.getKeys()) {
				if (tag.contains(directionKey)) {
					types.put(DirectionUtilities.byNameOrId(directionKey), TransferType.valueOf(tag.getString(directionKey)));
				}
			}
		}

		public CompoundTag toTag(CompoundTag tag) {
			for (Map.Entry<Direction, TransferType> entry : types.entrySet()) {
				if (entry.getValue() != TransferType.NONE)
					tag.putString(String.valueOf(entry.getKey().getName()), entry.getValue().toString());
			}

			return tag;
		}

		public boolean areAllNone() {
			for (TransferType value : types.values()) {
				if (value != TransferType.NONE)
					return false;
			}
			return true;
		}

		public ComponentKey<?> getComponentKey() {
			return componentKey;
		}
	}
}
