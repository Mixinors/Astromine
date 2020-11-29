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

import com.github.chainmailstudios.astromine.common.component.general.*;
import com.github.chainmailstudios.astromine.common.component.general.base.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergyConsumedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import org.jetbrains.annotations.NotNull;

public class BlockPlacerBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	private Fraction cooldown = Fraction.EMPTY;

	public BlockPlacerBlockEntity() {
		super(AstromineTechnologiesBlockEntityTypes.BLOCK_PLACER);
	}

	@Override
	public ItemComponent createItemComponent() {
		return SimpleDirectionalItemComponent.of(this, 1);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public double getEnergySize() {
		return AstromineConfig.get().blockPlacerEnergy;
	}

	@Override
	public double getEnergyConsumed() {
		return AstromineConfig.get().blockPlacerEnergyConsumed;
	}

	@Override
	public double getMachineSpeed() {
		return AstromineConfig.get().blockPlacerSpeed;
	}

	@Override
	public void tick() {
		super.tick();

		if (level == null || level.isClientSide || !tickRedstone())
			return;

		ItemComponent itemComponent = getItemComponent();

		EnergyComponent energyComponent = getEnergyComponent();

		if (itemComponent != null && energyComponent != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			if (energyVolume.getAmount() < getEnergyConsumed()) {
				cooldown = Fraction.EMPTY;

				tickInactive();
			} else {
				tickActive();

				cooldown = cooldown.add(Fraction.ofDecimal(1.0D / getMachineSpeed()));

				if (cooldown.biggerOrEqualThan(Fraction.of(1))) {
					cooldown = Fraction.EMPTY;

					ItemStack stored = itemComponent.getFirst();

					Direction direction = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

					BlockPos targetPos = worldPosition.relative(direction);

					BlockState targetState = level.getBlockState(targetPos);

					if (stored.getItem() instanceof BlockItem && targetState.isAir()) {
						BlockState newState = ((BlockItem) stored.getItem()).getBlock().defaultBlockState();

						level.setBlockAndUpdate(targetPos, newState);

						stored.shrink(1);

						energyVolume.take(getEnergyConsumed());
					}
				}
			}
		}
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.put("cooldown", cooldown.toTag());
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, @NotNull CompoundTag tag) {
		cooldown = Fraction.fromTag(tag.getCompound("cooldown"));
		super.load(state, tag);
	}
}
