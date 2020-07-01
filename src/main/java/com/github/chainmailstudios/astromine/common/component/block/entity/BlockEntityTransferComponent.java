package com.github.chainmailstudios.astromine.common.component.block.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;

public class BlockEntityTransferComponent implements Component {
	private final Map<ComponentType<?>, TransferEntry> components = Maps.newHashMap();

	public BlockEntityTransferComponent(ComponentType<?>... types) {
		Arrays.asList(types).forEach(this::add);
	}

	public TransferEntry get(ComponentType<?> type) {
		return components.get(type);
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
			components.put(ComponentRegistry.INSTANCE.stream().filter(component -> component.getId().equals(keyId)).findFirst().get(), entry);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		for (Map.Entry<ComponentType<?>, TransferEntry> entry : components.entrySet()) {
			dataTag.put(entry.getKey().getId().toString(), entry.getValue().toTag(new CompoundTag()));
		}

		tag.put("data", dataTag);

		return tag;
	}

	public static final class TransferEntry {
		private final Map<Direction, TransferType> types = Maps.newHashMap();

		public TransferEntry() {
			Arrays.asList(Direction.values()).forEach(direction -> set(direction, TransferType.NONE));
		}

		public void set(Direction direction, TransferType type) {
			types.put(direction, type);
		}

		public TransferType get(Direction direction) {
			return types.get(direction);
		}

		public void fromTag(CompoundTag tag) {
			for (String directionKey : tag.getKeys()) {
				types.put(Direction.byName(directionKey), TransferType.valueOf(tag.getString(directionKey)));
			}
		}

		public CompoundTag toTag(CompoundTag tag) {
			for (Map.Entry<Direction, TransferType> entry : types.entrySet()) {
				tag.putString(entry.getKey().getName(), entry.getValue().toString());
			}

			return tag;
		}
	}
}
