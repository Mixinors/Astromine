package com.github.chainmailstudios.astromine.foundations.datagen.registry;

import net.minecraft.resources.ResourceLocation;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.material.MaterialEntry;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineMaterialSets;

public class AstromineFoundationsMaterialSets extends AstromineMaterialSets {
	public static final MaterialSet WOOD = register(
			new MaterialSet.Builder("wood")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("oak_planks"), new ResourceLocation("planks")))
					.setType(MaterialItemType.PICKAXE, new ResourceLocation("wooden_pickaxe")).setType(MaterialItemType.AXE, new ResourceLocation("wooden_axe")).setType(MaterialItemType.SHOVEL, new ResourceLocation("wooden_shovel"))
					.setType(MaterialItemType.SWORD, new ResourceLocation("wooden_sword")).setType(MaterialItemType.HOE, new ResourceLocation("wooden_hoe"))
					.mattock(AstromineCommon.identifier("wooden_mattock")).miningTool(AstromineCommon.identifier("wooden_mining_tool"))
					.build()
	);
	public static final MaterialSet STONE = register(
			new MaterialSet.Builder("stone")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("cobblestone"), new ResourceLocation("stone_tool_materials")))
					.setType(MaterialItemType.PICKAXE, new ResourceLocation("stone_pickaxe")).setType(MaterialItemType.AXE, new ResourceLocation("stone_axe")).setType(MaterialItemType.SHOVEL, new ResourceLocation("stone_shovel"))
					.setType(MaterialItemType.SWORD, new ResourceLocation("stone_sword")).setType(MaterialItemType.HOE, new ResourceLocation("stone_hoe"))
					.mattock().miningTool()
					.build()
	);
	public static final MaterialSet LEATHER = register(
			new MaterialSet.Builder("leather")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("leather"), "leather"))
					.setType(MaterialItemType.HELMET, new ResourceLocation("leather_helmet")).setType(MaterialItemType.CHESTPLATE, new ResourceLocation("leather_chestplate"))
					.setType(MaterialItemType.LEGGINGS, new ResourceLocation("leather_leggings")).setType(MaterialItemType.BOOTS, new ResourceLocation("leather_boots"))
					.build()
	);
	public static final MaterialSet IRON = register(
			new MaterialSet.Builder("iron")
					.setType(MaterialItemType.INGOT, new MaterialEntry(new ResourceLocation("iron_ingot"), "iron_ingots"))
					.setType(MaterialItemType.NUGGET, new MaterialEntry(new ResourceLocation("iron_nugget"), "iron_nuggets"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("iron_block"), "iron_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new ResourceLocation("iron_ore"), "iron_ores"))
					.setType(MaterialItemType.PICKAXE, new ResourceLocation("iron_pickaxe")).setType(MaterialItemType.AXE, new ResourceLocation("iron_axe")).setType(MaterialItemType.SHOVEL, new ResourceLocation("iron_shovel"))
					.setType(MaterialItemType.SWORD, new ResourceLocation("iron_sword")).setType(MaterialItemType.HOE, new ResourceLocation("iron_hoe"))
					.setType(MaterialItemType.HELMET, new ResourceLocation("iron_helmet")).setType(MaterialItemType.CHESTPLATE, new ResourceLocation("iron_chestplate"))
					.setType(MaterialItemType.LEGGINGS, new ResourceLocation("iron_leggings")).setType(MaterialItemType.BOOTS, new ResourceLocation("iron_boots"))
					.dusts().asteroid()
					.gear().plate()
					.multiTools()
					.build()
	);
	public static final MaterialSet GOLD = register(
			new MaterialSet.Builder("gold")
					.setType(MaterialItemType.INGOT, new MaterialEntry(new ResourceLocation("gold_ingot"), "gold_ingots"))
					.setType(MaterialItemType.NUGGET, new MaterialEntry(new ResourceLocation("gold_nugget"), "gold_nuggets"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("gold_block"), "gold_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new ResourceLocation("gold_ore"), "gold_ores"))
					.setType(MaterialItemType.PICKAXE, new ResourceLocation("golden_pickaxe")).setType(MaterialItemType.AXE, new ResourceLocation("golden_axe")).setType(MaterialItemType.SHOVEL, new ResourceLocation("golden_shovel"))
					.setType(MaterialItemType.SWORD, new ResourceLocation("golden_sword")).setType(MaterialItemType.HOE, new ResourceLocation("golden_hoe"))
					.setType(MaterialItemType.HELMET, new ResourceLocation("golden_helmet")).setType(MaterialItemType.CHESTPLATE, new ResourceLocation("golden_chestplate"))
					.setType(MaterialItemType.LEGGINGS, new ResourceLocation("golden_leggings")).setType(MaterialItemType.BOOTS, new ResourceLocation("golden_boots"))
					.setType(MaterialItemType.APPLE, new MaterialEntry(new ResourceLocation("golden_apple"), "golden_apples"))
					.dusts().asteroid()
					.gear().plate().wire()
					.mattock(AstromineCommon.identifier("golden_mattock")).miningTool(AstromineCommon.identifier("golden_mining_tool"))
					.piglinLoved()
					.build()
	);
	public static final MaterialSet DIAMOND = register(
			new MaterialSet.Builder("diamond")
					.setType(MaterialItemType.GEM, new MaterialEntry(new ResourceLocation("diamond"), "diamonds"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("diamond_block"), "diamond_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new ResourceLocation("diamond_ore"), "diamond_ores"))
					.setType(MaterialItemType.PICKAXE, new ResourceLocation("diamond_pickaxe")).setType(MaterialItemType.AXE, new ResourceLocation("diamond_axe")).setType(MaterialItemType.SHOVEL, new ResourceLocation("diamond_shovel"))
					.setType(MaterialItemType.SWORD, new ResourceLocation("diamond_sword")).setType(MaterialItemType.HOE, new ResourceLocation("diamond_hoe"))
					.setType(MaterialItemType.HELMET, new ResourceLocation("diamond_helmet")).setType(MaterialItemType.CHESTPLATE, new ResourceLocation("diamond_chestplate"))
					.setType(MaterialItemType.LEGGINGS, new ResourceLocation("diamond_leggings")).setType(MaterialItemType.BOOTS, new ResourceLocation("diamond_boots"))
					.dusts().asteroid()
					.fragment()
					.multiTools()
					.build()
	);
	public static final MaterialSet EMERALD = register(
			new MaterialSet.Builder("emerald")
					.setType(MaterialItemType.GEM, new MaterialEntry(new ResourceLocation("emerald"), "emeralds"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("emerald_block"), "emerald_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new ResourceLocation("emerald_ore"), "emerald_ores"))
					.dusts().asteroid()
					.fragment()
					.build()
	);
	public static final MaterialSet LAPIS = register(
			new MaterialSet.Builder("lapis")
					.setType(MaterialItemType.GEM, new MaterialEntry(new ResourceLocation("lapis_lazuli"), "lapis_lazulis"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("lapis_block"), "lapis_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new ResourceLocation("lapis_ore"), "lapis_ores"))
					.dusts().asteroid()
					.build()
	);
	public static final MaterialSet QUARTZ = register(
			new MaterialSet.Builder("quartz")
					.setType(MaterialItemType.GEM, new MaterialEntry(new ResourceLocation("quartz"), "quartz"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new ResourceLocation("nether_quartz_ore"), "quartz_ores"))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new ResourceLocation("quartz_block"), "quartz_blocks"))
					.dusts()
					.fragment()
					.build()
	);
	public static final MaterialSet REDSTONE = register(
			new MaterialSet.Builder("redstone")
					.setType(MaterialItemType.DUST, new MaterialEntry(new ResourceLocation("redstone"), "redstone_dusts"))
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("redstone")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("redstone_block"), "redstone_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new ResourceLocation("redstone_ore"), "redstone_ores"))
					.asteroid()
					.build()
	);
	public static final MaterialSet COAL = register(
			new MaterialSet.Builder("coal")
					.setType(MaterialItemType.GEM, new MaterialEntry(new ResourceLocation("coal"), "coal"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("coal_block"), "coal_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new ResourceLocation("coal_ore"), "coal_ores"))
					.dusts().asteroid()
					.build()
	);
	public static final MaterialSet CHARCOAL = register(
			new MaterialSet.Builder("charcoal")
					.setType(MaterialItemType.GEM, new MaterialEntry(new ResourceLocation("charcoal"), "charcoal"))
					.dusts()
					.build()
	);
	public static final MaterialSet GLOWSTONE = register(
			new MaterialSet.Builder("glowstone")
					.setType(MaterialItemType.DUST, new MaterialEntry(new ResourceLocation("glowstone_dust"), "glowstone_dusts"))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new ResourceLocation("glowstone"), "glowstone_blocks"))
					.build()
	);
	public static final MaterialSet BONE = register(
			new MaterialSet.Builder("bone")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("bone_meal")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("bone_block"), "bone_blocks"))
					.build()
	);
	public static final MaterialSet WHEAT = register(
			new MaterialSet.Builder("wheat")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("wheat")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("hay_block")))
					.build()
	);
	public static final MaterialSet DRIED_KELP = register(
			new MaterialSet.Builder("dried_kelp")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("dried_kelp")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("dried_kelp_block")))
					.build()
	);
	public static final MaterialSet SLIME = register(
			new MaterialSet.Builder("slime")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("slime_ball")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("slime_block"), "slime_blocks"))
					.build()
	);
	public static final MaterialSet MAGMA = register(
			new MaterialSet.Builder("magma")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("magma_cream")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new ResourceLocation("magma_block")))
					.build()
	);
	public static final MaterialSet HONEYCOMB = register(
			new MaterialSet.Builder("honeycomb")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("honeycomb")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new ResourceLocation("honeycomb_block")))
					.build()
	);
	public static final MaterialSet NETHER_WART = register(
			new MaterialSet.Builder("nether_wart")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("nether_wart")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("nether_wart_block")))
					.build()
	);
	public static final MaterialSet PURPUR = register(
			new MaterialSet.Builder("purpur")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("popped_chorus_fruit")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new ResourceLocation("purpur_block"), "purpur_blocks"))
					.build()
	);
	public static final MaterialSet SNOW = register(
			new MaterialSet.Builder("snow")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("snowball")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new ResourceLocation("snow_block")))
					.build()
	);
	public static final MaterialSet WOOL = register(
			new MaterialSet.Builder("wool")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("string")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new ResourceLocation("white_wool"), new ResourceLocation("wool")))
					.build()
	);
	public static final MaterialSet SAND = register(
			new MaterialSet.Builder("sand")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("sand")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new ResourceLocation("sandstone"), "yellow_sandstones"))
					.build()
	);
	public static final MaterialSet RED_SAND = register(
			new MaterialSet.Builder("red_sand")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new ResourceLocation("red_sand")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new ResourceLocation("red_sandstone"), "red_sandstones"))
					.build()
	);
	public static final MaterialSet RAW_NETHERITE = register(
			new MaterialSet.Builder("raw_netherite")
					.setType(MaterialItemType.INGOT, new MaterialEntry(new ResourceLocation("netherite_scrap"), "netherite_scraps"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new ResourceLocation("ancient_debris"), "ancient_debris"))
					.dusts()
					.build()
	);
	public static final MaterialSet NETHERITE = register(
			new MaterialSet.Builder("netherite")
					.setType(MaterialItemType.INGOT, new MaterialEntry(new ResourceLocation("netherite_ingot"), "netherite_ingots"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new ResourceLocation("netherite_block"), "netherite_blocks"))
					.setType(MaterialItemType.PICKAXE, new ResourceLocation("netherite_pickaxe")).setType(MaterialItemType.AXE, new ResourceLocation("netherite_axe")).setType(MaterialItemType.SHOVEL, new ResourceLocation("netherite_shovel"))
					.setType(MaterialItemType.SWORD, new ResourceLocation("netherite_sword")).setType(MaterialItemType.HOE, new ResourceLocation("netherite_hoe"))
					.setType(MaterialItemType.HELMET, new ResourceLocation("netherite_helmet")).setType(MaterialItemType.CHESTPLATE, new ResourceLocation("netherite_chestplate"))
					.setType(MaterialItemType.LEGGINGS, new ResourceLocation("netherite_leggings")).setType(MaterialItemType.BOOTS, new ResourceLocation("netherite_boots"))
					.nugget().dusts()
					.gear().plate()
					.multiTools()
					.smithingBaseSet(DIAMOND)
					.build()
	);
	public static final MaterialSet TIN = register(
			new MaterialSet.Builder("tin")
					.basics().metal().ore().asteroid().wire().allTools().armor()
					.build()
	);
	public static final MaterialSet COPPER = register(
			new MaterialSet.Builder("copper")
					.basics().metal().ore().asteroid().wire().allTools().armor().wrench()
					.build()
	);

	public static final MaterialSet LEAD = register(
			new MaterialSet.Builder("lead")
					.basics().metal().ore().asteroid().wire().allTools().armor()
					.apple()
					.build()
	);

	public static final MaterialSet STEEL = register(
			new MaterialSet.Builder("steel")
					.basics().metal().wire().allTools().armor().wrench()
					.build()
	);

	public static final MaterialSet BRONZE = register(
			new MaterialSet.Builder("bronze")
					.basics().metal().allTools().armor().wrench()
					.build()
	);
}
