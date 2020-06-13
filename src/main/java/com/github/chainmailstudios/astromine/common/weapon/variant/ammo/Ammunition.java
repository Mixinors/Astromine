package com.github.chainmailstudios.astromine.common.weapon.variant.ammo;

import com.github.chainmailstudios.astromine.common.weapon.BaseAmmo;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class Ammunition {
	public static final class Nato762x51mm extends BaseAmmo {
		public static final Item.Settings SETTINGS = new Item.Settings().fireproof().maxCount(1).maxDamage(32).rarity(Rarity.COMMON);

		public Nato762x51mm() {
			super(SETTINGS);
		}

		@Override
		public boolean destroyEmpty() {
			return false;
		}
	}

	public static final class Lapua86x70mm extends BaseAmmo {
		public static final Item.Settings SETTINGS = new Item.Settings().fireproof().maxCount(1).maxDamage(6).rarity(Rarity.COMMON);

		public Lapua86x70mm() {
			super(SETTINGS);
		}

		@Override
		public boolean destroyEmpty() {
			return false;
		}
	}
}
