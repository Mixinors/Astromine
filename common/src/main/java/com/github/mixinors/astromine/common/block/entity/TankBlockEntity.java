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

import com.github.mixinors.astromine.common.component.base.FluidComponent;
import com.github.mixinors.astromine.common.component.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.github.mixinors.astromine.common.block.entity.base.ComponentFluidItemBlockEntity;
import com.github.mixinors.astromine.common.util.VolumeUtils;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.common.block.entity.machine.FluidSizeProvider;
import com.github.mixinors.astromine.common.block.entity.machine.SpeedProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class TankBlockEntity extends ComponentFluidItemBlockEntity implements FluidSizeProvider, SpeedProvider {
	public TankBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	private Fluid filter = Fluids.EMPTY;

	@Override
	public FluidComponent createFluidComponent() {
		return FluidComponent.of(this, 1).withInsertPredicate((direction, volume, slot) -> {
			if (!transfer.getFluid(direction).canInsert()) {
				return false;
			}
			
			return slot == 0 && (filter == Fluids.EMPTY || volume.getFluid() == filter);
		}).withSizes(getFluidSize());
	}

	@Override
	public ItemComponent createItemComponent() {
		return ItemComponent.of(this, 2).withInsertPredicate((direction, stack, slot) -> {
			if (!transfer.getItem(direction).canInsert()) {
				return false;
			}
			
			return slot == 0;
		}).withExtractPredicate((direction, stack, slot) -> {
			if (!transfer.getFluid(direction).canExtract()) {
				return false;
			}
			
			return slot == 1;
		});
	}

	public Fluid getFilter() {
		return filter;
	}

	public void setFilter(Fluid filter) {
		this.filter = filter;
	}

	@Override
	public void tick() {
		super.tick();
		
		if (!(world instanceof ServerWorld) || !tickRedstone())
			return;

		VolumeUtils.transferBetween(getItemComponent(), getFluidComponent(), 0, 1, 0);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putString("Fluid", Registry.FLUID.getId(filter).toString());
		
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		Registry.FLUID.getOrEmpty(new Identifier(tag.getString("Fluid"))).ifPresent(filter -> this.filter = filter);

		super.fromTag(state, tag);
	}

	public static class Primitive extends TankBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().primitiveTankSpeed;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().primitiveTankFluid;
		}
	}

	public static class Basic extends TankBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().basicTankSpeed;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().basicTankFluid;
		}
	}

	public static class Advanced extends TankBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().advancedTankSpeed;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().advancedTankFluid;
		}
	}

	public static class Elite extends TankBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AMConfig.get().eliteTankSpeed;
		}

		@Override
		public long getFluidSize() {
			return AMConfig.get().eliteTankFluid;
		}
	}

	public static class Creative extends TankBlockEntity {
		public Creative() {
			super(AMBlockEntityTypes.CREATIVE_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return Double.MAX_VALUE;
		}

		@Override
		public long getFluidSize() {
			return Long.MAX_VALUE;
		}

		@Override
		public void tick() {
			super.tick();
			
			var first = fluids.getFirst();

			first.setAmount(Long.MAX_VALUE);
			first.setSize(Long.MAX_VALUE);
		}
	}
}
