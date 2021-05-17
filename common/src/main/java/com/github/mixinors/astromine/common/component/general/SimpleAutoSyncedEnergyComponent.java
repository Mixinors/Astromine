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

import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

/**
 * A {@link SimpleEnergyComponent} that synchronizes itself
 * automatically.
 */
public class SimpleAutoSyncedEnergyComponent extends SimpleEnergyComponent implements AutoSyncedComponent {
    /** Instantiates a {@link SimpleAutoSyncedEnergyComponent}. */
    protected SimpleAutoSyncedEnergyComponent(double size) {
        super(size);
    }

    /** Instantiates a {@link SimpleAutoSyncedEnergyComponent}. */
    protected SimpleAutoSyncedEnergyComponent(EnergyVolume volume) {
        super(volume);
    }

    /** Instantiates a {@link SimpleAutoSyncedEnergyComponent}. */
    public static SimpleAutoSyncedEnergyComponent of(double size) {
        return new SimpleAutoSyncedEnergyComponent(size);
    }

    /** Instantiates a {@link SimpleAutoSyncedEnergyComponent}. */
    public static SimpleAutoSyncedEnergyComponent of(EnergyVolume volume) {
        return new SimpleAutoSyncedEnergyComponent(volume);
    }
}
