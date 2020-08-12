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

package com.github.chainmailstudios.astromine.common.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryComponentFromItemInventory;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.utilities.IngredientUtilities;
import com.github.chainmailstudios.astromine.common.utilities.PacketUtilities;
import com.github.chainmailstudios.astromine.common.utilities.ParsingUtilities;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;

public class AlloySmeltingRecipe implements EnergyConsumingRecipe<Inventory> {
	final Identifier identifier;
	final BetterIngredient firstInput;
	final BetterIngredient secondInput;
	final ItemStack output;
	final double energyConsumed;
	final int time;

	public AlloySmeltingRecipe(Identifier identifier, BetterIngredient firstInput, BetterIngredient secondInput, ItemStack output, double energyConsumed, int time) {
		this.identifier = identifier;
		this.firstInput = firstInput;
		this.secondInput = secondInput;
		this.output = output;
		this.energyConsumed = energyConsumed;
		this.time = time;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		ItemInventoryComponent component = ItemInventoryComponentFromItemInventory.of(inventory);
		if (component.getItemSize() < 2)
			return false;
		ItemStack stack1 = component.getStack(0);
		ItemStack stack2 = component.getStack(1);
		if (firstInput.test(stack1))
			return secondInput.test(stack2);
		if (firstInput.test(stack2))
			return secondInput.test(stack1);
		return false;
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return output.copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
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
	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.firstInput.asIngredient());
		defaultedList.add(this.secondInput.asIngredient());
		return defaultedList;
	}

	public BetterIngredient getFirstInput() {
		return firstInput;
	}

	public BetterIngredient getSecondInput() {
		return secondInput;
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineBlocks.ADVANCED_ALLOY_SMELTER);
	}

	public int getTime() {
		return time;
	}

	public double getEnergyConsumed() {
		return energyConsumed;
	}

	public static final class Serializer implements RecipeSerializer<AlloySmeltingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("alloy_smelting");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public AlloySmeltingRecipe read(Identifier identifier, JsonObject object) {
			AlloySmeltingRecipe.Format format = new Gson().fromJson(object, AlloySmeltingRecipe.Format.class);

			return new AlloySmeltingRecipe(identifier, IngredientUtilities.fromBetterJson(format.firstInput), IngredientUtilities.fromBetterJson(format.secondInput), StackUtilities.fromJson(format.output), EnergyUtilities.fromJson(format.energyConsumed), ParsingUtilities
				.fromJson(format.time, Integer.class));
		}

		@Override
		public AlloySmeltingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new AlloySmeltingRecipe(identifier, IngredientUtilities.fromBetterPacket(buffer), IngredientUtilities.fromBetterPacket(buffer), StackUtilities.fromPacket(buffer), EnergyUtilities.fromPacket(buffer), PacketUtilities.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, AlloySmeltingRecipe recipe) {
			IngredientUtilities.toBetterPacket(buffer, recipe.firstInput);
			IngredientUtilities.toBetterPacket(buffer, recipe.secondInput);
			StackUtilities.toPacket(buffer, recipe.output);
			EnergyUtilities.toPacket(buffer, recipe.energyConsumed);
			PacketUtilities.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AstromineRecipeType<AlloySmeltingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		JsonObject firstInput;
		JsonObject secondInput;
		JsonObject output;
		@SerializedName("time")
		JsonPrimitive time;
		@SerializedName("energy_consumed")
		JsonElement energyConsumed;

		@Override
		public String toString() {
			return "Format{" + "firstInput=" + firstInput + ", secondInput=" + secondInput + ", output=" + output + ", time=" + time + ", energyConsumed=" + energyConsumed + '}';
		}
	}
}
