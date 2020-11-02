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
import net.minecraft.block.FacingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.component.world.ChunkAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergyConsumedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.FluidSizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

public class VentBlockEntity extends ComponentEnergyFluidBlockEntity implements FluidSizeProvider, EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	public VentBlockEntity() {
		super(AstromineTechnologiesBlocks.VENT, AstromineTechnologiesBlockEntityTypes.VENT);

		getFluidComponent().getFirst().setSize(new Fraction(AstromineConfig.get().ventFluid, 1));
	}

	@Override
	public FluidComponent createFluidComponent() {
		FluidComponent fluidComponent = SimpleFluidComponent.of(1);
		fluidComponent.getFirst().setSize(getFluidSize());
		return fluidComponent;
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public double getEnergySize() {
		return AstromineConfig.get().ventEnergy;
	}

	@Override
	public Fraction getFluidSize() {
		return Fraction.of(AstromineConfig.get().ventFluid, 1);
	}

	@Override
	public double getMachineSpeed() {
		return AstromineConfig.get().ventSpeed;
	}

	@Override
	public double getEnergyConsumed() {
		return AstromineConfig.get().ventEnergyConsumed;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;

		FluidComponent fluidComponent = getFluidComponent();

		if (fluidComponent != null) {
			EnergyVolume energyVolume = getEnergyComponent().getVolume();
			if (energyVolume.hasStored(Fraction.of(1, 8))) {
				BlockPos position = getPos();

				Direction direction = world.getBlockState(position).get(FacingBlock.FACING);

				BlockPos output = position.offset(direction);

				if (energyVolume.hasStored(getEnergyConsumed()) && (world.getBlockState(output).isAir() || world.getBlockState(output).isSideSolidFullSquare(world, pos, direction.getOpposite()))) {
					ChunkAtmosphereComponent atmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(getPos()));

					FluidVolume centerVolume = fluidComponent.getFirst();

					if (ChunkAtmosphereComponent.isInChunk(world.getChunk(output).getPos(), pos)) {
						FluidVolume sideVolume = atmosphereComponent.get(output);

						if ((sideVolume.test(centerVolume.getFluid())) && sideVolume.smallerThan(centerVolume.getAmount())) {
							centerVolume.add(sideVolume, Fraction.of(1, 8));

							atmosphereComponent.add(output, sideVolume);

							energyVolume.minus(getEnergyConsumed());

							tickActive();
						} else {
							tickInactive();
						}
					} else {
						ChunkPos neighborPos = ChunkAtmosphereComponent.getNeighborFromPos(world.getChunk(output).getPos(), output);

						ChunkAtmosphereComponent neighborAtmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(neighborPos.x, neighborPos.z));

						FluidVolume sideVolume = neighborAtmosphereComponent.get(output);

						if ((centerVolume.test(sideVolume.getFluid())) && sideVolume.smallerThan(centerVolume.getAmount())) {
							centerVolume.add(sideVolume, Fraction.of(1, 8));

							neighborAtmosphereComponent.add(output, sideVolume);

							energyVolume.minus(getEnergyConsumed());

							tickActive();
						} else {
							tickInactive();
						}
					}
				} else {
					tickInactive();
				}
			} else {
				tickInactive();
			}
		}
	}
}
