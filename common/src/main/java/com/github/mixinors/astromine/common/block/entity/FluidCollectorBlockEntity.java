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
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.mixinors.astromine.cardinalcomponents.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.cardinalcomponents.common.component.base.FluidComponent;
import com.github.mixinors.astromine.techreborn.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergyConsumedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import org.jetbrains.annotations.NotNull;

public class FluidCollectorBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	private long cooldown = 0L;

	public FluidCollectorBlockEntity() {
		super(AMBlockEntityTypes.FLUID_EXTRACTOR);
	}

	@Override
	public FluidComponent createFluidComponent() {
		return FluidComponent.of(this, 1).withSizes(FluidVolume.BUCKET * 8);
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return EnergyComponent.of(getEnergySize());
	}

	@Override
	public double getEnergyConsumed() {
		return AMConfig.get().fluidCollectorEnergyConsumed;
	}

	@Override
	public double getEnergySize() {
		return AMConfig.get().fluidCollectorEnergy;
	}

	@Override
	public double getMachineSpeed() {
		return AMConfig.get().fluidCollectorSpeed;
	}

	@Override
	public void tick() {
		super.tick();
		
		if (!(world instanceof ServerWorld) || !tickRedstone())
			return;

		if (fluids != null) {
			if (energy.getAmount() < getEnergyConsumed()) {
				cooldown = 0L;

				tickInactive();
			} else {
				tickActive();

				cooldown = cooldown++;

				if (cooldown >= getMachineSpeed()) {
					cooldown = 0L;

					var volume = fluids.getFirst();

					var direction = getCachedState().get(HorizontalFacingBlock.FACING);

					var targetPos = pos.offset(direction);

					var targetBlockState = world.getBlockState(targetPos);
					var targetFluidState = world.getFluidState(targetPos);

					var targetBlock = targetBlockState.getBlock();

					if (targetBlock instanceof FluidDrainable drainable && targetFluidState.isStill()) {
						var volumeToInsert = FluidVolume.of(FluidVolume.BUCKET, targetFluidState.getFluid());

						if (volumeToInsert.test(volume)) {
							volume.take(volumeToInsert, volumeToInsert.getAmount());

							energy.take(getEnergyConsumed());

							drainable.tryDrainFluid(world, targetPos, targetBlockState);
							
							world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1, 1);
						}
					}
				}
			}
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putLong("Cooldown", cooldown);
		
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		cooldown = tag.getLong("Cooldown");
		
		super.fromTag(state, tag);
	}
}
