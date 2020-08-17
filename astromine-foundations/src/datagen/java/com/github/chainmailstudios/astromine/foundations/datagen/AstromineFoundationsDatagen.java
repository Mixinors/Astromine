package com.github.chainmailstudios.astromine.foundations.datagen;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.item.MultitoolItem;
import com.github.chainmailstudios.astromine.common.recipe.PressingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.TrituratingRecipe;
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
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import static com.github.chainmailstudios.astromine.foundations.datagen.MaterialEntry.of;

public class AstromineFoundationsDatagen implements PreLaunchEntrypoint {
	public static final BiMap<String, MaterialEntry> MATERIALS = ImmutableBiMap.<String, MaterialEntry>builder()
			.put("copper", of(AstromineFoundationsItems.COPPER_INGOT, "c:copper_ingots").dust(AstromineFoundationsItems.COPPER_DUST))
			.put("tin", of(AstromineFoundationsItems.TIN_INGOT, "c:tin_ingots").dust(AstromineFoundationsItems.TIN_DUST))
			.put("silver", of(AstromineFoundationsItems.SILVER_INGOT, "c:silver_ingots").dust(AstromineFoundationsItems.SILVER_DUST))
			.put("lead", of(AstromineFoundationsItems.LEAD_INGOT, "c:lead_ingots").dust(AstromineFoundationsItems.LEAD_DUST))
			.put("bronze", of(AstromineFoundationsItems.BRONZE_INGOT, "c:bronze_ingots").dust(AstromineFoundationsItems.BRONZE_DUST))
			.put("steel", of(AstromineFoundationsItems.STEEL_INGOT, "c:steel_ingots").dust(AstromineFoundationsItems.STEEL_DUST))
			.put("electrum", of(AstromineFoundationsItems.ELECTRUM_INGOT, "c:electrum_ingots").dust(AstromineFoundationsItems.ELECTRUM_DUST))
			.put("rose_gold", of(AstromineFoundationsItems.ROSE_GOLD_INGOT, "c:rose_gold_ingots").dust(AstromineFoundationsItems.ROSE_GOLD_DUST))
			.put("sterling_silver", of(AstromineFoundationsItems.STERLING_SILVER_INGOT, "c:sterling_silver_ingots").dust(AstromineFoundationsItems.STERLING_SILVER_DUST))
			.put("fools_gold", of(AstromineFoundationsItems.FOOLS_GOLD_INGOT, "c:fools_gold_ingots").dust(AstromineFoundationsItems.FOOLS_GOLD_DUST))
			.put("metite", of(AstromineFoundationsItems.METITE_INGOT, "c:metite_ingots").dust(AstromineFoundationsItems.METITE_DUST))
			.put("asterite", of(AstromineFoundationsItems.ASTERITE, "c:asterites").dust(AstromineFoundationsItems.ASTERITE_DUST))
			.put("stellum", of(AstromineFoundationsItems.STELLUM_INGOT, "c:stellum_ingots").dust(AstromineFoundationsItems.STELLUM_DUST))
			.put("galaxium", of(AstromineFoundationsItems.GALAXIUM, "c:galaxiums").dust(AstromineFoundationsItems.GALAXIUM_DUST))
			.put("diamond", of(Items.DIAMOND, "c:diamonds").dust(AstromineFoundationsItems.DIAMOND_DUST))
			.put("emerald", of(Items.EMERALD, "c:emeralds").dust(AstromineFoundationsItems.EMERALD_DUST))
			.put("quartz", of(Items.QUARTZ, "c:quartz").dust(AstromineFoundationsItems.QUARTZ_DUST))
			.put("univite", of(AstromineFoundationsItems.UNIVITE_INGOT, "c:univite_ingots").dust(AstromineFoundationsItems.UNIVITE_DUST))
			.put("wooden", of(Items.OAK_PLANKS, "minecraft:planks"))
			.put("stone", of(Items.COBBLESTONE, "c:cobblestones"))
			.put("iron", of(Items.IRON_INGOT, "c:iron_ingots").dust(AstromineFoundationsItems.IRON_DUST))
			.put("gold", of(Items.GOLD_INGOT, "c:gold_ingots").dust(AstromineFoundationsItems.GOLD_DUST))
			.put("lapis", of(Items.LAPIS_LAZULI, "c:lapis_lazulis").dust(AstromineFoundationsItems.LAPIS_DUST))
			.put("redstone", of(Items.REDSTONE, "c:redstone").dust(Items.REDSTONE).dustTag(asId("c:redstone_dusts")).tinyDust(AstromineFoundationsItems.REDSTONE_TINY_DUST))
			.put("netherite", of(Items.NETHERITE_INGOT, "c:netherite_ingots").dust(AstromineFoundationsItems.NETHERITE_DUST))
			.put("coal", of(Items.COAL, "c:coal").dust(AstromineFoundationsItems.COAL_DUST))
			.put("charcoal", of(Items.CHARCOAL, "c:charcoal").dust(AstromineFoundationsItems.CHARCOAL_DUST))
			.put("raw_netherite", of(Items.ANCIENT_DEBRIS, "c:netherite_ingots").dust(AstromineFoundationsItems.RAW_NETHERITE_DUST))
			.build();

