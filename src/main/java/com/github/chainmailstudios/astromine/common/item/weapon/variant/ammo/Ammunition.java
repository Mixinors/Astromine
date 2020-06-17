package com.github.chainmailstudios.astromine.common.item.weapon.variant.ammo;

import com.github.chainmailstudios.astromine.common.item.weapon.BaseAmmo;
import com.github.chainmailstudios.astromine.registry.AstromineItemGroups;

import net.minecraft.item.Item;

public class Ammunition {
	public static final class Nato762x51mm extends BaseAmmo {
		public static final Item.Settings SETTINGS = new Item.Settings().fireproof().maxCount(1).maxDamage(32).group(AstromineItemGroups.ASTROMINE);

		public Nato762x51mm() {
			super(SETTINGS);
		}
	}

	public static final class Lapua86x70mm extends BaseAmmo {
		public static final Item.Settings SETTINGS = new Item.Settings().fireproof().maxCount(1).maxDamage(6).group(AstromineItemGroups.ASTROMINE);

		public Lapua86x70mm() {
			super(SETTINGS);
		}
	}
}
