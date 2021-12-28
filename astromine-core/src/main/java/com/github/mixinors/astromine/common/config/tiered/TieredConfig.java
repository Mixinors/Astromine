package com.github.mixinors.astromine.common.config.tiered;

import com.github.mixinors.astromine.common.config.tiered.tier.TierConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public abstract class TieredConfig<T extends TierConfig> {
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

	@ConfigEntry.Gui.Excluded
	public T creative = createTierConfig(MachineTier.CREATIVE);

	public T getTierConfig(MachineTier tier) {
		return switch(tier) {
			case PRIMITIVE -> primitive;
			case BASIC -> basic;
			case ADVANCED -> advanced;
			case ELITE -> elite;
			case CREATIVE -> creative;
		};
	}

	public abstract T createTierConfig(MachineTier tier);
}
