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

import net.fabricmc.fabric.api.registry.FuelRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.EnergySizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.TierProvider;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import org.jetbrains.annotations.NotNull;

public abstract class SolidGeneratorBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public double available = 0;
	public double progress = 0;
	public int limit = 100;

	public SolidGeneratorBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return SimpleItemComponent.of(1).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 0) {
				return false;
			}

			return FuelRegistry.INSTANCE.get(stack.getItem()) != null;
		}).withExtractPredicate((direction, stack, slot) -> {
			return false;
		});
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return SimpleEnergyComponent.of(getEnergySize());
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;

		ItemComponent itemComponent = getItemComponent();

		if (itemComponent != null) {
			EnergyVolume energyVolume = getEnergyComponent().getVolume();

			if (available > 0) {
				double produced = 5;
				for (int i = 0; i < 3 * getMachineSpeed(); i++) {
					if (progress <= limit) {
						if (energyVolume.hasAvailable(produced)) {
							--available;
							++progress;
							energyVolume.add(produced * getMachineSpeed());

							tickActive();
						} else {
							tickInactive();
						}

					} else {
						progress = 0;
						limit = 0;

						tickInactive();
					}
				}
			} else {
				ItemStack burnStack = itemComponent.getFirst();

				Integer value = FuelRegistry.INSTANCE.get(burnStack.getItem());

				if (value != null) {
					boolean isFuel = !(burnStack.getItem() instanceof BucketItem) && value > 0;

					if (isFuel) {
						available = value;
						limit = value;

						burnStack.decrement(1);
					}

					if (isFuel || progress != 0) {
						tickActive();
					} else {
						tickInactive();
					}
				} else {
					tickInactive();
				}
			}
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("progress", progress);
		tag.putInt("limit", limit);
		tag.putDouble("available", available);
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		progress = tag.getDouble("progress");
		limit = tag.getInt("limit");
		available = tag.getDouble("available");
		super.fromTag(state, tag);
	}

	public static class Primitive extends SolidGeneratorBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_SOLID_GENERATOR, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_SOLID_GENERATOR);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveSolidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().primitiveSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends SolidGeneratorBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_SOLID_GENERATOR, AstromineTechnologiesBlockEntityTypes.BASIC_SOLID_GENERATOR);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicSolidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().basicSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends SolidGeneratorBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_SOLID_GENERATOR, AstromineTechnologiesBlockEntityTypes.ADVANCED_SOLID_GENERATOR);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedSolidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().advancedSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends SolidGeneratorBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_SOLID_GENERATOR, AstromineTechnologiesBlockEntityTypes.ELITE_SOLID_GENERATOR);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteSolidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AstromineConfig.get().eliteSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
