package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.utilities.ClientUtilities;
import com.github.vini2003.blade.client.utilities.Texts;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import javax.swing.*;
import java.util.*;

public class AstromineTooltips {
    public static final List<Item> PRIMITIVE = new ArrayList<>();
    public static final List<Item> BASIC = new ArrayList<>();
    public static final List<Item> ADVANCED = new ArrayList<>();
    public static final List<Item> ELITE = new ArrayList<>();
    public static final List<Item> CREATIVE = new ArrayList<>();
    public static final List<Item> SPECIAL = new ArrayList<>();

    public static final Map<Item, String> DESCRIPTION = new HashMap<>();

    public static final TextColor PRIMITIVE_COLOR = TextColor.parse("#7A7A7A");
    public static final TextColor BASIC_COLOR = TextColor.parse("#72C65F");
    public static final TextColor ADVANCED_COLOR = TextColor.parse("#C9594C");
    public static final TextColor ELITE_COLOR = TextColor.parse("#6295D1");
    public static final TextColor CREATIVE_COLOR = TextColor.parse("#CE76D3");
    public static final TextColor SPECIAL_COLOR = TextColor.parse("#D3BE43");

    private static final TextHandler HANDLER = new TextHandler((a, b) -> 0);

    public static void initialize() {
        ItemTooltipCallback.EVENT.register((stack, context, list) -> {
            if (DESCRIPTION.containsKey(stack.getItem())) {
                if (!Screen.hasShiftDown()) {
                    list.add(1, new TranslatableText("text.astromine.tooltip.shift_for_information").formatted(Formatting.ITALIC));
                } else {
                    list.add(1, new TranslatableText(DESCRIPTION.get(stack.getItem())));
                }
            }
        });
    }

    public static boolean isPrimitive(Item item) {
        return PRIMITIVE.contains(item);
    }

    public static boolean isBasic(Item item) {
        return BASIC.contains(item);
    }

    public static boolean isAdvanced(Item item) {
        return ADVANCED.contains(item);
    }

    public static boolean isElite(Item item) {
        return ELITE.contains(item);
    }

    public static boolean isCreative(Item item) {
        return CREATIVE.contains(item);
    }

    public static boolean isSpecial(Item item) {
        return SPECIAL.contains(item);
    }

    public static void registerPrimitive(Item... items) {
        PRIMITIVE.addAll(Arrays.asList(items));
    }

    public static void registerBasic(Item... items) {
        BASIC.addAll(Arrays.asList(items));
    }

    public static void registerAdvanced(Item... items) {
        ADVANCED.addAll(Arrays.asList(items));
    }

    public static void registerElite(Item... items) {
        ELITE.addAll(Arrays.asList(items));
    }

    public static void registerCreative(Item... items) {
        CREATIVE.addAll(Arrays.asList(items));
    }

    public static void registerSpecial(Item... items) {
        SPECIAL.addAll(Arrays.asList(items));
    }

    public static void registerDescription(String key, Item... items) {
        for (Item item : items) {
            DESCRIPTION.put(item, key);
        }
    }
}
