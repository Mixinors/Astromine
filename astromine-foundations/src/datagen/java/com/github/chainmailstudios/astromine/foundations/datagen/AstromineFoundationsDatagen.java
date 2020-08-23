package com.github.chainmailstudios.astromine.foundations.datagen;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.generator.entrypoint.RecipeGeneratorInitializer;
import com.github.chainmailstudios.astromine.common.generator.entrypoint.TagGeneratorInitializer;
import com.github.chainmailstudios.astromine.foundations.common.block.AstromineOreBlock;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsItems;
import com.github.chainmailstudios.astromine.registry.AstromineTagGenerators;
import com.github.chainmailstudios.astromine.registry.AstromineMaterialSets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.JsonObject;
import me.shedaniel.cloth.api.datagen.v1.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.model.Models;
import net.minecraft.data.client.model.Texture;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.util.TriConsumer;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class AstromineFoundationsDatagen implements PreLaunchEntrypoint {
	private static final Multimap<Class<?>, DataGenConsumer<?>> CONSUMERS = Multimaps.newListMultimap(Maps.newHashMap(), Lists::newArrayList);
	private static final Map<BiPredicate<Block, Identifier>, TriConsumer<LootTableData, Block, Identifier>> BLOCK_LOOT_TABLES = Maps.newLinkedHashMap();

	@Override
	public void onPreLaunch() {
		try {
			FabricLoader.getInstance().getEntrypoints("astromine_recipe_generators", RecipeGeneratorInitializer.class).forEach(RecipeGeneratorInitializer::onInitializeRecipeGenerators);
			FabricLoader.getInstance().getEntrypoints("astromine_tag_generators", TagGeneratorInitializer.class).forEach(TagGeneratorInitializer::onInitializeTagGenerators);

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

			System.exit(0);
		}

		System.exit(0);
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


	private static void register() {
		registerBlockLootTableOverrides((block, identifier) -> block instanceof SlabBlock, (lootTableData, block, identifier) ->
			lootTableData.register(block, LootTableData.dropsSlabs(block)));

		registerBlockLootTableOverrides((block, identifier) -> block instanceof AstromineOreBlock && identifier.getPath().startsWith("meteor_"),
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
		registerBlockLootTableOverrides((block, identifier) -> block instanceof AstromineOreBlock && identifier.getPath().startsWith("asteroid_"),
			(lootTableData, block, identifier) -> lootTableData.register(block, LootTableData.dropsBlockWithSilkTouch(
				block,
				LootTableData.addExplosionDecayLootFunction(
					block,
					ItemEntry.builder(Registry.ITEM.get(AstromineCommon.identifier(identifier.toString().replace("_ore", "_cluster"))))
						.apply(SetCountLootFunction.builder(ConstantLootTableRange.create(1)))
						.apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
				)
			))
		);
		register(Block.class, (handler, block) -> {
			ModelStateData modelStates = handler.getModelStates();
			Identifier blockId = Registry.BLOCK.getId(block);
			String blockIdPath = blockId.getPath();

			if (blockIdPath.endsWith("_block") || blockIdPath.endsWith("_ore")) {
				if (blockIdPath.endsWith("_ore") && (blockIdPath.startsWith("meteor_") || blockIdPath.startsWith("asteroid_"))) {
					Texture texture = Texture.sideEnd(Texture.getId(block), AstromineCommon.identifier("block/asteroid_stone"));
					Identifier identifier = Models.CUBE_COLUMN.upload(block, texture, modelStates::addModel);
					modelStates.addState(block, ModelStateData.createSingletonBlockState(block, identifier));
				} else {
					modelStates.addSingletonCubeAll(block);
				}
			}

			modelStates.addSimpleBlockItemModel(block);
		});

		register(Item.class, (handler, item) -> {
			ModelStateData modelStates = handler.getModelStates();
			Identifier itemId = Registry.ITEM.getId(item);
			String itemIdPath = itemId.getPath();

			if (item instanceof ToolItem || item instanceof DynamicAttributeTool) {
				modelStates.addHandheldItemModel(item);
			} else {
				modelStates.addGeneratedItemModel(item);
			}
		});
	}

	private static void registerConsumers() {
		register(Block.class, (handler, block) -> {
			Identifier blockId = Registry.BLOCK.getId(block);
			BLOCK_LOOT_TABLES.entrySet().stream()
				.filter(entry -> entry.getKey().test(block, blockId))
				.findFirst()
				.map(Map.Entry::getValue)
				.orElse((lootTableData, b, identifier) -> lootTableData.registerBlockDropSelf(b))
				.accept(handler.getLootTables(), block, blockId);
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
		AstromineMaterialSets.generateTags(tags);
		AstromineTagGenerators.generateOneTimeTags(tags);
	}

	private static void populateRecipes(RecipeData recipes) {
		AstromineMaterialSets.generateRecipes(recipes);
	}
}
