package com.github.chainmailstudios.astromine.foundations.datagen;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.item.MultitoolItem;
import com.github.chainmailstudios.astromine.common.recipe.PressingRecipe;
import com.github.chainmailstudios.astromine.foundations.common.block.AstromineOreBlock;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsItems;
import com.google.common.collect.*;
import com.google.gson.JsonObject;
import draylar.magna.item.ExcavatorItem;
import draylar.magna.item.HammerItem;
import me.shedaniel.cloth.api.datagen.v1.DataGeneratorHandler;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.server.recipe.*;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.util.TriConsumer;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class AstromineFoundationsDatagen implements PreLaunchEntrypoint {
	private static final Multimap<Class<?>, DataGenConsumer<?>> CONSUMERS = Multimaps.newListMultimap(Maps.newHashMap(), Lists::newArrayList);
	private static final Map<BiPredicate<Block, Identifier>, TriConsumer<LootTableData, Block, Identifier>> BLOCK_LOOT_TABLES = Maps.newLinkedHashMap();

	@Override
	public void onPreLaunch() {
		try {
			DataGeneratorHandler handler = DataGeneratorHandler.create(Paths.get("../astromine-foundations/src/generated/resources"));
			FabricLoader.getInstance().getEntrypoints("main", ModInitializer.class).forEach(ModInitializer::onInitialize);

			register();
			registerConsumers();

			tags(handler.getTags());
			recipes(handler.getRecipes());

			iterate(AstromineFoundationsBlocks.class, Block.class, value -> callConsumers(handler, Block.class, value));
			iterate(AstromineFoundationsItems.class, Item.class, value -> callConsumers(handler, Item.class, value));

			handler.run();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	private static void register() {
		registerBlockLootTableOverrides((block, identifier) -> block instanceof SlabBlock, (lootTableData, block, identifier) ->
			lootTableData.register(block, LootTableData.dropsSlabs(block)));
		registerBlockLootTableOverrides((block, identifier) -> block instanceof AstromineOreBlock && identifier.getPath().startsWith("asteroid_") || identifier.getPath().startsWith("meteor_"),
			(lootTableData, block, identifier) -> lootTableData.register(block, LootTableData.dropsBlockWithSilkTouch(
				block,
				LootTableData.addExplosionDecayLootFunction(
					block,
					ItemEntry.builder(Registry.ITEM.get(AstromineCommon.identifier(identifier.toString().replace("_ore", "_cluster"))))
						.apply(SetCountLootFunction.builder(UniformLootTableRange.between(1, 3)))
						.apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
				)
			))
		);
	}

	private static void registerConsumers() {
		register(Block.class, (handler, value) -> {
			Identifier id = Registry.BLOCK.getId(value);
			BLOCK_LOOT_TABLES.entrySet().stream()
				.filter(entry -> entry.getKey().test(value, id))
				.findFirst()
				.map(Map.Entry::getValue)
				.orElse((lootTableData, block, identifier) -> lootTableData.registerBlockDropSelf(block))
				.accept(handler.getLootTables(), value, id);
		});
	}

	private static <T> void callConsumers(DataGeneratorHandler handler, Class<T> valueClass, T value) {
		CONSUMERS.forEach((clazz, dataGenConsumer) -> {
			if (clazz.isAssignableFrom(valueClass)) {
				((DataGenConsumer<T>) dataGenConsumer).accept(handler, value);
			}
		});
	}

	private static <T> void register(Class<T> clazz, DataGenConsumer<T> consumer) {
		CONSUMERS.put(clazz, consumer);
	}

	private static <T> void registerBlockLootTableOverrides(BiPredicate<Block, Identifier> predicate, TriConsumer<LootTableData, Block, Identifier> consumer) {
		BLOCK_LOOT_TABLES.put(predicate, consumer);
	}

	private static void tags(TagData tags) {
		// items
		iterate(AstromineFoundationsItems.class, Item.class, item -> {
			Identifier key = Registry.ITEM.getKey(item).get().getValue();
			if (key.toString().endsWith("_cluster") || key.toString().endsWith("_gear") || key.toString().endsWith("_plates") ||
			    key.toString().endsWith("_dust") || key.toString().endsWith("_ingot") || key.toString().endsWith("_wire") ||
			    key.toString().endsWith("_nugget") || key.toString().endsWith("_fragment") ||
			    item == AstromineFoundationsItems.ASTERITE || item == AstromineFoundationsItems.GALAXIUM) {
				Identifier tagId = key.toString().endsWith("s") ? key : new Identifier(key.toString() + "s");
				tagId = new Identifier("c", tagId.getPath().replaceFirst("asteroid_", "").replaceFirst("meteor_", ""));

				TagData.TagBuilder<ItemConvertible> builder = tags.item(tagId).append(item);

				if (tagId.getPath().startsWith("fools_gold")) {
					tagId = new Identifier("c", tagId.getPath().replaceFirst("fools_gold", "pyrite"));
					tags.item(tagId).appendTag(builder);
				}
				if (tagId.getPath().endsWith("_fragments")) {
					tagId = new Identifier("c", tagId.getPath().replaceFirst("_fragments", "_nuggets"));
					tags.item(tagId).appendTag(builder);
				}
			}
			if (key.toString().contains("gold_ingot")) {
				tags.item(AstromineCommon.identifier("piglin_bartering_items")).append(item);
			}
			if (key.toString().contains("gold_nugget")) {
				tags.item(AstromineCommon.identifier("piglin_loved_nuggets")).append(item);
			}
			if (key.toString().contains("fools_gold")) {
				tags.item(AstromineCommon.identifier("tricks_piglins")).append(item);
			}
			if (key.toString().contains("gold")) {
				tags.item(new Identifier("piglin_loved")).append(item);

				if (item instanceof ArmorItem) {
					tags.item(AstromineCommon.identifier("piglin_safe_armor")).append(item);
				}
			}
			if (item instanceof PickaxeItem || item instanceof HammerItem || (item instanceof MultitoolItem && (((MultitoolItem) item).first instanceof PickaxeItem || ((MultitoolItem) item).second instanceof PickaxeItem))) {
				tags.item(new Identifier("fabric:pickaxes")).append(item);
			}
			if (item instanceof AxeItem || (item instanceof MultitoolItem && (((MultitoolItem) item).first instanceof AxeItem || ((MultitoolItem) item).second instanceof AxeItem))) {
				tags.item(new Identifier("fabric:axes")).append(item);
			}
			if (item instanceof HoeItem || (item instanceof MultitoolItem && (((MultitoolItem) item).first instanceof HoeItem || ((MultitoolItem) item).second instanceof HoeItem))) {
				tags.item(new Identifier("fabric:hoes")).append(item);
			}
			if (item instanceof ShovelItem || item instanceof ExcavatorItem || (item instanceof MultitoolItem && (((MultitoolItem) item).first instanceof ShovelItem || ((MultitoolItem) item).second instanceof ShovelItem))) {
				tags.item(new Identifier("fabric:shovels")).append(item);
			}
			if (item instanceof SwordItem) {
				tags.item(new Identifier("fabric:shovels")).append(item);
			}
		});

		tags.item(new Identifier("c:diamonds")).append(Items.DIAMOND);
		tags.item(new Identifier("c:emeralds")).append(Items.EMERALD);
		tags.item(new Identifier("c:iron_ingots")).append(Items.IRON_INGOT);
		tags.item(new Identifier("c:gold_ingots")).append(Items.GOLD_INGOT);
		tags.item(new Identifier("c:redstone_dusts")).append(Items.REDSTONE);
		tags.item(new Identifier("c:lapis_lazulis")).append(Items.LAPIS_LAZULI);
		tags.item(new Identifier("c:netherite_ingots")).append(Items.NETHERITE_INGOT);
		tags.item(new Identifier("c:quartz")).append(Items.QUARTZ);

		tags.item(new Identifier("c:wood_sticks")).append(Items.STICK);

		tags.item(AstromineCommon.identifier("carbon_dusts")).appendTag(new Identifier("c:coal_dusts")).appendTag(new Identifier("c:charcoal_dusts"));

		// blocks
		tags.block(AstromineCommon.identifier("metite_ores")).append(AstromineFoundationsBlocks.METEOR_METITE_ORE, AstromineFoundationsBlocks.ASTEROID_METITE_ORE);

		iterate(AstromineFoundationsBlocks.class, Block.class, block -> {
			Identifier key = Registry.BLOCK.getKey(block).get().getValue();
			if (key.toString().endsWith("_block") || key.toString().endsWith("_ore")) {
				Identifier tagId = new Identifier("c", key.getPath().replaceFirst("asteroid_", "").replaceFirst("meteor_", "") + "s");

				if (key.toString().endsWith("_block"))
					tags.block(new Identifier("beacon_base_blocks")).append(block);
				TagData.TagBuilder<ItemConvertible> itemBuilder = tags.item(tagId).append(block);
				TagData.TagBuilder<Block> blockBuilder = tags.block(tagId).append(block);

				if (tagId.getPath().startsWith("fools_gold")) {
					tagId = new Identifier("c", tagId.getPath().replaceFirst("fools_gold", "pyrite"));
					tags.item(tagId).appendTag(itemBuilder);
					tags.block(tagId).appendTag(blockBuilder);
				}
			}
			if (block instanceof AstromineOreBlock) {
				tags.block(AstromineCommon.identifier("rocket_explode")).append(block);
			}
		});
		tags.block(new Identifier("c:diamond_ores")).append(Blocks.DIAMOND_ORE);
		tags.block(new Identifier("c:emerald_ores")).append(Blocks.EMERALD_ORE);
		tags.block(new Identifier("c:iron_ores")).append(Blocks.IRON_ORE);
		tags.block(new Identifier("c:gold_ores")).appendTag(new Identifier("gold_ores"));
		tags.block(new Identifier("c:redstone_ores")).append(Blocks.REDSTONE_ORE);
		tags.block(new Identifier("c:lapis_ores")).append(Blocks.LAPIS_ORE);
		tags.block(new Identifier("c:quartz_ores")).append(Blocks.NETHER_QUARTZ_ORE);
		tags.block(new Identifier("gold_ores")).append(Blocks.NETHER_GOLD_ORE);
		tags.block(new Identifier("c:cobblestones")).append(Blocks.COBBLESTONE);

		tags.item(new Identifier("c:diamond_ores")).append(Blocks.DIAMOND_ORE);
		tags.item(new Identifier("c:emerald_ores")).append(Blocks.EMERALD_ORE);
		tags.item(new Identifier("c:iron_ores")).append(Blocks.IRON_ORE);
		tags.item(new Identifier("c:gold_ores")).appendTag(new Identifier("gold_ores"));
		tags.item(new Identifier("c:redstone_ores")).append(Blocks.REDSTONE_ORE);
		tags.item(new Identifier("c:lapis_ores")).append(Blocks.LAPIS_ORE);
		tags.item(new Identifier("c:quartz_ores")).append(Blocks.NETHER_QUARTZ_ORE);
		tags.item(new Identifier("gold_ores")).append(Blocks.NETHER_GOLD_ORE);
		tags.item(new Identifier("c:cobblestones")).append(Blocks.COBBLESTONE);
	}

	private static void recipes(RecipeData recipes) {
		ImmutableMap<String, String> materialMap = ImmutableMap.<String, String>builder()
			.put("copper", "c:copper_ingots")
			.put("tin", "c:tin_ingots")
			.put("silver", "c:silver_ingots")
			.put("lead", "c:lead_ingots")
			.put("bronze", "c:bronze_ingots")
			.put("steel", "c:steel_ingots")
			.put("electrum", "c:electrum_ingots")
			.put("rose_gold", "c:rose_gold_ingots")
			.put("sterling_silver", "c:sterling_silver_ingots")
			.put("fools_gold", "c:fools_gold_ingots")
			.put("metite", "c:metite_ingots")
			.put("asterite", "c:asterites")
			.put("stellum", "c:stellum_ingots")
			.put("galaxium", "c:galaxiums")
			.put("diamond", "c:diamonds")
			.put("emerald", "c:emeralds")
			.put("quartz", "c:quartz")
			.put("univite", "c:univite_ingots")
			.put("wooden", "minecraft:planks")
			.put("stone", "c:cobblestones")
			.put("iron", "c:iron_ingots")
			.put("gold", "c:gold_ingots")
			.put("netherite", "c:netherite_ingots")
			.build();
		Set<String> addedCluster = Sets.newHashSet();
		iterate(AstromineFoundationsItems.class, Item.class, item -> {
			Identifier key = Registry.ITEM.getKey(item).get().getValue();
			if (key.getPath().indexOf('_') > 0) {
				String material = materialMap.get(key.getPath().replace("meteor_", "").replace("asteroid_", "").replace("mining_tool", "miningtool").substring(0, key.getPath().replace("meteor_", "").replace("asteroid_", "").replace("mining_tool", "miningtool").lastIndexOf('_')));
				if (material != null) {
					Identifier materialFromTag = guessMaterialFromTag(material);
					if (!key.getPath().startsWith("univite_")) {
						if (key.toString().endsWith("_axe")) {
							axeRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_pickaxe")) {
							pickaxeRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_hoe")) {
							hoeRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_sword")) {
							swordRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_shovel")) {
							shovelRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_mining_tool")) {
							miningToolRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_mattock")) {
							mattockRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_hammer")) {
							hammerRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_excavator")) {
							excavatorRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_helmet")) {
							helmetRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_chestplate")) {
							chestplateRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_leggings")) {
							leggingsRecipe(recipes, item, new Identifier(material));
						} else if (key.toString().endsWith("_boots")) {
							bootsRecipe(recipes, item, new Identifier(material));
						}
					} else if (item instanceof ToolItem || item instanceof DynamicAttributeTool || item instanceof ArmorItem) {
						// univite smithing
						SmithingRecipeJsonFactory.create(
							Ingredient.ofItems(Registry.ITEM.get(AstromineCommon.identifier(key.getPath().replace("univite_", "galaxium_")))),
							Ingredient.fromTag(TagRegistry.item(new Identifier("c:univite_ingots"))),
							item
						).criterion("impossible", new ImpossibleCriterion.Conditions())
							.offerTo(recipes, key + "_smithing");
					}
					if (key.toString().endsWith("_nugget") || key.toString().endsWith("_fragment")) {
						// 1 ingot -> 9 nuggets / fragments
						ShapelessRecipeJsonFactory.create(item, 9)
							.criterion("impossible", new ImpossibleCriterion.Conditions())
							.input(TagRegistry.item(new Identifier(material)))
							.offerTo(recipes, key + "_from_ingot");
						// 9 nuggets / fragments -> 1 ingot
						Identifier ingotItem = materialFromTag;
						ShapedRecipeJsonFactory.create(Registry.ITEM.get(ingotItem))
							.criterion("impossible", new ImpossibleCriterion.Conditions())
							.pattern("###")
							.pattern("###")
							.pattern("###")
							.input('#', TagRegistry.item(new Identifier("c", key.getPath() + "s")))
							.offerTo(recipes, ingotItem.toString() + "_from_" + key.getPath().substring(key.getPath().lastIndexOf('_') + 1));
					} else if (key.toString().endsWith("_plates")) {
						// 2 ingots -> 1 plate
						ShapedRecipeJsonFactory.create(item)
							.criterion("impossible", new ImpossibleCriterion.Conditions())
							.pattern("#")
							.pattern("#")
							.input('#', TagRegistry.item(new Identifier(material)))
							.offerTo(recipes, key + "_from_ingot");
						// pressing 1 ingots -> 1 plate
						pressing(new Identifier(key + "_from_pressing_ingot"), Ingredient.fromTag(TagRegistry.item(new Identifier(material))), 1,
							new ItemStack(item), 60, 384).accept(recipes);
					} else if (key.toString().endsWith("_gear")) {
						// 4 ingots -> 1 gear
						ShapedRecipeJsonFactory.create(item)
							.criterion("impossible", new ImpossibleCriterion.Conditions())
							.pattern(" # ")
							.pattern("# #")
							.pattern(" # ")
							.input('#', TagRegistry.item(new Identifier(material)))
							.offerTo(recipes, key + "_from_ingot");
					}
					if (key.getPath().endsWith("_cluster")) {
						if (addedCluster.add(material)) {
							CookingRecipeJsonFactory.createSmelting(
								Ingredient.fromTag(TagRegistry.item(new Identifier("c", key.getPath() + "s"))),
								Registry.ITEM.get(materialFromTag),
								0.1F,
								200
							).criterion("impossible", new ImpossibleCriterion.Conditions())
								.offerTo(recipes, materialFromTag.getPath() + "_from_smelting_cluster");
							CookingRecipeJsonFactory.createBlasting(
								Ingredient.fromTag(TagRegistry.item(new Identifier("c", key.getPath() + "s"))),
								Registry.ITEM.get(materialFromTag),
								0.1F,
								100
							).criterion("impossible", new ImpossibleCriterion.Conditions())
								.offerTo(recipes, materialFromTag.getPath() + "_from_blasting_cluster");
						}
					}
				}
			}

			if (key.toString().endsWith("_tiny_dust")) {
				// 9 tiny dusts -> 1 dust
				Identifier dustItem = new Identifier(key.getNamespace(), key.getPath().replace("_tiny_dust", "_dust"));
				if (dustItem.toString().equals("astromine:redstone_dust")) {
					dustItem = new Identifier("redstone");
				} else if (!Registry.ITEM.getOrEmpty(dustItem).isPresent()) {
					dustItem = new Identifier(dustItem.getPath());
				}
				ShapedRecipeJsonFactory.create(Registry.ITEM.get(dustItem))
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("###")
					.pattern("###")
					.input('#', TagRegistry.item(new Identifier("c", key.getPath() + "s")))
					.offerTo(recipes, dustItem + "_from_tiny_dust");
				// 1 dust -> 9 tiny dusts
				ShapelessRecipeJsonFactory.create(item, 9)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.input(TagRegistry.item(new Identifier("c", dustItem.getPath() + "s")))
					.offerTo(recipes, key + "_from_dust");
			}
		});
		Set<String> addedOre = Sets.newHashSet();
		iterate(AstromineFoundationsBlocks.class, Block.class, block -> {
			Identifier key = Registry.BLOCK.getKey(block).get().getValue();
			String material = materialMap.get(key.getPath().replace("meteor_", "").replace("asteroid_", "").substring(0, key.getPath().replace("meteor_", "").replace("asteroid_", "").lastIndexOf('_')));
			if (material != null) {
				Identifier materialFromTag = guessMaterialFromTag(material);
				if (key.getPath().endsWith("_block")) {
					// 9 ingots -> 1 block
					ShapedRecipeJsonFactory.create(block)
						.criterion("impossible", new ImpossibleCriterion.Conditions())
						.pattern("###")
						.pattern("###")
						.pattern("###")
						.input('#', TagRegistry.item(new Identifier(material)))
						.offerTo(recipes, key + "_from_ingot");
					// 1 block -> 9 ingots
					ShapelessRecipeJsonFactory.create(Registry.ITEM.get(materialFromTag), 9)
						.criterion("impossible", new ImpossibleCriterion.Conditions())
						.input(TagRegistry.item(new Identifier("c", key.getPath() + "s")))
						.offerTo(recipes, materialFromTag + "_from_block");
				} else if (key.getPath().endsWith("_ore")) {
					if (addedOre.add(material)) {
						CookingRecipeJsonFactory.createSmelting(
							Ingredient.fromTag(TagRegistry.item(new Identifier("c", key.getPath() + "s"))),
							Registry.ITEM.get(materialFromTag),
							0.1F,
							200
						).criterion("impossible", new ImpossibleCriterion.Conditions())
							.offerTo(recipes, materialFromTag.getPath() + "_from_smelting_ore");
						CookingRecipeJsonFactory.createBlasting(
							Ingredient.fromTag(TagRegistry.item(new Identifier("c", key.getPath() + "s"))),
							Registry.ITEM.get(materialFromTag),
							0.1F,
							100
						).criterion("impossible", new ImpossibleCriterion.Conditions())
							.offerTo(recipes, materialFromTag.getPath() + "_from_blasting_ore");
					}
				}
			}
		});
	}

	private static Identifier guessMaterialFromTag(String tag) {
		Identifier ingotItem = AstromineCommon.identifier((new Identifier(tag).getNamespace().equals("c") ? "" : new Identifier(tag).getNamespace() + ":") +
		                                                  (tag.endsWith("s") ? new Identifier(tag).getPath().substring(0, new Identifier(tag).getPath().length() - 1) : new Identifier(tag).getPath()));
		if (!Registry.ITEM.getOrEmpty(ingotItem).isPresent()) {
			return new Identifier(ingotItem.getPath());
		}
		return ingotItem;
	}

	private static void axeRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("##")
			.pattern("#$")
			.pattern(" $")
			.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void pickaxeRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("###")
			.pattern(" $ ")
			.pattern(" $ ")
			.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void hoeRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("##")
			.pattern(" $")
			.pattern(" $")
			.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void swordRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("#")
			.pattern("#")
			.pattern("$")
			.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void shovelRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("#")
			.pattern("$")
			.pattern("$")
			.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void miningToolRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("###")
			.pattern(" # ")
			.pattern(" $ ")
			.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void mattockRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("###")
			.pattern("#$ ")
			.pattern(" $ ")
			.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void hammerRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("###")
			.pattern("#$#")
			.pattern(" $ ")
			.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void excavatorRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern(" # ")
			.pattern("#$#")
			.pattern(" $ ")
			.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void helmetRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("###")
			.pattern("# #")
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void chestplateRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("# #")
			.pattern("###")
			.pattern("###")
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void leggingsRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("###")
			.pattern("# #")
			.pattern("# #")
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static void bootsRecipe(RecipeData recipes, Item output, Identifier material) {
		ShapedRecipeJsonFactory.create(output)
			.criterion("impossible", new ImpossibleCriterion.Conditions())
			.pattern("# #")
			.pattern("# #")
			.input('#', TagRegistry.item(material))
			.offerTo(recipes);
	}

	private static Consumer<Consumer<RecipeJsonProvider>> pressing(Ingredient input, int inputCount, ItemStack output, int time, int energyConsumed) {
		return pressing(Registry.ITEM.getId(output.getItem()), input, inputCount, output, time, energyConsumed);
	}

	private static Consumer<Consumer<RecipeJsonProvider>> pressing(Identifier id, Ingredient input, int inputCount, ItemStack output, int time, int energyConsumed) {
		return consumer -> consumer.accept(new BasicRecipeJsonProvider(PressingRecipe.Serializer.INSTANCE, id) {
			@Override
			public void serialize(JsonObject json) {
				JsonObject inputJson = (JsonObject) input.toJson();
				inputJson.addProperty("count", inputCount);
				json.add("input", inputJson);
				JsonObject outputJson = new JsonObject();
				outputJson.addProperty("item", Registry.ITEM.getId(output.getItem()).toString());
				outputJson.addProperty("count", output.getCount());
				json.add("output", outputJson);
				json.addProperty("time", time);
				json.addProperty("energy_consumed", energyConsumed);
			}
		});
	}

	private static abstract class BasicRecipeJsonProvider implements RecipeJsonProvider {
		private final RecipeSerializer<?> serializer;
		private final Identifier id;

		public BasicRecipeJsonProvider(RecipeSerializer<?> serializer, Identifier id) {
			this.serializer = serializer;
			this.id = id;
		}

		@Override
		public Identifier getRecipeId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getSerializer() {
			return serializer;
		}

		@Override
		public JsonObject toAdvancementJson() {
			return null;
		}

		@Override
		public Identifier getAdvancementId() {
			return null;
		}
	}

	private static <T> void iterate(Class<?> scanning, Class<T> clazz, Consumer<T> consumer) {
		for (Field field : scanning.getDeclaredFields()) {
			if (clazz.isAssignableFrom(field.getType())) {
				try {
					consumer.accept((T) field.get(null));
				} catch (IllegalAccessException ignored) {

				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			}
		}
	}
}
