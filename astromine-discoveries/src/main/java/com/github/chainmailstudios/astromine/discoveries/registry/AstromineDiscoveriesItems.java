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

package com.github.chainmailstudios.astromine.discoveries.registry;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;

import com.github.chainmailstudios.astromine.common.item.UncoloredSpawnEggItem;
import com.github.chainmailstudios.astromine.discoveries.common.item.SpaceSuitItem;
import com.github.chainmailstudios.astromine.registry.AstromineItems;

public class AstromineDiscoveriesItems extends AstromineItems {
	public static final Item SPACE_SLIME_SPAWN_EGG = register("space_slime_spawn_egg", new UncoloredSpawnEggItem(AstromineDiscoveriesEntityTypes.SPACE_SLIME, AstromineDiscoveriesItems.getBasicSettings()));

	public static final Item SPACE_SLIME_BALL = register("space_slime_ball", new Item(AstromineDiscoveriesItems.getBasicSettings()));

	public static final Item ASTEROID_METITE_CLUSTER = register("asteroid_metite_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_ASTERITE_CLUSTER = register("asteroid_asterite_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_STELLUM_CLUSTER = register("asteroid_stellum_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings().fireproof()));
	public static final Item ASTEROID_GALAXIUM_CLUSTER = register("asteroid_galaxium_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));

	public static final Item ASTEROID_COPPER_CLUSTER = register("asteroid_copper_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_TIN_CLUSTER = register("asteroid_tin_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_SILVER_CLUSTER = register("asteroid_silver_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_LEAD_CLUSTER = register("asteroid_lead_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));

	public static final Item ASTEROID_COAL_CLUSTER = register("asteroid_coal_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_IRON_CLUSTER = register("asteroid_iron_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_GOLD_CLUSTER = register("asteroid_gold_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_REDSTONE_CLUSTER = register("asteroid_redstone_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_LAPIS_CLUSTER = register("asteroid_lapis_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_DIAMOND_CLUSTER = register("asteroid_diamond_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item ASTEROID_EMERALD_CLUSTER = register("asteroid_emerald_cluster", new Item(AstromineDiscoveriesItems.getBasicSettings()));

	public static final Item PRIMITIVE_ROCKET_FUEL_TANK = register("primitive_rocket_fuel_tank", new Item(AstromineDiscoveriesItems.getBasicSettings().maxCount(1)));
	public static final Item PRIMITIVE_ROCKET_PLATING = register("primitive_rocket_plating", new Item(AstromineDiscoveriesItems.getBasicSettings().maxCount(1)));
	public static final Item PRIMITIVE_ROCKET_HULL = register("primitive_rocket_hull", new Item(AstromineDiscoveriesItems.getBasicSettings().maxCount(1)));
	public static final Item PRIMITIVE_ROCKET_BOOSTER = register("primitive_rocket_booster", new Item(AstromineDiscoveriesItems.getBasicSettings().maxCount(1)));

	public static final Item SPACE_SUIT_HELMET = register("space_suit_helmet", new SpaceSuitItem(AstromineDiscoveriesArmorMaterials.SPACE_SUIT, EquipmentSlot.HEAD, AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item SPACE_SUIT_CHESTPLATE = register("space_suit_chestplate", new SpaceSuitItem(AstromineDiscoveriesArmorMaterials.SPACE_SUIT, EquipmentSlot.CHEST, AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item SPACE_SUIT_LEGGINGS = register("space_suit_leggings", new SpaceSuitItem(AstromineDiscoveriesArmorMaterials.SPACE_SUIT, EquipmentSlot.LEGS, AstromineDiscoveriesItems.getBasicSettings()));
	public static final Item SPACE_SUIT_BOOTS = register("space_suit_boots", new SpaceSuitItem(AstromineDiscoveriesArmorMaterials.SPACE_SUIT, EquipmentSlot.FEET, AstromineDiscoveriesItems.getBasicSettings()));

	public static final Item PRIMITIVE_ROCKET = register("rocket", new UncoloredSpawnEggItem(AstromineDiscoveriesEntityTypes.PRIMITIVE_ROCKET, AstromineDiscoveriesItems.getBasicSettings()));

	public static void initialize() {

	}

	public static Item.Settings getBasicSettings() {
		return AstromineItems.getBasicSettings().group(AstromineDiscoveriesItemGroups.DISCOVERIES);
	}
}
