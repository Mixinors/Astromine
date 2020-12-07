package com.github.chainmailstudios.astromine.transportations.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.general.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public class FluidFilterBlockEntity extends ComponentFluidBlockEntity {
	private Fluid filterFluid = Fluids.EMPTY;

	public FluidFilterBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.FLUID_FILTER);
	}

	@Override
	public FluidComponent createFluidComponent() {
		return SimpleFluidComponent.of(1).withInsertPredicate(((direction, volume, slot) -> {
			Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);

			return direction == facing && filterFluid == Fluids.EMPTY || filterFluid == volume.getFluid();
		})).withExtractPredicate(((direction, volume, slot) -> {
			Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);

			return direction == facing.getOpposite();
		}));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putString("filterFluid", Registry.FLUID.getKey(filterFluid).toString());
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		filterFluid = Registry.FLUID.get(new Identifier(tag.getString("filterFluid")));
		super.fromTag(state, tag);
	}

	public Fluid getFilterFluid() {
		return filterFluid;
	}

	public void setFilterFluid(Fluid filterFluid) {
		this.filterFluid = filterFluid;
	}
}
