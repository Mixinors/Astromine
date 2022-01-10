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

import java.util.Optional;
import java.util.function.Supplier;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SimpleMachineConfig;
import com.github.mixinors.astromine.common.provider.config.tiered.MachineConfigProvider;
import com.github.mixinors.astromine.common.recipe.PressingRecipe;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public abstract class PressBlockEntity extends ExtendedBlockEntity implements MachineConfigProvider<SimpleMachineConfig> {
	public double progress = 0;
	public int limit = 100;
	private boolean shouldTry = true;
	
	private static final int INPUT_SLOT = 1;
	
	private static final int OUTPUT_SLOT = 0;

	private static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	private static final int[] EXTRACT_SLOTS = new int[] { OUTPUT_SLOT };
	
	private Optional<PressingRecipe> optionalRecipe = Optional.empty();

	public PressBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
		
		energyStorage = new SimpleEnergyStorage(getEnergyStorageSize(), Long.MAX_VALUE, Long.MAX_VALUE);
		
		itemStorage = new SimpleItemStorage(2).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			return PressingRecipe.allows(world, variant);
		}).extractPredicate((variant, slot) ->
			slot == OUTPUT_SLOT
		).listener(() -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
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
			if (optionalRecipe.isEmpty() && shouldTry) {
				optionalRecipe = PressingRecipe.matching(world, itemStorage.slice(INPUT_SLOT, OUTPUT_SLOT));
				shouldTry = false;

				if (optionalRecipe.isEmpty()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				var recipe = optionalRecipe.get();

				limit = recipe.time();
				
				var speed = Math.min(getSpeed(), limit - progress);
				var consumed = (long) (recipe.energyInput() * speed / limit);

				try (var transaction = Transaction.openOuter()) {
					if (energyStorage.extract(consumed, transaction) == consumed) {
						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();
							
							var inputStorage = itemStorage.getStorage(INPUT_SLOT);
							
							inputStorage.extract(inputStorage.getResource(), recipe.input().getAmount(), transaction);
							
							var outputStorage = itemStorage.getStorage(OUTPUT_SLOT);
							
							outputStorage.insert(recipe.output().variant(), recipe.output().count(), transaction);
							
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

	@Override
	public SimpleMachineConfig getConfig() {
		return AMConfig.get().machines.press;
	}

	public static class Primitive extends PressBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_PRESSER, blockPos, blockState);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends PressBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_PRESSER, blockPos, blockState);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends PressBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_PRESSER, blockPos, blockState);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends PressBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_PRESSER, blockPos, blockState);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
