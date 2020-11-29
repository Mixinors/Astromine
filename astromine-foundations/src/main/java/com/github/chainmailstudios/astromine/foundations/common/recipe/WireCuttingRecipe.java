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

package com.github.chainmailstudios.astromine.foundations.common.recipe;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.utilities.IngredientUtilities;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Random;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class WireCuttingRecipe extends CustomRecipe {
	private static final Random RANDOM = new Random();
	private final Ingredient input;
	private final Ingredient tool;
	private final ItemStack output;

	public WireCuttingRecipe(ResourceLocation id, Ingredient input, Ingredient tool, ItemStack output) {
		super(id);
		this.input = input;
		this.tool = tool;
		this.output = output;
	}

	@Override
	public boolean matches(CraftingContainer inv, Level world) {
		int inputCount = 0;
		int shearsCount = 0;

		for (int k = 0; k < inv.getContainerSize(); ++k) {
			ItemStack itemStack = inv.getItem(k);
			if (!itemStack.isEmpty()) {
				if (this.input.test(itemStack)) {
					++inputCount;
				} else {
					if (!tool.test(itemStack)) {
						return false;
					}

					++shearsCount;
				}

				if (shearsCount > 1 || inputCount > 1) {
					return false;
				}
			}
		}

		return inputCount == 1 && shearsCount == 1;
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		return this.output.copy();
	}
	
	public Ingredient getInput() {
		return input;
	}

	public Ingredient getTool() {
		return tool;
	}

	@Override
	public ItemStack getResultItem() {
		return output;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> remainingStacks = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		for (int i = 0; i < remainingStacks.size(); ++i) {
			ItemStack itemStack = inv.getItem(i);
			if (itemStack.getItem().hasCraftingRemainingItem()) {
				remainingStacks.set(i, new ItemStack(itemStack.getItem().getCraftingRemainingItem()));
			} else if (tool.test(itemStack)) {
				ItemStack remainingTool = itemStack.copy();
				remainingTool.setCount(1);
				if (!remainingTool.hurt(1, RANDOM, null)) {
					remainingStacks.set(i, remainingTool);
				}
				break;
			}
		}

		return remainingStacks;
	}

	public static final class Serializer implements RecipeSerializer<WireCuttingRecipe> {
		public static final ResourceLocation ID = AstromineCommon.identifier("wire_cutting");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public WireCuttingRecipe fromJson(ResourceLocation identifier, JsonObject object) {
			WireCuttingRecipe.Format format = new Gson().fromJson(object, WireCuttingRecipe.Format.class);

			return new WireCuttingRecipe(identifier,
				IngredientUtilities.fromIngredientJson(format.input),
				IngredientUtilities.fromIngredientJson(format.tool),
				StackUtilities.fromJson(format.output));
		}

		@Override
		public WireCuttingRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf buffer) {
			return new WireCuttingRecipe(identifier,
				IngredientUtilities.fromIngredientPacket(buffer),
				IngredientUtilities.fromIngredientPacket(buffer),
				StackUtilities.fromPacket(buffer));
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, WireCuttingRecipe recipe) {
			IngredientUtilities.toIngredientPacket(buffer, recipe.input);
			IngredientUtilities.toIngredientPacket(buffer, recipe.tool);
			StackUtilities.toPacket(buffer, recipe.output);
		}
	}

	public static final class Type implements AstromineRecipeType<WireCuttingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		JsonObject input;
		JsonObject tool;
		JsonObject output;

		@Override
		public String toString() {
			return "Format{" + "inputs=" + input + ", tool=" + tool + ", output=" + output + '}';
		}
	}
}
