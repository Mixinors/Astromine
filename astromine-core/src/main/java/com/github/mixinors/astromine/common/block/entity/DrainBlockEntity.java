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
import com.github.mixinors.astromine.registry.common.AMComponents;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.block.transfer.TransferType;

public class DrainBlockEntity extends ComponentFluidBlockEntity {
	public DrainBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.DRAIN, blockPos, blockState);

		for (Direction direction : Direction.values()) {
			getTransferComponent().get(AMComponents.FLUID_INVENTORY_COMPONENT).set(direction, TransferType.INPUT);
		}
	}

	@Override
	public FluidStore createFluidComponent() {
		FluidStore fluidComponent = SimpleFluidComponent.of(1).withInsertPredicate((direction, volume, slot) -> {
			return shouldRun();
		});

		fluidComponent.getFirst().setSize(Long.MAX_VALUE);

		return fluidComponent;
	}

	// TODO: Ticking stuff
	public void tick() {
		if (world == null)
			return;

		getFluidComponent().getFirst().setFluid(Fluids.EMPTY);
		getFluidComponent().getFirst().setAmount(0L);
	}
}
