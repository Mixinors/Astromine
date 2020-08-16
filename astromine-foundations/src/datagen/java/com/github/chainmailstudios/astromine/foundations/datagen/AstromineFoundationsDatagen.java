package com.github.chainmailstudios.astromine.foundations.datagen;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.item.MultitoolItem;
import com.github.chainmailstudios.astromine.foundations.common.block.AstromineOreBlock;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsItems;
import draylar.magna.item.ExcavatorItem;
import draylar.magna.item.HammerItem;
import me.shedaniel.cloth.api.datagen.v1.DataGeneratorHandler;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;
import me.shedaniel.cloth.api.datagen.v1.TagData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class AstromineFoundationsDatagen implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		try {
			DataGeneratorHandler handler = DataGeneratorHandler.create(Paths.get("../astromine-foundations/src/generated/resources"));
			FabricLoader.getInstance().getEntrypoints("main", ModInitializer.class).forEach(ModInitializer::onInitialize);

			lootTables(handler.getLootTables());
			tags(handler.getTags());

			handler.run();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	private static void lootTables(LootTableData tables) {
		iterate(AstromineFoundationsBlocks.class, Block.class, block -> {
			if (block instanceof SlabBlock) {
				tables.register(block, LootTableData.dropsSlabs(block));
			} else if (block instanceof AstromineOreBlock) {
				Identifier key = Registry.BLOCK.getKey(block).get().getValue();
				if (key.getPath().startsWith("asteroid_") || key.getPath().startsWith("meteor_")) {
					tables.register(block, LootTableData.dropsBlockWithSilkTouch(
						block,
						LootTableData.addExplosionDecayLootFunction(
							block,
							ItemEntry.builder(Registry.ITEM.get(AstromineCommon.identifier(key.toString().replace("_ore", "_cluster"))))
								.apply(SetCountLootFunction.builder(UniformLootTableRange.between(1, 3)))
								.apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
						)
					));
				} else {
					tables.registerBlockDropSelf(block);
				}
			} else {
				tables.registerBlockDropSelf(block);
			}
		});
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

		tags.item(new Identifier("c:diamond_ores")).append(Blocks.DIAMOND_ORE);
		tags.item(new Identifier("c:emerald_ores")).append(Blocks.EMERALD_ORE);
		tags.item(new Identifier("c:iron_ores")).append(Blocks.IRON_ORE);
		tags.item(new Identifier("c:gold_ores")).appendTag(new Identifier("gold_ores"));
		tags.item(new Identifier("c:redstone_ores")).append(Blocks.REDSTONE_ORE);
		tags.item(new Identifier("c:lapis_ores")).append(Blocks.LAPIS_ORE);
		tags.item(new Identifier("c:quartz_ores")).append(Blocks.NETHER_QUARTZ_ORE);
		tags.item(new Identifier("gold_ores")).append(Blocks.NETHER_GOLD_ORE);
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
