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

package com.github.chainmailstudios.astromine.transportations.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class DrainBlockEntity extends ComponentFluidBlockEntity implements Tickable {
	public DrainBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.DRAIN);

		for (Direction direction : Direction.values()) {
			getTransferComponent().get(AstromineComponents.FLUID_INVENTORY_COMPONENT).set(direction, TransferType.INPUT);
		}
	}

	@Override
	public FluidComponent createFluidComponent() {
		FluidComponent fluidComponent = SimpleFluidComponent.of(1).withInsertPredicate((direction, volume, slot) -> {
			return tickRedstone();
		});

		fluidComponent.getFirst().setSize(Fraction.of(Long.MAX_VALUE));

		return fluidComponent;
	}

	@Override
	public void tick() {
		if (world == null)
			return;

		getFluidComponent().getFirst().setFluid(Fluids.EMPTY);
		getFluidComponent().getFirst().setAmount(Fraction.EMPTY);
	}
}
