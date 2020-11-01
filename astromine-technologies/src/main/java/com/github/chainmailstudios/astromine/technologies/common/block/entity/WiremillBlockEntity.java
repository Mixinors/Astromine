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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.InventoryFromItemComponent;
import com.github.chainmailstudios.astromine.common.inventory.BaseInventory;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.TierProvider;
import com.github.chainmailstudios.astromine.technologies.common.recipe.WireMillingRecipe;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class WireMillBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = true;

	private Optional<WireMillingRecipe> optionalRecipe = Optional.empty();

	public WireMillBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return SimpleItemComponent.of(2).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 1) {
				return false;
			}

			return WireMillingRecipe.allows(world, SimpleItemComponent.of(stack).asInventory());
		}).withExtractPredicate((direction, stack, slot) -> {
			return slot == 0;
		}).withListener((inventory) -> {
			shouldTry = true;
			optionalRecipe = Optional.empty();
		});
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
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

		if (world == null || world.isClient || !tickRedstone())
			return;

		ItemComponent itemComponent = getItemComponent();

		if (itemComponent != null) {
			EnergyVolume volume = getEnergyComponent().getVolume();
			BaseInventory inputInventory = BaseInventory.of(itemComponent.getFirst(), itemComponent.getSecond());

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = (Optional<WireMillingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) WireMillingRecipe.Type.INSTANCE, InventoryFromItemComponent.of(getItemComponent()), world);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				WireMillingRecipe recipe = optionalRecipe.get();

				if (recipe.matches(inputInventory, world)) {
					limit = recipe.getTime();

					double speed = Math.min(getMachineSpeed(), limit - progress);
					double consumed = recipe.getEnergy() * speed / limit;

					ItemStack output = recipe.getOutput().copy();

					boolean isEmpty = itemComponent.getFirst().isEmpty();
					boolean isEqual = ItemStack.areItemsEqual(itemComponent.getFirst(), output) && ItemStack.areTagsEqual(itemComponent.getFirst(), output);

					if (volume.hasStored(consumed)) {
						if ((isEmpty || isEqual) && itemComponent.getFirst().getCount() + output.getCount() <= itemComponent.getFirst().getMaxCount()) {
							volume.minus(consumed);

							if (progress + speed >= limit) {
								optionalRecipe = Optional.empty();

								itemComponent.getSecond().decrement(1);

								if (isEmpty) {
									itemComponent.setFirst(output);
								} else {
									itemComponent.getFirst().increment(output.getCount());
									shouldTry = true;
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
				}
			} else {
				tickInactive();
			}
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("progress", progress);
		tag.putInt("limit", limit);
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		progress = tag.getDouble("progress");
		limit = tag.getInt("limit");
		super.fromTag(state, tag);
	}

	public static class Primitive extends WireMillBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_WIREMILL, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_WIREMILL);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().primitiveWireMillEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends WireMillBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_WIREMILL, AstromineTechnologiesBlockEntityTypes.BASIC_WIREMILL);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().basicWireMillEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends WireMillBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_WIREMILL, AstromineTechnologiesBlockEntityTypes.ADVANCED_WIREMILL);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().advancedWireMillEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends WireMillBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_WIREMILL, AstromineTechnologiesBlockEntityTypes.ELITE_WIREMILL);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteWireMillSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().eliteWireMillEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
