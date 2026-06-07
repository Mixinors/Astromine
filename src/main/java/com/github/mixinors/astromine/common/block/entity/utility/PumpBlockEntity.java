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
import com.github.mixinors.astromine.common.util.DirectionUtils;
import com.github.mixinors.astromine.common.util.NbtUtils;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;

import java.util.ArrayDeque;
import java.util.Deque;

public class PumpBlockEntity extends ExtendedBlockEntity implements FluidStorageUtilityConfigProvider {
	public static final String COOLDOWN_KEY = "Cooldown";
	public static final String DEPTH_KEY = "Depth";
	
	public static final String POSITIONS_TO_PUMP_KEY = "PositionsToPump";
	
	public static final int OUTPUT_SLOT = 0;
	
	public static final int[] INSERT_SLOTS = new int[] { };
	
	public static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };
	
	public long cooldown = 0L;
	
	public double prevDepth = 0.0D;
	public double depth = 0.0D;
	
	public long age = 0L;
	
	private Deque<BlockPos> posToPump = new ArrayDeque<>();
	
	public PumpBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.PUMP, blockPos, blockState);
		
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
			age += 1;
			
			if (age % 5 == 0) {
				if (level.getBlockState(worldPosition.offset(0, (int) -Math.ceil(Math.max(depth, 1.0D) / 20.0D), 0)).isAir()) {
					depth += 2.5D;
				}
			}
			
			var consumed = getEnergyConsumed();
			
			if (energyStorage.amount < consumed) {
				cooldown = 0L;
				
				active = false;
			} else if (!posToPump.isEmpty()) {
				if (cooldown > getSpeed()) {
					cooldown = 0L;

					var targetPos = posToPump.pop();
					var targetBlockState = level.getBlockState(targetPos);
					var targetFluidState = targetBlockState.getFluidState();
					var targetBlock = targetBlockState.getBlock();
					var targetFluid = targetFluidState.getType();
					var outputStorage = fluidStorage.getStorage(OUTPUT_SLOT);
					var bucket = new FluidStack(targetFluid, FluidType.BUCKET_VOLUME);

					if (!targetFluidState.isEmpty() && targetFluidState.isSource() && targetBlock instanceof BucketPickup pickup && outputStorage.insert(bucket, FluidType.BUCKET_VOLUME, true, true) == FluidType.BUCKET_VOLUME) {
						outputStorage.insert(bucket, FluidType.BUCKET_VOLUME, true, false);
						pickup.pickupBlock(null, level, targetPos, targetBlockState);

						level.playSound(null, getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1, 1);

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
				var posToCheck = new ArrayDeque<BlockPos>();

				posToCheck.add(getBlockPos().offset(0, (int) -Math.ceil(depth / 20.0D), 0));

				var mainCheckFluidState = level.getFluidState(posToCheck.getLast());

				if (!mainCheckFluidState.isEmpty()) {
					while (!posToCheck.isEmpty()) {
						var checkPos = posToCheck.pop();
						var checkBlockState = level.getBlockState(checkPos);
						var checkFluidState = checkBlockState.getFluidState();

						if (mainCheckFluidState.isEmpty() && !checkFluidState.isEmpty()) {
							mainCheckFluidState = checkFluidState;
						}

						if (!posToPump.contains(checkPos) && !posToCheck.contains(checkPos) && !checkFluidState.isEmpty() && checkFluidState.equals(mainCheckFluidState)) {
							for (var directions : DirectionUtils.VALUES) {
								posToCheck.add(checkPos.relative(directions));
							}

							posToPump.add(checkPos);
						}
					}
				}

				active = false;
			}
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		nbt.putLong(COOLDOWN_KEY, cooldown);
		nbt.putDouble(DEPTH_KEY, depth);
		
		var posToPumpList = new ListTag();
		
		for (var pos : posToPump) {
			posToPumpList.add(NbtUtils.writeBlockPos(pos));
		}
		
		nbt.put(POSITIONS_TO_PUMP_KEY, posToPumpList);
		
		super.saveAdditional(nbt, registries);
	}
	
	@Override
	protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
		cooldown = nbt.getLong(COOLDOWN_KEY);
		depth = nbt.getDouble(DEPTH_KEY);
		
		posToPump.clear();
		
		var posToPumpList = nbt.getList(POSITIONS_TO_PUMP_KEY, Tag.TAG_COMPOUND);
		
		for (var pos : posToPumpList) {
			posToPump.add(NbtUtils.readBlockPos((CompoundTag) pos));
		}
		
		super.loadAdditional(nbt, registries);
	}
	
	@Override
	public FluidStorageUtilityConfig getConfig() {
		return AMConfig.get().blocks.utilities.fluidCollector;
	}
}
