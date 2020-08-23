package com.github.chainmailstudios.astromine.foundations.common.block.altar.entity;

import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

public class ItemDisplayerBlockEntity extends BlockEntity implements ItemInventoryFromInventoryComponent, Tickable, BlockEntityClientSerializable {
	private int spinAge;
	private int lastSpinAddition;
	private int yAge;
	public BlockPos parent;
	private ItemInventoryComponent inventory = new SimpleItemInventoryComponent(1).withListener(component -> {
		if (hasWorld() && !world.isClient) {
			sync();
			world.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1, 1);
		}
	});

	public ItemDisplayerBlockEntity() {
		super(AstromineFoundationsBlockEntityTypes.ITEM_DISPLAYER);
	}

	@Override
	public ItemInventoryComponent getItemComponent() {
		return inventory;
	}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		sync();
	}

	@Override
	public void tick() {
		lastSpinAddition= 1;
		spinAge++;
		yAge++;

		if (parent != null) {
			AltarBlockEntity blockEntity = (AltarBlockEntity) world.getBlockEntity(parent);
			spinAge += blockEntity.craftingTicks / 5;
			lastSpinAddition += blockEntity.craftingTicks / 5;

			int velX = pos.getX() - parent.getX();
			int velY = pos.getY() - parent.getY();
			int velZ = pos.getZ() - parent.getZ();
			world.addParticle(ParticleTypes.ENCHANT, parent.getX() + 0.5, parent.getY() + 1.8, parent.getZ() + 0.5, velX, velY - 1.3, velZ);
		}
	}

	public int getSpinAge() {
		return spinAge;
	}

	public int getLastSpinAddition() {
		return lastSpinAddition;
	}

	public int getYAge() {
		return yAge;
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
		if (tag.contains("parent"))
			parent = BlockPos.fromLong(tag.getLong("parent"));
		else parent = null;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		inventory.toTag(tag);
		if (parent != null)
			tag.putLong("parent", parent.asLong());
		return super.toTag(tag);
	}

	public void onRemove() {
		if (parent != null) {
			AltarBlockEntity blockEntity = (AltarBlockEntity) world.getBlockEntity(parent);
			blockEntity.onRemove();
		}
	}
}
