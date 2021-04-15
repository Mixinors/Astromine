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

package com.github.chainmailstudios.astromine.common.component.general;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

/**
 * A {@link SimpleFluidComponent} that synchronizes itself
 * automatically.
 */
public class SimpleAutoSyncedFluidComponent extends SimpleFluidComponent implements AutoSyncedComponent {
    /** Instantiates a {@link SimpleAutoSyncedItemComponent}. */
    protected SimpleAutoSyncedFluidComponent(int size) {
        super(size);
    }

    /** Instantiates a {@link SimpleAutoSyncedItemComponent}. */
    protected SimpleAutoSyncedFluidComponent(FluidVolume... volumes) {
        super(volumes);
    }

    /** Instantiates a {@link SimpleAutoSyncedFluidComponent}. */
    public static SimpleAutoSyncedFluidComponent of(int size) {
        return new SimpleAutoSyncedFluidComponent(size);
    }

    /** Instantiates a {@link SimpleAutoSyncedFluidComponent}. */
    public static SimpleAutoSyncedFluidComponent of(FluidVolume... volumes) {
        return new SimpleAutoSyncedFluidComponent(volumes);
    }
}
