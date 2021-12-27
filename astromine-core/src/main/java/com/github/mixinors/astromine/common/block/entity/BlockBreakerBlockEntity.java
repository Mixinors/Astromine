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

import java.util.List;
import java.util.Optional;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.block.entity.machine.EnergyConsumedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMConfig;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class BlockBreakerBlockEntity extends ExtendedBlockEntity implements EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	private long cooldown = 0L;

	public BlockBreakerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.BLOCK_BREAKER, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergySize(), Long.MAX_VALUE, Long.MAX_VALUE);
		
		itemStorage = new SimpleItemStorage(1);
	}

	@Override
	public long getEnergySize() {
		return AMConfig.get().blockBreakerEnergy;
	}

	@Override
	public long getEnergyConsumed() {
		return AMConfig.get().blockBreakerEnergyConsumed;
	}

	@Override
	public double getMachineSpeed() {
		return AMConfig.get().blockBreakerSpeed;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		if (itemStorage != null && energyStorage != null) {
			var consumed = getEnergyConsumed();
			
			if (energyStorage.getAmount() < consumed) {
				cooldown = 0L;

				isActive = false;
			} else {
				if (cooldown >= getMachineSpeed()) {
					try (var transaction = Transaction.openOuter()) {
						if (energyStorage.extract(consumed, transaction) == consumed) {
							var stored = itemStorage.getStack(0);
							
							var direction = getCachedState().get(HorizontalFacingBlock.FACING);
							
							var targetPos = getPos().offset(direction);
							
							var targetState = world.getBlockState(targetPos);

							if (!targetState.isAir()) {
								cooldown = 0;
								
								isActive = true;
								
								var targetEntity = world.getBlockEntity(targetPos);
								
								var drops = Block.getDroppedStacks(targetState, (ServerWorld) world, targetPos, targetEntity);
								
								var storedCopy = stored.copy();
								
								var matching = drops.stream().filter(stack -> storedCopy.isEmpty() || (StackUtils.areItemsAndTagsEqual(stack, storedCopy) && storedCopy.getMaxCount() - storedCopy.getCount() > stack.getCount())).findFirst();
								
								matching.ifPresent(match -> {
									var pair = StackUtils.merge(match, stored);
									
									itemStorage.setStack(0, pair.getRight());
									
									drops.remove(match);
									drops.add(pair.getLeft());
								});
								
								drops.forEach(stack -> {
									if (!stack.isEmpty()) {
										ItemScatterer.spawn(world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), stack);
									}
								});
								
								world.breakBlock(targetPos, false);
								
								transaction.commit();
							} else {
								isActive = false;
								
								transaction.abort();
							}
						}
					}
				} else {
					cooldown++;
					
					isActive = true;
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
}
