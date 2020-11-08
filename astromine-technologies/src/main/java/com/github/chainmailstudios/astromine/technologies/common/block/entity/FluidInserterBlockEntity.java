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

import com.github.chainmailstudios.astromine.common.component.inventory.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergyConsumedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

public class FluidInserterBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	private Fraction cooldown = Fraction.EMPTY;

	public FluidInserterBlockEntity() {
		super(AstromineTechnologiesBlocks.FLUID_INSERTER, AstromineTechnologiesBlockEntityTypes.FLUID_INSERTER);
	}

	@Override
	public FluidComponent createFluidComponent() {
		FluidComponent fluidComponent = SimpleFluidComponent.of(1);
		fluidComponent.getFirst().setSize(Fraction.of(8));
		return fluidComponent;
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public double getEnergyConsumed() {
		return AstromineConfig.get().fluidInserterEnergyConsumed;
	}

	@Override
	public double getEnergySize() {
		return AstromineConfig.get().fluidInserterEnergy;
	}

	@Override
	public double getMachineSpeed() {
		return AstromineConfig.get().fluidInserterSpeed;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;

		FluidComponent fluidComponent = getFluidComponent();

		if (fluidComponent != null) {
			EnergyVolume energyVolume = getEnergyComponent().getVolume();
			if (energyVolume.getAmount() < getEnergyConsumed()) {
				cooldown = Fraction.EMPTY;

				tickInactive();
			} else {
				tickActive();

				cooldown = cooldown.add(Fraction.ofDecimal(1.0D / getMachineSpeed()));

				cooldown.ifBiggerOrEqualThan(Fraction.of(1), () -> {
					cooldown = Fraction.EMPTY;

					FluidVolume fluidVolume = fluidComponent.getFirst();

					Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);

					BlockPos targetPos = pos.offset(direction);

					BlockState targetState = world.getBlockState(targetPos);

					if (targetState.isAir()) {
						if (fluidVolume.hasStored(Fraction.BUCKET)) {
							FluidVolume toInsert = FluidVolume.of(Fraction.BUCKET, fluidVolume.getFluid());

							fluidVolume.minus(Fraction.BUCKET);

							energyVolume.minus(getEnergyConsumed());

							world.setBlockState(targetPos, toInsert.getFluid().getDefaultState().getBlockState());
							world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);
						}
					}
				});
			}
		}
	}
}
