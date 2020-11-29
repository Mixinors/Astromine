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

package com.github.chainmailstudios.astromine.technologies.common.recipe;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.chainmailstudios.astromine.common.utilities.DoubleUtilities;
import com.github.chainmailstudios.astromine.common.utilities.IntegerUtilities;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public final class FluidGeneratingRecipe implements Recipe<Container>, EnergyGeneratingRecipe<Container> {
	private final ResourceLocation identifier;
	private final FluidIngredient firstInput;
	private final double energyOutput;
	private final int time;

	private static final Map<Level, FluidGeneratingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public FluidGeneratingRecipe(ResourceLocation identifier, FluidIngredient firstInput, double energyOutput, int time) {
		this.identifier = identifier;
		this.firstInput = firstInput;
		this.energyOutput = energyOutput;
		this.time = time;
	}

	public static boolean allows(Level world, FluidComponent fluidComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().byType(Type.INSTANCE).values().stream().map(it -> (FluidGeneratingRecipe) it).toArray(FluidGeneratingRecipe[]::new));
		}

		for (FluidGeneratingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(fluidComponent)) {
				return true;
			}
		}

		return false;
	}

	public static Optional<FluidGeneratingRecipe> matching(Level world, FluidComponent fluidComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().byType(Type.INSTANCE).values().stream().map(it -> (FluidGeneratingRecipe) it).toArray(FluidGeneratingRecipe[]::new));
		}

		for (FluidGeneratingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(fluidComponent)) {
				return Optional.of(recipe);
			}
		}

		return Optional.empty();
	}

	public boolean matches(FluidComponent fluidComponent) {
		if (fluidComponent.getSize() < 1) {
			return false;
		}

		return firstInput.test(fluidComponent.getFirst());
	}

	public boolean allows(FluidComponent fluidComponent) {
		if (fluidComponent.getSize() < 1) {
			return false;
		}

		return firstInput.testWeak(fluidComponent.getFirst());
	}

	@Override
	public boolean matches(Container inventory, Level world) {
		return false;
	}

	@Override
	public ItemStack assemble(Container inventory) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return identifier;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_FLUID_GENERATOR);
	}

	public FluidIngredient getFirstInput() {
		return firstInput;
	}

	public int getTime() {
		return time;
	}

	@Override
	public double getEnergyOutput() {
		return energyOutput;
	}

	public static final class Serializer implements RecipeSerializer<FluidGeneratingRecipe> {
		public static final ResourceLocation ID = AstromineCommon.identifier("fluid_generating");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public FluidGeneratingRecipe fromJson(ResourceLocation identifier, JsonObject object) {
			FluidGeneratingRecipe.Format format = new Gson().fromJson(object, FluidGeneratingRecipe.Format.class);

			return new FluidGeneratingRecipe(identifier, FluidIngredient.fromJson(format.firstInput), DoubleUtilities.fromJson(format.energyOutput), IntegerUtilities.fromJson(format.time)
			);
		}

		@Override
		public FluidGeneratingRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf buffer) {
			return new FluidGeneratingRecipe(
					identifier,
					FluidIngredient.fromPacket(buffer),
					DoubleUtilities.fromPacket(buffer),
					IntegerUtilities.fromPacket(buffer)
			);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, FluidGeneratingRecipe recipe) {
			recipe.firstInput.toPacket(buffer);
			DoubleUtilities.toPacket(buffer, recipe.energyOutput);
			IntegerUtilities.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AstromineRecipeType<FluidGeneratingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		@SerializedName("input")
		JsonElement firstInput;

		@SerializedName("energy_output")
		JsonElement energyOutput;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "firstInput=" + firstInput + ", energyOutput=" + energyOutput + ", time=" + time + '}';
		}
	}
}
