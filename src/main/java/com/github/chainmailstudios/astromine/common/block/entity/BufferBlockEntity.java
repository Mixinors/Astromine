package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.utilities.type.BufferType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public class BufferBlockEntity extends DefaultedItemBlockEntity {
	private BufferType type;

	public BufferBlockEntity(BufferType type) {
		super(AstromineBlockEntityTypes.BUFFER);

		this.type = type;

		((SimpleItemInventoryComponent) itemComponent).resize(9 * type.getHeight());

		itemComponent.addListener(this::markDirty);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(1);
	}

	public BufferBlockEntity() {
		super(AstromineBlockEntityTypes.BUFFER);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("type", type.ordinal());
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		type = BufferType.values()[tag.getInt("type")];
		((SimpleItemInventoryComponent) itemComponent).resize(9 * type.getHeight());
		itemComponent.addListener(this::markDirty);
		super.fromTag(state, tag);
	}

	@Override
	protected @NotNull Map<NetworkType, Collection<NetworkMemberType>> createMemberProperties() {
		return ofTypes(AstromineNetworkTypes.ITEM, BUFFER);
	}
}
