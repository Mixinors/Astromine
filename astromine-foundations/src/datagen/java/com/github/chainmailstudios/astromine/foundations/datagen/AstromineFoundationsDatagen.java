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
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import static com.github.chainmailstudios.astromine.foundations.datagen.MaterialEntry.of;

public class AstromineFoundationsDatagen implements PreLaunchEntrypoint {
	public static final BiMap<String, MaterialEntry> MATERIALS = ImmutableBiMap.<String, MaterialEntry>builder()
		.put("copper", of(AstromineFoundationsItems.COPPER_INGOT, "c:copper_ingots"))
		.put("tin", of(AstromineFoundationsItems.TIN_INGOT, "c:tin_ingots"))
		.put("silver", of(AstromineFoundationsItems.SILVER_INGOT, "c:silver_ingots"))
		.put("lead", of(AstromineFoundationsItems.LEAD_INGOT, "c:lead_ingots"))
		.put("bronze", of(AstromineFoundationsItems.BRONZE_INGOT, "c:bronze_ingots"))
		.put("steel", of(AstromineFoundationsItems.STEEL_INGOT, "c:steel_ingots"))
		.put("electrum", of(AstromineFoundationsItems.ELECTRUM_INGOT, "c:electrum_ingots"))
		.put("rose_gold", of(AstromineFoundationsItems.ROSE_GOLD_INGOT, "c:rose_gold_ingots"))
		.put("sterling_silver", of(AstromineFoundationsItems.STERLING_SILVER_INGOT, "c:sterling_silver_ingots"))
		.put("fools_gold", of(AstromineFoundationsItems.FOOLS_GOLD_INGOT, "c:fools_gold_ingots"))
		.put("metite", of(AstromineFoundationsItems.METITE_INGOT, "c:metite_ingots"))
		.put("asterite", of(AstromineFoundationsItems.ASTERITE, "c:asterites"))
		.put("stellum", of(AstromineFoundationsItems.STELLUM_INGOT, "c:stellum_ingots"))
		.put("galaxium", of(AstromineFoundationsItems.GALAXIUM, "c:galaxiums"))
		.put("diamond", of(Items.DIAMOND, "c:diamonds"))
		.put("emerald", of(Items.EMERALD, "c:emeralds"))
		.put("quartz", of(Items.QUARTZ, "c:quartz"))
		.put("univite", of(AstromineFoundationsItems.UNIVITE_INGOT, "c:univite_ingots"))
		.put("wooden", of(Items.OAK_PLANKS, "minecraft:planks"))
		.put("stone", of(Items.COBBLESTONE, "c:cobblestones"))
		.put("iron", of(Items.IRON_INGOT, "c:iron_ingots"))
		.put("gold", of(Items.GOLD_INGOT, "c:gold_ingots"))
		.put("redstone", of(Items.REDSTONE, "c:redstone"))
		.put("netherite", of(Items.NETHERITE_INGOT, "c:netherite_ingots"))
		.build();

	private static final Multimap<Class<?>, DataGenConsumer<?>> CONSUMERS = Multimaps.newListMultimap(Maps.newHashMap(), Lists::newArrayList);
	private static final Map<BiPredicate<Block, Identifier>, TriConsumer<LootTableData, Block, Identifier>> BLOCK_LOOT_TABLES = Maps.newLinkedHashMap();

