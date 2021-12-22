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

import com.github.mixinors.astromine.common.recipe.base.AMRecipeType;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.recipe.result.FluidResult;
import com.github.mixinors.astromine.common.util.LongUtils;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import dev.architectury.core.AbstractRecipeSerializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.mixinors.astromine.common.util.DoubleUtils;
import com.github.mixinors.astromine.common.util.IntegerUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ElectrolyzingRecipe implements Recipe<Inventory> {
	public final Identifier id;
	public final FluidIngredient input;
	public final FluidResult firstOutput;
	public final FluidResult secondOutput;
	public final long energyInput;
	public final int time;

	private static final Map<World, ElectrolyzingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public ElectrolyzingRecipe(Identifier id, FluidIngredient input, FluidResult firstOutput, FluidResult secondOutput, long energyInput, int time) {
		this.id = id;
		this.input = input;
		this.firstOutput = firstOutput;
		this.secondOutput = secondOutput;
		this.energyInput = energyInput;
		this.time = time;
	}

	public static boolean allows(World world, FluidVariant... variants) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (ElectrolyzingRecipe) it).toArray(ElectrolyzingRecipe[]::new));
		}

		for (ElectrolyzingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(variants)) {
				return true;
			}
		}

		return false;
	}

	public static Optional<ElectrolyzingRecipe> matching(World world, SingleSlotStorage<FluidVariant>... storages) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (ElectrolyzingRecipe) it).toArray(ElectrolyzingRecipe[]::new));
		}

		for (ElectrolyzingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.matches(storages)) {
				return Optional.of(recipe);
			}
		}

		return Optional.empty();
	}

	public boolean matches(SingleSlotStorage<FluidVariant>... variants) {
		var inputStorage = variants[0];
		
		var firstOutputStorage = variants[1];
		var secondOutputStorage = variants[2];

		if (!input.test(inputStorage)) {
			return false;
		}
		
		return firstOutput.equalsAndFitsIn(firstOutputStorage) && secondOutput.equalsAndFitsIn(secondOutputStorage)
			|| secondOutput.equalsAndFitsIn(firstOutputStorage) && firstOutput.equalsAndFitsIn(secondOutputStorage);
	}

	public boolean allows(FluidVariant... variants) {
		var inputVariant = variants[0];
		
		return input.test(inputVariant, Long.MAX_VALUE);
	}


	@Override
	public Identifier getId() {
		return id;
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
	public ItemStack createIcon() {
		return new ItemStack(AMBlocks.ADVANCED_ELECTROLYZER.get());
	}

	public static final class Serializer extends AbstractRecipeSerializer<ElectrolyzingRecipe>
	{
		public static final Identifier ID = AMCommon.id("electrolyzing");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public ElectrolyzingRecipe read(Identifier identifier, JsonObject object) {
			ElectrolyzingRecipe.Format format = new Gson().fromJson(object, ElectrolyzingRecipe.Format.class);

			return new ElectrolyzingRecipe(
					identifier,
					FluidIngredient.fromJson(format.input),
					FluidResult.fromJson(format.firstOutput),
					FluidResult.fromJson(format.secondOutput),
					LongUtils.fromJson(format.energyInput),
					IntegerUtils.fromJson(format.time)
			);
		}

		@Override
		public ElectrolyzingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new ElectrolyzingRecipe(
					identifier,
					FluidIngredient.fromPacket(buffer),
					FluidResult.fromPacket(buffer),
					FluidResult.fromPacket(buffer),
					LongUtils.fromPacket(buffer),
					IntegerUtils.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, ElectrolyzingRecipe recipe) {
			FluidIngredient.toPacket(buffer, recipe.input);
			FluidResult.toPacket(buffer, recipe.firstOutput);
			FluidResult.toPacket(buffer, recipe.secondOutput);
			LongUtils.toPacket(buffer, recipe.energyInput);
			IntegerUtils.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AMRecipeType<ElectrolyzingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		@SerializedName("input")
		JsonElement input;

		@SerializedName("first_output")
		JsonElement firstOutput;

		@SerializedName("second_output")
		JsonElement secondOutput;

		@SerializedName("energy_input")
		JsonElement energyInput;

		JsonElement time;
	}
}
