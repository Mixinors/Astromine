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

package com.github.mixinors.astromine.common.recipe;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.base.AMRecipeType;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import me.shedaniel.architectury.core.AbstractRecipeSerializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.mixinors.astromine.common.recipe.ingredient.ItemIngredient;
import com.github.mixinors.astromine.common.util.DoubleUtils;
import com.github.mixinors.astromine.common.util.IntegerUtils;
import com.github.mixinors.astromine.common.util.StackUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class AlloySmeltingRecipe implements EnergyConsumingRecipe<Inventory> {
	private final Identifier identifier;
	private final ItemIngredient firstInput;
	private final ItemIngredient secondInput;
	private final ItemStack firstOutput;
	private final double energyInput;
	private final int time;

	private static final Map<World, AlloySmeltingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public AlloySmeltingRecipe(Identifier identifier, ItemIngredient firstInput, ItemIngredient secondInput, ItemStack firstOutput, double energyInput, int time) {
		this.identifier = identifier;
		this.firstInput = firstInput;
		this.secondInput = secondInput;
		this.firstOutput = firstOutput;
		this.energyInput = energyInput;
		this.time = time;
	}


	public static boolean allows(World world, ItemComponent itemComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (AlloySmeltingRecipe) it).toArray(AlloySmeltingRecipe[]::new));
		}

		for (AlloySmeltingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(itemComponent)) {
				return true;
			}
		}

		return false;
	}

	public static Optional<AlloySmeltingRecipe> matching(World world, ItemComponent itemComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (AlloySmeltingRecipe) it).toArray(AlloySmeltingRecipe[]::new));
		}

		for (AlloySmeltingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.matches(itemComponent)) {
				return Optional.of(recipe);
			}
		}

		return Optional.empty();
	}

	public boolean matches(ItemComponent itemComponent) {
		if (itemComponent.getSize() < 3) {
			return false;
		}

		if (!firstInput.test(itemComponent.getFirst()) && !secondInput.test(itemComponent.getFirst())) {
			return false;
		}

		if (!firstInput.test(itemComponent.getSecond()) && !secondInput.test(itemComponent.getSecond())) {
			return false;
		}

		return StackUtils.test(firstOutput, itemComponent.getThird());
	}

	public boolean allows(ItemComponent itemComponent) {
		if (itemComponent.getSize() < 2) {
			return false;
		}

		if (!firstInput.testWeak(itemComponent.getFirst()) && !secondInput.testWeak(itemComponent.getFirst())) {
			return false;
		}

		return firstInput.testWeak(itemComponent.getSecond()) || secondInput.testWeak(itemComponent.getSecond());
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
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
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
		return new ItemStack(AMBlocks.ADVANCED_ALLOY_SMELTER.get());
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public ItemIngredient getFirstInput() {
		return firstInput;
	}

	public ItemIngredient getSecondInput() {
		return secondInput;
	}

	public ItemStack getFirstOutput() {
		return firstOutput.copy();
	}
	
	public int getTime() {
		return time;
	}

	public double getEnergyInput() {
		return energyInput;
	}

	public static final class Serializer extends AbstractRecipeSerializer<AlloySmeltingRecipe> {
		public static final Identifier ID = AMCommon.id("alloy_smelting");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public AlloySmeltingRecipe read(Identifier identifier, JsonObject object) {
			AlloySmeltingRecipe.Format format = new Gson().fromJson(object, AlloySmeltingRecipe.Format.class);

			return new AlloySmeltingRecipe(
					identifier,
					ItemIngredient.fromJson(format.firstInput),
					ItemIngredient.fromJson(format.secondInput),
					StackUtils.fromJson(format.output.getAsJsonObject()),
					DoubleUtils.fromJson(format.energyInput),
					IntegerUtils.fromJson(format.time)
			);
		}

		@Override
		public AlloySmeltingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new AlloySmeltingRecipe(
					identifier,
					ItemIngredient.fromPacket(buffer),
					ItemIngredient.fromPacket(buffer),
					StackUtils.fromPacket(buffer),
					DoubleUtils.fromPacket(buffer),
					IntegerUtils.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, AlloySmeltingRecipe recipe) {
			recipe.firstInput.toPacket(buffer);
			recipe.secondInput.toPacket(buffer);
			StackUtils.toPacket(buffer, recipe.firstOutput);
			DoubleUtils.toPacket(buffer, recipe.energyInput);
			IntegerUtils.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AMRecipeType<AlloySmeltingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		@SerializedName("first_input")
		JsonElement firstInput;

		@SerializedName("second_input")
		JsonElement secondInput;

		JsonElement output;

		@SerializedName("energy_input")
		JsonElement energyInput;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" +
					"firstInput=" + firstInput +
					", secondInput=" + secondInput +
					", output=" + output +
					", energyInput=" + energyInput +
					", time=" + time +
					'}';
		}
	}
}
