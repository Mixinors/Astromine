package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.item.Item;

import com.github.chainmailstudios.astromine.registry.AstromineTags;

public class PiglinUtilities {
    public static boolean isFoolsGoldItem(Item item) {
        return item.isIn(AstromineTags.TRICKS_PIGLINS);
    }

    public static boolean isLovedNugget(Item item) {
        return item.isIn(AstromineTags.PIGLIN_LOVED_NUGGETS);
    }

    public static boolean isBarteringItem(Item item) {
        return item.isIn(AstromineTags.PIGLIN_BARTERING_ITEMS);
    }
}
