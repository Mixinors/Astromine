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
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.component.general.SimpleEnergyComponent;
import com.github.mixinors.astromine.common.component.general.SimpleFluidComponent;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergyConsumedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import org.jetbrains.annotations.NotNull;

public class FluidPlacerBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	private long cooldown = 0L;

	public FluidPlacerBlockEntity() {
		super(AMBlockEntityTypes.FLUID_INSERTER);
	}

	@Override
	public FluidComponent createFluidComponent() {
		FluidComponent fluidComponent = SimpleFluidComponent.of(1);
		fluidComponent.getFirst().setSize(FluidVolume.BOTTLE * 8L);
		return fluidComponent;
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public double getEnergyConsumed() {
		return AMConfig.get().fluidPlacerEnergyConsumed;
	}

	@Override
	public double getEnergySize() {
		return AMConfig.get().fluidPlacerEnergy;
	}

	@Override
	public double getMachineSpeed() {
		return AMConfig.get().fluidPlacerSpeed;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;

		FluidComponent fluidComponent = getFluidComponent();

		EnergyComponent energyComponent = getEnergyComponent();

		if (fluidComponent != null && energyComponent != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			if (energyVolume.getAmount() < getEnergyConsumed()) {
				cooldown = 0L;

				tickInactive();
			} else {
				tickActive();

				cooldown++;

				if (cooldown >= getMachineSpeed()) {
					cooldown = 0L;

					FluidVolume fluidVolume = fluidComponent.getFirst();

					Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);

					BlockPos targetPos = pos.offset(direction);

					BlockState targetState = world.getBlockState(targetPos);

					if (targetState.isAir()) {
						if (fluidVolume.hasStored(FluidVolume.BUCKET)) {
							FluidVolume toInsert = FluidVolume.of(FluidVolume.BUCKET, fluidVolume.getFluid());

							fluidVolume.take(FluidVolume.BUCKET);

							energyVolume.take(getEnergyConsumed());

							world.setBlockState(targetPos, toInsert.getFluid().getDefaultState().getBlockState());
							world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);
						}
					}
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
