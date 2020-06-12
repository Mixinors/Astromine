package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineSounds {
	public static final SoundEvent EMPTY = register("empty");

	public static final SoundEvent EMPTY_MAGAZINE = register("empty_magazine");

	public static final SoundEvent SCAR_H_SHOT = register("scar_h_shot");

	public static final SoundEvent BARRET_M98B_SHOT = register("barret_m98b_shot");

	public static SoundEvent register(String id) {
		return Registry.register(Registry.SOUND_EVENT, new Identifier(AstromineCommon.MOD_ID, id), new SoundEvent(new Identifier(AstromineCommon.MOD_ID, id)));
	}

	public static void initialize() {
		// Unused.
	}
}
