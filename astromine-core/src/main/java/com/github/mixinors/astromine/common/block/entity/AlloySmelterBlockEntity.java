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

package com.github.mixinors.astromine.common.block.entity;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import com.github.mixinors.astromine.common.recipe.AlloySmeltingRecipe;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.Math.min;

public abstract class AlloySmelterBlockEntity extends ExtendedBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	private double progress = 0;
	private int limit = 100;
	private boolean shouldTry = false;
	
	private static final int INPUT_SLOT_1 = 0;
	private static final int INPUT_SLOT_2 = 1;
	
	private static final int OUTPUT_SLOT = 2;
	
	private static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT_1, INPUT_SLOT_2 };
	
	private static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };

	private Optional<AlloySmeltingRecipe> optionalRecipe = Optional.empty();

	public AlloySmelterBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergySize(), Long.MAX_VALUE, Long.MAX_VALUE);
		
		itemStorage = new SimpleItemStorage(3).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT_1 && slot != INPUT_SLOT_2) {
				return false;
			}
			
			if (!itemStorage.getVariant(INPUT_SLOT_1).equals(variant) &&
				!itemStorage.getVariant(INPUT_SLOT_2).equals(variant)) {
				return false;
			}
			
			return AlloySmeltingRecipe.allows(world, variant, itemStorage.getVariant(INPUT_SLOT_2)) ||
				   AlloySmeltingRecipe.allows(world, itemStorage.getVariant(INPUT_SLOT_1), variant);
		}).extractPredicate((variant, slot) -> {
			return slot == OUTPUT_SLOT;
		}).listener(() -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !shouldRun())
			return;

		if (itemStorage != null && energyStorage != null) {
			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = AlloySmeltingRecipe.matching(world, itemStorage.slice(INPUT_SLOT_1, INPUT_SLOT_2, OUTPUT_SLOT));
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				var recipe = optionalRecipe.get();

				limit = recipe.time;

				var speed = min(getMachineSpeed(), limit - progress);
				var consumed = (long) (recipe.energyInput * speed / limit);

				try (var transaction = Transaction.openOuter()) {
					if (energyStorage.extract(consumed, transaction) == consumed) {
						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();
							
							var firstInputStorage = itemStorage.getStorage(INPUT_SLOT_1);
							var secondInputStorage = itemStorage.getStorage(INPUT_SLOT_2);
							
							if (recipe.firstInput.test(firstInputStorage) && recipe.secondInput.test(secondInputStorage)) {
								firstInputStorage.extract(firstInputStorage.getResource(), recipe.firstInput.getAmount(), transaction);
								secondInputStorage.extract(secondInputStorage.getResource(), recipe.secondInput.getAmount(), transaction);
							} else if (recipe.firstInput.test(secondInputStorage.getResource(), secondInputStorage.getAmount()) &&
									   recipe.secondInput.test(firstInputStorage.getResource(), firstInputStorage.getAmount())) {
								
								firstInputStorage.extract(secondInputStorage.getResource(), recipe.secondInput.getAmount(), transaction);
								secondInputStorage.extract(firstInputStorage.getResource(), recipe.firstInput.getAmount(), transaction);
							}
							
							var outputStorage = itemStorage.getStorage(OUTPUT_SLOT);
							
							outputStorage.insert(ItemVariant.of(recipe.output.copy()), recipe.output.getCount(), transaction);

							transaction.commit();
							
							progress = 0;
						} else {
							progress += speed;
						}
						
						isActive = true;
					} else {
						isActive = false;
					}
				}
			} else {
				isActive = false;
			}
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putDouble("Progress", progress);
		nbt.putInt("Limit", limit);
		
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		progress = nbt.getDouble("Progress");
		limit = nbt.getInt("Limit");
		
		super.readNbt(nbt);
	}

	public static class Primitive extends AlloySmelterBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_ALLOY_SMELTER, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveAlloySmelterSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().primitiveAlloySmelterEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends AlloySmelterBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_ALLOY_SMELTER, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicAlloySmelterSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().basicAlloySmelterEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends AlloySmelterBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_ALLOY_SMELTER, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedAlloySmelterSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().advancedAlloySmelterEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends AlloySmelterBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_ALLOY_SMELTER, blockPos, blockState);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteAlloySmelterSpeed;
		}

		@Override
		public long getEnergySize() {
			return AMConfig.get().eliteAlloySmelterEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
