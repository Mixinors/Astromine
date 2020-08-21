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

package com.github.chainmailstudios.astromine.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.ElectrolyzerBlock;
import com.github.chainmailstudios.astromine.common.block.base.AbstractBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.AbstractEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.recipe.ElectrolyzingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ElectrolyzerBlockEntity extends AbstractEnergyFluidBlockEntity implements RecipeConsumer, Tickable {
	public double current = 0;
	public int limit = 100;

	public boolean isActive = false;

	public boolean[] activity = { false, false, false, false, false };

	private Optional<ElectrolyzingRecipe> recipe = Optional.empty();

	private static final int INPUT_FLUID_VOLUME = 0;
	private static final int FIRST_OUTPUT_FLUID_VOLUME = 1;
	private static final int SECOND_OUTPUT_FLUID_VOLUME = 2;

	public ElectrolyzerBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);

		fluidComponent.getVolume(INPUT_FLUID_VOLUME).setSize(getTankSize());
		fluidComponent.getVolume(FIRST_OUTPUT_FLUID_VOLUME).setSize(getTankSize());
		fluidComponent.getVolume(SECOND_OUTPUT_FLUID_VOLUME).setSize(getTankSize());

		addEnergyListener(fluidComponent::dispatchConsumers);
	}

	abstract Fraction getTankSize();

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		return new SimpleFluidInventoryComponent(3).withListener((inv) -> {
			if (this.world != null && !this.world.isClient() && (!recipe.isPresent() || !recipe.get().canCraft(this)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(ElectrolyzingRecipe.Type.INSTANCE).values().stream().filter(recipe -> recipe instanceof ElectrolyzingRecipe).filter(recipe -> ((ElectrolyzingRecipe) recipe).canCraft(this)).findFirst();
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
		current += ((ElectrolyzerBlock) this.getCachedState().getBlock()).getMachineSpeed();
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		readRecipeProgress(tag);
		super.fromTag(state, tag);
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

		if (recipe.isPresent()) {
			recipe.get().tick(this);

			if (recipe.isPresent() && !recipe.get().canCraft(this)) {
				recipe = Optional.empty();
			}

			isActive = true;
		} else {
			isActive = false;
		}

		if (activity.length - 1 >= 0)
			System.arraycopy(activity, 1, activity, 0, activity.length - 1);

		activity[4] = isActive;

		if (isActive && !activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(AbstractBlockWithEntity.ACTIVE, true));
		} else if (!isActive && activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(AbstractBlockWithEntity.ACTIVE, false));
		}
	}

	public static class Primitive extends ElectrolyzerBlockEntity {
		public Primitive() {
			super(AstromineBlocks.PRIMITIVE_ELECTROLYZER, AstromineBlockEntityTypes.PRIMITIVE_ELECTROLYZER);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().primitiveElectrolyzerFluid, 1);
		}
	}

	public static class Basic extends ElectrolyzerBlockEntity {
		public Basic() {
			super(AstromineBlocks.BASIC_ELECTROLYZER, AstromineBlockEntityTypes.BASIC_ELECTROLYZER);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().basicElectrolyzerFluid, 1);
		}
	}

	public static class Advanced extends ElectrolyzerBlockEntity {
		public Advanced() {
			super(AstromineBlocks.ADVANCED_ELECTROLYZER, AstromineBlockEntityTypes.ADVANCED_ELECTROLYZER);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().advancedElectrolyzerFluid, 1);
		}
	}

	public static class Elite extends ElectrolyzerBlockEntity {
		public Elite() {
			super(AstromineBlocks.ELITE_ELECTROLYZER, AstromineBlockEntityTypes.ELITE_ELECTROLYZER);
		}

		@Override
		Fraction getTankSize() {
			return Fraction.of(AstromineConfig.get().eliteElectrolyzerFluid, 1);
		}
	}
}
