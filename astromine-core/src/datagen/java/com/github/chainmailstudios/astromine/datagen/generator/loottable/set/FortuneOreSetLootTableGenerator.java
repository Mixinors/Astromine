package com.github.chainmailstudios.astromine.datagen.generator.loottable.set;

import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;

public class FortuneOreSetLootTableGenerator extends GenericSetLootTableGenerator {
	protected final MaterialItemType drop;

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
				LootItem.lootTableItem(set.getItem(drop))
					.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
			)
		));
	}

	@Override
	public String getGeneratorName() {
		return "fortune_ore_set";
	}
}
