/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.technologies.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyInventoryBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.inventory.BaseInventory;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.handler.ItemHandler;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.TierProvider;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ElectricSmelterBlockEntity extends ComponentEnergyInventoryBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = true;

	private Optional<SmeltingRecipe> optionalRecipe = Optional.empty();

	public ElectricSmelterBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(2).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 1) {
				return false;
			}

			BaseInventory inputInventory = BaseInventory.of(stack);

			if (world != null) {
				Optional<SmeltingRecipe> recipe = (Optional<SmeltingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) RecipeType.SMELTING, inputInventory, world);
				return recipe.isPresent();
			}

			return false;
		}).withExtractPredicate(((direction, stack, slot) -> {
			return slot == 0;
		})).withListener((inventory) -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		});
	}

	@Override
	protected EnergyInventoryComponent createEnergyComponent() {
		return new SimpleEnergyInventoryComponent(getEnergySize());
	}

	@Override
	public IntSet getItemInputSlots() {
		return IntSets.singleton(1);
	}

	@Override
	public IntSet getItemOutputSlots() {
		return IntSets.singleton(0);
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null)
			return;
		if (world.isClient)
			return;

		ItemHandler.ofOptional(this).ifPresent(items -> {
			EnergyVolume energyVolume = getEnergyComponent().getVolume();
			BaseInventory inputInventory = BaseInventory.of(items.getSecond());

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = (Optional<SmeltingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) RecipeType.SMELTING, inputInventory, world);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				SmeltingRecipe recipe = optionalRecipe.get();

				if (recipe.matches(inputInventory, world)) {
					limit = recipe.getCookTime();

					double speed = Math.min(getMachineSpeed() * 2, limit - progress);

					ItemStack output = recipe.getOutput().copy();

					boolean isEmpty = items.getFirst().isEmpty();
					boolean isEqual = ItemStack.areItemsEqual(items.getFirst(), output) && ItemStack.areTagsEqual(items.getFirst(), output);

					if ((isEmpty || isEqual) && items.getFirst().getCount() + output.getCount() <= items.getFirst().getMaxCount() && energyVolume.use(500.0D / limit * speed)) {
						if (progress + speed >= limit) {
							optionalRecipe = Optional.empty();

							items.getSecond().decrement(1);

							if (isEmpty) {
								items.setFirst(output);
							} else {
								items.getFirst().increment(output.getCount());

								shouldTry = true; // Vanilla is garbage; if we don't do it here, it only triggers the listener on #setStack.
							}

							progress = 0;
						} else {
							progress += speed;
						}

						tickActive();
					} else {
						tickInactive();
					}
				} else {
					tickInactive();
				}
			} else {
				tickInactive();
			}
		});
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		super.fromTag(state, tag);
		progress = tag.getDouble("progress");
		limit = tag.getInt("limit");
		shouldTry = true;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("progress", progress);
		tag.putInt("limit", limit);
		return super.toTag(tag);
	}

	public static class Primitive extends ElectricSmelterBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_ELECTRIC_SMELTER, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_ELECTRIC_SMELTER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveElectricSmelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().primitiveElectricSmelterEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends ElectricSmelterBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_ELECTRIC_SMELTER, AstromineTechnologiesBlockEntityTypes.BASIC_ELECTRIC_SMELTER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicElectricSmelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().basicElectricSmelterEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends ElectricSmelterBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_ELECTRIC_SMELTER, AstromineTechnologiesBlockEntityTypes.ADVANCED_ELECTRIC_SMELTER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedElectricSmelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().advancedElectricSmelterEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends ElectricSmelterBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_ELECTRIC_SMELTER, AstromineTechnologiesBlockEntityTypes.ELITE_ELECTRIC_SMELTER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteElectricSmelterSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().eliteElectricSmelterEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
