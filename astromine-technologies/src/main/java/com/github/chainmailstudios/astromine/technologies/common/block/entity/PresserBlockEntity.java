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

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyInventoryBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.inventory.BaseInventory;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.handler.ItemHandler;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.TierProvider;
import com.github.chainmailstudios.astromine.technologies.common.recipe.PressingRecipe;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class PresserBlockEntity extends ComponentEnergyInventoryBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public double progress = 0;
	public int limit = 100;
	public boolean shouldTry = true;

	private Optional<PressingRecipe> optionalRecipe = Optional.empty();

	public PresserBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(2).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 1) {
				return false;
			}

			return PressingRecipe.allows(world, new SimpleItemInventoryComponent(stack).asInventory());
		}).withExtractPredicate((direction, stack, slot) -> {
			return slot == 0;
		}).withListener((inventory) -> {
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

		if (world == null || world.isClient || !tickRedstone())
			return;

		ItemHandler.ofOptional(this).ifPresent(items -> {
			EnergyVolume volume = getEnergyComponent().getVolume();
			BaseInventory inputInventory = BaseInventory.of(items.getFirst(), items.getSecond());

			if (!optionalRecipe.isPresent() && shouldTry) {
				optionalRecipe = (Optional<PressingRecipe>) world.getRecipeManager().getFirstMatch((RecipeType) PressingRecipe.Type.INSTANCE, ItemInventoryFromInventoryComponent.of(itemComponent), world);
				shouldTry = false;

				if (!optionalRecipe.isPresent()) {
					progress = 0;
					limit = 100;
				}
			}

			if (optionalRecipe.isPresent()) {
				PressingRecipe recipe = optionalRecipe.get();

				if (recipe.matches(inputInventory, world)) {
					limit = recipe.getTime();

					double speed = Math.min(getMachineSpeed(), limit - progress);
					double consumed = recipe.getEnergy() * speed / limit;

					ItemStack output = recipe.getOutput().copy();

					boolean isEmpty = items.getFirst().isEmpty();
					boolean isEqual = ItemStack.areItemsEqual(items.getFirst(), output) && ItemStack.areTagsEqual(items.getFirst(), output);

					if (volume.hasStored(consumed)) {
						if ((isEmpty || isEqual) && items.getFirst().getCount() + output.getCount() <= items.getFirst().getMaxCount()) {
							volume.minus(consumed);

							if (progress + speed >= limit) {
								optionalRecipe = Optional.empty();

								items.getSecond().decrement(1);

								if (isEmpty) {
									items.setFirst(output);
								} else {
									items.getFirst().increment(output.getCount());
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
		});
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

	public static class Primitive extends PresserBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_PRESSER, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_PRESSER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitivePresserSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().primitivePresserEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends PresserBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_PRESSER, AstromineTechnologiesBlockEntityTypes.BASIC_PRESSER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicPresserSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().basicPresserEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends PresserBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_PRESSER, AstromineTechnologiesBlockEntityTypes.ADVANCED_PRESSER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedPresserSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().advancedPresserEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends PresserBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_PRESSER, AstromineTechnologiesBlockEntityTypes.ELITE_PRESSER);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().elitePresserSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().elitePresserEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
