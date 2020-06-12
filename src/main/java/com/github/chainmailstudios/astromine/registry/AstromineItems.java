package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.weapon.variant.Weapon;
import com.github.chainmailstudios.astromine.common.weapon.variant.ammo.Ammunition;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineItems {
	/**
	 * Weapons.
	 */
	public static final Item SCAR_H = register("scar_h", new Weapon.ScarH());
	public static final Item BARRET_M98B = register("barret_m98b", new Weapon.BarretM98B());

	/**
	 * Ammunition.
	 */
	public static final Item NATO_7_62_X_51_MM = register("nato_7_62x51mm", new Ammunition.Nato762x51mm());
	public static final Item LAPUA_8_6_X_70_MM = register("lapua_8_6x70mm", new Ammunition.Lapua86x70mm());

	public static void initialize() {
		// Unused.
	}

	/**
	 * @param name Name of item instance to be registered
	 * @param item Item instance to be registered
	 * @return Item instanced registered
	 */
	public static <T extends Item> T register(String name, T item) {
		return register(new Identifier(AstromineCommon.MOD_ID, name), item);
	}

	/**
	 * @param name Identifier of item instance to be registered
	 * @param item Item instance to be registered
	 * @return Item instance registered
	 */
	public static <T extends Item> T register(Identifier name, T item) {
		return Registry.register(Registry.ITEM, name, item);
	}
}
