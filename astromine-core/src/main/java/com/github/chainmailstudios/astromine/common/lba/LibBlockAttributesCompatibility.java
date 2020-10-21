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

package com.github.chainmailstudios.astromine.common.lba;

import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import alexiil.mc.lib.attributes.Attribute;
import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidInvTankChangeListener;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class LibBlockAttributesCompatibility {
	public static void initialize() {
		FluidAttributes.forEachInv(LibBlockAttributesCompatibility::appendAdder);
	}

	private static <T> void appendAdder(Attribute<T> attribute) {
		attribute.appendBlockAdder(LibBlockAttributesCompatibility::append);
	}

	private static <T> void append(World world, BlockPos blockPos, BlockState state, AttributeList<T> list) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);

		if (blockEntity instanceof ComponentProvider) {
			@Nullable
			Direction direction = list.getTargetSide();

			if (direction != null) {
				BlockEntityTransferComponent transferComponent = BlockEntityTransferComponent.get(blockEntity);

				// This does not check canInsert or canExtract; because I do not know how the hell to do that with LBA.
				if (transferComponent != null && !transferComponent.get(AstromineComponents.FLUID_INVENTORY_COMPONENT).get(direction).isNone()) {
					FluidComponent component = FluidComponent.get(blockEntity);

					if (component != null) {
						list.offer(new LibBlockAttributesWrapper(component));
					}
				}
			}
		}
	}

	private static alexiil.mc.lib.attributes.fluid.volume.FluidVolume wrapLibBlockAttributes(FluidVolume volume) {
		if (FluidKeys.get(volume.getFluid()).isEmpty()) {
			return FluidKeys.EMPTY.withAmount(FluidAmount.ZERO);
		} else {
			return FluidKeys.get(volume.getFluid()).withAmount(wrapLibBlockAttributes(volume.getAmount().copy()));
		}
	}

	private static Optional<FluidVolume> wrapVolumeToAstromine(alexiil.mc.lib.attributes.fluid.volume.FluidVolume volume) {
		if (volume.getRawFluid() == null)
			return Optional.empty();

		return Optional.of(FluidVolume.of(wrapVolumeToAstromine(volume.amount()), volume.getRawFluid()));
	}

	private static FluidAmount wrapLibBlockAttributes(Fraction fraction) {
		return FluidAmount.of(fraction.getNumerator(), fraction.getDenominator());
	}

	private static Fraction wrapVolumeToAstromine(FluidAmount amount) {
		return Fraction.of(amount.whole, amount.numerator, amount.denominator);
	}

	private static class LibBlockAttributesWrapper implements FixedFluidInv {
		private final FluidComponent component;

		public LibBlockAttributesWrapper(FluidComponent component) {
			this.component = component;
		}

		private void validateTankIndex(int tank) {
			if (tank < 0 || tank >= getTankCount()) {
				throw new IndexOutOfBoundsException("Tank (" + tank + ") was out of bounds [0, " + getTankCount() + ")");
			}
		}

		@Override
		public int getTankCount() {
			return component.getSize();
		}

		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume getInvFluid(int tank) {
			validateTankIndex(tank);
			return wrapLibBlockAttributes(component.getVolume(tank));
		}

		@Override
		public boolean setInvFluid(int tank, alexiil.mc.lib.attributes.fluid.volume.FluidVolume fluidVolume, Simulation simulation) {
			if (!isFluidValidForTank(tank, fluidVolume.getFluidKey()))
				return false;

			Optional<FluidVolume> optionalFluidVolume = wrapVolumeToAstromine(fluidVolume);

			if (!optionalFluidVolume.isPresent())
				return false;

			FluidVolume incoming = optionalFluidVolume.get();
			FluidVolume current = component.getVolume(tank);

			if (incoming.getAmount().biggerThan(current.getSize())) {
				return false;
			}

			boolean allowed;

			if (incoming.isEmpty()) {
				if (current.isEmpty()) {
					return true;
				}
				allowed = component.canExtract(null, current, tank);
			} else if (current.isEmpty()) {
				allowed = component.canInsert(null, incoming, tank);
			} else if (incoming.getFluid() == current.getFluid()) {

				if (incoming.getAmount().equals(current.getAmount())) {
					return true;
				}

				if (incoming.smallerThan(current.getAmount())) {
					allowed = component.canExtract(null, current, tank);
				} else {
					allowed = component.canInsert(null, incoming, tank);
				}
			} else {
				allowed = component.canExtract(null, current, tank) && component.canInsert(null, incoming, tank);
			}

			if (allowed && simulation.isAction()) {

				current.setFluid(incoming.getFluid());
				current.setAmount(incoming.getAmount());

				component.setVolume(tank, current);
			}

			return allowed;
		}

		@Override
		public boolean isFluidValidForTank(int tank, FluidKey fluidKey) {
			validateTankIndex(tank);
			Fluid fluid = fluidKey.getRawFluid();
			return fluid != null && component.canInsert(null, FluidVolume.of(Fraction.bucket(), fluid), tank);
		}

		@Override
		public FluidAmount getMaxAmount_F(int tank) {
			validateTankIndex(tank);
			return wrapLibBlockAttributes(component.getVolume(tank).getSize());
		}

		@Override
		public ListenerToken addListener(FluidInvTankChangeListener fluidInvTankChangeListener, ListenerRemovalToken listenerRemovalToken) {
			// We don't support listeners
			return null;
		}
	}
}
