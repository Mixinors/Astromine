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

package com.github.mixinors.astromine.common.block.entity;

import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;

import me.shedaniel.architectury.hooks.BlockEntityHooks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.common.component.general.SimpleItemComponent;
import com.github.mixinors.astromine.common.component.general.compatibility.InventoryFromItemComponent;
import org.jetbrains.annotations.ApiStatus;

public class AltarPedestalBlockEntity extends BlockEntity implements InventoryFromItemComponent {
	public BlockPos parentPos;
	private int spinAge;
	private int lastSpinAddition;
	private int yAge;
	private ItemComponent inventory = SimpleItemComponent.of(1).withListener(inventory -> {
		if (hasWorld() && !world.isClient) {
			syncData();
			world.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1, 1);
		}
	});

	public AltarPedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.ALTAR_PEDESTAL.get(), blockPos, blockState);
	}

	@Override
	public ItemComponent getItemComponent() {
		return inventory;
	}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		syncData();
	}

	@Override
	public void tick() {
		lastSpinAddition = 1;
		spinAge++;
		yAge++;

		if (parentPos != null) {
			AltarBlockEntity blockEntity = getParent();
			if (blockEntity == null)
				onRemove();
			else {
				spinAge += blockEntity.craftingTicks / 5;
				lastSpinAddition += blockEntity.craftingTicks / 5;

				int velX = pos.getX() - parentPos.getX();
				int velY = pos.getY() - parentPos.getY();
				int velZ = pos.getZ() - parentPos.getZ();
				world.addParticle(ParticleTypes.ENCHANT, parentPos.getX() + 0.5, parentPos.getY() + 1.8, parentPos.getZ() + 0.5, velX, velY - 1.3, velZ);
			}
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
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		inventory.readFromNbt(tag);
		if (tag.contains("parent"))
			parentPos = BlockPos.fromLong(tag.getLong("parent"));
		else parentPos = null;
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		inventory.writeToNbt(tag);
		if (parentPos != null)
			tag.putLong("parent", parentPos.asLong());
		super.writeNbt(tag);
	}

	public void onRemove() {
		if (parentPos != null) {
			AltarBlockEntity blockEntity = (AltarBlockEntity) world.getBlockEntity(parentPos);
			if (blockEntity != null) {
				blockEntity.onRemove();
			}
		}
		parentPos = null;
	}

	public boolean hasParent() {
		return parentPos != null && getParent() != null;
	}

	public AltarBlockEntity getParent() {
		if (hasWorld() && parentPos != null) {
			BlockEntity be = world.getBlockEntity(parentPos);
			if (be instanceof AltarBlockEntity) {
				return (AltarBlockEntity)be;
			}
		}
		return null;
	}
}
