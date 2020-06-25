package com.github.chainmailstudios.astromine.common.component.block.entity;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.google.common.collect.Maps;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

import java.util.Map;

public class BlockEntityTransferComponent implements Component {
	private final Map<ComponentType<?>, TransferEntry> components = Maps.newHashMap();

	@Override
	public void fromTag(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			TransferEntry entry = new TransferEntry();
			entry.fromTag(dataTag.getCompound(key));
			components.put(ComponentRegistry.INSTANCE.stream().filter(component -> component.getRawId() == Integer.parseInt(key)).findFirst().get(), entry);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		for (Map.Entry<ComponentType<?>, TransferEntry> entry : components.entrySet()) {
			dataTag.put(String.valueOf(entry.getKey().getRawId()), entry.getValue().toTag(new CompoundTag()));
		}

		tag.put("data", dataTag);

		return tag;
	}

	private static final class TransferEntry {
		private final Map<Direction, TransferType> types = Maps.newHashMap();

		public void set(Direction direction, TransferType type) {
			types.put(direction, type);
		}

		public TransferType get(Direction direction) {
			return types.get(direction);
		}

		public void fromTag(CompoundTag tag) {
			for (String directionKey : tag.getKeys()) {
				types.put(Direction.valueOf(directionKey), TransferType.valueOf(tag.getString(directionKey)));
			}
		}

		public CompoundTag toTag(CompoundTag tag) {
			for (Map.Entry<Direction, TransferType> entry : types.entrySet()) {
				tag.putString(entry.getKey().toString(), entry.getValue().toString());
			}

			return tag;
		}
	}
}
