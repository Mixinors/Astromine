/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.datagen.provider;

import com.github.mixinors.astromine.common.block.base.BlockWithEntity;
import com.github.mixinors.astromine.datagen.AMDatagenLists;
import com.github.mixinors.astromine.datagen.family.block.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.material.AMMaterialFamilies;
import com.github.mixinors.astromine.datagen.family.material.family.MaterialFamily;
import com.github.mixinors.astromine.datagen.family.material.variant.ItemVariant;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyCustomDataFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AMBlockLootTableProvider extends BlockLootSubProvider {
	private static final String BLOCK_ENTITY_TAG_KEY = "BlockEntityTag";
	
	private static final String REDSTONE_TYPE_KEY = "RedstoneType";
	private static final String ENERGY_STORAGE_KEY = "EnergyStorage";
	private static final String ITEM_STORAGE_KEY = "ItemStorage";
	private static final String FLUID_STORAGE_KEY = "FluidStorage";
	
	public static final List<Block> DROPS_SELF = ImmutableList.of(
			AMBlocks.BLAZING_ASTEROID_STONE.get(),
			
			AMBlocks.FLUID_PIPE.get(),
			
			AMBlocks.ITEM_CONDUIT.get(),
			
			AMBlocks.PRIMITIVE_ENERGY_CABLE.get(),
			AMBlocks.BASIC_ENERGY_CABLE.get(),
			AMBlocks.ADVANCED_ENERGY_CABLE.get(),
			AMBlocks.ELITE_ENERGY_CABLE.get(),
			
			AMBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR.get(),
			
			AMBlocks.NUCLEAR_WARHEAD.get(),
			
			AMBlocks.AIRLOCK.get(),
			
			AMBlocks.DRAIN.get()
	);
	
	public AMBlockLootTableProvider(HolderLookup.Provider registries) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
	}
	
	public LootTable.Builder machineDrops(Block drop) {
		if (drop instanceof BlockWithEntity machine && machine.saveTagToDroppedItem()) {
			var builder = LootTable.lootTable().withPool(applyExplosionCondition(machine, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(machine))));
			
			var copyNbtBuilder = CopyCustomDataFunction.copyData(ContextNbtProvider.BLOCK_ENTITY);
			
			var savedData = machine.getSavedDataForDroppedItem();
			
			if (savedData.redstoneControl()) {
				copyNbtBuilder = copyNbtBuilder.copy(REDSTONE_TYPE_KEY, BLOCK_ENTITY_TAG_KEY + "." + REDSTONE_TYPE_KEY);
			}
			if (savedData.energyStorage()) {
				copyNbtBuilder = copyNbtBuilder.copy(ENERGY_STORAGE_KEY, BLOCK_ENTITY_TAG_KEY + "." + ENERGY_STORAGE_KEY);
			}
			if (savedData.itemStorage()) {
				copyNbtBuilder = copyNbtBuilder.copy(ITEM_STORAGE_KEY, BLOCK_ENTITY_TAG_KEY + "." + ITEM_STORAGE_KEY);
			}
			if (savedData.fluidStorage()) {
				copyNbtBuilder = copyNbtBuilder.copy(FLUID_STORAGE_KEY, BLOCK_ENTITY_TAG_KEY + "." + FLUID_STORAGE_KEY);
			}
			
			builder.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(copyNbtBuilder);
			
			return builder;
		}
		
		return createSingleItemTable(drop);
	}
	
	private static boolean shouldGenerate(Block block) {
		return !block.getLootTable().equals(BuiltInLootTables.EMPTY);
	}
	
	@Override
	protected void generate() {
		AMMaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateLootTables).forEachOrdered((family) ->
				family.getBlockVariants().forEach((variant, block) -> {
					if (family.shouldGenerateLootTable(variant)) {
						switch (variant) {
							case BLOCK, RAW_ORE_BLOCK -> dropSelf(block);
							case STONE_ORE, DEEPSLATE_ORE, NETHER_ORE -> {
								Item drop;
								
								if (family.hasVariant(ItemVariant.RAW_ORE)) {
									drop = family.getVariant(ItemVariant.RAW_ORE);
								} else {
									drop = family.getBaseItem();
								}
								
								add(block, createOreDrop(block, drop));
							}
							case METEOR_ORE -> this.add(block, createOreDrop(block, family.getVariant(ItemVariant.METEOR_ORE_CLUSTER)));
							case ASTEROID_ORE -> this.add(block, createOreDrop(block, family.getVariant(ItemVariant.ASTEROID_ORE_CLUSTER)));
							case MOON_ORE, DARK_MOON_ORE -> {
								Item drop;
								
								if (family.hasVariant(ItemVariant.RAW_ORE)) {
									drop = family.getVariant(ItemVariant.RAW_ORE);
								} else {
									drop = family.getBaseItem();
								}
								
								if (drop == Items.REDSTONE) {
									add(block, createRedstoneOreDrops(block));
								} else if (drop == Items.LAPIS_LAZULI) {
									add(block, createLapisOreDrops(block));
								} else {
									add(block, createOreDrop(block,  drop));
								}
							}
						}
					}
				})
		);
		
		AMBlockFamilies.getFamilies().forEachOrdered((family) -> {
			dropSelf(family.getBaseBlock());
			
			family.getVariants().forEach((variant, block) -> {
				switch (variant) {
					case DOOR -> add(block, this::createDoorTable);
					case SLAB -> add(block, this::createSlabItemTable);
					
					default -> dropSelf(block);
				}
			});
		});
		
		DROPS_SELF.forEach(this::dropSelf);
		
		add(AMBlocks.AIRLOCK.get(), this::createDoorTable);
		
		AMDatagenLists.BlockLists.MACHINES.stream().filter(AMBlockLootTableProvider::shouldGenerate).forEach((block) -> this.add(block, machineDrops(block)));
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks() {
		var blocks = new LinkedHashSet<Block>();
		
		AMMaterialFamilies.getFamilies().filter(MaterialFamily::shouldGenerateLootTables).forEachOrdered((family) ->
				family.getBlockVariants().forEach((variant, block) -> {
					if (family.shouldGenerateLootTable(variant)) {
						blocks.add(block);
					}
				})
		);
		
		AMBlockFamilies.getFamilies().forEachOrdered((family) -> {
			blocks.add(family.getBaseBlock());
			blocks.addAll(family.getVariants().values());
		});
		
		blocks.addAll(DROPS_SELF);
		AMDatagenLists.BlockLists.MACHINES.stream().filter(AMBlockLootTableProvider::shouldGenerate).forEach(blocks::add);
		blocks.add(AMBlocks.AIRLOCK.get());
		
		return blocks;
	}
}
