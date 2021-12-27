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

package com.github.mixinors.astromine.datagen.recipe;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import dev.architectury.core.AbstractRecipeSerializer;
import org.jetbrains.annotations.Nullable;

import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.data.server.recipe.CraftingRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class MachineRecipeJsonFactory<T extends EnergyConsumingRecipe> implements CraftingRecipeJsonFactory {
	protected final int processingTime;
	protected final AbstractRecipeSerializer<T> serializer;

	protected MachineRecipeJsonFactory(int processingTime, AbstractRecipeSerializer<T> serializer) {
		this.processingTime = processingTime;
		this.serializer = serializer;
	}

	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter) {
		this.offerTo(exporter, getRecipeId());
	}

	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter, String recipePath) {
		var defaultId = getRecipeId();
		var givenId = new Identifier(recipePath);
		if (givenId.equals(defaultId)) {
			throw new IllegalStateException("Recipe " + recipePath + " should remove its 'recipePath' argument as it is equal to default one");
		} else {
			this.offerTo(exporter, givenId);
		}
	}

	public abstract String getName();

	@Override
	public Item getOutputItem() {
		return Items.AIR;
	}

	public Fluid getOutputFluid() {
		return Fluids.EMPTY;
	}

	public Identifier getOutputId() {
		return switch (getOutputType()) {
			case ITEM -> CraftingRecipeJsonFactory.getItemId(getOutputItem());
			case FLUID -> getFluidId(getOutputFluid());
			case ENERGY -> AMCommon.id("energy");
		};
	}

	public Identifier getRecipeId() {
		return new Identifier(getOutputId() + "_from_" + getName());
	}

	@Override
	public CraftingRecipeJsonFactory criterion(String name, CriterionConditions conditions) {
		// we don't use recipe advancements here!
		return this;
	}

	@Override
	public CraftingRecipeJsonFactory group(@Nullable String group) {
		// we don't use groups here!
		return this;
	}

	static Identifier getFluidId(Fluid fluid) {
		return Registry.FLUID.getId(fluid);
	}

	public abstract OutputType getOutputType();

	public static PressingRecipeJsonFactory createPressing(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new PressingRecipeJsonFactory(input, output, outputCount, processingTime, energy);
	}

	public static TrituratingRecipeJsonFactory createTriturating(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new TrituratingRecipeJsonFactory(input, output, outputCount, processingTime, energy);
	}

	public static WireMillingRecipeJsonFactory createWireMilling(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new WireMillingRecipeJsonFactory(input, output, outputCount, processingTime, energy);
	}

	public static AlloySmeltingRecipeJsonFactory createAlloySmelting(Ingredient firstInput, int firstCount, Ingredient secondInput, int secondCount, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new AlloySmeltingRecipeJsonFactory(firstInput, firstCount, secondInput, secondCount, output, outputCount, processingTime, energy);
	}

	public static MeltingRecipeJsonFactory createMelting(Ingredient input, Fluid output, int outputAmount, int processingTime, int energy) {
		return new MeltingRecipeJsonFactory(input, output, outputAmount, processingTime, energy);
	}

	public abstract static class MachineRecipeJsonProvider<T extends EnergyConsumingRecipe> implements RecipeJsonProvider {
		protected final Identifier recipeId;
		protected final int processingTime;
		protected final RecipeSerializer<T> serializer;

		public MachineRecipeJsonProvider(Identifier recipeId, int processingTime, RecipeSerializer<T> serializer) {
			this.recipeId = recipeId;
			this.processingTime = processingTime;
			this.serializer = serializer;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty("time", this.processingTime);
		}

		@Override
		public RecipeSerializer<?> getSerializer() {
			return this.serializer;
		}

		@Nullable
		@Override
		public JsonObject toAdvancementJson() {
			// we don't use recipe advancements here!
			return null;
		}

		@Nullable
		@Override
		public Identifier getAdvancementId() {
			// we don't use recipe advancements here!
			return null;
		}

		@Override
		public Identifier getRecipeId() {
			return this.recipeId;
		}
	}

	public enum OutputType {
		ITEM,
		FLUID,
		ENERGY
	}
}
