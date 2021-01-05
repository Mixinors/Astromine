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

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.general.SimpleEnergyComponent;
import com.github.chainmailstudios.astromine.common.component.general.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergyConsumedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import static com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks.PUMP_PIPE;

public class FluidPumpBlockEntity extends ComponentEnergyFluidBlockEntity implements EnergySizeProvider, SpeedProvider, EnergyConsumedProvider {
	private Fraction cooldown = Fraction.EMPTY;
	private BlockPos pumpPos = null;
	private Deque<BlockPos> fluidsToDrain;
	private Block pipe = PUMP_PIPE;

	public FluidPumpBlockEntity() {
		super(AstromineTechnologiesBlockEntityTypes.FLUID_PUMP);
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
		return AstromineConfig.get().fluidCollectorEnergyConsumed;
	}

	@Override
	public double getEnergySize() {
		return AstromineConfig.get().fluidCollectorEnergy;
	}

	@Override
	public double getMachineSpeed() {
		return AstromineConfig.get().fluidCollectorSpeed / 4;
	}

	@Override
	public void tick() {
		super.tick();

		if (pumpPos == null) {
			pumpPos = this.getPos().offset(Direction.DOWN);
		}

		if (world == null || world.isClient || !tickRedstone())
			return;

		FluidComponent fluidComponent = getFluidComponent();

		EnergyComponent energyComponent = getEnergyComponent();

		if (fluidComponent != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			if (energyVolume.getAmount() < getEnergyConsumed()) {
				cooldown = Fraction.EMPTY;

				tickInactive();
			} else {
				tickActive();

				cooldown = cooldown.add(Fraction.ofDecimal(1.0D / getMachineSpeed()));

				if (cooldown.biggerOrEqualThan(Fraction.of(1))) {
					cooldown = Fraction.EMPTY;
					if (extendPumpAndFindFluid()) {
						FluidVolume fluidVolume = fluidComponent.getFirst();
						if (fluidsToDrain == null) {
							fluidsToDrain = searchForFluids(pumpPos);
						}
						if (fluidsToDrain.isEmpty()) {
							fluidsToDrain = null;
						} else {
							BlockPos currentPos = fluidsToDrain.peekLast();
							tryDrainFluidAndUpdateVolumes(currentPos, fluidVolume, energyVolume);
							fluidsToDrain.removeLast();
						}
					}
				}
			}
		}
	}

	private boolean extendPumpAndFindFluid() {
		BlockState targetBlockState = world.getBlockState(pumpPos);
		Block targetBlock = targetBlockState.getBlock();
		BlockPos current = pos.offset(Direction.DOWN);
		while (!current.equals(pumpPos)) {
			if (world.getBlockState(current).isAir()) {
				world.setBlockState(current, pipe.getDefaultState());
			}
			current = current.offset(Direction.DOWN);
		}

		if (targetBlock instanceof FluidDrainable) {
			return true;
		}
		if (targetBlock instanceof AirBlock) {
		    pumpPos = pumpPos.offset(Direction.DOWN);
		}
		return false;
	}

	public void removePipe() {
		BlockPos current = pos.offset(Direction.DOWN);
		while (!current.equals(pumpPos)) {
			if (!world.getBlockState(current).isAir()) {
			    world.breakBlock(current, false);
			}
			current = current.offset(Direction.DOWN);
		}
	}

	private Deque<BlockPos> searchForFluids(BlockPos startPos) {
		FluidState startFluidState = world.getFluidState(startPos);
		Deque<BlockPos> fluidsToDrain = new ArrayDeque();
		Deque<BlockPos> fluidsToSearch = new ArrayDeque<>();
		HashSet<BlockPos> searchedPos = new HashSet();
		Fluid fluid = startFluidState.getFluid();

		fluidsToSearch.add(startPos);
		searchHelper(fluid, fluidsToDrain, fluidsToSearch, searchedPos);
		return fluidsToDrain;
	}

	private void searchHelper(Fluid fluid, Deque<BlockPos> fluidsToDrain, Deque<BlockPos> fluidsToSearch, Set<BlockPos> searchedPos) {

		while (!fluidsToSearch.isEmpty() && searchedPos.size() < 4096) {
			BlockPos startPos = fluidsToSearch.pop();
			BlockState startBlockState = world.getBlockState(startPos);
			Block startBlock = startBlockState.getBlock();
			if (startBlock instanceof FluidDrainable) {

				FluidState startFluidState = world.getFluidState(startPos);
				if (startFluidState.isStill() && fluid.equals(startFluidState.getFluid())) {
					fluidsToDrain.add(startPos);
				}

				BlockPos[] neighboringPos = {
						startPos.offset(Direction.UP),
						startPos.offset(Direction.NORTH),
						startPos.offset(Direction.SOUTH),
						startPos.offset(Direction.EAST),
						startPos.offset(Direction.WEST)
				};
				for (BlockPos pos : neighboringPos) {
					if (!searchedPos.contains(pos)) {
						fluidsToSearch.addLast(pos);
						searchedPos.add(pos);
					}
				}
			}
		}
	}

	private boolean tryDrainFluidAndUpdateVolumes(BlockPos targetPos, FluidVolume fluidVolume, EnergyVolume energyVolume) {
		BlockState targetBlockState = world.getBlockState(targetPos);
		FluidState targetFluidState = world.getFluidState(targetPos);

		Block targetBlock = targetBlockState.getBlock();
		if (targetBlock instanceof FluidDrainable && targetFluidState.isStill()) {
			FluidVolume toInsert = FluidVolume.of(Fraction.BUCKET, targetFluidState.getFluid());

			if (toInsert.test(fluidVolume)) {
				fluidVolume.take(toInsert, toInsert.getAmount());

				energyVolume.take(getEnergyConsumed());

				((FluidDrainable)targetBlock).tryDrainFluid(world, targetPos, targetBlockState);
				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 0, 1);
				return true;
			}
		}
		return false;
	}

}
