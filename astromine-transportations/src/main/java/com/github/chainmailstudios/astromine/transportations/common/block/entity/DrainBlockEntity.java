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
