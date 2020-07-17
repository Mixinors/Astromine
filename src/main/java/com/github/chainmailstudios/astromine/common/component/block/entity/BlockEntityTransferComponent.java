package com.github.chainmailstudios.astromine.common.component.block.entity;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.utilities.DirectionUtilities;
import com.github.chainmailstudios.astromine.common.utilities.MirrorUtilities;
import com.google.common.collect.Maps;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.Map;

public class BlockEntityTransferComponent implements Component {
	public static final Map<Identifier, TransferComponentInfo> INFOS = Maps.newHashMap();
	private final Map<Identifier, TransferEntry> components = Maps.newHashMap();

	public BlockEntityTransferComponent(ComponentType<?>... types) {
		Arrays.asList(types).forEach(this::add);
	}

	public TransferEntry get(ComponentType<?> type) {
		return get(type.getId());
	}

	public TransferEntry get(Identifier type) {
		return components.get(type);
	}

	public Map<Identifier, TransferEntry> get() {
		return components;
	}

	public void add(ComponentType<?> type) {
		add(type.getId());
	}

	public void add(Identifier type) {
		components.put(type, new TransferEntry());
	}

	@Override
	public void fromTag(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			Identifier keyId = new Identifier(key);
			TransferEntry entry = new TransferEntry();
			entry.fromTag(dataTag.getCompound(key));
			components.put(keyId, entry);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		for (Map.Entry<Identifier, TransferEntry> entry : components.entrySet()) {
			dataTag.put(entry.getKey().toString(), entry.getValue().toTag(new CompoundTag()));
		}

		tag.put("data", dataTag);

		return tag;
	}
	
	public interface TransferComponentInfo {
		Item getSymbol();

		TranslatableText getName();
	}

	public static final class TransferEntry {
		private final Map<Direction, TransferType> types = Maps.newHashMap();

		public TransferEntry() {
			Arrays.asList(Direction.values()).forEach(direction -> set(direction, TransferType.NONE));
		}

		public void set(Direction direction, TransferType type) {
			types.put(direction, type);
		}

		public TransferType get(Direction origin, Direction rotation) {
			return types.get(MirrorUtilities.rotate(origin, rotation));
		}

		public void fromTag(CompoundTag tag) {
			for (String directionKey : tag.getKeys()) {
				types.put(DirectionUtilities.byNameOrId(directionKey), TransferType.valueOf(tag.getString(directionKey)));
			}
		}

		public CompoundTag toTag(CompoundTag tag) {
			for (Map.Entry<Direction, TransferType> entry : types.entrySet()) {
				tag.putString(String.valueOf(entry.getKey().getName()), entry.getValue().toString());
			}

			return tag;
		}
	}
}
