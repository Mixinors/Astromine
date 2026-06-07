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

import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import com.github.mixinors.astromine.registry.common.AMItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.stream.Stream;

public class AMEntityLootTableProvider extends EntityLootSubProvider {
	public AMEntityLootTableProvider(HolderLookup.Provider registries) {
		super(FeatureFlags.REGISTRY.allFlags(), registries);
	}
	
	@Override
	public void generate() {
		add(AMEntityTypes.SPACE_SLIME.get(), LootTable.lootTable()
																	.withPool(LootPool.lootPool()
																				  .setRolls(ConstantValue.exactly(1))
																				  .add(LootItem.lootTableItem(AMItems.SPACE_SLIME_BALL.get())
																								 .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
																								 .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0, 1)))
																				  )
																	)
		);
	}
	
	@Override
	protected Stream<EntityType<?>> getKnownEntityTypes() {
		return Stream.of(AMEntityTypes.SPACE_SLIME.get());
	}
}
