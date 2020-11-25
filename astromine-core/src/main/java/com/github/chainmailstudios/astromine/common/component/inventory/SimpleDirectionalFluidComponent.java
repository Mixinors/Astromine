package com.github.chainmailstudios.astromine.common.component.inventory;

import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A {@link FluidComponent} which bases insertion and
 * extraction on a {@link BlockEntityTransferComponent}.
 */
public class SimpleDirectionalFluidComponent extends SimpleFluidComponent {
    private BlockEntityTransferComponent transferComponent;

    private Supplier<BlockEntityTransferComponent> transferComponentSupplier;

    /** Instantiates a {@link SimpleDirectionalFluidComponent}. */
    protected <V> SimpleDirectionalFluidComponent(V v, int size) {
        super(size);

        transferComponentSupplier = () -> BlockEntityTransferComponent.get(v);
    }

    /** Instantiates a {@link SimpleDirectionalFluidComponent}. */
    protected  <V> SimpleDirectionalFluidComponent(V v, FluidVolume... volumes) {
        super(volumes);

        transferComponentSupplier = () -> BlockEntityTransferComponent.get(v);
    }

    /** Instantiates a {@link SimpleDirectionalFluidComponent}. */
    public static <V> SimpleDirectionalFluidComponent of(V v, int size) {
        return new SimpleDirectionalFluidComponent(v, size);
    }

    /** Instantiates a {@link SimpleDirectionalFluidComponent}. */
    public static <V> SimpleDirectionalFluidComponent of(V v, FluidVolume... volumes) {
        return new SimpleDirectionalFluidComponent(v, volumes);
    }

    /** Override behavior to query transfer component. */
    @Override
    public boolean canInsert(@Nullable Direction direction, FluidVolume volume, int slot) {
        if (transferComponent == null && transferComponentSupplier != null) {
            transferComponent = transferComponentSupplier.get();
            transferComponentSupplier = null;
        }

        return direction == null ? super.canInsert(direction, volume, slot) : transferComponent.getFluid(direction).canInsert() && super.canInsert(direction, volume, slot);
    }

    /** Override behavior to query transfer component. */
    @Override
    public boolean canExtract(@Nullable Direction direction, FluidVolume volume, int slot) {
        if (transferComponent == null && transferComponentSupplier != null) {
            transferComponent = transferComponentSupplier.get();
            transferComponentSupplier = null;
        }

        return direction == null ? super.canExtract(direction, volume, slot) : transferComponent.getFluid(direction).canExtract() && super.canExtract(direction, volume, slot);
    }
}
