package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class AstromineSounds {
	public static final SoundEvent EMPTY = register("empty");

	// Weaponry
	public static final SoundEvent EMPTY_MAGAZINE = register("empty_magazine");
	public static final SoundEvent SCAR_H_SHOT = register("scar_h_shot");
	public static final SoundEvent BARRET_M98B_SHOT = register("barret_m98b_shot");

	// Tooling
	public static final SoundEvent FIRE_EXTINGUISHER_OPEN = register("fire_extinguisher_open");
	public static final SoundEvent HOLOGRAPHIC_CONNECTOR_CLICK = register("holographic_connector_click");

	// Machinery
	public static final SoundEvent HUMMING = register("humming");

	// Armory
	public static final SoundEvent METITE_ARMOR_EQUIPPED = register("item.armor.equip_metite");
	public static final SoundEvent ASTERITE_ARMOR_EQUIPPED = register("item.armor.equip_asterite");
	public static final SoundEvent STELLUM_ARMOR_EQUIPPED = register("item.armor.equip_stellum");
	public static final SoundEvent GALAXIUM_ARMOR_EQUIPPED = register("item.armor.equip_galaxium");
	public static final SoundEvent UNIVITE_ARMOR_EQUIPPED = register("item.armor.equip_univite");
	public static final SoundEvent SPACE_SUIT_EQUIPPED = register("item.armor.equip_space_suit");

	public static SoundEvent register(String id) {
		return Registry.register(Registry.SOUND_EVENT, AstromineCommon.identifier(id), new SoundEvent(AstromineCommon.identifier(id)));
	}

	public static void initialize() {
		// Unused.
	}
}
