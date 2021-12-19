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

import com.github.mixinors.astromine.common.block.entity.machine.EnergyConsumedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMConfig;
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

import java.util.List;
import java.util.Optional;

public class BlockBreakerBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	private long cooldown = 0L;

	public BlockBreakerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.BLOCK_BREAKER, blockPos, blockState);
	}

	@Override
	public ItemStore createItemComponent() {
		return SimpleDirectionalItemComponent.of(this, 1);
	}

	@Override
	public EnergyStore createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public long getEnergySize() {
		return AMConfig.get().blockBreakerEnergy;
	}

	@Override
	public double getEnergyConsumed() {
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

		ItemStore itemStorage = getItemComponent();

		EnergyStore energyComponent = getEnergyComponent();

		if (itemStorage != null && energyComponent != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			if (energyVolume.getAmount() < getEnergyConsumed()) {
				cooldown = 0L;

				isActive = false;
			} else {
				isActive = true;

				cooldown = cooldown++;

				if (cooldown >= getMachineSpeed()) {
					cooldown = 0L;

					ItemStack stored = itemStorage.getStack(0);

					Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);

					BlockPos targetPos = getPos().offset(direction);

					BlockState targetState = world.getBlockState(targetPos);

					if (targetState.isAir()) {
						isActive = false;
					} else {
						BlockEntity targetEntity = world.getBlockEntity(targetPos);

						List<ItemStack> drops = Block.getDroppedStacks(targetState, (ServerWorld) world, targetPos, targetEntity);

						ItemStack storedCopy = stored.copy();

						Optional<ItemStack> matching = drops.stream().filter(stack -> storedCopy.isEmpty() || (StackUtils.areItemsAndTagsEqual(stack, storedCopy) && storedCopy.getMaxCount() - storedCopy.getCount() > stack.getCount())).findFirst();

						matching.ifPresent(match -> {
							Pair<ItemStack, ItemStack> pair = StackUtils.merge(match, stored);
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

						energyVolume.take(getEnergyConsumed());
					}
				}
			}
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putLong("cooldown", cooldown);
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		cooldown = nbt.getLong("cooldown");
		super.readNbt(nbt);
	}
}
