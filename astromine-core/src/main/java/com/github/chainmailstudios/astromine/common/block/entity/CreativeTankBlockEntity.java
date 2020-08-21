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

package com.github.chainmailstudios.astromine.common.block.entity;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;

public class CreativeTankBlockEntity extends ComponentFluidBlockEntity implements Tickable {
	public static final Identifier FLUID_CHANGE_PACKET = AstromineCommon.identifier("fluid_change_component");

	public CreativeTankBlockEntity() {
		super(AstromineBlockEntityTypes.CREATIVE_TANK);

		addConsumer(FLUID_CHANGE_PACKET, (buffer, context) -> {
			Identifier fluidIdentifier = buffer.readIdentifier();

			Fluid fluid = Registry.FLUID.get(fluidIdentifier);

			fluidComponent.setVolume(0, new FluidVolume(fluid, new Fraction(Integer.MAX_VALUE, 1)));

			sync();
		});

		fluidComponent.getVolume(0).setSize(new Fraction(Integer.MAX_VALUE, 1));
	}

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		return new SimpleFluidInventoryComponent(1);
	}

	@Override
	public void tick() {
		fluidComponent.getVolume(0).setFraction(new Fraction(Integer.MAX_VALUE, 1));
	}
}
