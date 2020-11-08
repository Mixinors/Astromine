/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.technologies.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergyConsumedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BlockBreakerBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	private Fraction cooldown = Fraction.EMPTY;

	public BlockBreakerBlockEntity() {
		super(AstromineTechnologiesBlocks.BLOCK_BREAKER, AstromineTechnologiesBlockEntityTypes.BLOCK_BREAKER);
	}

	@Override
	public ItemComponent createItemComponent() {
		return SimpleItemComponent.of(1);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public double getEnergySize() {
		return AstromineConfig.get().blockBreakerEnergy;
	}

	@Override
	public double getEnergyConsumed() {
		return AstromineConfig.get().blockBreakerEnergyConsumed;
	}

	@Override
	public double getMachineSpeed() {
		return AstromineConfig.get().blockBreakerSpeed;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;

		ItemComponent itemComponent = getItemComponent();

		if (itemComponent != null) {
			EnergyVolume energyVolume = getEnergyComponent().getVolume();
			if (energyVolume.getAmount() < getEnergyConsumed()) {
				cooldown = Fraction.EMPTY;

				tickInactive();
			} else {
				tickActive();

				cooldown = cooldown.add(Fraction.ofDecimal(1.0D / getMachineSpeed()));

				cooldown.ifBiggerOrEqualThan(Fraction.of(1), () -> {
					cooldown = Fraction.EMPTY;

					ItemStack stored = itemComponent.getFirst();

					Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);

					BlockPos targetPos = getPos().offset(direction);

					BlockState targetState = world.getBlockState(targetPos);

					if (targetState.isAir()) {
						tickInactive();
					} else {
						BlockEntity targetEntity = world.getBlockEntity(targetPos);

						List<ItemStack> drops = Block.getDroppedStacks(targetState, (ServerWorld) world, targetPos, targetEntity);

						ItemStack storedCopy = stored.copy();

						Optional<ItemStack> matching = drops.stream().filter(stack -> storedCopy.isEmpty() || StackUtilities.equalItemAndTag(stack, storedCopy)).findFirst();

						matching.ifPresent(match -> {
							Pair<ItemStack, ItemStack> pair = StackUtilities.merge(match, stored, match.getMaxCount(), stored.getMaxCount());
							itemComponent.setFirst(pair.getRight());
							drops.remove(match);
							drops.add(pair.getLeft());
						});

						drops.forEach(stack -> {
							if (!stack.isEmpty()) {
								ItemScatterer.spawn(world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), stack);
							}
						});

						world.breakBlock(targetPos, false);

						energyVolume.minus(getEnergyConsumed());
					}
				});
			}
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("cooldown", cooldown.toTag());
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		cooldown = Fraction.fromTag(tag.getCompound("cooldown"));
		super.fromTag(state, tag);
	}
}
