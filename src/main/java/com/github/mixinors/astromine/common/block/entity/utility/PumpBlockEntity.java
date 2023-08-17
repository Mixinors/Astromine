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
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

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
	
	private Deque<BlockPos> posToPump = new ArrayDeque<>() {
		private final LongSet poses = new LongOpenHashSet();
		
		@Override
		public boolean add(@NotNull BlockPos blockPos) {
			return poses.add(blockPos.asLong()) && super.add(blockPos);
		}
		
		@Override
		public boolean contains(Object o) {
			return o instanceof BlockPos && poses.contains(((BlockPos) o).asLong());
		}
	};
	
	public PumpBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.PUMP, blockPos, blockState);
		
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
			age += 1;
			
			if (age % 5 == 0) {
				if (world.getBlockState(pos.add(0, (int) -Math.ceil(Math.max(depth, 1.0D) / 20.0D), 0)).isAir()) {
					depth += 2.5D;
				}
			}
			
			var consumed = getEnergyConsumed();
			
			if (energyStorage.amount < consumed) {
				cooldown = 0L;
				
				active = false;
			} else {
				try (var transaction = Transaction.openOuter()) {
					if (energyStorage.amount >= consumed) {
						if (!posToPump.isEmpty()) {
							if (cooldown > getSpeed()) {
								cooldown = 0L;
								
								var targetPos = posToPump.pop();
								
								var targetBlockState = world.getBlockState(targetPos);
								var targetFluidState = targetBlockState.getFluidState();
								
								var targetBlock = targetBlockState.getBlock();
								
								var targetFluid = targetFluidState.getFluid();
								
								var outputStorage = fluidStorage.getStorage(OUTPUT_SLOT);
								
								if (!targetFluidState.isEmpty() && targetFluidState.isStill() && outputStorage.insert(FluidVariant.of(targetFluid), FluidConstants.BUCKET, transaction, true) == FluidConstants.BUCKET) {
									((FluidDrainable) targetBlock).tryDrainFluid(world, targetPos, targetBlockState);
									
									world.playSound(null, getPos(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1, 1);
									
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
							var posesChecked = new LongOpenHashSet();
							var posToCheck = new ArrayDeque<BlockPos>();
							
							posToCheck.add(getPos().add(0, (int) -Math.ceil(depth / 20.0D), 0));
							
							var mainCheckFluidState = world.getFluidState(posToCheck.getLast());
							
							if (!mainCheckFluidState.isEmpty()) {
								while (!posToCheck.isEmpty()) {
									var checkPos = posToCheck.pop();
									var checkBlockState = world.getBlockState(checkPos);
									var checkFluidState = checkBlockState.getFluidState();
									
									if (mainCheckFluidState.isEmpty() && !checkFluidState.isEmpty()) {
										mainCheckFluidState = checkFluidState;
									}
									
									if (checkPos.getManhattanDistance(getPos()) > 10 + depth * 1.5) {
										continue;
									}
									
									if (!posToPump.contains(checkPos) && !posToCheck.contains(checkPos) && !checkFluidState.isEmpty() && checkFluidState.equals(mainCheckFluidState)) {
										for (var directions : DirectionUtils.VALUES) {
											posToCheck.add(checkPos.offset(directions));
										}
										
										posToPump.add(checkPos);
									}
								}
							}
							
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
		nbt.putDouble(DEPTH_KEY, depth);
		
		var posToPumpList = new NbtList();
		
		for (var pos : posToPump) {
			posToPumpList.add(NbtHelper.fromBlockPos(pos));
		}
		
		nbt.put(POSITIONS_TO_PUMP_KEY, posToPumpList);
		
		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		cooldown = nbt.getLong(COOLDOWN_KEY);
		depth = nbt.getDouble(DEPTH_KEY);
		
		posToPump.clear();
		
		var posToPumpList = nbt.getList(POSITIONS_TO_PUMP_KEY, NbtElement.COMPOUND_TYPE);
		
		for (var pos : posToPumpList) {
			posToPump.add(NbtHelper.toBlockPos((NbtCompound) pos));
		}
		
		super.readNbt(nbt);
	}
	
	@Override
	public FluidStorageUtilityConfig getConfig() {
		return AMConfig.get().blocks.utilities.fluidCollector;
	}
}
