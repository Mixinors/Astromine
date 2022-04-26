/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.config.entry.utility;

import com.github.mixinors.astromine.common.config.entry.AMConfigEntry;
import com.github.mixinors.astromine.common.config.entry.provider.DefaultedEnergyConsumedProvider;
import com.github.mixinors.astromine.common.config.entry.provider.DefaultedEnergyStorageSizeProvider;
import com.github.mixinors.astromine.common.config.entry.provider.DefaultedSpeedProvider;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class UtilityConfig implements AMConfigEntry, DefaultedSpeedProvider, DefaultedEnergyStorageSizeProvider, DefaultedEnergyConsumedProvider {
	@Comment("The delay between actions of the utility (smaller = faster)")
	public double delay = getDefaultSpeed();
	
	@Comment("The maximum energy able to be stored by this utility")
	public long energyStorageSize = getDefaultEnergyStorageSize();
	
	@Comment("The energy consumed by each action of this utility")
	public long energyConsumed = getDefaultEnergyConsumed();

	@Override
	public double getSpeed() {
		return delay;
	}

	@Override
	public long getEnergyStorageSize() {
		return energyStorageSize;
	}

	@Override
	public long getEnergyConsumed() {
		return energyConsumed;
	}
}
