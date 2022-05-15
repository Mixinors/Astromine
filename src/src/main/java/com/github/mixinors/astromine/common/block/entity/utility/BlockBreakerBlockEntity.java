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
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class BlockBreakerBlockEntity extends ExtendedBlockEntity implements UtilityConfigProvider<UtilityConfig> {
	public static final String COOLDOWN_KEY = "Cooldown";
	
	public static final int OUTPUT_SLOT = 0;
	
	public static final int[] INSERT_SLOTS = new int[] { };
	
	public static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };
	
	private long cooldown = 0L;
	
	public BlockBreakerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.BLOCK_BREAKER, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		itemStorage = new SimpleItemStorage(1).extractPredicate((variant, slot) ->
				slot == OUTPUT_SLOT
		).insertPredicate((variant, slot) ->
				false
		).listener(() -> {
			markDirty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (world == null || world.isClient || !shouldRun()) {
			return;
		}
		
		if (itemStorage != null && energyStorage != null) {
			var consumed = getEnergyConsumed();
			
			if (energyStorage.getAmount() < consumed) {
				cooldown = 0L;
				
				active = false;
			} else {
				try (var transaction = Transaction.openOuter()) {
					if (energyStorage.amount >= consumed) {
						var outputStorage = itemStorage.getStorage(OUTPUT_SLOT);
						
						var stored = outputStorage.getStack();
						
						var direction = getCachedState().get(HorizontalFacingBlock.FACING);
						
						var targetPos = getPos().offset(direction);
						
						var targetState = world.getBlockState(targetPos);
						
						if (!targetState.isAir() && targetState.getHardness(world, pos) <= Blocks.OBSIDIAN.getHardness()) {
							++cooldown;
							
							if (cooldown >= getSpeed()) {
								cooldown = 0;
								
								active = true;
								
								var targetEntity = world.getBlockEntity(targetPos);
								
								var drops = Block.getDroppedStacks(targetState, (ServerWorld) world, targetPos, targetEntity);
								
								var storedCopy = stored.copy();
								
								var matching = drops.stream().filter(stack -> storedCopy.isEmpty() || (StackUtils.areItemsAndTagsEqual(stack, storedCopy) && storedCopy.getMaxCount() - storedCopy.getCount() >= stack.getCount())).findFirst();
								
								matching.ifPresent(match -> {
									drops.remove(match);
									match.decrement((int) outputStorage.insert(ItemVariant.of(match), match.getCount(), transaction, true));
									drops.add(match);
								});
								
								var finished = true;
								
								for (var stack : drops) {
									if (!stack.isEmpty()) {
										finished = false;
										
										break;
									}
								}
								
								if (finished) {
									world.breakBlock(targetPos, false);
									
									energyStorage.amount -= consumed;
									
									transaction.commit();
								} else {
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
	public UtilityConfig getConfig() {
		return AMConfig.get().blocks.utilities.blockBreaker;
	}
}
