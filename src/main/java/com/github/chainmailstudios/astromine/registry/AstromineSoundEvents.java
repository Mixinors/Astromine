package com.github.chainmailstudios.astromine.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;

public class AstromineSoundEvents {
    public static final SoundEvent ASTERITE_ARMOR_EQUIPPED = register("item.armor.equip_asterite");
    public static final SoundEvent STELLUM_ARMOR_EQUIPPED = register("item.armor.equip_stellum");
    public static final SoundEvent UNIVITE_ARMOR_EQUIPPED = register("item.armor.equip_univite");
    public static final SoundEvent SPACE_SUIT_EQUIPPED = register("item.armor.equip_space_suit");

    public static void initialize() {
        // Unused.
    }

    public static SoundEvent register(String name) {
        return register(AstromineCommon.id(name));
    }

    public static SoundEvent register(Identifier name) {
        return Registry.register(Registry.SOUND_EVENT, name, new SoundEvent(name));
    }
}
