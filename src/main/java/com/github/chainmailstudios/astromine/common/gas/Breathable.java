package com.github.chainmailstudios.astromine.common.gas;

import net.minecraft.entity.damage.DamageSource;

public interface Breathable {
	boolean isToxic();

	int getDamage();

	DamageSource getSource();
}
