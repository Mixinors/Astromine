package com.github.chainmailstudios.astromine.common.component.general;

import com.github.chainmailstudios.astromine.common.component.block.entity.TransferComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A {@link FluidComponent} which bases insertion and
 * extraction on a {@link TransferComponent}.
 */
public class SimpleDirectionalFluidComponent extends SimpleFluidComponent {
    private TransferComponent transferComponent;

    private Supplier<TransferComponent> transferComponentSupplier;

    /** Instantiates a {@link SimpleDirectionalFluidComponent}. */
    protected <V> SimpleDirectionalFluidComponent(V v, int size) {
        super(size);

        transferComponentSupplier = () -> TransferComponent.get(v);
    }

    /** Instantiates a {@link SimpleDirectionalFluidComponent}. */
    protected  <V> SimpleDirectionalFluidComponent(V v, FluidVolume... volumes) {
        super(volumes);

        transferComponentSupplier = () -> TransferComponent.get(v);
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
        }

        if (transferComponent == null) {
            return false;
        }

        return direction == null ? super.canInsert(direction, volume, slot) : transferComponent.getFluid(direction).canInsert() && super.canInsert(direction, volume, slot);
    }

    /** Override behavior to query transfer component. */
    @Override
    public boolean canExtract(@Nullable Direction direction, FluidVolume volume, int slot) {
        if (transferComponent == null && transferComponentSupplier != null) {
            transferComponent = transferComponentSupplier.get();
        }

        if (transferComponent == null) {
            return false;
        }

        return direction == null ? super.canExtract(direction, volume, slot) : transferComponent.getFluid(direction).canExtract() && super.canExtract(direction, volume, slot);
    }
}
