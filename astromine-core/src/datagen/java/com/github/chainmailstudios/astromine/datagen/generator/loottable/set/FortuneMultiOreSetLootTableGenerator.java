package com.github.chainmailstudios.astromine.datagen.generator.loottable.set;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;

public class FortuneMultiOreSetLootTableGenerator extends FortuneOreSetLootTableGenerator {
	private final LootTableRange dropCountRange;

	public FortuneMultiOreSetLootTableGenerator(MaterialItemType ore, MaterialItemType drop, LootTableRange dropCountRange) {
		super(ore, drop);
		this.dropCountRange = dropCountRange;
	}

	@Override
	public void generate(LootTableData data, MaterialSet set) {
		data.register(getBlock(set), LootTableData.dropsBlockWithSilkTouch(
				getBlock(set),
				LootTableData.addExplosionDecayLootFunction(
						getBlock(set),
						ItemEntry.builder(set.getItem(drop))
								.apply(SetCountLootFunction.builder(dropCountRange))
								.apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
				)
		));
	}

	@Override
	public String getGeneratorName() {
		return "fortune_multi_ore_set";
	}
}
