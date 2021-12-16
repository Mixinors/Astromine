package com.github.mixinors.astromine.datagen.provider;

import java.util.List;

import com.github.mixinors.astromine.datagen.AMDatagen;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.registry.common.AMBlocks;

import net.minecraft.block.Block;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTablesProvider;

public class AMBlockLootTableProvider extends FabricBlockLootTablesProvider {
	public static final List<Block> DROPS_SELF = List.of(
			AMBlocks.BLAZING_ASTEROID_STONE.get(),

			AMBlocks.ALTAR.get(),
			AMBlocks.ALTAR_PEDESTAL.get(),

			AMBlocks.FLUID_PIPE.get(),

			AMBlocks.PRIMITIVE_ENERGY_CABLE.get(),
			AMBlocks.BASIC_ENERGY_CABLE.get(),
			AMBlocks.ADVANCED_ENERGY_CABLE.get(),
			AMBlocks.ELITE_ENERGY_CABLE.get(),

			AMBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR.get(),

			AMBlocks.NUCLEAR_WARHEAD.get(),

			AMBlocks.AIRLOCK.get(),

			AMBlocks.DRAIN.get()
	);

	public AMBlockLootTableProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	public static LootTable.Builder machineDrops(Block drop) {
		return LootTable.builder().pool(addSurvivesExplosionCondition(drop, LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(drop)).apply(CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY))));
	}

	@Override
	protected void generateBlockLootTables() {
		MaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateLootTables).forEachOrdered((family) -> {
			family.getBlockVariants().forEach((variant, block) -> {
				if (family.shouldGenerateLootTable(variant)) {
					switch (variant) {
						case BLOCK, RAW_ORE_BLOCK -> addDrop(block);
						case STONE_ORE, DEEPSLATE_ORE, NETHER_ORE -> {
							Item drop;
							if (family.hasVariant(ItemVariant.RAW_ORE)) {
								drop = family.getVariant(ItemVariant.RAW_ORE);
							} else {
								drop = family.getBaseItem();
							}
							addDrop(block, (b) -> oreDrops(block, drop));
						}
						case METEOR_ORE -> this.addDrop(block, oreDrops(block, family.getVariant(ItemVariant.METEOR_ORE_CLUSTER)));
						case ASTEROID_ORE -> this.addDrop(block, oreDrops(block, family.getVariant(ItemVariant.ASTEROID_ORE_CLUSTER)));
					}
				}
			});
		});

		AMBlockFamilies.getFamilies().forEachOrdered((family) -> {
			addDrop(family.getBaseBlock());
			family.getVariants().forEach((variant, block) -> {
				switch (variant) {
					case DOOR -> addDoorDrop(block);
					case SLAB -> addDrop(block, BlockLootTableGenerator::slabDrops);
					default -> addDrop(block);
				}
			});
		});

		DROPS_SELF.forEach(this::addDrop);

		addDoorDrop(AMBlocks.AIRLOCK.get());

		AMDatagen.MACHINES.forEach((block) -> this.addDrop(block, machineDrops(block)));
	}
}
