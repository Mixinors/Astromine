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

package com.github.mixinors.astromine.techreborn.common.volume.energy;

import team.reborn.energy.EnergyStorage;

public class TREnergyVolume extends EnergyVolume {
    private final EnergyStorage storage;

    private TREnergyVolume(EnergyStorage storage) {
        super(storage.getStored(null), storage.getMaxStoredPower());
        this.storage = storage;
    }

    private TREnergyVolume(EnergyStorage storage, Runnable runnable) {
        super(storage.getStored(null), storage.getMaxStoredPower(), runnable);
        this.storage = storage;
    }

    public static TREnergyVolume of(EnergyStorage storage) {
        return new TREnergyVolume(storage);
    }

    @Override
    public Double getAmount() {
        return storage.getStored(null);
    }

    @Override
    public void setAmount(Double amount) {
        storage.setStored(amount);
    }

    @Override
    public Double getSize() {
        return storage.getMaxStoredPower();
    }

    @Override
    public void setSize(Double size) {
        // Unsupported, because of the fucking API.
    }
}
