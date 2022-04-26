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

package com.github.mixinors.astromine.common.config.entry.provider.tiered;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;
import com.github.mixinors.astromine.common.provider.tiered.TieredSpeedProvider;
import com.github.mixinors.astromine.common.util.data.tier.MachineTier;

public interface DefaultedTieredSpeedProvider extends TieredSpeedProvider {
	default double getDefaultBaseSpeed() {
		return DefaultConfigValues.BASE_SPEED;
	}

	default double getDefaultSpeedModifier(MachineTier tier) {
		return switch (tier) {
			case PRIMITIVE -> DefaultConfigValues.PRIMITIVE_SPEED_MODIFIER;
			case BASIC -> DefaultConfigValues.BASIC_SPEED_MODIFIER;
			case ADVANCED -> DefaultConfigValues.ADVANCED_SPEED_MODIFIER;
			case ELITE -> DefaultConfigValues.ELITE_SPEED_MODIFIER;
			case CREATIVE -> Double.MAX_VALUE;
		};
	}
}
