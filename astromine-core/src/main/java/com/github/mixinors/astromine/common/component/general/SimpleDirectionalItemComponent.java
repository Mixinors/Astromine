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
import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * An {@link ItemComponent} which bases insertion and
 * extraction on a {@link TransferComponent}.
 */
public class SimpleDirectionalItemComponent extends SimpleItemComponent {
    private TransferComponent transferComponent;

    private Supplier<TransferComponent> transferComponentSupplier;

    /** Instantiates a {@link SimpleDirectionalItemComponent}. */
    protected <V> SimpleDirectionalItemComponent(V v, int size) {
        super(size);

        transferComponentSupplier = () -> TransferComponent.get(v);
    }

    /** Instantiates a {@link SimpleDirectionalItemComponent}. */
    protected  <V> SimpleDirectionalItemComponent(V v, ItemStack... stacks) {
        super(stacks);

        transferComponentSupplier = () -> TransferComponent.get(v);
    }

    /** Updates the transfer component if not yet acquired. */

    /** Instantiates a {@link SimpleDirectionalItemComponent}. */
    public static <V> SimpleDirectionalItemComponent of(V v, int size) {
        return new SimpleDirectionalItemComponent(v, size);
    }

    /** Instantiates a {@link SimpleDirectionalItemComponent}. */
    public static <V> SimpleDirectionalItemComponent of(V v, ItemStack... stacks) {
        return new SimpleDirectionalItemComponent(v, stacks);
    }

    /** Override behavior to query transfer component. */
    @Override
    public boolean canInsert(@Nullable Direction direction, ItemStack stack, int slot) {
        if (transferComponent == null && transferComponentSupplier != null) {
            transferComponent = transferComponentSupplier.get();
        }

        if (transferComponent == null) {
            return false;
        }

        return direction == null ? super.canInsert(direction, stack, slot) : transferComponent.getItem(direction).canInsert() && super.canInsert(direction, stack, slot);
    }

    /** Override behavior to query transfer component. */
    @Override
    public boolean canExtract(@Nullable Direction direction, ItemStack stack, int slot) {
        if (transferComponent == null && transferComponentSupplier != null) {
            transferComponent = transferComponentSupplier.get();
        }

        if (transferComponent == null) {
            return false;
        }

        return direction == null ? super.canExtract(direction, stack, slot) : transferComponent.getItem(direction).canExtract() && super.canExtract(direction, stack, slot);
    }
}
