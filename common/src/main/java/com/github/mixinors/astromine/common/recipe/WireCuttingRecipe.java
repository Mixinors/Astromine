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
import com.github.mixinors.astromine.common.recipe.type.HiddenRecipeType;
import com.github.mixinors.astromine.common.util.IngredientUtils;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.shedaniel.architectury.core.AbstractRecipeSerializer;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Random;

public class WireCuttingRecipe extends SpecialCraftingRecipe {
	private static final Random RANDOM = new Random();
	private final Ingredient input;
	private final Ingredient tool;
	private final ItemStack output;

	public WireCuttingRecipe(Identifier id, Ingredient input, Ingredient tool, ItemStack output) {
		super(id);
		this.input = input;
		this.tool = tool;
		this.output = output;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		int inputCount = 0;
		int shearsCount = 0;

		for (var k = 0; k < inv.size(); ++k) {
			ItemStack itemStack = inv.getStack(k);
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
	public ItemStack craft(CraftingInventory inv) {
		return this.output.copy();
	}
	
	public Ingredient getInput() {
		return input;
	}

	public Ingredient getTool() {
		return tool;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public DefaultedList<ItemStack> getRemainingStacks(CraftingInventory inv) {
		var remainingStacks = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);

		for (var i = 0; i < remainingStacks.size(); ++i) {
			var itemStack = inv.getStack(i);
			
			if (itemStack.getItem().hasRecipeRemainder()) {
				remainingStacks.set(i, new ItemStack(itemStack.getItem().getRecipeRemainder()));
			} else if (tool.test(itemStack)) {
				var remainingTool = itemStack.copy();
				remainingTool.setCount(1);
				
				if (!remainingTool.damage(1, RANDOM, null)) {
					remainingStacks.set(i, remainingTool);
				}
				
				break;
			}
		}

		return remainingStacks;
	}

	public static final class Serializer extends AbstractRecipeSerializer<WireCuttingRecipe> {
		public static final Identifier ID = AMCommon.id("wire_cutting");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public WireCuttingRecipe read(Identifier identifier, JsonObject object) {
			WireCuttingRecipe.Format format = new Gson().fromJson(object, WireCuttingRecipe.Format.class);

			return new WireCuttingRecipe(
					identifier,
					IngredientUtils.fromIngredientJson(format.input),
					IngredientUtils.fromIngredientJson(format.tool),
					StackUtils.fromJson(format.output));
		}

		@Override
		public WireCuttingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new WireCuttingRecipe(
					identifier,
					IngredientUtils.fromIngredientPacket(buffer),
					IngredientUtils.fromIngredientPacket(buffer),
					StackUtils.fromPacket(buffer));
		}

		@Override
		public void write(PacketByteBuf buffer, WireCuttingRecipe recipe) {
			IngredientUtils.toIngredientPacket(buffer, recipe.input);
			IngredientUtils.toIngredientPacket(buffer, recipe.tool);
			StackUtils.toPacket(buffer, recipe.output);
		}
	}

	public static final class Type implements HiddenRecipeType<WireCuttingRecipe> {
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
