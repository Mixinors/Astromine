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

import com.github.mixinors.astromine.cardinalcomponents.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.cardinalcomponents.common.component.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.techreborn.common.util.StackUtils;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergyConsumedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import org.jetbrains.annotations.NotNull;

public class BlockBreakerBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	private long cooldown = 0L;

	public BlockBreakerBlockEntity() {
		super(AMBlockEntityTypes.BLOCK_BREAKER);
	}

	@Override
	public ItemComponent createItemComponent() {
		return ItemComponent.of(this, 1);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return EnergyComponent.of(getEnergySize());
	}

	@Override
	public double getEnergySize() {
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

		if (!(world instanceof ServerWorld) || !tickRedstone())
			return;
		
		if (energy.getAmount() < getEnergyConsumed()) {
			cooldown = 0L;

			tickInactive();
		} else {
			tickActive();

			cooldown = cooldown++;

			if (cooldown >= getMachineSpeed()) {
				cooldown = 0L;

				var stored = items.getFirst();

				var direction = getCachedState().get(HorizontalFacingBlock.FACING);

				var targetPos = getPos().offset(direction);

				var targetState = world.getBlockState(targetPos);

				if (targetState.isAir()) {
					tickInactive();
				} else {
					var targetEntity = world.getBlockEntity(targetPos);

					var drops = Block.getDroppedStacks(targetState, (ServerWorld) world, targetPos, targetEntity);

					var storedCopy = stored.copy();

					var matching = drops.stream().filter(stack -> storedCopy.isEmpty() || (StackUtils.areItemsAndTagsEqual(stack, storedCopy) && storedCopy.getMaxCount() - storedCopy.getCount() > stack.getCount())).findFirst();

					matching.ifPresent(match -> {
						var pair = StackUtils.merge(match, stored);
						
						items.setFirst(pair.getRight());
						
						drops.remove(match);
						drops.add(pair.getLeft());
					});

					drops.forEach(stack -> {
						if (!stack.isEmpty()) {
							ItemScatterer.spawn(world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), stack);
						}
					});

					world.breakBlock(targetPos, false);

					energy.take(getEnergyConsumed());
				}
			}
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putLong("cooldown", cooldown);
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		cooldown = tag.getLong("cooldown");
		super.fromTag(state, tag);
	}
}
