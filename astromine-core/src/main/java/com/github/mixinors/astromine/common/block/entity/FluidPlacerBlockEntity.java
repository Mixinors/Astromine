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
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergyConsumedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class FluidPlacerBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, SpeedProvider, EnergyConsumedProvider, Storage<FluidVariant> {
	private long cooldown = 0L;

	public FluidPlacerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.FLUID_INSERTER, blockPos, blockState);
	}

	@Override
	public SimpleFluidStorage createFluidComponent() {
		SimpleFluidStorage fluidStorage = SimpleDirectionalFluidComponent.of(this, 1);
		fluidStorage.getFirst().setSize(FluidVolume.BOTTLE * 8L);
		return fluidStorage;
	}

	@Override
	public EnergyStore createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public long getEnergyConsumed() {
		return AMConfig.get().fluidPlacerEnergyConsumed;
	}

	@Override
	public long getEnergySize() {
		return AMConfig.get().fluidPlacerEnergy;
	}

	@Override
	public double getMachineSpeed() {
		return AMConfig.get().fluidPlacerSpeed;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		SimpleFluidStorage fluidStorage = getFluidComponent();

		EnergyStore energyComponent = getEnergyComponent();

		if (fluidStorage != null && energyComponent != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			if (energyVolume.getAmount() < getEnergyConsumed()) {
				cooldown = 0L;

				isActive = false;
			} else {
				isActive = true;

				cooldown++;

				if (cooldown >= getMachineSpeed()) {
					cooldown = 0L;

					FluidVolume fluidVolume = fluidStorage.getFirst();

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
	public void writeNbt(NbtCompound nbt) {
		nbt.putLong("cooldown", cooldown);
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		cooldown = nbt.getLong("cooldown");
		super.readNbt(nbt);
	}
	
	@Override
	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		transaction
	}
	
	@Override
	public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		return 0;
	}
	
	@Override
	public Iterator<StorageView<FluidVariant>> iterator(TransactionContext transaction) {
		return null;
	}
}
