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

package com.github.chainmailstudios.astromine.common.component.general.compatibility;

import com.github.chainmailstudios.astromine.common.component.general.base.EnergyComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.energy.WrappedEnergyVolume;
import team.reborn.energy.EnergyStorage;

import java.util.Collections;
import java.util.List;

/**
 * An {@link EnergyComponent} wrapped over an {@link EnergyStorage}.
 */
public class EnergyComponentFromEnergyStorage implements EnergyComponent {
    private final EnergyVolume volume;

    private EnergyComponentFromEnergyStorage(EnergyStorage storage) {
        this.volume = WrappedEnergyVolume.of(storage);
    }

    public static EnergyComponentFromEnergyStorage of(EnergyStorage storage) {
        return new EnergyComponentFromEnergyStorage(storage);
    }

    @Override
    public List<Runnable> getListeners() {
        return Collections.emptyList();
    }

    @Override
    public EnergyVolume getVolume() {
        return volume;
    }
}
