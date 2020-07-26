/*
 * MIT License
 * 
 * Copyright (c) 2020 Chainmail Studios
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Lazy;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.function.Supplier;

public class AstromineBlacklistedConveyorBlocks {
	public static HashMap<Item, Pair<Float, Boolean>> blacklistedBlocks = new HashMap<>();

	static {
		addBlacklistedBlock(Items.CHEST, true);
		addBlacklistedBlock(Items.TRAPPED_CHEST, true);
		addBlacklistedBlock(Items.ENDER_CHEST, true);
		addBlacklistedBlock(Items.CREEPER_HEAD, true);
		addBlacklistedBlock(Items.DRAGON_HEAD, 0.625F, true);
		addBlacklistedBlock(Items.PLAYER_HEAD, true);
		addBlacklistedBlock(Items.ZOMBIE_HEAD, true);
		addBlacklistedBlock(Items.SKELETON_SKULL, true);
		addBlacklistedBlock(Items.WITHER_SKELETON_SKULL, true);
		addBlacklistedBlock(Items.REDSTONE, 0.8F, false);
	}

	public static void addBlacklistedBlock(Item item, boolean lifted) {
		addBlacklistedBlock(item, 1, lifted);
	}

	public static void addBlacklistedBlock(Item item, float scale, boolean lifted) {
		blacklistedBlocks.put(item, new Pair<>(scale, lifted));
	}
}
