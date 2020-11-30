package com.github.chainmailstudios.astromine.datagen.generator.loottable.set;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.RandomIntGenerator;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;

public class FortuneMultiOreSetLootTableGenerator extends FortuneOreSetLootTableGenerator {
	private final RandomIntGenerator dropCountRange;

	public FortuneMultiOreSetLootTableGenerator(MaterialItemType ore, MaterialItemType drop, RandomIntGenerator dropCountRange) {
		super(ore, drop);
		this.dropCountRange = dropCountRange;
	}

	@Override
	public void generate(LootTableData data, MaterialSet set) {
		data.register(getBlock(set), LootTableData.dropsBlockWithSilkTouch(
			getBlock(set),
			LootTableData.addExplosionDecayLootFunction(
				getBlock(set),
				LootItem.lootTableItem(set.getItem(drop))
					.apply(SetItemCountFunction.setCount(dropCountRange))
					.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
			)
		));
	}

	@Override
	public String getGeneratorName() {
		return "fortune_multi_ore_set";
	}
}
