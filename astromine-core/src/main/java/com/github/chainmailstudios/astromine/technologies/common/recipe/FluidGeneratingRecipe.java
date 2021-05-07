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

package com.github.chainmailstudios.astromine.technologies.common.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

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

public final class FluidGeneratingRecipe implements Recipe<Inventory>, EnergyGeneratingRecipe<Inventory> {
	private final Identifier identifier;
	private final FluidIngredient firstInput;
	private final double energyOutput;
	private final int time;

	private static final Map<World, FluidGeneratingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public FluidGeneratingRecipe(Identifier identifier, FluidIngredient firstInput, double energyOutput, int time) {
		this.identifier = identifier;
		this.firstInput = firstInput;
		this.energyOutput = energyOutput;
		this.time = time;
	}

	public static boolean allows(World world, FluidComponent fluidComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (FluidGeneratingRecipe) it).toArray(FluidGeneratingRecipe[]::new));
		}

		for (FluidGeneratingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(fluidComponent)) {
				return true;
			}
		}

		return false;
	}

	public static Optional<FluidGeneratingRecipe> matching(World world, FluidComponent fluidComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (FluidGeneratingRecipe) it).toArray(FluidGeneratingRecipe[]::new));
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
	public boolean matches(Inventory inventory, World world) {
		return false;
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public Identifier getId() {
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
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_LIQUID_GENERATOR);
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
		public static final Identifier ID = AstromineCommon.identifier("fluid_generating");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public FluidGeneratingRecipe read(Identifier identifier, JsonObject object) {
			FluidGeneratingRecipe.Format format = new Gson().fromJson(object, FluidGeneratingRecipe.Format.class);

			return new FluidGeneratingRecipe(identifier, FluidIngredient.fromJson(format.firstInput), DoubleUtilities.fromJson(format.energyOutput), IntegerUtilities.fromJson(format.time)
			);
		}

		@Override
		public FluidGeneratingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new FluidGeneratingRecipe(
					identifier,
					FluidIngredient.fromPacket(buffer),
					DoubleUtilities.fromPacket(buffer),
					IntegerUtilities.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, FluidGeneratingRecipe recipe) {
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
