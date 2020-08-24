package com.github.chainmailstudios.astromine.datagen.generator.loottable.set;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableRange;
import net.minecraft.loot.LootTableRanges;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;

public class FortuneOreSetLootTableGenerator extends GenericSetLootTableGenerator {
	private final MaterialItemType drop;

	public FortuneOreSetLootTableGenerator(MaterialItemType ore, MaterialItemType drop) {
		super(ore);
		this.drop = drop;
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return super.shouldGenerate(set) && set.hasType(drop);
	}

	@Override
	public void generate(LootTableData data, MaterialSet set) {
		data.register(getBlock(set), LootTableData.dropsBlockWithSilkTouch(
				getBlock(set),
				LootTableData.addExplosionDecayLootFunction(
						getBlock(set),
						ItemEntry.builder(set.getItem(drop))
								.apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
				)
		));
	}

	@Override
	public String getGeneratorName() {
		return "fortune_ore_set";
	}
}
