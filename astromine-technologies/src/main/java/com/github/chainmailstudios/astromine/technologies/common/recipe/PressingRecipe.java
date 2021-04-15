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
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.ingredient.ItemIngredient;
import com.github.chainmailstudios.astromine.common.utilities.DoubleUtilities;
import com.github.chainmailstudios.astromine.common.utilities.IntegerUtilities;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class PressingRecipe implements EnergyConsumingRecipe<Inventory> {
	private final Identifier identifier;
	private final ItemIngredient firstInput;
	private final ItemStack firstOutput;
	private final double energyInput;
	private final int time;

	private static final Map<World, PressingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public PressingRecipe(Identifier identifier, ItemIngredient firstInput, ItemStack output, double energy, int time) {
		this.identifier = identifier;
		this.firstInput = firstInput;
		this.firstOutput = output;
		this.energyInput = energy;
		this.time = time;
	}

	public static boolean allows(World world, ItemComponent itemComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (PressingRecipe) it).toArray(PressingRecipe[]::new));
		}

		for (PressingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(itemComponent)) {
				return true;
			}
		}

		return false;
	}

	public static Optional<PressingRecipe> matching(World world, ItemComponent itemComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (PressingRecipe) it).toArray(PressingRecipe[]::new));
		}

		for (PressingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.matches(itemComponent)) {
				return Optional.of(recipe);
			}
		}

		return Optional.empty();
	}

	public boolean matches(ItemComponent itemComponent) {
		if (itemComponent.getSize() < 2) {
			return false;
		}

		if (!firstInput.test(itemComponent.getSecond())) {
			return false;
		}

		return StackUtilities.test(firstOutput, itemComponent.getFirst());
	}

	public boolean allows(ItemComponent itemComponent) {
		if (itemComponent.getSize() < 1) {
			return false;
		}

		return firstInput.testWeak(itemComponent.getSecond());
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
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_PRESSER);
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public ItemIngredient getFirstInput() {
		return firstInput;
	}

	public ItemStack getFirstOutput() {
		return firstOutput;
	}

	public int getTime() {
		return time;
	}

	@Override
	public double getEnergyInput() {
		return energyInput;
	}

	public static final class Serializer implements RecipeSerializer<PressingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("pressing");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public PressingRecipe read(Identifier identifier, JsonObject object) {
			PressingRecipe.Format format = new Gson().fromJson(object, PressingRecipe.Format.class);

			return new PressingRecipe(
					identifier,
					ItemIngredient.fromJson(format.firstInput),
					StackUtilities.fromJson(format.firstOutput),
					DoubleUtilities.fromJson(format.energyInput),
					IntegerUtilities.fromJson(format.time)
			);
		}

		@Override
		public PressingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new PressingRecipe(
					identifier,
					ItemIngredient.fromPacket(buffer),
					StackUtilities.fromPacket(buffer),
					DoubleUtilities.fromPacket(buffer),
					IntegerUtilities.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, PressingRecipe recipe) {
			recipe.firstInput.toPacket(buffer);
			StackUtilities.toPacket(buffer, recipe.firstOutput);
			DoubleUtilities.toPacket(buffer, recipe.energyInput);
			IntegerUtilities.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AstromineRecipeType<PressingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		@SerializedName("input")
		JsonElement firstInput;

		@SerializedName("output")
		JsonElement firstOutput;

		@SerializedName("energy_input")
		JsonElement energyInput;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" +
					"firstInput=" + firstInput +
					", firstOutput=" + firstOutput +
					", energyInput=" + energyInput +
					", time=" + time +
					'}';
		}
	}
}
