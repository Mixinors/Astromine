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

package com.github.mixinors.astromine.common.block.entity.machine;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SimpleMachineConfig;
import com.github.mixinors.astromine.common.provider.config.tiered.MachineConfigProvider;
import com.github.mixinors.astromine.common.recipe.AlloySmeltingRecipe;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.Math.min;

public abstract class AlloySmelterBlockEntity extends ExtendedBlockEntity implements MachineConfigProvider<SimpleMachineConfig> {
	public static final int INPUT_SLOT_1 = 0;
	public static final int INPUT_SLOT_2 = 1;
	
	public static final int OUTPUT_SLOT = 2;
	
	public static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT_1, INPUT_SLOT_2 };
	
	public static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };
	
	private Optional<AlloySmeltingRecipe> optionalRecipe = Optional.empty();
	
	public AlloySmelterBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		itemStorage = new SimpleItemStorage(3).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT_1 && slot != INPUT_SLOT_2) {
				return false;
			}
			
			return AlloySmeltingRecipe.allows(world, variant, itemStorage.getVariant(INPUT_SLOT_2)) ||
					AlloySmeltingRecipe.allows(world, itemStorage.getVariant(INPUT_SLOT_1), variant);
		}).extractPredicate((variant, slot) ->
				slot == OUTPUT_SLOT
		).listener(() -> {
			if (optionalRecipe.isPresent() && !optionalRecipe.get().matches(itemStorage.slice(INPUT_SLOT_1, INPUT_SLOT_2, OUTPUT_SLOT))) {
				optionalRecipe = Optional.empty();
			}
			
			markDirty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (world == null || world.isClient || !shouldRun()) {
			return;
		}
		
		if (itemStorage != null && energyStorage != null) {
			if (optionalRecipe.isEmpty()) {
				optionalRecipe = AlloySmeltingRecipe.matching(world, itemStorage.slice(INPUT_SLOT_1, INPUT_SLOT_2, OUTPUT_SLOT));
			}
			
			if (optionalRecipe.isPresent()) {
				var recipe = optionalRecipe.get();
				
				limit = recipe.time();
				
				var speed = min(getSpeed(), limit - progress);
				var consumed = (long) (recipe.energyInput() * speed / limit);
				
				try (var transaction = Transaction.openOuter()) {
					if (energyStorage.amount >= consumed) {
						energyStorage.amount -= consumed;
						
						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();
							
							var firstInputStorage = itemStorage.getStorage(INPUT_SLOT_1);
							var secondInputStorage = itemStorage.getStorage(INPUT_SLOT_2);
							
							if (recipe.firstInput().test(firstInputStorage) && recipe.secondInput().test(secondInputStorage)) {
								firstInputStorage.extract(firstInputStorage.getResource(), recipe.firstInput().getAmount(), transaction, true);
								secondInputStorage.extract(secondInputStorage.getResource(), recipe.secondInput().getAmount(), transaction, true);
							} else if (recipe.firstInput().test(secondInputStorage) && recipe.secondInput().test(firstInputStorage)) {
								firstInputStorage.extract(firstInputStorage.getResource(), recipe.secondInput().getAmount(), transaction, true);
								secondInputStorage.extract(secondInputStorage.getResource(), recipe.firstInput().getAmount(), transaction, true);
							}
							
							var outputStorage = itemStorage.getStorage(OUTPUT_SLOT);
							
							outputStorage.insert(recipe.output().variant(), recipe.output().count(), transaction, true);
							
							transaction.commit();
							
							progress = 0.0D;
						} else {
							progress += speed;
						}
						
						active = true;
					} else {
						active = false;
					}
				}
			} else {
				progress = 0.0D;
				limit = 100.0D;
				
				active = false;
			}
		}
	}
	
	@Override
	public SimpleMachineConfig getConfig() {
		return AMConfig.get().blocks.machines.alloySmelter;
	}
	
	public static class Primitive extends AlloySmelterBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_ALLOY_SMELTER, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.PRIMITIVE;
		}
	}
	
	public static class Basic extends AlloySmelterBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_ALLOY_SMELTER, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.BASIC;
		}
	}
	
	public static class Advanced extends AlloySmelterBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_ALLOY_SMELTER, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ADVANCED;
		}
	}
	
	public static class Elite extends AlloySmelterBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_ALLOY_SMELTER, blockPos, blockState);
		}
		
		@Override
		public Tier getMachineTier() {
			return Tier.ELITE;
		}
	}
}
