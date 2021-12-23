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
import dev.architectury.core.AbstractRecipeSerializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
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

public final class SolidifyingRecipe implements EnergyConsumingRecipe<Inventory> {
	private final Identifier identifier;
	private final FluidIngredient input;
	private final ItemStack output;
	private final double energyInput;
	private final int time;

	private static final Map<World, SolidifyingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public SolidifyingRecipe(Identifier identifier, FluidIngredient input, ItemStack output, double energyInput, int time) {
		this.identifier = identifier;
		this.input = input;
		this.output = output;
		this.energyInput = energyInput;
		this.time = time;
	}

	public static boolean allows(World world, FluidComponent fluidComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (SolidifyingRecipe) it).toArray(SolidifyingRecipe[]::new));
		}

		for (SolidifyingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(fluidComponent)) {
				return true;
			}
		}

		return false;
	}

	public static Optional<SolidifyingRecipe> matching(World world, ItemComponent itemComponent, FluidComponent fluidComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (SolidifyingRecipe) it).toArray(SolidifyingRecipe[]::new));
		}

		for (SolidifyingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.matches(itemComponent, fluidComponent)) {
				return Optional.of(recipe);
			}
		}

		return Optional.empty();
	}

	public boolean matches(ItemComponent itemComponent, FluidComponent fluidComponent) {
		if (fluidComponent.getSize() < 1) {
			return false;
		}

		if (itemComponent.getSize() < 1) {
			return false;
		}

		if (!input.test(fluidComponent.getFirst())) {
			return false;
		}

		return StackUtils.test(output, itemComponent.getFirst()) ;
	}

	public boolean allows(FluidComponent fluidComponent) {
		if (fluidComponent.getSize() < 1) {
			return false;
		}

		return input.testWeak(fluidComponent.getFirst());
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
		return output.copy();
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
	public DefaultedList<Ingredient> getIngredients() {
		return DefaultedList.of();
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(AMBlocks.ADVANCED_SOLIDIFIER.get());
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public FluidIngredient getInput() {
		return input;
	}

	public ItemStack getItemOutput() {
		return output;
	}

	@Override
	public double getEnergyInput() {
		return energyInput;
	}

	public int getTime() {
		return time;
	}

	public static final class Serializer extends AbstractRecipeSerializer<SolidifyingRecipe>
	{
		public static final Identifier ID = AMCommon.id("solidifying");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public SolidifyingRecipe read(Identifier identifier, JsonObject object) {
			SolidifyingRecipe.Format format = new Gson().fromJson(object, SolidifyingRecipe.Format.class);

			return new SolidifyingRecipe(
					identifier,
					FluidIngredient.fromJson(format.input),
					StackUtils.fromJson(format.output),
					DoubleUtils.fromJson(format.energyInput),
					IntegerUtils.fromJson(format.time)
			);
		}

		@Override
		public SolidifyingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new SolidifyingRecipe(
					identifier,
					FluidIngredient.fromPacket(buffer),
					StackUtils.fromPacket(buffer),
					DoubleUtils.fromPacket(buffer),
					IntegerUtils.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, SolidifyingRecipe recipe) {
			recipe.input.toPacket(buffer);
			StackUtils.toPacket(buffer, recipe.getItemOutput());
			DoubleUtils.toPacket(buffer, recipe.energyInput);
			IntegerUtils.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AMRecipeType<SolidifyingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		JsonElement input;

		JsonElement output;

		@SerializedName("energy_input")
		JsonElement energyInput;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" +
					"input=" + input +
					", output=" + output +
					", energyInput=" + energyInput +
					", time=" + time +
					'}';
		}
	}
}