	private static final Multimap<Class<?>, DataGenConsumer<?>> CONSUMERS = Multimaps.newListMultimap(Maps.newHashMap(), Lists::newArrayList);
	private static final Map<BiPredicate<Block, Identifier>, TriConsumer<LootTableData, Block, Identifier>> BLOCK_LOOT_TABLES = Maps.newLinkedHashMap();

	@Override
	public void onPreLaunch() {
		System.out.println(MATERIALS.size() + " Materials: ");
		for (MaterialEntry value : MATERIALS.values()) {
			System.out.println("- " + value);
		}
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
			AstromineCommon.LOGGER.error("Data generation failed with stack trace:");
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
		// Items
		forEach(AstromineFoundationsItems.class, Item.class, item -> {
			Registry.ITEM.getKey(item).ifPresent(registryKey -> {
				Identifier key = registryKey.getValue();

				if (key.toString().endsWith("_cluster") || key.toString().endsWith("_gear") || key.toString().endsWith("_plates") ||
						key.toString().endsWith("_dust") || key.toString().endsWith("_ingot") || key.toString().endsWith("_wire") ||
						key.toString().endsWith("_nugget") || key.toString().endsWith("_fragment") ||
						item == AstromineFoundationsItems.ASTERITE || item == AstromineFoundationsItems.GALAXIUM) {
					Identifier tagId = convertIdToCommonTag(key);

					TagData.TagBuilder<ItemConvertible> builder = tags.item(tagId).append(item);

					if (tagId.getPath().startsWith("fools_gold")) {
						tagId = asId("c", tagId.getPath().replaceFirst("fools_gold", "pyrite"));
						tags.item(tagId).appendTag(builder);
					}
					if (tagId.getPath().endsWith("_fragments")) {
						tagId = asId("c", tagId.getPath().replaceFirst("_fragments", "_nuggets"));
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
					tags.item(asId("piglin_loved")).append(item);

					if (item instanceof ArmorItem) {
						tags.item(AstromineCommon.identifier("piglin_safe_armor")).append(item);
					}
				}
				if (item instanceof PickaxeItem || item instanceof HammerItem || (item instanceof MultitoolItem && (((MultitoolItem) item).first instanceof PickaxeItem || ((MultitoolItem) item).second instanceof PickaxeItem))) {
					tags.item(asId("fabric:pickaxes")).append(item);
				}
				if (item instanceof AxeItem || (item instanceof MultitoolItem && (((MultitoolItem) item).first instanceof AxeItem || ((MultitoolItem) item).second instanceof AxeItem))) {
					tags.item(asId("fabric:axes")).append(item);
				}
				if (item instanceof HoeItem || (item instanceof MultitoolItem && (((MultitoolItem) item).first instanceof HoeItem || ((MultitoolItem) item).second instanceof HoeItem))) {
					tags.item(asId("fabric:hoes")).append(item);
				}
				if (item instanceof ShovelItem || item instanceof ExcavatorItem || (item instanceof MultitoolItem && (((MultitoolItem) item).first instanceof ShovelItem || ((MultitoolItem) item).second instanceof ShovelItem))) {
					tags.item(asId("fabric:shovels")).append(item);
				}
				if (item instanceof SwordItem) {
					tags.item(asId("fabric:shovels")).append(item);
				}
			});
		});

		tags.item(asId("c:diamonds")).append(Items.DIAMOND);
		tags.item(asId("c:emeralds")).append(Items.EMERALD);
		tags.item(asId("c:iron_ingots")).append(Items.IRON_INGOT);
		tags.item(asId("c:gold_ingots")).append(Items.GOLD_INGOT);
		tags.item(asId("c:redstone_dusts")).append(Items.REDSTONE);
		tags.item(asId("c:lapis_lazulis")).append(Items.LAPIS_LAZULI);
		tags.item(asId("c:netherite_ingots")).append(Items.NETHERITE_INGOT);
		tags.item(asId("c:quartz")).append(Items.QUARTZ);
		tags.item(asId("c:redstone")).append(Items.REDSTONE);
		tags.item(asId("c:coal")).append(Items.COAL);
		tags.item(asId("c:charcoal")).append(Items.CHARCOAL);

		tags.item(asId("c:wood_sticks")).append(Items.STICK);

		tags.item(AstromineCommon.identifier("carbon_dusts")).appendTag(asId("c:coal_dusts")).appendTag(asId("c:charcoal_dusts"));

		tags.block(AstromineCommon.identifier("metite_ores")).append(AstromineFoundationsBlocks.METEOR_METITE_ORE, AstromineFoundationsBlocks.ASTEROID_METITE_ORE);

		// Blocks
		forEach(AstromineFoundationsBlocks.class, Block.class, block -> {
			Registry.BLOCK.getKey(block).ifPresent(registryKey -> {
				Identifier key = registryKey.getValue();

				if (key.toString().endsWith("_block") || key.toString().endsWith("_ore")) {
					Identifier tagId = convertIdToCommonTag(key);

					if (key.toString().endsWith("_block")) {
						tags.block(asId("beacon_base_blocks")).append(block);
					}

					TagData.TagBuilder<ItemConvertible> itemBuilder = tags.item(tagId).append(block);
					TagData.TagBuilder<Block> blockBuilder = tags.block(tagId).append(block);

					if (tagId.getPath().startsWith("fools_gold")) {
						tagId = asId("c", tagId.getPath().replaceFirst("fools_gold", "pyrite"));
						tags.item(tagId).appendTag(itemBuilder);
						tags.block(tagId).appendTag(blockBuilder);
					}
				}

				if (block instanceof AstromineOreBlock) {
					tags.block(AstromineCommon.identifier("rocket_explode")).append(block);
				}
			});
		});

		tags.block(asId("c:diamond_ores")).append(Blocks.DIAMOND_ORE);
		tags.block(asId("c:emerald_ores")).append(Blocks.EMERALD_ORE);
		tags.block(asId("c:iron_ores")).append(Blocks.IRON_ORE);
		tags.block(asId("c:gold_ores")).appendTag(asId("gold_ores"));
		tags.block(asId("c:redstone_ores")).append(Blocks.REDSTONE_ORE);
		tags.block(asId("c:lapis_ores")).append(Blocks.LAPIS_ORE);
		tags.block(asId("c:quartz_ores")).append(Blocks.NETHER_QUARTZ_ORE);
		tags.block(asId("gold_ores")).append(Blocks.NETHER_GOLD_ORE);
		tags.block(asId("c:cobblestones")).append(Blocks.COBBLESTONE);

		tags.item(asId("c:diamond_ores")).append(Blocks.DIAMOND_ORE);
		tags.item(asId("c:emerald_ores")).append(Blocks.EMERALD_ORE);
		tags.item(asId("c:iron_ores")).append(Blocks.IRON_ORE);
		tags.item(asId("c:gold_ores")).appendTag(asId("gold_ores"));
		tags.item(asId("c:redstone_ores")).append(Blocks.REDSTONE_ORE);
		tags.item(asId("c:lapis_ores")).append(Blocks.LAPIS_ORE);
		tags.item(asId("c:quartz_ores")).append(Blocks.NETHER_QUARTZ_ORE);
		tags.item(asId("gold_ores")).append(Blocks.NETHER_GOLD_ORE);
		tags.item(asId("c:cobblestones")).append(Blocks.COBBLESTONE);
	}

	private static void populateRecipes(RecipeData recipes) {
		forEach(AstromineFoundationsItems.class, Item.class, item -> {
			Identifier itemId = Registry.ITEM.getId(item);
			String itemIdPath = itemId.getPath();

			MaterialEntry material = getMaterialFromId(itemId);

			if (material != null) {
				if (!material.getMaterialId().equals("univite")) {
					if (itemIdPath.endsWith("_axe")) {
						// Axe
						Generators.ofAxe(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_pickaxe")) {
						// Pickaxe
						Generators.ofPickaxe(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_hoe")) {
						// Hoe
						Generators.ofHoe(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_sword")) {
						// Sword
						Generators.ofSword(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_shovel")) {
						// Shovel
						Generators.ofShovel(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_mining_tool")) {
						// Mining Tool
						Generators.ofMiningTool(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_mattock")) {
						// Mattock
						Generators.ofMattock(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_hammer")) {
						// Hammer
						Generators.ofHammer(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_excavator")) {
						// Excavator
						Generators.ofExcavator(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_helmet")) {
						// Helmet
						Generators.ofHelmet(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_chestplate")) {
						// Chestplate
						Generators.ofChestplate(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_leggings")) {
						// Leggings
						Generators.ofLeggings(recipes, item, material.asIngredient());
					} else if (itemIdPath.endsWith("_boots")) {
						// Boots
						Generators.ofBoots(recipes, item, material.asIngredient());
					}
				} else if (item instanceof ToolItem || item instanceof ArmorItem || item instanceof DynamicAttributeTool) {
					// Univite
					Generators.ofUniviteSmithing(recipes, item, itemId);
				}

				if (itemIdPath.endsWith("_nugget") || itemIdPath.endsWith("_fragment")) {
					// 1 ingot to 9 nuggets (/ fragments)
					Generators.ofIngotToNugget(recipes, item, material.asIngredient());

					// 9 nuggets (/fragments) to 1 ingot
					Generators.ofNuggetToIngot(recipes, material.getIngotItemId(), itemId);

					// 1 nugget (/fragment) to 1 tiny dust
					Generators.ofTrituratoringItemToDust(recipes, convertIdToCommonTag(itemId), material.getTinyDustId(), 1, 10, 90);

					// 1 cluster to 1 nugget (/fragment)
					Generators.ofSmelting(recipes, convertIdToCommonTag(asId(material.getMaterialId() + "_cluster")), itemId);

					// 1 cluster to 1 nugget (/fragment)
					Generators.ofBlasting(recipes, convertIdToCommonTag(asId(material.getMaterialId() + "_cluster")), itemId);

					// 1 cluster to 2 tiny dusts
					Generators.ofTrituratoringItemToDust(recipes, convertIdToCommonTag(asId(material.getMaterialId() + "_cluster")), material.getTinyDustId(), 2, 20, 180);

					// 1 tiny dust to 1 nugget (/fragment)
					Generators.ofSmelting(recipes, material.getTinyDustTagId(), itemId);

					// 1 tiny dust to 1 nugget (/fragment)
					Generators.ofBlasting(recipes, material.getTinyDustTagId(), itemId);

					if (!material.getIngotItemId().equals(material.getDustId())) {
						// 1 dust to 1 ingot (/fragment)
						Generators.ofSmelting(recipes, material.getDustTagId(), material.getIngotItemId());

						// 1 dust to 1 ingot (/fragment)
						Generators.ofBlasting(recipes, material.getDustTagId(), material.getIngotItemId());
					}
				} else if (itemIdPath.endsWith("_plates")) {
					// 2 ingots to 1 plate
					Generators.ofIngotToPlate(recipes, item, material.asIngredient());

					// 1 ingot to 1 plate
					Generators.ofPresserIngotToPlate(recipes, item, material.asIngredient());
				} else if (itemIdPath.endsWith("_gear")) {
					// 4 ingots to 1 gear
					Generators.ofIngotToGear(recipes, item, material.asIngredient());
				} else if (itemIdPath.endsWith("_tiny_dust")) {
					// 9 tiny dust to 1 dust
					Generators.ofTinyDustToDust(recipes, material.getDustId(), material.getTinyDustTagId());

					// 1 dust to 9 tiny dust
					Generators.ofDustToTinyDust(recipes, material.getTinyDustId(), material.getDustTagId());

					if (!material.getIngotItemId().equals(material.getDustId())) {
						// 1 ingot to 1 dusts
						Generators.ofTrituratoringItemToDust(recipes, material.getItemTagId(), material.getDustId(), 1, 30, 270);
					}
				}
			}
		});

		Set<String> addedOre = Sets.newHashSet();

		forEach(AstromineFoundationsBlocks.class, Block.class, block -> {
			Identifier blockId = Registry.BLOCK.getId(block);
			String blockIdPath = blockId.getPath();

			MaterialEntry material = getMaterialFromId(blockId);

			if (material != null) {
				if (blockIdPath.endsWith("_block")) {
					// 9 ingot to 1 block
					Generators.ofIngotToBlock(recipes, block, material.asIngredient());

					// 1 block to 9 ingot
					Generators.ofBlockToIngot(recipes, material.getIngotItemId(), block);
				} else if (blockIdPath.endsWith("_ore")) {
					if (addedOre.add(material.getMaterialId())) {
						// 1 ore to 1 ingot
						Generators.ofSmelting(recipes, convertIdToCommonTag(blockId), material.getIngotItemId());

						// 1 ore to 1 ingot
						Generators.ofBlasting(recipes, convertIdToCommonTag(blockId), material.getIngotItemId());

						// 1 ore to 2 dusts
						Generators.ofTrituratoringItemToDust(recipes, convertIdToCommonTag(blockId), material.getDustId(), 2, 30, 270);
					}
				}
			}
		});
	}

	public static Identifier asId(String id) {
		return new Identifier(id);
	}

	public static Identifier asId(String namespace, String path) {
		return new Identifier(namespace, path);
	}

	public static Identifier convertIdToCommonTag(Identifier identifier) {
		String s = identifier.getPath().replaceFirst("asteroid_", "").replaceFirst("meteor_", "");
		return asId("c", s.endsWith("s") ? s : s + "s");
	}

	@Nullable
	private static MaterialEntry getMaterialFromTinyDust(Identifier itemId) {
		return MATERIALS.values().stream()
				.filter(material -> Objects.equals(material.getTinyDustId(), itemId))
				.findFirst()
				.orElse(null);
	}

	@Nullable
	private static MaterialEntry getMaterialFromId(Identifier itemId) {
		String path = itemId.getPath().replaceFirst("asteroid_", "").replaceFirst("meteor_", "");
		if (path.contains("_")) {
			return MATERIALS.keySet().stream()
					.filter(path::startsWith)
					.findFirst()
					.map(MATERIALS::get)
					.orElse(getMaterialFromTinyDust(itemId));
		}

		return getMaterialFromTinyDust(itemId);
	}

	private static String getTypeFromId(String id) {
		if (id.contains("tiny_dusts"))
			return "tiny_dust";
		return id.substring(id.lastIndexOf('_') + 1, id.endsWith("s") ? id.length() - 1 : id.length());
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
					.input('$', TagRegistry.item(asId("c:wood_sticks")))
					.input('#', material)
					.offerTo(recipes);
		}

		private static void ofPickaxe(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern(" $ ")
					.pattern(" $ ")
					.input('$', TagRegistry.item(asId("c:wood_sticks")))
					.input('#', material)
					.offerTo(recipes);
		}

		private static void ofHoe(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("##")
					.pattern(" $")
					.pattern(" $")
					.input('$', TagRegistry.item(asId("c:wood_sticks")))
					.input('#', material)
					.offerTo(recipes);
		}

		private static void ofSword(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("#")
					.pattern("#")
					.pattern("$")
					.input('$', TagRegistry.item(asId("c:wood_sticks")))
					.input('#', material)
					.offerTo(recipes);
		}

		private static void ofShovel(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("#")
					.pattern("$")
					.pattern("$")
					.input('$', TagRegistry.item(asId("c:wood_sticks")))
					.input('#', material)
					.offerTo(recipes);
		}

		private static void ofMiningTool(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern(" # ")
					.pattern(" $ ")
					.input('$', TagRegistry.item(asId("c:wood_sticks")))
					.input('#', material)
					.offerTo(recipes);
		}

		private static void ofMattock(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("#$ ")
					.pattern(" $ ")
					.input('$', TagRegistry.item(asId("c:wood_sticks")))
					.input('#', material)
					.offerTo(recipes);
		}

		private static void ofHammer(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("#$#")
					.pattern(" $ ")
					.input('$', TagRegistry.item(asId("c:wood_sticks")))
					.input('#', material)
					.offerTo(recipes);
		}

		private static void ofExcavator(RecipeData recipes, Item output, Ingredient material) {
			ShapedRecipeJsonFactory.create(output)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern(" # ")
					.pattern("#$#")
					.pattern(" $ ")
					.input('$', TagRegistry.item(asId("c:wood_sticks")))
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
					Ingredient.ofItems(Registry.ITEM.get(asId(keyPath.toString().replace("univite_", "galaxium_")))),
					Ingredient.fromTag(TagRegistry.item(asId("c:univite_ingots"))),
					output);
		}

		private static void ofIngotToNugget(RecipeData recipes, Item nugget, Ingredient ingot) {
			ShapelessRecipeJsonFactory.create(nugget, 9)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.input(ingot)
					.offerTo(recipes, Registry.ITEM.getId(nugget) + "_from_ingot");
		}

		private static void ofNuggetToIngot(RecipeData recipes, Identifier ingotItem, Identifier nuggetId) {
			ShapedRecipeJsonFactory.create(Registry.ITEM.get(ingotItem))
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("###")
					.pattern("###")
					.input('#', TagRegistry.item(convertIdToCommonTag(nuggetId)))
					.offerTo(recipes, ingotItem.toString() + "_from_" + getTypeFromId(nuggetId.getPath()));
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
			Generators.ofPressing(recipes, asId(Registry.ITEM.getId(plate) + "_from_pressing_ingot"),
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

		private static void ofSmelting(RecipeData recipes, Identifier itemTag, Identifier result) {
			CookingRecipeJsonFactory
					.createSmelting(
							Ingredient.fromTag(TagRegistry.item(itemTag)),
							Registry.ITEM.get(result),
							0.1F,
							200)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.offerTo(recipes, result + "_from_smelting_" + getTypeFromId(itemTag.getPath()));
		}

		private static void ofBlasting(RecipeData recipes, Identifier itemTag, Identifier result) {
			CookingRecipeJsonFactory
					.createBlasting(
							Ingredient.fromTag(TagRegistry.item(itemTag)),
							Registry.ITEM.get(result),
							0.1F,
							100)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.offerTo(recipes, result + "_from_blasting_" + getTypeFromId(itemTag.getPath()));
		}

		private static void ofTinyDustToDust(RecipeData recipes, Identifier dustItem, Identifier tinyDustTagId) {
			ShapedRecipeJsonFactory.create(Registry.ITEM.get(dustItem))
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.pattern("###")
					.pattern("###")
					.pattern("###")
					.input('#', TagRegistry.item(tinyDustTagId))
					.offerTo(recipes, dustItem + "_from_tiny_dust");
		}

		private static void ofDustToTinyDust(RecipeData recipes, Identifier tinyDustItem, Identifier dustTagId) {
			ShapelessRecipeJsonFactory.create(Registry.ITEM.get(tinyDustItem), 9)
					.criterion("impossible", new ImpossibleCriterion.Conditions())
					.input(TagRegistry.item(dustTagId))
					.offerTo(recipes, tinyDustItem + "_from_dust");
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
					.input(TagRegistry.item(convertIdToCommonTag(Registry.BLOCK.getId(block))))
					.offerTo(recipes, ingot + "_from_block");
		}

		private static void ofTrituratoringItemToDust(RecipeData recipes, Identifier itemTag, Identifier dust, int dustCount, int time, int energyConsumed) {
			ofTriturating(recipes, asId(dust + "_from_triturating_" + getTypeFromId(itemTag.getPath())),
					Ingredient.fromTag(TagRegistry.item(itemTag)), 1, new ItemStack(Registry.ITEM.get(dust), dustCount), time, energyConsumed);
		}

		/*
		 * Machine recipe generators.
		 */
		private static void ofPressing(RecipeData recipes, Ingredient input, int inputCount, ItemStack output, int time, int energyConsumed) {
			ofPressing(recipes, Registry.ITEM.getId(output.getItem()), input, inputCount, output, time, energyConsumed);
		}

		private static void ofPressing(RecipeData recipes, Identifier id, Ingredient input, int inputCount, ItemStack output, int time, int energyConsumed) {
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

		private static void ofTriturating(RecipeData recipes, Ingredient input, int inputCount, ItemStack output, int time, int energyConsumed) {
			ofTriturating(recipes, Registry.ITEM.getId(output.getItem()), input, inputCount, output, time, energyConsumed);
		}

		private static void ofTriturating(RecipeData recipes, Identifier id, Ingredient input, int inputCount, ItemStack output, int time, int energyConsumed) {
			recipes.accept(Providers.createProvider(TrituratingRecipe.Serializer.INSTANCE, id, json -> {
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
