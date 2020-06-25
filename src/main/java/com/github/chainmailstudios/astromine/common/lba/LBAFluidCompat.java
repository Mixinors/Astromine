package com.github.chainmailstudios.astromine.common.lba;

import alexiil.mc.lib.attributes.*;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidInvTankChangeListener;
import alexiil.mc.lib.attributes.fluid.GroupedFluidInv;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public final class LBAFluidCompat {
	public static void initialize() {
		FluidAttributes.INSERTABLE.appendBlockAdder(LBAFluidCompat::append);
		FluidAttributes.EXTRACTABLE.appendBlockAdder(LBAFluidCompat::append);
	}

	private static void append(World world, BlockPos blockPos, BlockState state, AttributeList<? super LBAFluidWrapper> list) {
		if (state.getBlock() instanceof AttributeProvider) {
			return;
		}

		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity != null) {
			ComponentProvider componentProvider = ComponentProvider.fromBlockEntity(blockEntity);
			@Nullable Direction direction = list.getSearchDirection();
			FluidInventoryComponent component = componentProvider.getSidedComponent(direction, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

			if (component != null) {
				append(component, list);
			}
		}
	}

	private static void append(FluidInventoryComponent component, AttributeList<? super LBAFluidWrapper> list) {
		list.add(new LBAFluidWrapper(component));
	}

	private static alexiil.mc.lib.attributes.fluid.volume.FluidVolume wrapToLBA(FluidVolume volume) {
		return FluidKeys.get(volume.getFluid()).withAmount(wrapToLBA(volume.getFraction().copy()));
	}

	private static Optional<FluidVolume> wrapToAstromine(alexiil.mc.lib.attributes.fluid.volume.FluidVolume volume) {
		if (volume.getRawFluid() == null) return Optional.empty();
		return Optional.of(new FluidVolume(volume.getRawFluid(), wrapToAstromine(volume.amount())));
	}

	private static FluidAmount wrapToLBA(Fraction fraction) {
		return FluidAmount.of(fraction.getNumerator(), fraction.getDenominator());
	}

	private static Fraction wrapToAstromine(FluidAmount amount) {
		return new Fraction(amount.numerator, amount.denominator);
	}

	private static class LBAFluidWrapper implements GroupedFluidInv, FixedFluidInv {
		private final FluidInventoryComponent component;

		public LBAFluidWrapper(FluidInventoryComponent component) {
			this.component = component;
		}

		@Override
		public boolean setInvFluid(int i, alexiil.mc.lib.attributes.fluid.volume.FluidVolume fluidVolume, Simulation simulation) {
			if (!isFluidValidForTank(i, fluidVolume.getFluidKey())) return false;
			Optional<FluidVolume> optionalFluidVolume = wrapToAstromine(fluidVolume);
			if (!optionalFluidVolume.isPresent()) return false;
			if (simulation.isAction()) {
				component.setVolume(i, optionalFluidVolume.get());
			}
			return true;
		}

		@Override
		public int getTankCount() {
			return component.getSize();
		}

		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume getInvFluid(int i) {
			return wrapToLBA(component.getVolume(i));
		}

		@Override
		public boolean isFluidValidForTank(int i, FluidKey fluidKey) {
			return fluidKey.getRawFluid() != null;
		}

		@Override
		public ListenerToken addListener(FluidInvTankChangeListener fluidInvTankChangeListener, ListenerRemovalToken listenerRemovalToken) {
			return null;
		}

		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume attemptInsertion(alexiil.mc.lib.attributes.fluid.volume.FluidVolume fluidVolume, Simulation simulation) {
			Optional<FluidVolume> optionalFluidVolume = wrapToAstromine(fluidVolume);
			if (!optionalFluidVolume.isPresent()) return fluidVolume;
			FluidInventoryComponent component = this.component;
			if (simulation.isSimulate()) {
				component = component.copy();
			}
			TypedActionResult<FluidVolume> result = component.insert(optionalFluidVolume.get());
			if (result.getResult().isAccepted())
				return wrapToLBA(result.getValue());
			return fluidVolume;
		}

		@Override
		public Set<FluidKey> getStoredFluids() {
			Set<FluidKey> set = Sets.newLinkedHashSet();
			for (FluidVolume value : component.getContents().values()) {
				set.add(FluidKeys.get(value.getFluid()));
			}
			return set;
		}

		@Override
		public FluidInvStatistic getStatistics(FluidFilter fluidFilter) {
			FluidAmount amount = FluidAmount.ZERO;
			FluidAmount spaceAddable = FluidAmount.ZERO;
			FluidAmount spaceTotal = FluidAmount.ZERO;
			for (FluidVolume volume : component.getContents().values()) {
				amount = amount.roundedAdd(wrapToLBA(volume.getFraction()));
				spaceAddable = spaceAddable.roundedAdd(wrapToLBA(Fraction.simplify(Fraction.subtract(volume.getSize(), volume.getFraction()))));
				spaceTotal = spaceTotal.roundedAdd(wrapToLBA(volume.getSize()));
			}
			return new FluidInvStatistic(fluidFilter, amount, spaceAddable, spaceTotal);
		}
	}
}
