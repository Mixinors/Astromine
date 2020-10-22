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

import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.utilities.VolumeUtilities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentFluidItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.tier.MachineTier;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.FluidSizeProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.SpeedProvider;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.machine.TierProvider;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public abstract class TankBlockEntity extends ComponentFluidItemBlockEntity implements TierProvider, FluidSizeProvider, SpeedProvider {
	public TankBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	private Fluid filter = Fluids.EMPTY;

	@Override
	public FluidComponent createFluidComponent() {
		FluidComponent fluidComponent = SimpleFluidComponent.of(1).withInsertPredicate((direction, volume, slot) -> {
			return slot == 0 && (filter == Fluids.EMPTY || volume.getFluid() == filter);
		});

		fluidComponent.getFirst().setSize(getFluidSize());
		return fluidComponent;
	}

	@Override
	public ItemComponent createItemComponent() {
		return SimpleItemComponent.of(2).withInsertPredicate((direction, stack, slot) -> {
			return slot == 0;
		}).withExtractPredicate((direction, stack, slot) -> {
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

		if (world == null || world.isClient || !tickRedstone())
			return;

		VolumeUtilities.transferBetweenFirstAndSecond(getFluidComponent(), getItemComponent());
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putString("fluid", Registry.FLUID.getId(filter).toString());
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		Registry.FLUID.getOrEmpty(new Identifier(tag.getString("fluid"))).ifPresent(filter -> this.filter = filter);

		super.fromTag(state, tag);
	}

	public static class Primitive extends TankBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlockEntityTypes.PRIMITIVE_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().primitiveTankSpeed;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().primitiveTankFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.PRIMITIVE;
		}
	}

	public static class Basic extends TankBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlockEntityTypes.BASIC_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().basicTankSpeed;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().basicTankFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.BASIC;
		}
	}

	public static class Advanced extends TankBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlockEntityTypes.ADVANCED_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().advancedTankSpeed;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().advancedTankFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ADVANCED;
		}
	}

	public static class Elite extends TankBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlockEntityTypes.ELITE_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return AstromineConfig.get().eliteTankSpeed;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(AstromineConfig.get().eliteTankFluid, 1);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.ELITE;
		}
	}

	public static class Creative extends TankBlockEntity {
		public Creative() {
			super(AstromineTechnologiesBlockEntityTypes.CREATIVE_TANK);
		}

		@Override
		public double getMachineSpeed() {
			return Double.MAX_VALUE;
		}

		@Override
		public Fraction getFluidSize() {
			return Fraction.of(Long.MAX_VALUE);
		}

		@Override
		public MachineTier getMachineTier() {
			return MachineTier.CREATIVE;
		}

		@Override
		public void tick() {
			super.tick();

			getFluidComponent().getFirst().setAmount(Fraction.of(Long.MAX_VALUE));
			getFluidComponent().getFirst().setSize(Fraction.of(Long.MAX_VALUE));
		}
	}
}
