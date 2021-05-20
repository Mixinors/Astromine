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

import com.github.mixinors.astromine.common.component.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.base.ItemComponent;
import com.github.mixinors.astromine.common.util.FuelUtils;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BucketItem;
import net.minecraft.nbt.CompoundTag;

import com.github.mixinors.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.EnergySizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class SolidGeneratorBlockEntity extends ComponentEnergyItemBlockEntity implements EnergySizeProvider, SpeedProvider {
	private double available = 0;
	private double progress = 0;
	private int limit = 100;

	public SolidGeneratorBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return ItemComponent.of(this, 1).withInsertPredicate((direction, stack, slot) -> {
			if (slot != 0) {
				return false;
			}
			
			if (!transfer.getItem(direction).canInsert()) {
				return false;
			}

			if (!StackUtils.test(stack, getItemComponent().getFirst())) {
				return false;
			}

			return FuelUtils.getBurnTime(stack.getItem()) != null;
		}).withExtractPredicate((direction, stack, slot) -> {
			return false;
		});
	}

	@Override
	public EnergyComponent createEnergyComponent() {
		return EnergyComponent.of(getEnergySize());
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
		
		if (!(world instanceof ServerWorld) || !tickRedstone())
			return;
		
		if (available > 0) {
			var produced = 5;

			for (var i = 0; i < 3 * getMachineSpeed(); i++) {
				if (progress <= limit) {
					if (energy.hasAvailable(produced)) {
						--available;
						++progress;
						energy.give(produced);

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
			var burnStack = items.getFirst();

			var value = FuelUtils.getBurnTime(burnStack.getItem());

			if (value != null) {
				var isFuel = !(burnStack.getItem() instanceof BucketItem) && value > 0;

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

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putDouble("Progress", progress);
		tag.putInt("Limit", limit);
		tag.putDouble("Available", available);
		
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		progress = tag.getDouble("Progress");
		limit = tag.getInt("Limit");
		available = tag.getDouble("Available");
		
		super.fromTag(state, tag);
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
	}
}