	@Override
	public void onPreLaunch() {
		try {
			DataGeneratorHandler handler = DataGeneratorHandler.create(Paths.get("../astromine-foundations/src/generated/resources"));
			FabricLoader.getInstance().getEntrypoints("main", ModInitializer.class).forEach(ModInitializer::onInitialize);

			register();
			registerConsumers();

			populateTags(handler.getTags());
			populateRecipes(handler.getRecipes());

			forEach(AstromineFoundationsBlocks.class, Block.class, value -> callConsumers(handler, Block.class, value));
			forEach(AstromineFoundationsItems.class, Item.class, value -> callConsumers(handler, Item.class, value));

			handler.run();
		} catch (Throwable throwable) {
			AstromineCommon.LOGGER.error("Data generation failed with trace:");
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

	private static void populateTags(TagData tags) {
		// items
		forEach(AstromineFoundationsItems.class, Item.class, item -> {
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
		tags.item(new Identifier("c:redstone")).append(Items.REDSTONE);

		tags.item(new Identifier("c:wood_sticks")).append(Items.STICK);

		tags.item(AstromineCommon.identifier("carbon_dusts")).appendTag(new Identifier("c:coal_dusts")).appendTag(new Identifier("c:charcoal_dusts"));

		// blocks
		tags.block(AstromineCommon.identifier("metite_ores")).append(AstromineFoundationsBlocks.METEOR_METITE_ORE, AstromineFoundationsBlocks.ASTEROID_METITE_ORE);

		forEach(AstromineFoundationsBlocks.class, Block.class, block -> {
			Identifier key = Registry.BLOCK.getId(block);
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

	private static void populateRecipes(RecipeData recipes) {
		Set<String> addedCluster = Sets.newHashSet();

		forEach(AstromineFoundationsItems.class, Item.class, item -> {
			Identifier itemId = Registry.ITEM.getId(item);
			String itemIdPath = itemId.getPath();

			MaterialEntry material = getMaterial(itemId);

			if (material != null) {
				if (!material.getMaterialId().equals("univite")) {
					if (itemIdPath.endsWith("_axe")) {
						Generators.ofAxe(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_pickaxe")) {
						Generators.ofPickaxe(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_hoe")) {
						Generators.ofHoe(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_sword")) {
						Generators.ofSword(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_shovel")) {
						Generators.ofShovel(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_mining_tool")) {
						Generators.ofMiningTool(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_mattock")) {
						Generators.ofMattock(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_hammer")) {
						Generators.ofHammer(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_excavator")) {
						Generators.ofExcavator(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_helmet")) {
						Generators.ofHelmet(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_chestplate")) {
						Generators.ofChestplate(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_leggings")) {
						Generators.ofLeggings(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_boots")) {
						Generators.ofBoots(recipes, item, material.asIngredient());
					}
				} else if (item instanceof ToolItem || item instanceof ArmorItem || item instanceof DynamicAttributeTool) {
					Generators.ofUniviteSmithing(recipes, item, itemId);
				}

				if (itemIdPath.endsWith("_nugget") || itemIdPath.endsWith("_fragment")) {
					// 1 ingot to 9 nuggets (/ fragments)
					Generators.ofIngotToNugget(recipes, item, material.asIngredient());

					// 9 nuggets (/fragments) to 1 ingot
					Generators.ofNuggetToIngot(recipes, material.getIngotItemId(), itemId);
				} else if (itemIdPath.endsWith("_plates")) {
					// 2 ingots to 1 plate
					Generators.ofIngotToPlate(recipes, item, material.asIngredient());

					// 1 ingot to 1 plate
					Generators.ofPresserIngotToPlate(recipes, item, material.asIngredient());
				} else if (itemIdPath.endsWith("_gear")) {
					// 4 ingots to 1 gear
					Generators.ofIngotToGear(recipes, item, material.asIngredient());
				}
				if (itemIdPath.endsWith("_cluster")) {
					if (addedCluster.add(material.getMaterialId())) {
						// 1 cluster to 1 ingot
						Generators.ofSmeltingCluster(recipes, item, material.getIngotItemId());

						// 1 cluster to 1 ingot
						Generators.ofBlastingCluster(recipes, item, material.getIngotItemId());
					}
				}
			}

			if (itemIdPath.endsWith("_tiny_dust")) {
				// 9 tiny dusts to 1 dust
				Identifier dustItem = new Identifier(itemId.getNamespace(), itemIdPath.replace("_tiny_dust", "_dust"));
				if (dustItem.toString().equals("astromine:redstone_dust")) {
					dustItem = new Identifier("redstone");
				} else if (!Registry.ITEM.getOrEmpty(dustItem).isPresent()) {
					dustItem = new Identifier(dustItem.getPath());
				}

				// 9 tiny dust to 1 dust
				Generators.ofTinyDustToDust(recipes, dustItem, itemIdPath);

				// 1 dust to 9 tiny dust
				Generators.ofDustToTinyDust(recipes, item, dustItem, itemId);
			}
		});

		Set<String> addedOre = Sets.newHashSet();

		forEach(AstromineFoundationsBlocks.class, Block.class, block -> {
			Identifier blockId = Registry.BLOCK.getId(block);
			String blockIdPath = blockId.getPath();

			MaterialEntry material = getMaterial(blockId);

			if (material != null) {
				if (blockIdPath.endsWith("_block")) {
					// 9 ingot to 1 block
					Generators.ofIngotToBlock(recipes, block, material.asIngredient());

					// 1 block to 9 ingot
					Generators.ofBlockToIngot(recipes, material.getIngotItemId(), block);
				} else if (blockIdPath.endsWith("_ore")) {
					if (addedOre.add(material.getMaterialId())) {
						// 1 ore to 1 ingot
						Generators.ofSmeltingOre(recipes, block, material.getIngotItemId());

						// 1 ore to 1 ingot
						Generators.ofBlastingOre(recipes, block, material.getIngotItemId());
					}
				}
			}
		});
	}

	@Nullable
	private static MaterialEntry getMaterial(Identifier identifier) {
		String path = identifier.getPath().replaceFirst("asteroid_", "").replaceFirst("meteor_", "");
		if (path.contains("_")) {
			return MATERIALS.keySet().stream()
				.filter(path::startsWith)
				.findFirst()
				.map(MATERIALS::get)
				.orElse(null);
		}

		return null;
	}

	private static Identifier guessMaterialFromTag(String tag) {
		Identifier ingotItem = AstromineCommon.identifier((new Identifier(tag).getNamespace().equals("c") ? "" : new Identifier(tag).getNamespace() + ":") +
		                                                  (tag.endsWith("s") ? new Identifier(tag).getPath().substring(0, new Identifier(tag).getPath().length() - 1) : new Identifier(tag).getPath()));
		if (!Registry.ITEM.getOrEmpty(ingotItem).isPresent()) {
			return new Identifier(ingotItem.getPath());
		}
		return ingotItem;
	}


	private static <T> void forEach(Class<?> scanning, Class<T> clazz, Consumer<T> consumer) {
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

	private static final class Providers {
		private static DefaultedRecipeJsonProvider createProvider(RecipeSerializer<?> type, Identifier id, Consumer<JsonObject> serializer) {
			return new DefaultedRecipeJsonProvider(type, id) {
				@Override
				public void serialize(JsonObject json) {
					serializer.accept(json);
				}
			};
		}

		private static abstract class DefaultedRecipeJsonProvider implements RecipeJsonProvider {
			private final RecipeSerializer<?> type;
			private final Identifier id;

			public DefaultedRecipeJsonProvider(RecipeSerializer<?> type, Identifier id) {
				this.type = type;
				this.id = id;
			}

			@Override
			public Identifier getRecipeId() {
				return id;
			}

			@Override
			public RecipeSerializer<?> getSerializer() {
				return type;
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
	}

	private static final class Generators {
		/*
		 * Crafting recipe generators.
		 */
		private static void ofAxe(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("##")
				.pattern("#$")
				.pattern(" $")
				.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofPickaxe(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("###")
				.pattern(" $ ")
				.pattern(" $ ")
				.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofHoe(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("##")
				.pattern(" $")
				.pattern(" $")
				.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofSword(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("#")
				.pattern("#")
				.pattern("$")
				.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofShovel(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("#")
				.pattern("$")
				.pattern("$")
				.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofMiningTool(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("###")
				.pattern(" # ")
				.pattern(" $ ")
				.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofMattock(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("###")
				.pattern("#$ ")
				.pattern(" $ ")
				.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofHammer(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("###")
				.pattern("#$#")
				.pattern(" $ ")
				.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofExcavator(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern(" # ")
				.pattern("#$#")
				.pattern(" $ ")
				.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofHelmet(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("###")
				.pattern("# #")
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofChestplate(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("# #")
				.pattern("###")
				.pattern("###")
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofLeggings(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("###")
				.pattern("# #")
				.pattern("# #")
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofBoots(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("# #")
				.pattern("# #")
				.input('#', material)
				.offerTo(recipes);
		}

		private static void ofSmithing(RecipeData recipes, Ingredient input, Ingredient addition, Item output) {
			SmithingRecipeJsonFactory
				.create(input, addition, output)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, Registry.ITEM.getId(output) + "_smithing");
		}

		private static void ofUniviteSmithing(RecipeData recipes, Item output, Identifier keyPath) {
			ofSmithing(recipes,
				Ingredient.ofItems(Registry.ITEM.get(new Identifier(keyPath.toString().replace("univite_", "galaxium_")))),
				Ingredient.fromTag(TagRegistry.item(new Identifier("c:univite_ingots"))),
				output);
		}

		private static void ofIngotToNugget(RecipeData recipes, Item nugget, Ingredient ingot) {
			ShapelessRecipeJsonFactory.create(nugget, 9)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.input(ingot)
				.offerTo(recipes, Registry.ITEM.getId(nugget) + "_from_ingot");
		}

		private static void ofNuggetToIngot(RecipeData recipes, Identifier ingotItem, Identifier nuggetId) {
			String nuggetIdPath = nuggetId.getPath();
			ShapedRecipeJsonFactory.create(Registry.ITEM.get(ingotItem))
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.input('#', TagRegistry.item(new Identifier("c", nuggetIdPath + "s")))
				.offerTo(recipes, ingotItem.toString() + "_from_" + nuggetIdPath.substring(nuggetIdPath.lastIndexOf('_') + 1));
		}

		private static void ofIngotToPlate(RecipeData recipes, Item plate, Ingredient ingot) {
			ShapedRecipeJsonFactory.create(plate)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("#")
				.pattern("#")
				.input('#', ingot)
				.offerTo(recipes, Registry.ITEM.getId(plate) + "_from_ingot");
		}

		private static void ofPresserIngotToPlate(RecipeData recipes, Item plate, Ingredient ingot) {
			Generators.ofPresser(recipes, new Identifier(Registry.ITEM.getId(plate) + "_from_pressing_ingot"),
				ingot, 1, new ItemStack(plate), 60, 384);
		}

		private static void ofIngotToGear(RecipeData recipes, Item gear, Ingredient input) {
			ShapedRecipeJsonFactory.create(gear)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern(" # ")
				.pattern("# #")
				.pattern(" # ")
				.input('#', input)
				.offerTo(recipes, Registry.ITEM.getId(gear) + "_from_ingot");
		}

		private static void ofSmeltingCluster(RecipeData recipes, Item cluster, Identifier result) {
			CookingRecipeJsonFactory
				.createSmelting(
					Ingredient.fromTag(TagRegistry.item(new Identifier("c", Registry.ITEM.getId(cluster).getPath() + "s"))),
					Registry.ITEM.get(result),
					0.1F,
					200)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, result + "_from_smelting_cluster");
		}

		private static void ofBlastingCluster(RecipeData recipes, Item cluster, Identifier result) {
			CookingRecipeJsonFactory
				.createBlasting(
					Ingredient.fromTag(TagRegistry.item(new Identifier("c", Registry.ITEM.getId(cluster).getPath() + "s"))),
					Registry.ITEM.get(result),
					0.1F,
					100)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, result + "_from_blasting_cluster");
		}

		private static void ofTinyDustToDust(RecipeData recipes, Identifier dustItem, String keyPath) {
			ShapedRecipeJsonFactory.create(Registry.ITEM.get(dustItem))
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.input('#', TagRegistry.item(new Identifier("c", keyPath + "s")))
				.offerTo(recipes, dustItem + "_from_tiny_dust");
		}

		private static void ofDustToTinyDust(RecipeData recipes, Item item, Identifier dustItem, Identifier keyId) {
			ShapelessRecipeJsonFactory.create(item, 9)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.input(TagRegistry.item(new Identifier("c", dustItem.getPath() + "s")))
				.offerTo(recipes, keyId + "_from_dust");
		}

		private static void ofIngotToBlock(RecipeData recipes, Block block, Ingredient ingot) {
			ShapedRecipeJsonFactory.create(block)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.input('#', ingot)
				.offerTo(recipes, Registry.BLOCK.getId(block) + "_from_ingot");
		}

		private static void ofBlockToIngot(RecipeData recipes, Identifier ingot, Block block) {
			ShapelessRecipeJsonFactory.create(Registry.ITEM.get(ingot), 9)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.input(TagRegistry.item(new Identifier("c", Registry.BLOCK.getId(block).getPath() + "s")))
				.offerTo(recipes, ingot + "_from_block");
		}

		private static void ofSmeltingOre(RecipeData recipes, Block ore, Identifier ingot) {
			CookingRecipeJsonFactory
				.createSmelting(
					Ingredient.fromTag(TagRegistry.item(new Identifier("c", Registry.BLOCK.getId(ore).getPath() + "s"))),
					Registry.ITEM.get(ingot),
					0.1F,
					200)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, ingot + "_from_smelting_ore");
		}

		private static void ofBlastingOre(RecipeData recipes, Block ore, Identifier ingot) {
			CookingRecipeJsonFactory
				.createBlasting(
					Ingredient.fromTag(TagRegistry.item(new Identifier("c", Registry.BLOCK.getId(ore).getPath()  + "s"))),
					Registry.ITEM.get(ingot),
					0.1F,
					100)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, ingot + "_from_blasting_ore");
		}

		/*
		 * Machine recipe generators.
		 */
		private static void ofPresser(RecipeData recipes, Ingredient input, int inputCount, ItemStack output, int time, int energyConsumed) {
			ofPresser(recipes, Registry.ITEM.getId(output.getItem()), input, inputCount, output, time, energyConsumed);
		}

		private static void ofPresser(RecipeData recipes, Identifier id, Ingredient input, int inputCount, ItemStack output, int time, int energyConsumed) {
			recipes.accept(Providers.createProvider(PressingRecipe.Serializer.INSTANCE, id, json -> {
				JsonObject inputJson = input.toJson().getAsJsonObject();
				inputJson.addProperty("count", inputCount);

				json.add("input", inputJson);

				JsonObject outputJson = new JsonObject();
				outputJson.addProperty("item", Registry.ITEM.getId(output.getItem()).toString());
				outputJson.addProperty("count", output.getCount());

				json.add("output", outputJson);
				json.addProperty("time", time);
				json.addProperty("energy_consumed", energyConsumed);
			}));
		}
	}
}
