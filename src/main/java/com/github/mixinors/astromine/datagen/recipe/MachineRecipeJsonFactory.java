/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.base.input.EnergyInputRecipe;
import com.github.mixinors.astromine.common.recipe.ingredient.ItemIngredient;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import com.mojang.serialization.JsonOps;
import org.jetbrains.annotations.Nullable;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public abstract class MachineRecipeJsonFactory<T extends EnergyInputRecipe> implements RecipeBuilder {
	protected final int processingTime;
	
	protected final RecipeSerializer<T> serializer;
	
	protected MachineRecipeJsonFactory(int processingTime, RecipeSerializer<T> serializer) {
		this.processingTime = processingTime;
		
		this.serializer = serializer;
	}
	
	@Override
	public void save(RecipeOutput exporter) {
		this.save(exporter, getRecipeId());
	}
	
	@Override
	public void save(RecipeOutput exporter, String recipePath) {
		var defaultId = getRecipeId();
		
		var givenId = ResourceLocation.parse(recipePath);
		
		if (givenId.equals(defaultId)) {
			throw new IllegalStateException("Recipe " + recipePath + " should remove its 'recipePath' argument as it is equal to default one");
		} else {
			this.save(exporter, givenId);
		}
	}
	
	@Override
	public void save(RecipeOutput exporter, ResourceLocation recipeId) {
		exporter.accept(recipeId, createRecipe(recipeId), null);
	}
	
	protected abstract T createRecipe(ResourceLocation recipeId);
	
	public abstract String getName();
	
	@Override
	public Item getResult() {
		return Items.AIR;
	}
	
	public Fluid getOutputFluid() {
		return Fluids.EMPTY;
	}
	
	public ResourceLocation getOutputId() {
		return switch (getOutputType()) {
			case ITEM -> RecipeBuilder.getDefaultRecipeId(getResult());
			case FLUID -> getFluidId(getOutputFluid());
			case ENERGY -> AMCommon.id("energy");
		};
	}
	
	public ResourceLocation getRecipeId() {
		return ResourceLocation.parse(getOutputId() + "_from_" + getName());
	}
	
	@Override
	public RecipeBuilder unlockedBy(String name, Criterion<?> conditions) {
		// we don't use recipe advancements here!
		return this;
	}
	
	@Override
	public RecipeBuilder group(@Nullable String group) {
		// we don't use groups here!
		return this;
	}
	
	static ResourceLocation getFluidId(Fluid fluid) {
		return BuiltInRegistries.FLUID.getKey(fluid);
	}
	
	protected static ItemIngredient itemIngredient(Ingredient ingredient) {
		return itemIngredient(ingredient, 1);
	}
	
	protected static ItemIngredient itemIngredient(Ingredient ingredient, int amount) {
		var json = Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, ingredient).getOrThrow();
		var jsonObject = json.getAsJsonObject();
		
		if (jsonObject.has("item")) {
			var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(jsonObject.get("item").getAsString()));
			
			return new ItemIngredient(new ItemIngredient.StackEntry(new ItemStack(item), amount));
		}
		
		if (jsonObject.has("tag")) {
			var tag = AMTagKeys.createItemTag(ResourceLocation.parse(jsonObject.get("tag").getAsString()));
			
			return new ItemIngredient(new ItemIngredient.TagEntry(tag, amount));
		}
		
		return ItemIngredient.fromJson(json);
	}
	
	public abstract OutputType getOutputType();
	
	public static PressingRecipeJsonFactory createPressing(Ingredient input, ItemLike output, int outputCount, int processingTime, int energy) {
		return new PressingRecipeJsonFactory(input, output, outputCount, processingTime, energy);
	}
	
	public static TrituratingRecipeJsonFactory createTriturating(Ingredient input, ItemLike output, int outputCount, int processingTime, int energy) {
		return new TrituratingRecipeJsonFactory(input, output, outputCount, processingTime, energy);
	}
	
	public static WireMillingRecipeJsonFactory createWireMilling(Ingredient input, ItemLike output, int outputCount, int processingTime, int energy) {
		return new WireMillingRecipeJsonFactory(input, output, outputCount, processingTime, energy);
	}
	
	public static AlloySmeltingRecipeJsonFactory createAlloySmelting(Ingredient firstInput, int firstCount, Ingredient secondInput, int secondCount, ItemLike output, int outputCount, int processingTime, int energy) {
		return new AlloySmeltingRecipeJsonFactory(firstInput, firstCount, secondInput, secondCount, output, outputCount, processingTime, energy);
	}
	
	public static MeltingRecipeJsonFactory createMelting(Ingredient input, Fluid output, long outputAmount, int processingTime, int energy) {
		return new MeltingRecipeJsonFactory(input, output, outputAmount, processingTime, energy);
	}
	
	public enum OutputType {
		ITEM,
		FLUID,
		ENERGY
	}
}
