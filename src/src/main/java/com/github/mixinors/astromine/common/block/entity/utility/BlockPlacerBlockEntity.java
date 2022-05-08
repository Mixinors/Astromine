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
import com.github.mixinors.astromine.common.config.entry.utility.UtilityConfig;
import com.github.mixinors.astromine.common.provider.config.UtilityConfigProvider;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class BlockPlacerBlockEntity extends ExtendedBlockEntity implements UtilityConfigProvider<UtilityConfig> {
	private long cooldown = 0L;
	
	public static final int INPUT_SLOT = 0;
	
	public static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	public static final int[] EXTRACT_SLOTS = new int[] { };
	
	public BlockPlacerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.BLOCK_PLACER, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		itemStorage = new SimpleItemStorage(1).extractPredicate((variant, slot) ->
				false
		).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			return variant.getItem() instanceof BlockItem;
		}).listener(() -> {
			markDirty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (world == null || world.isClient || !shouldRun()) {
			return;
		}
		
		if (itemStorage != null && itemStorage != null) {
			var consumed = getEnergyConsumed();
			
			if (energyStorage.getAmount() < consumed) {
				cooldown = 0L;
				
				isActive = false;
			} else {
				try (var transaction = Transaction.openOuter()) {
					if (energyStorage.amount >= consumed) {
						var stored = itemStorage.getStack(0);
						
						var direction = getCachedState().get(HorizontalFacingBlock.FACING);
						
						var targetPos = pos.offset(direction);
						
						var targetState = world.getBlockState(targetPos);
						
						if (stored.getItem() instanceof BlockItem blockItem) {
							var storedBlock = blockItem.getBlock();
							
							var storedState = blockItem.getBlock().getDefaultState();
							
							if (storedBlock instanceof DoorBlock || storedBlock instanceof SlabBlock || storedBlock instanceof PlantBlock || storedBlock instanceof BedBlock || storedBlock instanceof BannerBlock) {
								return;
							}
							
							if (storedState.canPlaceAt(world, targetPos) && targetState.isAir()) {
								if (cooldown >= getSpeed()) {
									world.setBlockState(targetPos, storedState);
									
									blockItem.getBlock().onPlaced(world, targetPos, storedState, null, stored);
									
									var inputStorage = itemStorage.getStorage(INPUT_SLOT);
									
									inputStorage.extract(inputStorage.getResource(), 1, transaction, true);
									
									energyStorage.amount -= consumed;
									
									cooldown = 0;
									
									transaction.commit();
								} else {
									++cooldown;
									
									isActive = true;
								}
							} else {
								isActive = false;
								
								transaction.abort();
							}
						}
					} else {
						isActive = false;
						
						transaction.abort();
					}
				}
			}
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putLong("Cooldown", cooldown);
		
		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		cooldown = nbt.getLong("Cooldown");
		
		super.readNbt(nbt);
	}
	
	@Override
	public UtilityConfig getConfig() {
		return AMConfig.get().blocks.utilities.blockPlacer;
	}
}
