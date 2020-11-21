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
import alexiil.mc.lib.attributes.misc.LibBlockAttributes;
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

/**
 * A compatibility addon for {@link LibBlockAttributes}.
 */
public final class LibBlockAttributesCompatibility {
	/** Appends our appender to all {@link FluidAttributes}
	 * and {@link ItemAttributes} attributes.
	 */
	public static void initialize() {
		FluidAttributes.forEachInv(LibBlockAttributesCompatibility::appendAdder);
		ItemAttributes.forEachInv(LibBlockAttributesCompatibility::appendAdder);
	}

	/** Appends our appender to the given attribute. */
	private static <T> void appendAdder(Attribute<T> attribute) {
		attribute.appendBlockAdder(LibBlockAttributesCompatibility::append);
	}

	/** Appends a {@link FixedFluidInvFromComponent} to {@link BlockEntity}-ies
	 * with an attached {@link FluidComponent}.
	 *
	 * Appends a {@link FixedItemInvFromComponent} to {@link BlockEntity}-ies
	 * with an attached {@link ItemComponent}.
	 *
	 * Since {@link LibBlockAttributes} queries this method for every insertion,
	 * we also instantiate a new wrapper for each insertion. If I may be honest,
	 * this seems entirely unnecessary and not performant, but I have not run
	 * any tests.
	 */
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

	/** Returns a {@link LibBlockAttributes} volume from the given Astromine one. */
	private static alexiil.mc.lib.attributes.fluid.volume.FluidVolume wrapToLibBlockAttributes(FluidVolume volume) {
		if (FluidKeys.get(volume.getFluid()).isEmpty()) {
			return FluidKeys.EMPTY.withAmount(FluidAmount.ZERO);
		} else {
			return FluidKeys.get(volume.getFluid()).withAmount(wrapToLibBlockAttributes(volume.getAmount().copy()));
		}
	}

	/** Returns an Astromine volume from the given {@link LibBlockAttributes} one. */
	private static Optional<FluidVolume> wrapToAstromine(alexiil.mc.lib.attributes.fluid.volume.FluidVolume volume) {
		if (volume.getRawFluid() == null)
			return Optional.empty();

		return Optional.of(FluidVolume.of(wrapToAstromine(volume.amount()), volume.getRawFluid()));
	}

	/** Returns a {@link LibBlockAttributes} fraction from the given Astromine one. */
	private static FluidAmount wrapToLibBlockAttributes(Fraction fraction) {
		return FluidAmount.of(fraction.getNumerator(), fraction.getDenominator());
	}

	/** Returns an Astromine fraction from the given {@link LibBlockAttributes} one. */
	private static Fraction wrapToAstromine(FluidAmount amount) {
		return Fraction.of(amount.whole, amount.numerator, amount.denominator);
	}

	/**
	 * A {@link FixedItemInv} wrapped over
	 * an {@link ItemComponent}, implementing {@link ItemTransferable}
	 * for siding control.
	 */
	private static class FixedItemInvFromComponent implements FixedItemInv, ItemTransferable {
		private final ItemComponent itemComponent;

		private final BlockEntityTransferComponent transferComponent;

		private final Direction direction;

		private boolean isExtracting = false;

		/** Instantiates a {@link FixedItemInvFromComponent}. */
		public FixedItemInvFromComponent(ItemComponent itemComponent, BlockEntityTransferComponent transferComponent, Direction direction) {
			this.itemComponent = itemComponent;
			this.transferComponent = transferComponent;
			this.direction = direction;
		}

		/** Asserts whether the given {@link ItemStack} is valid for
		 * the specified slot or not, based on {@link ItemComponent#canInsert(Direction, ItemStack, int)}
		 * or {@link ItemComponent#canExtract(Direction, ItemStack, int)}.
		 *
		 * Importantly, we need to keep track of whether the item is being
		 * inserted or extracted, which is done in {@link #attemptInsertion(ItemStack, Simulation)}
		 * or {@link #attemptExtraction(ItemFilter, int, Simulation)}, respectively. */
		@Override
		public boolean isItemValidForSlot(int slot, ItemStack stack) {
			return ((isExtracting && itemComponent.canExtract(direction, stack, slot)) || (!isExtracting && itemComponent.canInsert(direction, stack, slot)));
		}

		/** Returns this inventory's size. */
		@Override
		public int getSlotCount() {
			return itemComponent.getSize();
		}

		/** Returns the {@link ItemStack} at the specified slot. */
		@Override
		public ItemStack getInvStack(int slot) {
			return itemComponent.getStack(slot);
		}

		/** Attempts to set the {@link ItemStack} at the given slot
		 * to the specified one, returning whether the operation
		 * was successful or not. */
		@Override
		public boolean setInvStack(int slot, ItemStack stack, Simulation simulation) {
			if (!isItemValidForSlot(slot, stack))
				return false;

			if (!simulation.isSimulate()) {
				itemComponent.setStack(slot, stack);
			}

			return true;
		}

