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

import com.github.mixinors.astromine.common.component.general.*;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.fabricmc.fabric.api.registry.FuelRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.common.util.tier.MachineTier;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import com.github.mixinors.astromine.common.block.entity.machine.TierProvider;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class SolidGeneratorBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, TierProvider, SpeedProvider {
	public double available = 0;
	public double progress = 0;
	public int limit = 100;

	public SolidGeneratorBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return SimpleDirectionalItemComponent.of(this, 1).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 0) {
				return false;
			}

			if (!StackUtils.test(stack, getItemComponent().getFirst())) {
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
	public IntSet getItemInputSlots() {
		return IntSets.singleton(0);
	}

	@Override
	public IntSet getItemOutputSlots() {
		return IntSets.EMPTY_SET;
	}

	@Override
	public void tick() {
		super.tick();

		if (world == null || world.isClient || !tickRedstone())
			return;

		ItemComponent itemComponent = getItemComponent();

		EnergyComponent energyComponent = getEnergyComponent();

		if (itemComponent != null && energyComponent != null) {
			EnergyVolume energyVolume = energyComponent.getVolume();

			if (available > 0) {
				double produced = 5;

				for (int i = 0; i < 3 * getMachineSpeed(); i++) {
					if (progress <= limit) {
						if (energyVolume.hasAvailable(produced)) {
							--available;
							++progress;
							energyVolume.give(produced);

							tickActive();
						} else {
							tickInactive();
						}
					}

					if (progress > limit || available <= 0) {
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
						progress = 0;

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
	public NbtCompound writeNbt(NbtCompound tag) {
		tag.putDouble("progress", progress);
		tag.putInt("limit", limit);
		tag.putDouble("available", available);
		return super.writeNbt(tag);
	}

	@Override
	public void readNbt(BlockState state, @NotNull NbtCompound tag) {
		progress = tag.getDouble("progress");
		limit = tag.getInt("limit");
		available = tag.getDouble("available");
		super.readNbt(state, tag);
	}

	public static class Primitive extends SolidGeneratorBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_SOLID_GENERATOR);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveSolidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().primitiveSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends SolidGeneratorBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_SOLID_GENERATOR);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicSolidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().basicSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends SolidGeneratorBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_SOLID_GENERATOR);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedSolidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().advancedSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends SolidGeneratorBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_SOLID_GENERATOR);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteSolidGeneratorSpeed;
		}

		@Override
		public double getEnergySize() {
			return AMConfig.get().eliteSolidGeneratorEnergy;
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}
}
