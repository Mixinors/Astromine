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
import net.minecraft.util.registry.RegistryKey;
import org.apache.logging.log4j.util.TriConsumer;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
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

		tags.item(new Identifier("c:wood_sticks")).append(Items.STICK);

		tags.item(AstromineCommon.identifier("carbon_dusts")).appendTag(new Identifier("c:coal_dusts")).appendTag(new Identifier("c:charcoal_dusts"));

		// blocks
		tags.block(AstromineCommon.identifier("metite_ores")).append(AstromineFoundationsBlocks.METEOR_METITE_ORE, AstromineFoundationsBlocks.ASTEROID_METITE_ORE);

		forEach(AstromineFoundationsBlocks.class, Block.class, block -> {
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

	private static void populateRecipes(RecipeData recipes) {
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

		forEach(AstromineFoundationsItems.class, Item.class, item -> {
			Optional<RegistryKey<Item>> optionalKey = Registry.ITEM.getKey(item);

			optionalKey.ifPresent((registryKey) -> {
				Identifier keyId = registryKey.getValue();

				String keyPath = keyId.getPath();
				
				String keyString = keyId.toString();

				if (keyPath.contains("_")) {
					materialMap.keySet().forEach(materialKey -> {
						if (keyPath.contains(materialKey)) {
							String materialValue = materialMap.get(materialKey);
							Identifier materialIdentifier = guessMaterialFromTag(materialValue);

							if (!keyPath.startsWith("univite_")) {
								if (keyString.endsWith("_axe")) {
									Generators.ofAxe(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_pickaxe")) {
									Generators.ofPickaxe(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_hoe")) {
									Generators.ofHoe(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_sword")) {
									Generators.ofSword(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_shovel")) {
									Generators.ofShovel(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_mining_tool")) {
									Generators.ofMiningTool(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_mattock")) {
									Generators.ofMattock(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_hammer")) {
									Generators.ofHammer(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_excavator")) {
									Generators.ofExcavator(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_helmet")) {
									Generators.ofHelmet(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_chestplate")) {
									Generators.ofChestplate(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_leggings")) {
									Generators.ofLeggings(recipes, item, new Identifier(materialValue));
								} else if (keyString.endsWith("_boots")) {
									Generators.ofBoots(recipes, item, new Identifier(materialValue));
								}
							} else if (item instanceof ToolItem || item instanceof ArmorItem) {
								Generators.ofUniviteSmithing(recipes, item, keyPath);
							}

							if (keyString.endsWith("_nugget") || keyString.endsWith("_fragment")) {
								// 1 ingot to 9 nuggets (/ fragments)
								Generators.ofIngotToNugget(recipes, item, keyString, materialValue);

								// 9 nuggets (/fragments) to 1 ingot
								Generators.ofNuggetToIngot(recipes, materialIdentifier, keyPath);
							} else if (keyString.endsWith("_plates")) {
								// 2 ingots to 1 plate
								Generators.ofIngotToPlate(recipes, item, keyString, materialValue);

								// 1 ingot to 1 plate
								Generators.ofPresserIngotToPlate(recipes, item, keyString, materialValue);
							} else if (keyString.endsWith("_gear")) {
								// 4 ingots to 1 gear
								Generators.ofIngotToGear(recipes, item, keyString, materialValue);
							}
							if (keyPath.endsWith("_cluster")) {
								if (addedCluster.add(materialValue)) {
									// 1 cluster to 1 ingot
									Generators.ofSmeltingCluster(recipes, keyPath, materialIdentifier);

									// 1 cluster to 1 ingot
									Generators.ofBlastingCluster(recipes, keyPath, materialIdentifier);
								}
							}
						}

						if (keyString.endsWith("_tiny_dust")) {
							// 9 tiny dusts to 1 dust
							Identifier dustItem = new Identifier(keyId.getNamespace(), keyPath.replace("_tiny_dust", "_dust"));
							if (dustItem.toString().equals("astromine:redstone_dust")) {
								dustItem = new Identifier("redstone");
							} else if (!Registry.ITEM.getOrEmpty(dustItem).isPresent()) {
								dustItem = new Identifier(dustItem.getPath());
							}

							// 9 tiny dust to 1 dust
							Generators.ofTinyDustToDust(recipes, dustItem, keyPath);

							// 1 dust to 9 tiny dust
							Generators.ofDustToTinyDust(recipes, item, dustItem, keyId);
						}
					});
				}
			});
		});

		Set<String> addedOre = Sets.newHashSet();

		forEach(AstromineFoundationsBlocks.class, Block.class, block -> {
			Optional<RegistryKey<Block>> optionalKey = Registry.BLOCK.getKey(block);

			optionalKey.ifPresent((registryKey) -> {
				Identifier keyId = registryKey.getValue();

				String keyPath = keyId.getPath();

				if (keyPath.contains("_")) {
					materialMap.keySet().forEach(materialKey -> {
						if (keyPath.contains(materialKey)) {
							String materialValue = materialMap.get(materialKey);
							Identifier materialIdentifier = guessMaterialFromTag(materialValue);

							if (keyPath.endsWith("_block")) {
								// 9 ingot to 1 block
								Generators.ofIngotToBlock(recipes, block, materialValue, keyId);

								// 1 block to 9 ingot
								Generators.ofBlockToIngot(recipes, materialIdentifier, keyPath);
							} else if (keyPath.endsWith("_ore")) {
								if (addedOre.add(materialValue)) {
									// 1 ore to 1 ingot
									Generators.ofSmeltingOre(recipes, keyPath, materialIdentifier);

									// 1 ore to 1 ingot
									Generators.ofBlastingOre(recipes, keyPath, materialIdentifier);
								}
							}
						}
					});
				}
			});
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
		private static void ofAxe(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("##")
					.pattern("#$")
					.pattern(" $")
					.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofPickaxe(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern(" $ ")
					.pattern(" $ ")
					.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofHoe(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("##")
					.pattern(" $")
					.pattern(" $")
					.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofSword(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("#")
					.pattern("#")
					.pattern("$")
					.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofShovel(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("#")
					.pattern("$")
					.pattern("$")
					.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofMiningTool(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern(" # ")
					.pattern(" $ ")
					.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofMattock(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("#$ ")
					.pattern(" $ ")
					.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofHammer(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("#$#")
					.pattern(" $ ")
					.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofExcavator(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern(" # ")
					.pattern("#$#")
					.pattern(" $ ")
					.input('$', TagRegistry.item(new Identifier("c:wood_sticks")))
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofHelmet(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("# #")
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofChestplate(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("# #")
					.pattern("###")
					.pattern("###")
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofLeggings(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("# #")
					.pattern("# #")
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofBoots(RecipeData recipes, Item output, Identifier material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("# #")
					.pattern("# #")
					.input('#', TagRegistry.item(material))
					.offerTo(recipes);
		}

		private static void ofSmithing(RecipeData recipes, Ingredient input, Ingredient addition, Item output) {
			SmithingRecipeJsonFactory
					.create(input, addition, output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.offerTo(recipes, Registry.ITEM.getId(output).getNamespace() + "_smithing");
		}

		private static void ofUniviteSmithing(RecipeData recipes, Item output, String keyPath) {
			ofSmithing(recipes,
					Ingredient.ofItems(Registry.ITEM.get(AstromineCommon.identifier(keyPath.replace("univite_", "galaxium_")))),
					Ingredient.fromTag(TagRegistry.item(new Identifier("c:univite_ingots"))),
					output);
		}

		private static void ofIngotToNugget(RecipeData recipes, Item item, String keyString, String materialKey) {
			ShapelessRecipeJsonFactory.create(item, 9)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.input(TagRegistry.item(new Identifier(materialKey)))
					.offerTo(recipes, keyString + "_from_ingot");
		}

		private static void ofNuggetToIngot(RecipeData recipes, Identifier ingotItem, String keyPath) {
			ShapedRecipeJsonFactory.create(Registry.ITEM.get(ingotItem))
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("###")
					.pattern("###")
					.input('#', TagRegistry.item(new Identifier("c", keyPath + "s")))
					.offerTo(recipes, ingotItem.toString() + "_from_" + keyPath.substring(keyPath.lastIndexOf('_') + 1));
		}

		private static void ofIngotToPlate(RecipeData recipes, Item item, String keyString, String materialKey) {
			ShapedRecipeJsonFactory.create(item)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("#")
					.pattern("#")
					.input('#', TagRegistry.item(new Identifier(materialKey)))
					.offerTo(recipes, keyString + "_from_ingot");
		}

		private static void ofIngotToGear(RecipeData recipes, Item item, String keyString, String materialKey) {
			ShapedRecipeJsonFactory.create(item)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern(" # ")
					.pattern("# #")
					.pattern(" # ")
					.input('#', TagRegistry.item(new Identifier(materialKey)))
					.offerTo(recipes, keyString + "_from_ingot");
		}

		private static void ofSmeltingCluster(RecipeData recipes, String keyPath, Identifier materialIdentifier) {
			CookingRecipeJsonFactory
				.createSmelting(
					Ingredient.fromTag(TagRegistry.item(new Identifier("c", keyPath + "s"))),
					Registry.ITEM.get(materialIdentifier),
					0.1F,
					200)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, materialIdentifier.getPath() + "_from_smelting_cluster");
		}

		private static void ofBlastingCluster(RecipeData recipes, String keyPath, Identifier materialIdentifier) {
			CookingRecipeJsonFactory
				.createBlasting(
					Ingredient.fromTag(TagRegistry.item(new Identifier("c", keyPath + "s"))),
					Registry.ITEM.get(materialIdentifier),
					0.1F,
					100)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, materialIdentifier.getPath() + "_from_blasting_cluster");
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

		private static void ofIngotToBlock(RecipeData recipes, Block block, String materialKey, Identifier keyId) {
			ShapedRecipeJsonFactory.create(block)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("###")
					.pattern("###")
					.input('#', TagRegistry.item(new Identifier(materialKey)))
					.offerTo(recipes, keyId + "_from_ingot");
		}

		private static void ofBlockToIngot(RecipeData recipes, Identifier materialIdentifier, String keyPath) {
			ShapelessRecipeJsonFactory.create(Registry.ITEM.get(materialIdentifier), 9)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.input(TagRegistry.item(new Identifier("c", keyPath + "s")))
					.offerTo(recipes, materialIdentifier + "_from_block");
		}

		private static void ofSmeltingOre(RecipeData recipes, String keyPath, Identifier materialIdentifier) {
			CookingRecipeJsonFactory
					.createSmelting(
							Ingredient.fromTag(TagRegistry.item(new Identifier("c", keyPath + "s"))),
							Registry.ITEM.get(materialIdentifier),
							0.1F,
							200)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.offerTo(recipes, materialIdentifier.getPath() + "_from_smelting_ore");
		}

		private static void ofBlastingOre(RecipeData recipes, String keyPath, Identifier materialIdentifier) {
			CookingRecipeJsonFactory
					.createBlasting(
							Ingredient.fromTag(TagRegistry.item(new Identifier("c", keyPath + "s"))),
							Registry.ITEM.get(materialIdentifier),
							0.1F,
							100)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.offerTo(recipes, materialIdentifier.getPath() + "_from_blasting_ore");
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

		private static void ofPresserIngotToPlate(RecipeData recipes, Item item, String keyString, String materialKey) {
			Generators.ofPresser(recipes, new Identifier(keyString + "_from_pressing_ingot"), Ingredient.fromTag(TagRegistry.item(new Identifier(materialKey))), 1, new ItemStack(item), 60, 384);
		}
	}
}
