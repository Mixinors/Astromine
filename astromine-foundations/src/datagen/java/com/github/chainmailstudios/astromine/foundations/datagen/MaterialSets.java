package com.github.chainmailstudios.astromine.foundations.datagen;

import com.github.chainmailstudios.astromine.AstromineCommon;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType.*;

public class MaterialSets {
	public static final List<MaterialSet> MATERIAL_SETS = new ArrayList<>();

	public static final MaterialSet WOOD = register(
		new MaterialSet.Builder("wood")
			.setType(MISC_RESOURCE, new MaterialEntry(new Identifier("oak_planks"), new Identifier("planks")))
			.setType(PICKAXE, new Identifier("wooden_pickaxe")).setType(AXE, new Identifier("wooden_axe")).setType(SHOVEL, new Identifier("wooden_shovel"))
			.setType(SWORD, new Identifier("wooden_sword")).setType(HOE, new Identifier("wooden_hoe"))
			.mattock(AstromineCommon.identifier("wooden_mattock")).miningTool(AstromineCommon.identifier("wooden_mining_tool"))
			.build()
	);
	public static final MaterialSet STONE = register(
		new MaterialSet.Builder("stone")
			.setType(MISC_RESOURCE, new MaterialEntry(new Identifier("cobblestone"), new Identifier("stone_tool_materials")))
			.setType(PICKAXE, new Identifier("stone_pickaxe")).setType(AXE, new Identifier("stone_axe")).setType(SHOVEL, new Identifier("stone_shovel"))
			.setType(SWORD, new Identifier("stone_sword")).setType(HOE, new Identifier("stone_hoe"))
			.mattock().miningTool()
			.build()
	);
	public static final MaterialSet LEATHER = register(
		new MaterialSet.Builder("leather")
			.setType(MISC_RESOURCE, new MaterialEntry(new Identifier("leather"), "leather"))
			.setType(HELMET, new Identifier("leather_helmet")).setType(CHESTPLATE, new Identifier("leather_chestplate"))
			.setType(LEGGINGS, new Identifier("leather_leggings")).setType(BOOTS, new Identifier("leather_boots"))
			.build()
	);
	public static final MaterialSet IRON = register(
		new MaterialSet.Builder("iron")
			.setType(INGOT, new MaterialEntry(new Identifier("iron_ingot"), "iron_ingots"))
			.setType(NUGGET, new MaterialEntry(new Identifier("iron_nugget"), "iron_nuggets"))
			.setType(BLOCK, new MaterialEntry(new Identifier("iron_block"), "iron_blocks"))
			.setType(ORE, new MaterialEntry(new Identifier("iron_ore"), "iron_ores"))
			.setType(PICKAXE, new Identifier("iron_pickaxe")).setType(AXE, new Identifier("iron_axe")).setType(SHOVEL, new Identifier("iron_shovel"))
			.setType(SWORD, new Identifier("iron_sword")).setType(HOE, new Identifier("iron_hoe"))
			.setType(HELMET, new Identifier("iron_helmet")).setType(CHESTPLATE, new Identifier("iron_chestplate"))
			.setType(LEGGINGS, new Identifier("iron_leggings")).setType(BOOTS, new Identifier("iron_boots"))
			.dusts().asteroid()
			.gear().plates()
			.multiTools()
			.build()
	);
	public static final MaterialSet GOLD = register(
		new MaterialSet.Builder("gold")
			.setType(INGOT, new MaterialEntry(new Identifier("gold_ingot"), "gold_ingots"))
			.setType(NUGGET, new MaterialEntry(new Identifier("gold_nugget"), "gold_nuggets"))
			.setType(BLOCK, new MaterialEntry(new Identifier("gold_block"), "gold_blocks"))
			.setType(ORE, new MaterialEntry(new Identifier("gold_ore"), "gold_ores"))
			.setType(PICKAXE, new Identifier("golden_pickaxe")).setType(AXE, new Identifier("golden_axe")).setType(SHOVEL, new Identifier("golden_shovel"))
			.setType(SWORD, new Identifier("golden_sword")).setType(HOE, new Identifier("golden_hoe"))
			.setType(HELMET, new Identifier("golden_helmet")).setType(CHESTPLATE, new Identifier("golden_chestplate"))
			.setType(LEGGINGS, new Identifier("golden_leggings")).setType(BOOTS, new Identifier("golden_boots"))
			.dusts().asteroid()
			.gear().plates().wire()
			.mattock(AstromineCommon.identifier("golden_mattock")).miningTool(AstromineCommon.identifier("golden_mining_tool"))
			.piglinLoved()
			.build()
	);
	public static final MaterialSet DIAMOND = register(
		new MaterialSet.Builder("diamond")
			.setType(GEM, new MaterialEntry(new Identifier("diamond"), "diamonds"))
			.setType(BLOCK, new MaterialEntry(new Identifier("diamond_block"), "diamond_blocks"))
			.setType(ORE, new MaterialEntry(new Identifier("diamond_ore"), "diamond_ores"))
			.setType(PICKAXE, new Identifier("diamond_pickaxe")).setType(AXE, new Identifier("diamond_axe")).setType(SHOVEL, new Identifier("diamond_shovel"))
			.setType(SWORD, new Identifier("diamond_sword")).setType(HOE, new Identifier("diamond_hoe"))
			.setType(HELMET, new Identifier("diamond_helmet")).setType(CHESTPLATE, new Identifier("diamond_chestplate"))
			.setType(LEGGINGS, new Identifier("diamond_leggings")).setType(BOOTS, new Identifier("diamond_boots"))
			.dusts().asteroid()
			.fragment()
			.multiTools()
			.build()
	);
	public static final MaterialSet EMERALD = register(
		new MaterialSet.Builder("emerald")
			.setType(GEM, new MaterialEntry(new Identifier("emerald"), "emeralds"))
			.setType(BLOCK, new MaterialEntry(new Identifier("emerald_block"), "emerald_blocks"))
			.setType(ORE, new MaterialEntry(new Identifier("emerald_ore"), "emerald_ores"))
			.dusts().asteroid()
			.fragment()
			.build()
	);
	public static final MaterialSet LAPIS = register(
		new MaterialSet.Builder("lapis")
			.setType(GEM, new MaterialEntry(new Identifier("lapis_lazuli"), "lapis_lazulis"))
			.setType(BLOCK, new MaterialEntry(new Identifier("lapis_block"), "lapis_blocks"))
			.setType(ORE, new MaterialEntry(new Identifier("lapis_ore"), "lapis_ores"))
			.dusts().asteroid()
			.build()
	);
	public static final MaterialSet QUARTZ = register(
		new MaterialSet.Builder("quartz")
			.setType(GEM, new MaterialEntry(new Identifier("quartz"), "quartz"))
			.setType(ORE, new MaterialEntry(new Identifier("nether_quartz_ore"), "quartz_ores"))
			.dusts()
			.fragment()
			.build()
	);
	public static final MaterialSet REDSTONE = register(
		new MaterialSet.Builder("redstone")
			.setType(DUST, new MaterialEntry(new Identifier("redstone"), "redstone_dusts"))
			.setType(MISC_RESOURCE, new MaterialEntry(new Identifier("redstone")))
			.setType(BLOCK, new MaterialEntry(new Identifier("redstone_block"), "redstone_blocks"))
			.setType(ORE, new MaterialEntry(new Identifier("redstone_ore"), "redstone_ores"))
			.tinyDust().asteroid()
			.build()
	);
	public static final MaterialSet COAL = register(
		new MaterialSet.Builder("coal")
			.setType(GEM, new MaterialEntry(new Identifier("coal"), "coal"))
			.setType(BLOCK, new MaterialEntry(new Identifier("coal_block"), "coal_blocks"))
			.setType(ORE, new MaterialEntry(new Identifier("coal_ore"), "coal_ores"))
			.dusts().asteroid()
			.build()
	);
	public static final MaterialSet CHARCOAL = register(
		new MaterialSet.Builder("charcoal")
			.setType(GEM, new MaterialEntry(new Identifier("charcoal"), "charcoal"))
			.dusts()
			.build()
	);
	public static final MaterialSet RAW_NETHERITE = register(
		new MaterialSet.Builder("raw_netherite")
			.setType(INGOT, new MaterialEntry(new Identifier("netherite_scrap"), "netherite_scraps"))
			.setType(ORE, new MaterialEntry(new Identifier("ancient_debris"), "ancient_debris"))
			.dusts()
			.build()
	);
	public static final MaterialSet NETHERITE = register(
		new MaterialSet.Builder("netherite")
			.setType(INGOT, new MaterialEntry(new Identifier("netherite_ingot"), "netherite_ingots"))
			.setType(BLOCK, new MaterialEntry(new Identifier("netherite_block"), "netherite_blocks"))
			.setType(PICKAXE, new Identifier("netherite_pickaxe")).setType(AXE, new Identifier("netherite_axe")).setType(SHOVEL, new Identifier("netherite_shovel"))
			.setType(SWORD, new Identifier("netherite_sword")).setType(HOE, new Identifier("netherite_hoe"))
			.setType(HELMET, new Identifier("netherite_helmet")).setType(CHESTPLATE, new Identifier("netherite_chestplate"))
			.setType(LEGGINGS, new Identifier("netherite_leggings")).setType(BOOTS, new Identifier("netherite_boots"))
			.nugget().dusts()
			.gear().plates()
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
			.basics().metal().ore().asteroid().wire().allTools().armor()
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
			.build()
	);
	public static final MaterialSet STEEL = register(
		new MaterialSet.Builder("steel")
			.basics().metal().wire().allTools().armor()
			.build()
	);
	public static final MaterialSet BRONZE = register(
		new MaterialSet.Builder("bronze")
			.basics().metal().allTools().armor()
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
			.piglinLoved()
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
			.piglinLoved()
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

	public static MaterialSet register(MaterialSet set) {
		MATERIAL_SETS.add(set);
		System.out.println("registered material " + set.getName());
		return set;
	}

	public static void generateRecipes(RecipeData recipes) {
		MATERIAL_SETS.forEach((set) -> {
			try {
				RecipeGenerators.generateRecipes(recipes, set);
				System.out.println("generated recipes for " + set.getName());
			} catch (Exception e) {
				System.out.println("oh fuck recipe *big* bronked for " + set.getName());
				System.out.println(e.getMessage());
			}
		});
	}

	public static void generateTags(TagData tags) {
		MATERIAL_SETS.forEach((set) -> {
			try {
				TagGenerators.generateSetTags(tags, set);
				set.generateTags(tags);
				System.out.println("generated tags for " + set.getName());
			} catch (Exception e) {
				System.out.println("oh fuck tag bronked for " + set.getName());
				System.out.println(e.getMessage());
			}
		});
	}

	public static void initialize() {

	}
}
