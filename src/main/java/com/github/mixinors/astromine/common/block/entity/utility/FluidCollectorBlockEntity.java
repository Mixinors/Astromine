/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.block.entity.utility;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.config.entry.utility.FluidStorageUtilityConfig;
import com.github.mixinors.astromine.common.provider.config.FluidStorageUtilityConfigProvider;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class FluidCollectorBlockEntity extends ExtendedBlockEntity implements FluidStorageUtilityConfigProvider {
	public static final String COOLDOWN_KEY = "Cooldown";
	
	public static final int OUTPUT_SLOT = 0;
	
	public static final int[] INSERT_SLOTS = new int[] { };
	
	public static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };
	
	private long cooldown = 0L;
	
	public FluidCollectorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.FLUID_COLLECTOR, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		fluidStorage = new SimpleFluidStorage(1, getFluidStorageSize()).extractPredicate((variant, slot) ->
				slot == OUTPUT_SLOT
		).insertPredicate((variant, slot) ->
				false
		).listener(() -> {
			markDirty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
		
		fluidStorage.getStorage(OUTPUT_SLOT).setCapacity(getFluidStorageSize());
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (world == null || world.isClient || !shouldRun()) {
			return;
		}
		
		if (fluidStorage != null && energyStorage != null) {
			var consumed = getEnergyConsumed();
			
			if (energyStorage.amount < consumed) {
				cooldown = 0L;
				
				active = false;
			} else {
				try (var transaction = Transaction.openOuter()) {
					if (energyStorage.amount >= consumed) {
						var direction = getCachedState().get(HorizontalFacingBlock.FACING);
						
						var targetPos = pos.offset(direction);
						
						var targetBlockState = world.getBlockState(targetPos);
						var targetFluidState = world.getFluidState(targetPos);
						
						var targetBlock = targetBlockState.getBlock();
						
						if (targetBlock instanceof FluidDrainable && targetFluidState.isStill()) {
							if (cooldown >= getSpeed()) {
								cooldown = 0L;
								
								var targetFluid = targetFluidState.getFluid();
								
								var outputStorage = fluidStorage.getStorage(OUTPUT_SLOT);
								
								if (outputStorage.insert(FluidVariant.of(targetFluid), FluidConstants.BUCKET, transaction, true) == FluidConstants.BUCKET) {
									((FluidDrainable) targetBlock).tryDrainFluid(world, targetPos, targetBlockState);
									
									world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1, 1);
									
									energyStorage.amount -= consumed;
									
									cooldown = 0L;
									
									transaction.commit();
								} else {
									active = false;
									
									transaction.abort();
								}
							} else {
								++cooldown;
								
								active = true;
							}
						} else {
							active = false;
							
							transaction.abort();
						}
					} else {
						active = false;
						
						transaction.abort();
					}
				}
			}
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putLong(COOLDOWN_KEY, cooldown);
		
		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		cooldown = nbt.getLong(COOLDOWN_KEY);
		
		super.readNbt(nbt);
	}
	
	@Override
	public FluidStorageUtilityConfig getConfig() {
		return AMConfig.get().blocks.utilities.fluidCollector;
	}
}
