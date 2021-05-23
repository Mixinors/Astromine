/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.datagen.generator.loottable.set;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;

import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.LootTableData;

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
