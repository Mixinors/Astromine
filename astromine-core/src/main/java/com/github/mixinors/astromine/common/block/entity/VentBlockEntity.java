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

import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.component.world.ChunkAtmosphereComponent;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergyConsumedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.FluidSizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;

public class VentBlockEntity extends ComponentEnergyFluidBlockEntity implements FluidSizeProvider, EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	public VentBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.VENT, blockPos, blockState);

		getFluidComponent().getFirst().setSize(AMConfig.get().ventFluid);
	}

	@Override
	public SimpleFluidStorage createFluidComponent() {
		SimpleFluidStorage fluidStorage = SimpleDirectionalFluidComponent.of(this, 1);
		fluidStorage.getFirst().setSize(getFluidSize());
		return fluidStorage;
	}

	@Override
	public EnergyStore createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public long getEnergySize() {
		return AMConfig.get().ventEnergy;
	}

	@Override
	public long getFluidSize() {
		return AMConfig.get().ventFluid;
	}

	@Override
	public double getMachineSpeed() {
		return AMConfig.get().ventSpeed;
	}

	@Override
	public long getEnergyConsumed() {
		return AMConfig.get().ventEnergyConsumed;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		SimpleFluidStorage fluidStorage = getFluidComponent();

		if (fluidStorage != null) {
			EnergyVolume energyVolume = getEnergyComponent().getVolume();
			if (energyVolume.hasStored(128)) {
				BlockPos position = getPos();

				Direction direction = world.getBlockState(position).get(FacingBlock.FACING);

				BlockPos output = position.offset(direction);

				if (energyVolume.hasStored(getEnergyConsumed()) && (world.getBlockState(output).isAir() || world.getBlockState(output).isSideSolidFullSquare(world, pos, direction.getOpposite()))) {
					ChunkAtmosphereComponent atmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(getPos()));

					FluidVolume centerVolume = fluidStorage.getFirst();

					if (ChunkAtmosphereComponent.isInChunk(world.getChunk(output).getPos(), pos)) {
						FluidVolume sideVolume = atmosphereComponent.get(output);

						if ((sideVolume.test(centerVolume.getFluid())) && sideVolume.smallerThan(centerVolume.getAmount())) {
							centerVolume.give(sideVolume, FluidVolume.BUCKET / 9L);

							atmosphereComponent.add(output, sideVolume);

							energyVolume.take(getEnergyConsumed());

							isActive = true;
						} else {
							isActive = false;
						}
					} else {
						ChunkPos neighborPos = ChunkAtmosphereComponent.getNeighborFromPos(world.getChunk(output).getPos(), output);

						ChunkAtmosphereComponent neighborAtmosphereComponent = ChunkAtmosphereComponent.get(world.getChunk(neighborPos.x, neighborPos.z));

						FluidVolume sideVolume = neighborAtmosphereComponent.get(output);

						if ((centerVolume.test(sideVolume.getFluid())) && sideVolume.smallerThan(centerVolume.getAmount())) {
							centerVolume.give(sideVolume, FluidVolume.BUCKET / 9L);

							neighborAtmosphereComponent.add(output, sideVolume);

							energyVolume.take(getEnergyConsumed());

							isActive = true;
						} else {
							isActive = false;
						}
					}
				} else {
					isActive = false;
				}
			} else {
				isActive = false;
			}
		}
	}
}