		/** Attempts to extract an {@link ItemStack} from this inventory
		 * based on an {@link ItemFilter}.
		 *
		 * Sets this inventory's mode to extracting, for use in
		 * {@link #setInvStack(int, ItemStack, Simulation)}. */
		@Override
		public ItemStack attemptExtraction(ItemFilter filter, int amount, Simulation simulation) {
			if (transferComponent.getItem(direction).canExtract()) {
				isExtracting = true;
				return getGroupedInv().attemptExtraction(filter, amount, simulation);
			} else {
				return ItemStack.EMPTY;
			}
		}

		/** Attempts to insert an {@link ItemStack} into this inventory.
		 *
		 * Sets this inventory's mode to inserting, for use in
		 * {@link #setInvStack(int, ItemStack, Simulation)}. */
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

	/**
	 * A {@link FixedFluidInv} wrapped over
	 * a {@link FluidComponent}, implementing {@link FluidTransferable}
	 * for siding control.
	 */
	private static class FixedFluidInvFromComponent implements FixedFluidInv, FluidTransferable {
		private final FluidComponent fluidComponent;

		private final BlockEntityTransferComponent transferComponent;

		private final Direction direction;

		private boolean isExtracting = false;

		/** Instantiates a {@link FixedFluidInvFromComponent}. */
		public FixedFluidInvFromComponent(FluidComponent fluidComponent, BlockEntityTransferComponent transferComponent, Direction direction) {
			this.fluidComponent = fluidComponent;
			this.transferComponent = transferComponent;
			this.direction = direction;
		}

		/** Asserts whether the given slot is valid for this inventory or not. */
		private void validateTankIndex(int slot) {
			if (slot < 0 || slot >= getTankCount()) {
				throw new IndexOutOfBoundsException("Tank (" + slot + ") was out of bounds [0, " + getTankCount() + ")");
			}
		}

		/** Asserts whether the given {@link FluidVolume} is valid for
		 * the specified slot or not, based on {@link FluidComponent#canInsert(Direction, FluidVolume, int)}
		 * or {@link FluidComponent#canExtract(Direction, FluidVolume, int)}.
		 *
		 * Importantly, we need to keep track of whether the item is being
		 * inserted or extracted, which is done in {@link #attemptInsertion(alexiil.mc.lib.attributes.fluid.volume.FluidVolume, Simulation)}
		 * or {@link #attemptExtraction(FluidFilter, FluidAmount, Simulation)}, respectively. */
		public boolean isVolumeValidForSlot(int slot, FluidVolume volume) {
			return ((isExtracting && fluidComponent.canExtract(direction, volume, slot)) || (!isExtracting && fluidComponent.canInsert(direction, volume, slot)));
		}

		/** Asserts whether the given {@link FluidKey} is valid for
		 * the specified slot or not. */
		@Override
		public boolean isFluidValidForTank(int tank, FluidKey fluidKey) {
			validateTankIndex(tank);
			Fluid fluid = fluidKey.getRawFluid();
			return fluid != null;
		}

		/** Returns this inventory's size. */
		@Override
		public int getTankCount() {
			return fluidComponent.getSize();
		}

		/** Returns the {@link FluidVolume} at the specified slot. */
		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume getInvFluid(int tank) {
			validateTankIndex(tank);

			return wrapToLibBlockAttributes(fluidComponent.getVolume(tank));
		}

		/** Attempts to set the {@link FluidVolume} at the given slot
		 * to the specified one, returning whether the operation
		 * was successful or not. */
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

		/** Returns the size of the volume at the given slot. */
		@Override
		public FluidAmount getMaxAmount_F(int slot) {
			validateTankIndex(slot);
			return wrapToLibBlockAttributes(fluidComponent.getVolume(slot).getSize());
		}

		/** We do not support listeners. */
		@Override
		public ListenerToken addListener(FluidInvTankChangeListener fluidInvTankChangeListener, ListenerRemovalToken listenerRemovalToken) {
			return null;
		}

		/** Attempts to extract a {@link FluidVolume} from this inventory.
		 *
		 * Sets this inventory's mode to extracting, for use in
		 * {@link #setInvFluid(int, alexiil.mc.lib.attributes.fluid.volume.FluidVolume, Simulation)}. */
		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume attemptExtraction(FluidFilter filter, FluidAmount amount, Simulation simulation) {
			if (transferComponent.getFluid(direction).canExtract()) {
				isExtracting = true;
				return getGroupedInv().attemptExtraction(filter, amount, simulation);
			} else {
				return FluidVolumeUtil.EMPTY;
			}
		}

		/** Attempts to insert a {@link FluidVolume} into this inventory.
		 *
		 * Sets this inventory's mode to inserting, for use in
		 * {@link #setInvFluid(int, alexiil.mc.lib.attributes.fluid.volume.FluidVolume, Simulation)}. */
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
