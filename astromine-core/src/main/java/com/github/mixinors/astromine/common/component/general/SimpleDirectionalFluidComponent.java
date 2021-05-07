/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.component.general;

import com.github.mixinors.astromine.common.component.block.entity.TransferComponent;
import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
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
