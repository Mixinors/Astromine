package com.github.chainmailstudios.astromine.transportations.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.handler.FluidHandler;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class DrainBlockEntity extends ComponentFluidBlockEntity implements Tickable {
    public DrainBlockEntity() {
        super(AstromineTransportationsBlockEntityTypes.DRAIN);

        for (Direction direction : Direction.values()) {
            transferComponent.get(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).set(direction, TransferType.INPUT);
        }
    }

    @Override
    protected FluidInventoryComponent createFluidComponent() {
        FluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(1).withInsertPredicate((direction, volume, slot) -> {
            return tickRedstone();
        });

        FluidHandler.of(fluidComponent).getFirst().setSize(Fraction.of(Long.MAX_VALUE));
        return fluidComponent;
    }

    @Override
    public void tick() {
        if (world == null)
            return;

        getFluidComponent().getVolume(0).setFluid(Fluids.EMPTY);
        getFluidComponent().getVolume(0).setAmount(Fraction.empty());
    }
}
