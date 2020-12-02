package com.github.chainmailstudios.astromine.transportations.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.general.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class FluidFilterBlockEntity extends ComponentFluidBlockEntity {
    private Fluid filterFluid = Fluids.EMPTY;

    public FluidFilterBlockEntity() {
        super(AstromineTransportationsBlockEntityTypes.FLUID_FILTER);
    }

    @Override
    public FluidComponent createFluidComponent() {
        return SimpleFluidComponent.of(1).withInsertPredicate(((direction, volume, slot) -> {
            Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

            return direction == facing && filterFluid == Fluids.EMPTY || filterFluid == volume.getFluid();
        })).withExtractPredicate(((direction, volume, slot) -> {
            Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

            return direction == facing.getOpposite();
        }));
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("filterFluid", Registry.FLUID.getKey(filterFluid).toString());
        return super.save(tag);
    }

    @Override
    public void load(BlockState state, @NotNull CompoundTag tag) {
        filterFluid = Registry.FLUID.get(new ResourceLocation(tag.getString("filterFluid")));
        super.load(state, tag);
    }

    public Fluid getFilterFluid() {
        return filterFluid;
    }

    public void setFilterFluid(Fluid filterFluid) {
        this.filterFluid = filterFluid;
    }
}
