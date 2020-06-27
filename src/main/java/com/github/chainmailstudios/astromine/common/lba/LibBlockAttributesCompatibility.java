package com.github.chainmailstudios.astromine.common.lba;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.Simulation;
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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class LibBlockAttributesCompatibility {
	public static void initialize() {
		FluidAttributes.INSERTABLE.appendBlockAdder(LibBlockAttributesCompatibility::append);
		FluidAttributes.EXTRACTABLE.appendBlockAdder(LibBlockAttributesCompatibility::append);
	}

	private static void append(World world, BlockPos blockPos, BlockState state, AttributeList<? super LibBlockAttributesWrapper> list) {
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

	private static void append(FluidInventoryComponent component, AttributeList<? super LibBlockAttributesWrapper> list) {
		list.add(new LibBlockAttributesWrapper(component));
	}

	private static alexiil.mc.lib.attributes.fluid.volume.FluidVolume wrapLibBlockAttributes(FluidVolume volume) {
		return FluidKeys.get(volume.getFluid()).withAmount(wrapLibBlockAttributes(volume.getFraction().copy()));
	}

	private static Optional<FluidVolume> wrapVolumeToAstromine(alexiil.mc.lib.attributes.fluid.volume.FluidVolume volume) {
		if (volume.getRawFluid() == null) return Optional.empty();

		return Optional.of(new FluidVolume(volume.getRawFluid(), wrapVolumeToAstromine(volume.amount())));
	}

	private static FluidAmount wrapLibBlockAttributes(Fraction fraction) {
		return FluidAmount.of(fraction.getNumerator(), fraction.getDenominator());
	}

	private static Fraction wrapVolumeToAstromine(FluidAmount amount) {
		return new Fraction(amount.numerator, amount.denominator);
	}

	private static class LibBlockAttributesWrapper implements GroupedFluidInv, FixedFluidInv {
		private final FluidInventoryComponent component;

		public LibBlockAttributesWrapper(FluidInventoryComponent component) {
			this.component = component;
		}

		@Override
		public boolean setInvFluid(int tank, alexiil.mc.lib.attributes.fluid.volume.FluidVolume fluidVolume, Simulation simulation) {
			if (!isFluidValidForTank(tank, fluidVolume.getFluidKey())) return false;

			Optional<FluidVolume> optionalFluidVolume = wrapVolumeToAstromine(fluidVolume);

			if (!optionalFluidVolume.isPresent()) return false;

			if (simulation.isAction()) {
				component.setVolume(tank, optionalFluidVolume.get());
			}

			return true;
		}

		@Override
		public int getTankCount() {
			return component.getSize();
		}

		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume getInvFluid(int tank) {
			return wrapLibBlockAttributes(component.getVolume(tank));
		}

		@Override
		public boolean isFluidValidForTank(int tank, FluidKey fluidKey) {
			return fluidKey.getRawFluid() != null;
		}

		@Override
		public ListenerToken addListener(FluidInvTankChangeListener fluidInvTankChangeListener, ListenerRemovalToken listenerRemovalToken) {
			return () -> { };
		}

		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume attemptExtraction(FluidFilter filter, FluidAmount maxAmount, Simulation simulation) {
			Optional<FluidVolume> optionalFluidVolume = Optional.ofNullable(component
					.getContents()
					.values()
					.stream()
					.map((volume -> new Pair<>(FluidKeys.get(volume.getFluid()), volume)))
					.filter((pair -> pair.getRight().getFluid() != Fluids.EMPTY && filter.matches(pair.getLeft()) && pair.getRight().hasStored(wrapVolumeToAstromine(maxAmount))))
					.findFirst()
					.orElse(new Pair<>(FluidKeys.EMPTY, FluidVolume.empty())).getRight());

			if (!optionalFluidVolume.isPresent()) return wrapLibBlockAttributes(FluidVolume.empty());

			if (simulation.isSimulate()) optionalFluidVolume = Optional.of(optionalFluidVolume.get().copy());

			return wrapLibBlockAttributes(optionalFluidVolume.get().extractVolume(optionalFluidVolume.get().getFluid(), optionalFluidVolume.get().getFraction()));
		}

		@Override
		public alexiil.mc.lib.attributes.fluid.volume.FluidVolume attemptInsertion(alexiil.mc.lib.attributes.fluid.volume.FluidVolume fluidVolume, Simulation simulation) {
			Optional<FluidVolume> optionalFluidVolume = wrapVolumeToAstromine(fluidVolume);

			if (!optionalFluidVolume.isPresent()) return fluidVolume;

			FluidInventoryComponent component = simulation.isSimulate() ? this.component : this.component.copy();

			TypedActionResult<FluidVolume> result = component.insert(optionalFluidVolume.get());

			if (result.getResult().isAccepted()) return wrapLibBlockAttributes(result.getValue());

			return fluidVolume;
		}

		@Override
		public Set<FluidKey> getStoredFluids() {
			return component.getContents().values().stream().map((volume -> FluidKeys.get(volume.getFluid()))).collect(Collectors.toSet());
		}

		@Override
		public FluidInvStatistic getStatistics(FluidFilter fluidFilter) {
			FluidAmount amount = FluidAmount.ZERO;
			FluidAmount spaceAddable = FluidAmount.ZERO;
			FluidAmount spaceTotal = FluidAmount.ZERO;

			for (FluidVolume volume : component.getContents().values()) {
				amount = amount.roundedAdd(wrapLibBlockAttributes(volume.getFraction()));
				spaceAddable = spaceAddable.roundedAdd(wrapLibBlockAttributes(Fraction.simplify(Fraction.subtract(volume.getSize(), volume.getFraction()))));
				spaceTotal = spaceTotal.roundedAdd(wrapLibBlockAttributes(volume.getSize()));
			}

			return new FluidInvStatistic(fluidFilter, amount, spaceAddable, spaceTotal);
		}
	}
}
