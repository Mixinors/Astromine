package com.github.chainmailstudios.astromine.foundations.datagen.registry;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.material.MaterialEntry;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineMaterialSets;

public class AstromineFoundationsMaterialSets extends AstromineMaterialSets {
	public static final MaterialSet WOOD = register(
			new MaterialSet.Builder("wood")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("oak_planks"), new Identifier("planks")))
					.setType(MaterialItemType.PICKAXE, new Identifier("wooden_pickaxe")).setType(MaterialItemType.AXE, new Identifier("wooden_axe")).setType(MaterialItemType.SHOVEL, new Identifier("wooden_shovel"))
					.setType(MaterialItemType.SWORD, new Identifier("wooden_sword")).setType(MaterialItemType.HOE, new Identifier("wooden_hoe"))
					.mattock(AstromineCommon.identifier("wooden_mattock")).miningTool(AstromineCommon.identifier("wooden_mining_tool"))
					.build()
	);
	public static final MaterialSet STONE = register(
			new MaterialSet.Builder("stone")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("cobblestone"), new Identifier("stone_tool_materials")))
					.setType(MaterialItemType.PICKAXE, new Identifier("stone_pickaxe")).setType(MaterialItemType.AXE, new Identifier("stone_axe")).setType(MaterialItemType.SHOVEL, new Identifier("stone_shovel"))
					.setType(MaterialItemType.SWORD, new Identifier("stone_sword")).setType(MaterialItemType.HOE, new Identifier("stone_hoe"))
					.mattock().miningTool()
					.build()
	);
	public static final MaterialSet LEATHER = register(
			new MaterialSet.Builder("leather")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("leather"), "leather"))
					.setType(MaterialItemType.HELMET, new Identifier("leather_helmet")).setType(MaterialItemType.CHESTPLATE, new Identifier("leather_chestplate"))
					.setType(MaterialItemType.LEGGINGS, new Identifier("leather_leggings")).setType(MaterialItemType.BOOTS, new Identifier("leather_boots"))
					.build()
	);
	public static final MaterialSet IRON = register(
			new MaterialSet.Builder("iron")
					.setType(MaterialItemType.INGOT, new MaterialEntry(new Identifier("iron_ingot"), "iron_ingots"))
					.setType(MaterialItemType.NUGGET, new MaterialEntry(new Identifier("iron_nugget"), "iron_nuggets"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("iron_block"), "iron_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new Identifier("iron_ore"), "iron_ores"))
					.setType(MaterialItemType.PICKAXE, new Identifier("iron_pickaxe")).setType(MaterialItemType.AXE, new Identifier("iron_axe")).setType(MaterialItemType.SHOVEL, new Identifier("iron_shovel"))
					.setType(MaterialItemType.SWORD, new Identifier("iron_sword")).setType(MaterialItemType.HOE, new Identifier("iron_hoe"))
					.setType(MaterialItemType.HELMET, new Identifier("iron_helmet")).setType(MaterialItemType.CHESTPLATE, new Identifier("iron_chestplate"))
					.setType(MaterialItemType.LEGGINGS, new Identifier("iron_leggings")).setType(MaterialItemType.BOOTS, new Identifier("iron_boots"))
					.dusts().asteroid()
					.gear().plate()
					.multiTools()
					.build()
	);
	public static final MaterialSet GOLD = register(
			new MaterialSet.Builder("gold")
					.setType(MaterialItemType.INGOT, new MaterialEntry(new Identifier("gold_ingot"), "gold_ingots"))
					.setType(MaterialItemType.NUGGET, new MaterialEntry(new Identifier("gold_nugget"), "gold_nuggets"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("gold_block"), "gold_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new Identifier("gold_ore"), "gold_ores"))
					.setType(MaterialItemType.PICKAXE, new Identifier("golden_pickaxe")).setType(MaterialItemType.AXE, new Identifier("golden_axe")).setType(MaterialItemType.SHOVEL, new Identifier("golden_shovel"))
					.setType(MaterialItemType.SWORD, new Identifier("golden_sword")).setType(MaterialItemType.HOE, new Identifier("golden_hoe"))
					.setType(MaterialItemType.HELMET, new Identifier("golden_helmet")).setType(MaterialItemType.CHESTPLATE, new Identifier("golden_chestplate"))
					.setType(MaterialItemType.LEGGINGS, new Identifier("golden_leggings")).setType(MaterialItemType.BOOTS, new Identifier("golden_boots"))
					.setType(MaterialItemType.APPLE, new MaterialEntry(new Identifier("golden_apple"), "golden_apples"))
					.dusts().asteroid()
					.gear().plate().wire()
					.mattock(AstromineCommon.identifier("golden_mattock")).miningTool(AstromineCommon.identifier("golden_mining_tool"))
					.piglinLoved()
					.build()
	);
	public static final MaterialSet DIAMOND = register(
			new MaterialSet.Builder("diamond")
					.setType(MaterialItemType.GEM, new MaterialEntry(new Identifier("diamond"), "diamonds"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("diamond_block"), "diamond_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new Identifier("diamond_ore"), "diamond_ores"))
					.setType(MaterialItemType.PICKAXE, new Identifier("diamond_pickaxe")).setType(MaterialItemType.AXE, new Identifier("diamond_axe")).setType(MaterialItemType.SHOVEL, new Identifier("diamond_shovel"))
					.setType(MaterialItemType.SWORD, new Identifier("diamond_sword")).setType(MaterialItemType.HOE, new Identifier("diamond_hoe"))
					.setType(MaterialItemType.HELMET, new Identifier("diamond_helmet")).setType(MaterialItemType.CHESTPLATE, new Identifier("diamond_chestplate"))
					.setType(MaterialItemType.LEGGINGS, new Identifier("diamond_leggings")).setType(MaterialItemType.BOOTS, new Identifier("diamond_boots"))
					.dusts().asteroid()
					.fragment()
					.multiTools()
					.build()
	);
	public static final MaterialSet EMERALD = register(
			new MaterialSet.Builder("emerald")
					.setType(MaterialItemType.GEM, new MaterialEntry(new Identifier("emerald"), "emeralds"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("emerald_block"), "emerald_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new Identifier("emerald_ore"), "emerald_ores"))
					.dusts().asteroid()
					.fragment()
					.build()
	);
	public static final MaterialSet LAPIS = register(
			new MaterialSet.Builder("lapis")
					.setType(MaterialItemType.GEM, new MaterialEntry(new Identifier("lapis_lazuli"), "lapis_lazulis"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("lapis_block"), "lapis_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new Identifier("lapis_ore"), "lapis_ores"))
					.dusts().asteroid()
					.build()
	);
	public static final MaterialSet QUARTZ = register(
			new MaterialSet.Builder("quartz")
					.setType(MaterialItemType.GEM, new MaterialEntry(new Identifier("quartz"), "quartz"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new Identifier("nether_quartz_ore"), "quartz_ores"))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new Identifier("quartz_block"), "quartz_blocks"))
					.dusts()
					.fragment()
					.build()
	);
	public static final MaterialSet REDSTONE = register(
			new MaterialSet.Builder("redstone")
					.setType(MaterialItemType.DUST, new MaterialEntry(new Identifier("redstone"), "redstone_dusts"))
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("redstone")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("redstone_block"), "redstone_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new Identifier("redstone_ore"), "redstone_ores"))
					.tinyDust().asteroid()
					.build()
	);
	public static final MaterialSet COAL = register(
			new MaterialSet.Builder("coal")
					.setType(MaterialItemType.GEM, new MaterialEntry(new Identifier("coal"), "coal"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("coal_block"), "coal_blocks"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new Identifier("coal_ore"), "coal_ores"))
					.dusts().asteroid()
					.build()
	);
	public static final MaterialSet CHARCOAL = register(
			new MaterialSet.Builder("charcoal")
					.setType(MaterialItemType.GEM, new MaterialEntry(new Identifier("charcoal"), "charcoal"))
					.dusts()
					.build()
	);
	public static final MaterialSet GLOWSTONE = register(
			new MaterialSet.Builder("glowstone")
					.setType(MaterialItemType.DUST, new MaterialEntry(new Identifier("glowstone_dust"), "glowstone_dusts"))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new Identifier("glowstone"), "glowstone_blocks"))
					.tinyDust()
					.build()
	);
	public static final MaterialSet BONE = register(
			new MaterialSet.Builder("bone")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("bone_meal")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("bone_block"), "bone_blocks"))
					.build()
	);
	public static final MaterialSet WHEAT = register(
			new MaterialSet.Builder("wheat")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("wheat")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("hay_block")))
					.build()
	);
	public static final MaterialSet DRIED_KELP = register(
			new MaterialSet.Builder("dried_kelp")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("dried_kelp")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("dried_kelp_block")))
					.build()
	);
	public static final MaterialSet SLIME = register(
			new MaterialSet.Builder("slime")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("slime_ball")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("slime_block"), "slime_blocks"))
					.build()
	);
	public static final MaterialSet MAGMA = register(
			new MaterialSet.Builder("magma")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("magma_cream")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new Identifier("magma_block")))
					.build()
	);
	public static final MaterialSet HONEYCOMB = register(
			new MaterialSet.Builder("honeycomb")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("honeycomb")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new Identifier("honeycomb_block")))
					.build()
	);
	public static final MaterialSet NETHER_WART = register(
			new MaterialSet.Builder("nether_wart")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("nether_wart")))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("nether_wart_block")))
					.build()
	);
	public static final MaterialSet PURPUR = register(
			new MaterialSet.Builder("purpur")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("popped_chorus_fruit")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new Identifier("purpur_block"), "purpur_blocks"))
					.build()
	);
	public static final MaterialSet SNOW = register(
			new MaterialSet.Builder("snow")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("snowball")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new Identifier("snow_block")))
					.build()
	);
	public static final MaterialSet WOOL = register(
			new MaterialSet.Builder("wool")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("string")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new Identifier("white_wool"), new Identifier("wool")))
					.build()
	);
	public static final MaterialSet SAND = register(
			new MaterialSet.Builder("sand")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("sand")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new Identifier("sandstone"), "yellow_sandstones"))
					.build()
	);
	public static final MaterialSet RED_SAND = register(
			new MaterialSet.Builder("red_sand")
					.setType(MaterialItemType.MISC_RESOURCE, new MaterialEntry(new Identifier("red_sand")))
					.setType(MaterialItemType.BLOCK_2x2, new MaterialEntry(new Identifier("red_sandstone"), "red_sandstones"))
					.build()
	);
	public static final MaterialSet RAW_NETHERITE = register(
			new MaterialSet.Builder("raw_netherite")
					.setType(MaterialItemType.INGOT, new MaterialEntry(new Identifier("netherite_scrap"), "netherite_scraps"))
					.setType(MaterialItemType.ORE, new MaterialEntry(new Identifier("ancient_debris"), "ancient_debris"))
					.dusts()
					.build()
	);
	public static final MaterialSet NETHERITE = register(
			new MaterialSet.Builder("netherite")
					.setType(MaterialItemType.INGOT, new MaterialEntry(new Identifier("netherite_ingot"), "netherite_ingots"))
					.setType(MaterialItemType.BLOCK, new MaterialEntry(new Identifier("netherite_block"), "netherite_blocks"))
					.setType(MaterialItemType.PICKAXE, new Identifier("netherite_pickaxe")).setType(MaterialItemType.AXE, new Identifier("netherite_axe")).setType(MaterialItemType.SHOVEL, new Identifier("netherite_shovel"))
					.setType(MaterialItemType.SWORD, new Identifier("netherite_sword")).setType(MaterialItemType.HOE, new Identifier("netherite_hoe"))
					.setType(MaterialItemType.HELMET, new Identifier("netherite_helmet")).setType(MaterialItemType.CHESTPLATE, new Identifier("netherite_chestplate"))
					.setType(MaterialItemType.LEGGINGS, new Identifier("netherite_leggings")).setType(MaterialItemType.BOOTS, new Identifier("netherite_boots"))
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
	public static final MaterialSet SILVER = register(
			new MaterialSet.Builder("silver")
					.basics().metal().ore().asteroid().wire().allTools().armor()
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
	public static final MaterialSet ELECTRUM = register(
			new MaterialSet.Builder("electrum")
					.basics().metal().wire().allTools().armor()
					.build()
	);
	public static final MaterialSet ROSE_GOLD = register(
			new MaterialSet.Builder("rose_gold")
					.basics().metal().allTools().armor()
					.piglinLoved().apple()
					.build()
	);
	public static final MaterialSet STERLING_SILVER = register(
			new MaterialSet.Builder("sterling_silver")
					.basics().metal().allTools().armor()
					.build()
	);
	public static final MaterialSet FOOLS_GOLD = register(
			new MaterialSet.Builder("fools_gold")
					.basics().metal().allTools().armor()
					.piglinLoved().apple()
					.build()
	);
	public static final MaterialSet METITE = register(
			new MaterialSet.Builder("metite")
					.basics().metal().asteroid().meteor().allTools().armor()
					.build()
	);
	public static final MaterialSet ASTERITE = register(
			new MaterialSet.Builder("asterite")
					.basics().gem().fragment().asteroid().allTools().armor()
					.build()
	);
	public static final MaterialSet STELLUM = register(
			new MaterialSet.Builder("stellum")
					.basics().metal().asteroid().allTools().armor()
					.build()
	);
	public static final MaterialSet GALAXIUM = register(
			new MaterialSet.Builder("galaxium")
					.basics().gem().fragment().asteroid().allTools().armor()
					.build()
	);
	public static final MaterialSet UNIVITE = register(
			new MaterialSet.Builder("univite")
					.basics().metal().allTools().armor()
					.smithingBaseSet(GALAXIUM)
					.build()
	);
	public static final MaterialSet LUNUM = register(
			new MaterialSet.Builder("lunum")
					.basics().metal().moonOre().allTools().armor()
					.build()
	);
	public static final MaterialSet METEORIC_STEEL = register(
			new MaterialSet.Builder("meteoric_steel")
					.basics().metal().allTools().armor()
					.build()
	);
}
