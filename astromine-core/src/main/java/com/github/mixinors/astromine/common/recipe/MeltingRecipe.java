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
import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.recipe.result.FluidResult;
import com.github.mixinors.astromine.common.util.LongUtils;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import dev.architectury.core.AbstractRecipeSerializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.github.mixinors.astromine.common.recipe.ingredient.ItemIngredient;
import com.github.mixinors.astromine.common.util.IntegerUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MeltingRecipe implements EnergyConsumingRecipe {
	public final Identifier id;
	public final ItemIngredient input;
	public final FluidResult output;
	public final long energyInput;
	public final int time;

	private static final Map<World, MeltingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public MeltingRecipe(Identifier id, ItemIngredient input, FluidResult output, long energyInput, int time) {
		this.id = id;
		this.input = input;
		this.output = output;
		this.energyInput = energyInput;
		this.time = time;
	}

	public static boolean allows(World world, ItemVariant... variants) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (MeltingRecipe) it).toArray(MeltingRecipe[]::new));
		}

		for (MeltingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(variants)) {
				return true;
			}
		}

		return false;
	}

	public static Optional<MeltingRecipe> matching(World world, SingleSlotStorage<ItemVariant>[] itemStorages, SingleSlotStorage<FluidVariant>[] fluidStorages) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (MeltingRecipe) it).toArray(MeltingRecipe[]::new));
		}

		for (MeltingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.matches(itemStorages, fluidStorages)) {
				return Optional.of(recipe);
			}
		}

		return Optional.empty();
	}

	public boolean matches(SingleSlotStorage<ItemVariant>[] itemStorages, SingleSlotStorage<FluidVariant>[] fluidStorages) {
		var itemInputStorage = itemStorages[0];
		
		var fluidOutputStorage = fluidStorages[0];
		
		if (!input.test(itemInputStorage)) {
			return false;
		}

		return output.equalsAndFitsIn(fluidOutputStorage);
	}

	public boolean allows(ItemVariant... variants) {
		var inputVariant = variants[0];
		
		return input.test(inputVariant, Long.MAX_VALUE);
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
	public ItemStack createIcon() {
		return new ItemStack(AMBlocks.ADVANCED_MELTER.get());
	}

	@Override
	public long getEnergyInput() {
		return energyInput;
	}

	@Override
	public int getTime() {
		return time;
	}

	public ItemIngredient getInput() {
		return input;
	}

	public FluidResult getFluidOutput() {
		return output;
	}
	
	public static final class Serializer extends AbstractRecipeSerializer<MeltingRecipe> {
		public static final Identifier ID = AMCommon.id("melting");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public MeltingRecipe read(Identifier identifier, JsonObject object) {
			MeltingRecipe.Format format = new Gson().fromJson(object, MeltingRecipe.Format.class);

			return new MeltingRecipe(
					identifier,
					ItemIngredient.fromJson(format.input),
					FluidResult.fromJson(format.output),
					LongUtils.fromJson(format.energyInput),
					IntegerUtils.fromJson(format.time)
			);
		}

		@Override
		public MeltingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new MeltingRecipe(
					identifier,
					ItemIngredient.fromPacket(buffer),
					FluidResult.fromPacket(buffer),
					LongUtils.fromPacket(buffer),
					IntegerUtils.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, MeltingRecipe recipe) {
			ItemIngredient.toPacket(buffer, recipe.input);
			FluidResult.toPacket(buffer, recipe.output);
			LongUtils.toPacket(buffer, recipe.energyInput);
			IntegerUtils.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AMRecipeType<MeltingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		@SerializedName("input")
		JsonElement input;

		@SerializedName("output")
		JsonElement output;

		@SerializedName("energy")
		JsonElement energyInput;

		JsonElement time;
	}
}
