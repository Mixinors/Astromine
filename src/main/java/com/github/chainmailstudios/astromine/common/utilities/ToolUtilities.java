package com.github.chainmailstudios.astromine.common.utilities;

import com.github.chainmailstudios.astromine.access.MiningToolItemAccess;
import net.minecraft.item.MiningToolItem;

public class ToolUtilities {
	public static float getAttackDamage(MiningToolItem first, MiningToolItem second) {
		return (first.getAttackDamage() + second.getAttackDamage()) / 2;
	}

	public static float getAttackSpeed(MiningToolItem first, MiningToolItem second) {
		return (float) (((MiningToolItemAccess) first).astromine_getAttackSpeed() + ((MiningToolItemAccess) second).astromine_getAttackSpeed()) / 2;
	}
}
