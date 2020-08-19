package com.github.chainmailstudios.astromine.foundations.common.block.altar.entity;

import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class ItemDisplayerBlockEntity extends BlockEntity implements ItemInventoryFromInventoryComponent, Tickable, BlockEntityClientSerializable {
	private int age;
	private ItemInventoryComponent inventory = new SimpleItemInventoryComponent(1);

	public ItemDisplayerBlockEntity() {
		super(AstromineFoundationsBlockEntityTypes.ITEM_DISPLAYER);
	}

	@Override
	public ItemInventoryComponent getItemComponent() {
		return inventory;
	}

	@Override
	public void tick() {
		age++;
	}

	public int getAge() {
		return age;
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(null, compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		inventory.fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		inventory.toTag(tag);
		return super.toTag(tag);
	}
}
