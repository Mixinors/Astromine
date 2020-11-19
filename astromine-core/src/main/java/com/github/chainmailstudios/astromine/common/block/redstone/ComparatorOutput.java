/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.block.redstone;

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ComparatorBlockEntity;
import net.minecraft.screen.ScreenHandler;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import java.util.Collection;
import java.util.function.Function;

/**
 * A class representing a handler of {@link ComparatorBlockEntity}
 * output levels.
 */
public class ComparatorOutput {
    /** Returns the output level for a {@link BlockEntity} with an {@link ItemComponent}. */
    public static int forItems(@Nullable BlockEntity entity) {
        return ScreenHandler.calculateComparatorOutput(entity);
    }

    /** Returns the output level for a {@link BlockEntity} with an {@link EnergyComponent}. */
    public static int forEnergy(@Nullable BlockEntity entity) {
        if (entity == null) {
            return 0;
        }

        EnergyComponent component = EnergyComponent.get(entity);

        if (component == null) {
            return 0;
        }

        if (component.getAmount() <= 0.0001) {
            return 0;
        }

        return 1 + (int) (component.getAmount() / component.getSize() * 14.0);
    }

    /** Returns the output level for a {@link BlockEntity} with a {@link FluidComponent}. */
    public static int forFluids(@Nullable BlockEntity entity) {
        if (entity == null) {
            return 0;
        }

        FluidComponent fluidComponent = FluidComponent.get(entity);

        if (fluidComponent == null) {
            return 0;
        }

        Collection<FluidVolume> contents = fluidComponent.getContents().values();

        Fraction amounts = sumBy(contents, FluidVolume::getAmount);

        if (amounts.getNumerator() == 0) {
            return 0;
        }

        Fraction sizes = sumBy(contents, FluidVolume::getSize);
        Fraction ratio = amounts.divide(sizes);

        return 1 + (int) (ratio.floatValue() * 14.0f);
    }

    /** Sums collection of {@link T} into a {@link Fraction} by the given {@link Function}. */
    private static <T> Fraction sumBy(Collection<T> ts, Function<? super T, Fraction> extractor) {
        Fraction result = Fraction.EMPTY;

        for (T t : ts) {
            result = result.add(extractor.apply(t));
        }

        return result;
    }
}
