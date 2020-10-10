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
import com.github.chainmailstudios.astromine.common.utilities.DirectionUtilities;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BlockEntityTransferComponent implements Component {
	private final Reference2ReferenceMap<ComponentType<?>, TransferEntry> components = new Reference2ReferenceOpenHashMap<>();

	public TransferEntry get(ComponentType<?> type) {
		return components.computeIfAbsent(type, t -> new TransferEntry());
	}

	public Map<ComponentType<?>, TransferEntry> get() {
		return components;
	}

	public void add(ComponentType<?> type) {
		components.put(type, new TransferEntry());
	}

	@Override
	public void fromTag(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			Identifier keyId = new Identifier(key);
			TransferEntry entry = new TransferEntry();
			entry.fromTag(dataTag.getCompound(key));
			components.put(ComponentRegistry.INSTANCE.get(keyId), entry);
		}
	}

	@Override
	public @NotNull CompoundTag toTag(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		for (Map.Entry<ComponentType<?>, TransferEntry> entry : components.entrySet()) {
			dataTag.put(entry.getKey().getId().toString(), entry.getValue().toTag(new CompoundTag()));
		}

		tag.put("data", dataTag);

		return tag;
	}

	public static class TransferEntry {
		public static final Direction[] DIRECTIONS = Direction.values();
		private final Reference2ReferenceMap<Direction, TransferType> types = new Reference2ReferenceOpenHashMap<>(6, 1);

		public TransferEntry() {
			for (Direction direction : DIRECTIONS) {
				this.set(direction, TransferType.NONE);
			}
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
	}

	private static class ImmutableTransferEntry extends TransferEntry {
		private static final TransferEntry INSTANCE = new ImmutableTransferEntry();

		@Override
		public void set(Direction direction, TransferType type) {}

		@Override
		public TransferType get(Direction origin) {
			return TransferType.NONE;
		}

		@Override
		public void fromTag(CompoundTag tag) {}

		@Override
		public CompoundTag toTag(CompoundTag tag) {
			return tag;
		}

		@Override
		public boolean areAllNone() {
			return true;
		}
	}
}
