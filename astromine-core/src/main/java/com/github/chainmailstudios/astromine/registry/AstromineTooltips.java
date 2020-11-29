package com.github.chainmailstudios.astromine. registry;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import java.util.*;

public class AstromineTooltips {
    public static final List<Item> PRIMITIVE = new ArrayList<>();
    public static final List<Item> BASIC = new ArrayList<>();
    public static final List<Item> ADVANCED = new ArrayList<>();
    public static final List<Item> ELITE = new ArrayList<>();
    public static final List<Item> CREATIVE = new ArrayList<>();
    public static final List<Item> SPECIAL = new ArrayList<>();

    public static final Map<Item, String> DESCRIPTION = new HashMap<>();

    public static final TextColor PRIMITIVE_COLOR = TextColor.parseColor("#7A7A7A");
    public static final TextColor BASIC_COLOR = TextColor.parseColor("#72C65F");
    public static final TextColor ADVANCED_COLOR = TextColor.parseColor("#C9594C");
    public static final TextColor ELITE_COLOR = TextColor.parseColor("#6295D1");
    public static final TextColor CREATIVE_COLOR = TextColor.parseColor("#CE76D3");
    public static final TextColor SPECIAL_COLOR = TextColor.parseColor("#D3BE43");

    private static final StringSplitter HANDLER = new StringSplitter((a, b) -> 0);

    public static void initialize() {
        ItemTooltipCallback.EVENT.register((stack, context, list) -> {
            if (DESCRIPTION.containsKey(stack.getItem())) {
                if (!Screen.hasShiftDown()) {
                    list.add(1, new TranslatableComponent("text.astromine.tooltip.shift_for_information").withStyle(ChatFormatting.ITALIC));
                } else {
                    list.add(1, new TranslatableComponent(DESCRIPTION.get(stack.getItem())));
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
