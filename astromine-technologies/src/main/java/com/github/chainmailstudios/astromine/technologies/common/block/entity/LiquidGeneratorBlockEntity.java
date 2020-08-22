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

import com.github.chainmailstudios.astromine.technologies.common.block.LiquidGeneratorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.base.BlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.recipe.LiquidGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class LiquidGeneratorBlockEntity extends ComponentEnergyFluidBlockEntity implements RecipeConsumer, Tickable {
	public double current = 0;
	public int limit = 100;

	public boolean isActive = false;

	public boolean[] activity = { false, false, false, false, false };

	private Optional<LiquidGeneratingRecipe> recipe = Optional.empty();

	private static final int INPUT_ENERGY_VOLUME = 0;
	private static final int INPUT_FLUID_VOLUME = 0;
	public boolean shouldTry = true;

	public LiquidGeneratorBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);

		fluidComponent.getVolume(INPUT_FLUID_VOLUME).setSize(getTankSize());
	}

	abstract Fraction getTankSize();

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		return new SimpleFluidInventoryComponent(1).withListener((inv) -> {
			shouldTry = true;
		});
	}

	@Override
	public double getCurrent() {
		return current;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setCurrent(double current) {
		this.current = current;
	}

	@Override
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public void increment() {
		this.current += 1 * ((LiquidGeneratorBlock) this.getCachedState().getBlock()).getMachineSpeed();
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		super.fromTag(state, tag);
		readRecipeProgress(tag);
		shouldTry = true;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		writeRecipeProgress(tag);
		return super.toTag(tag);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient())
			return;

		if (shouldTry) {
			if (!this.world.isClient() && (!recipe.isPresent() || !recipe.get().canCraft(this)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(LiquidGeneratingRecipe.Type.INSTANCE).values().stream().filter(recipe -> recipe instanceof LiquidGeneratingRecipe).filter(recipe -> ((LiquidGeneratingRecipe) recipe).canCraft(this)).findFirst();
			if (recipe.isPresent()) {
				recipe.get().tick(this);

				if (recipe.isPresent() && !recipe.get().canCraft(this)) {
					recipe = Optional.empty();
				}

				isActive = true;
			} else {
				isActive = false;
				shouldTry = false;
			}
		} else {
			isActive = false;
		}

		if (activity.length - 1 >= 0)
			System.arraycopy(activity, 1, activity, 0, activity.length - 1);

		activity[4] = isActive;

		if (isActive && !activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, true));
		} else if (!isActive && activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, false));
		}
	}

	public static class Primitive extends LiquidGeneratorBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlocks.PRIMITIVE_LIQUID_GENERATOR, AstromineTechnologiesBlockEntityTypes.PRIMITIVE_LIQUID_GENERATOR);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().primitiveLiquidGeneratorFluid, 1);
		}
	}

	public static class Basic extends LiquidGeneratorBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlocks.BASIC_LIQUID_GENERATOR, AstromineTechnologiesBlockEntityTypes.BASIC_LIQUID_GENERATOR);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().basicLiquidGeneratorFluid, 1);
		}
	}

	public static class Advanced extends LiquidGeneratorBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlocks.ADVANCED_LIQUID_GENERATOR, AstromineTechnologiesBlockEntityTypes.ADVANCED_LIQUID_GENERATOR);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().advancedLiquidGeneratorFluid, 1);
		}
	}

	public static class Elite extends LiquidGeneratorBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlocks.ELITE_LIQUID_GENERATOR, AstromineTechnologiesBlockEntityTypes.ELITE_LIQUID_GENERATOR);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().eliteLiquidGeneratorFluid, 1);
		}
	}
}
