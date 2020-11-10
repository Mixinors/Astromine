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

import alexiil.mc.lib.attributes.fluid.*;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.item.*;
import alexiil.mc.lib.attributes.item.filter.ItemFilter;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import alexiil.mc.lib.attributes.Attribute;
import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class LibBlockAttributesCompatibility {
	public static void initialize() {
		FluidAttributes.forEachInv(LibBlockAttributesCompatibility::appendAdder);
		ItemAttributes.forEachInv(LibBlockAttributesCompatibility::appendAdder);
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

				if (transferComponent != null) {
					if (transferComponent.hasFluid()) {
						FluidComponent fluidComponent = FluidComponent.get(blockEntity);

						if (fluidComponent != null) {
							list.offer(new FixedFluidInvFromComponent(fluidComponent, transferComponent, direction));
						}
					}

					if (transferComponent.hasItem()) {
						ItemComponent itemComponent = ItemComponent.get(blockEntity);

						if (itemComponent != null) {
							list.offer(new FixedItemInvFromComponent(itemComponent, transferComponent, direction));
						}
					}
				}
			}
		}
	}

	private static alexiil.mc.lib.attributes.fluid.volume.FluidVolume wrapToLibBlockAttributes(FluidVolume volume) {
		if (FluidKeys.get(volume.getFluid()).isEmpty()) {
			return FluidKeys.EMPTY.withAmount(FluidAmount.ZERO);
		} else {
			return FluidKeys.get(volume.getFluid()).withAmount(wrapToLibBlockAttributes(volume.getAmount().copy()));
		}
	}

	private static Optional<FluidVolume> wrapToAstromine(alexiil.mc.lib.attributes.fluid.volume.FluidVolume volume) {
		if (volume.getRawFluid() == null)
			return Optional.empty();

		return Optional.of(FluidVolume.of(wrapToAstromine(volume.amount()), volume.getRawFluid()));
	}

	private static FluidAmount wrapToLibBlockAttributes(Fraction fraction) {
		return FluidAmount.of(fraction.getNumerator(), fraction.getDenominator());
	}

	private static Fraction wrapToAstromine(FluidAmount amount) {
		return Fraction.of(amount.whole, amount.numerator, amount.denominator);
	}

	private static class FixedItemInvFromComponent implements FixedItemInv, ItemTransferable {
		private final ItemComponent itemComponent;

		private final BlockEntityTransferComponent transferComponent;

		private final Direction direction;

		private boolean isExtracting = false;

		public FixedItemInvFromComponent(ItemComponent itemComponent, BlockEntityTransferComponent transferComponent, Direction direction) {
			this.itemComponent = itemComponent;
			this.transferComponent = transferComponent;
			this.direction = direction;
		}

		@Override
		public ItemStack getInvStack(int slot) {
			return itemComponent.getStack(slot);
		}

		@Override
		public boolean isItemValidForSlot(int slot, ItemStack stack) {
			return ((isExtracting && itemComponent.canExtract(direction, stack, slot)) || (!isExtracting && itemComponent.canInsert(direction, stack, slot)));
		}

		@Override
		public boolean setInvStack(int slot, ItemStack stack, Simulation simulation) {
			if (!isItemValidForSlot(slot, stack))
				return false;

			if (!simulation.isSimulate()) {
				itemComponent.setStack(slot, stack);
			}

			return true;
		}

		@Override
		public int getSlotCount() {
			return itemComponent.getSize();
		}

		@Override
		public ItemStack attemptExtraction(ItemFilter filter, int amount, Simulation simulation) {
			if (transferComponent.getItem(direction).canExtract()) {
				isExtracting = true;
				return getGroupedInv().attemptExtraction(filter, amount, simulation);
			} else {
				return ItemStack.EMPTY;
			}
		}

		@Override
		public ItemStack attemptInsertion(ItemStack stack, Simulation simulation) {
			if (transferComponent.getItem(direction).canInsert()) {
				isExtracting = false;
				return getGroupedInv().attemptInsertion(stack, simulation);
			} else {
				return stack;
			}
		}
	}

	private static class FixedFluidInvFromComponent implements FixedFluidInv, FluidTransferable {
		private final FluidComponent fluidComponent;

		private final BlockEntityTransferComponent transferComponent;

		private final Direction direction;

		private boolean isExtracting = false;

		public FixedFluidInvFromComponent(FluidComponent fluidComponent, BlockEntityTransferComponent transferComponent, Direction direction) {
			this.fluidComponent = fluidComponent;
			this.transferComponent = transferComponent;
			this.direction = direction;
		}

		private void validateTankIndex(int tank) {
			if (tank < 0 || tank >= getTankCount()) {
				throw new IndexOutOfBoundsException("Tank (" + tank + ") was out of bounds [0, " + getTankCount() + ")");
			}
		}

		@Override
		public int getTankCount() {
			return fluidComponent.getSize();
		}

		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume getInvFluid(int tank) {
			validateTankIndex(tank);

			return wrapToLibBlockAttributes(fluidComponent.getVolume(tank));
		}

		@Override
		public boolean setInvFluid(int slot, alexiil.mc.lib.attributes.fluid.volume.FluidVolume fluidVolume, Simulation simulation) {
			if (!isFluidValidForTank(slot, fluidVolume.getFluidKey()))
				return false;

			if (((isExtracting && !fluidComponent.canExtract(direction, wrapToAstromine(fluidVolume).get(), slot)) || (!isExtracting && !fluidComponent.canInsert(direction, wrapToAstromine(fluidVolume).get(), slot))))
				return false;

			Optional<FluidVolume> optionalFluidVolume = wrapToAstromine(fluidVolume);

			if (!optionalFluidVolume.isPresent())
				return false;

			FluidVolume incoming = optionalFluidVolume.get();
			FluidVolume current = fluidComponent.getVolume(slot);

			if (incoming.getAmount().biggerThan(current.getSize())) {
				return false;
			}

			boolean allowed;

			allowed = current.test(incoming.getFluid());

			if (allowed && simulation.isAction()) {

				current.setFluid(incoming.getFluid());
				current.setAmount(incoming.getAmount());

				fluidComponent.setVolume(slot, current);
			}

			return allowed;
		}

		@Override
		public boolean isFluidValidForTank(int tank, FluidKey fluidKey) {
			validateTankIndex(tank);
			Fluid fluid = fluidKey.getRawFluid();
			return fluid != null;
		}

		@Override
		public FluidAmount getMaxAmount_F(int tank) {
			validateTankIndex(tank);
			return wrapToLibBlockAttributes(fluidComponent.getVolume(tank).getSize());
		}

		@Override
		public ListenerToken addListener(FluidInvTankChangeListener fluidInvTankChangeListener, ListenerRemovalToken listenerRemovalToken) {
			// We don't support listeners
			return null;
		}

		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume attemptExtraction(FluidFilter filter, FluidAmount amount, Simulation simulation) {
			if (transferComponent.getFluid(direction).canExtract()) {
				isExtracting = true;
				return getGroupedInv().attemptExtraction(filter, amount, simulation);
			} else {
				return FluidVolumeUtil.EMPTY;
			}
		}

		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume attemptInsertion(alexiil.mc.lib.attributes.fluid.volume.FluidVolume volume, Simulation simulation) {
			if (transferComponent.getFluid(direction).canInsert()) {
				isExtracting = false;
				return getGroupedInv().attemptInsertion(volume, simulation);
			} else {
				return volume;
			}
		}
	}
}
