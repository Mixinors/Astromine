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

package com.github.mixinors.astromine.common.config.entry.tiered;

import com.github.mixinors.astromine.common.config.entry.AMConfigEntry;
import com.github.mixinors.astromine.common.config.entry.tiered.tier.TierConfig;
import com.github.mixinors.astromine.common.util.data.tier.MachineTier;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public abstract class TieredConfig<T extends TierConfig> implements AMConfigEntry {
	@Comment("Settings for the primitive tier of this machine.")
	@ConfigEntry.Gui.CollapsibleObject
	public T primitive = createTierConfig(MachineTier.PRIMITIVE);

	@Comment("Settings for the basic tier of this machine.")
	@ConfigEntry.Gui.CollapsibleObject
	public T basic = createTierConfig(MachineTier.BASIC);

	@Comment("Settings for the advanced tier of this machine.")
	@ConfigEntry.Gui.CollapsibleObject
	public T advanced = createTierConfig(MachineTier.ADVANCED);

	@Comment("Settings for the elite tier of this machine.")
	@ConfigEntry.Gui.CollapsibleObject
	public T elite = createTierConfig(MachineTier.ELITE);

	@Comment("Settings for the creative tier of this machine if it exists.")
	@ConfigEntry.Gui.CollapsibleObject
	public T creative = createTierConfig(MachineTier.CREATIVE);

	public T getTierConfig(MachineTier tier) {
		return switch (tier) {
			case PRIMITIVE -> primitive;
			case BASIC -> basic;
			case ADVANCED -> advanced;
			case ELITE -> elite;
			case CREATIVE -> creative;
		};
	}

	public abstract T createTierConfig(MachineTier tier);
}
