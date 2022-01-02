package com.github.mixinors.astromine.common.config.entry.tiered.tier;

import com.github.mixinors.astromine.common.provider.SpeedModifierProvider;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public abstract class SpeedModifierTierConfig extends TierConfig implements SpeedModifierProvider {
	@Comment("A modifier for this tier applied to the machine's base speed.")
	public double speedModifier;

	public SpeedModifierTierConfig(double speedModifier) {
		this.speedModifier = speedModifier;
	}

	@Override
	public double getSpeedModifier() {
		return speedModifier;
	}
}
