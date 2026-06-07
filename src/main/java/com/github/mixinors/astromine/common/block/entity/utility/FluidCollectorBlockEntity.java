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
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;

public class FluidCollectorBlockEntity extends ExtendedBlockEntity implements FluidStorageUtilityConfigProvider {
	public static final String COOLDOWN_KEY = "Cooldown";
	
	public static final int OUTPUT_SLOT = 0;
	
	public static final int[] INSERT_SLOTS = new int[] { };
	
	public static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };
	
	private long cooldown = 0L;
	
	public FluidCollectorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.FLUID_COLLECTOR, blockPos, blockState);
		
		energyStorage = new LongEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		fluidStorage = new SimpleFluidStorage(1, getFluidStorageSize()).extractPredicate((variant, slot) ->
				slot == OUTPUT_SLOT
		).insertPredicate((variant, slot) ->
				false
		).listener(() -> {
			setChanged();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
		
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (level == null || level.isClientSide || !shouldRun()) {
			return;
		}
		
		if (fluidStorage != null && energyStorage != null) {
			var consumed = getEnergyConsumed();
			
			if (energyStorage.amount < consumed) {
				cooldown = 0L;
				
				active = false;
			} else {
				var direction = getBlockState().getValue(HorizontalDirectionalBlock.FACING);
				var targetPos = worldPosition.relative(direction);
				var targetBlockState = level.getBlockState(targetPos);
				var targetFluidState = level.getFluidState(targetPos);
				var targetBlock = targetBlockState.getBlock();
				
				if (targetBlock instanceof BucketPickup && targetFluidState.isSource()) {
					if (cooldown >= getSpeed()) {
						cooldown = 0L;
						
						var targetFluid = targetFluidState.getType();
						var outputStorage = fluidStorage.getStorage(OUTPUT_SLOT);
						var collectedStack = new FluidStack(targetFluid, FluidType.BUCKET_VOLUME);
						
						if (outputStorage.insert(collectedStack, FluidType.BUCKET_VOLUME, true, true) == FluidType.BUCKET_VOLUME) {
							outputStorage.insert(collectedStack, FluidType.BUCKET_VOLUME, true, false);
							
							((BucketPickup) targetBlock).pickupBlock(null, level, targetPos, targetBlockState);
							
							level.playSound(null, worldPosition, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1, 1);
							
							energyStorage.amount -= consumed;
							
							cooldown = 0L;
						} else {
							active = false;
						}
					} else {
						++cooldown;
						
						active = true;
					}
				} else {
					active = false;
				}
			}
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		nbt.putLong(COOLDOWN_KEY, cooldown);
		
		super.saveAdditional(nbt, registries);
	}
	
	@Override
	protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
		cooldown = nbt.getLong(COOLDOWN_KEY);
		
		super.loadAdditional(nbt, registries);
	}
	
	@Override
	public FluidStorageUtilityConfig getConfig() {
		return AMConfig.get().blocks.utilities.fluidCollector;
	}
}
