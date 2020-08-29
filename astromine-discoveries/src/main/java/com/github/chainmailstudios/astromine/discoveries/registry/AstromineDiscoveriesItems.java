package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.common.item.UncoloredSpawnEggItem;
import com.github.chainmailstudios.astromine.discoveries.common.item.SpaceSuitItem;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsItems;
import com.github.chainmailstudios.astromine.registry.AstromineEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineItems;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;

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

	public static final Item SPACE_SUIT_HELMET = register("space_suit_helmet", new SpaceSuitItem(AstromineDiscoveriesArmorMaterials.SPACE_SUIT, EquipmentSlot.HEAD, getBasicSettings()));
	public static final Item SPACE_SUIT_CHESTPLATE = register("space_suit_chestplate", new SpaceSuitItem(AstromineDiscoveriesArmorMaterials.SPACE_SUIT, EquipmentSlot.CHEST, getBasicSettings()));
	public static final Item SPACE_SUIT_LEGGINGS = register("space_suit_leggings", new SpaceSuitItem(AstromineDiscoveriesArmorMaterials.SPACE_SUIT, EquipmentSlot.LEGS, getBasicSettings()));
	public static final Item SPACE_SUIT_BOOTS = register("space_suit_boots", new SpaceSuitItem(AstromineDiscoveriesArmorMaterials.SPACE_SUIT, EquipmentSlot.FEET, getBasicSettings()));

	public static final Item ROCKET = register("rocket", new UncoloredSpawnEggItem(AstromineDiscoveriesEntityTypes.ROCKET, getBasicSettings()));

	public static void initialize() {

	}

	public static Item.Settings getBasicSettings() {
		return AstromineItems.getBasicSettings().group(AstromineDiscoveriesItemGroups.DISCOVERIES);
	}
}
