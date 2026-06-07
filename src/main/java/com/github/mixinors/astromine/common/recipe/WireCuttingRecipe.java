/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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
import com.github.mixinors.astromine.common.util.IngredientUtils;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import com.github.mixinors.astromine.common.recipe.base.AstromineRecipeSerializer;
import net.minecraft.world.level.Level;

public class WireCuttingRecipe extends CustomRecipe {
	private final Ingredient input;
	private final Ingredient tool;
	private final ItemStack output;
	
	public WireCuttingRecipe(ResourceLocation id, Ingredient input, Ingredient tool, ItemStack output) {
		super(CraftingBookCategory.MISC);
		
		this.input = input;
		this.tool = tool;
		this.output = output;
	}
	
	@Override
	public boolean matches(CraftingInput inv, Level world) {
		var inputCount = 0;
		var shearsCount = 0;
		
		for (var k = 0; k < inv.size(); ++k) {
			var itemStack = inv.getItem(k);
			
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
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
		return this.output.copy();
	}
	
	public Ingredient getInput() {
		return input;
	}
	
	public Ingredient getTool() {
		return tool;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
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
	public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
		var remainingStacks = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
		
		for (var i = 0; i < remainingStacks.size(); ++i) {
			var itemStack = inv.getItem(i);
			
			if (itemStack.getItem().hasCraftingRemainingItem()) {
				remainingStacks.set(i, new ItemStack(itemStack.getItem().getCraftingRemainingItem()));
			} else if (tool.test(itemStack)) {
				var remainingTool = itemStack.copy();
				remainingTool.setCount(1);
				
				remainingStacks.set(i, remainingTool);
				
				break;
			}
		}
		
		return remainingStacks;
	}
	
	public static final class Serializer implements AstromineRecipeSerializer<WireCuttingRecipe> {
		public static final ResourceLocation ID = AMCommon.id("wire_cutting");
		
		public static final Serializer INSTANCE = new Serializer();
		
		private Serializer() {}
		
		@Override
		public WireCuttingRecipe fromJson(ResourceLocation identifier, JsonObject object) {
			var format = new Gson().fromJson(object, WireCuttingRecipe.Format.class);
			
			return new WireCuttingRecipe(identifier,
					IngredientUtils.fromIngredientJson(format.input),
					IngredientUtils.fromIngredientJson(format.tool),
					StackUtils.fromJson(format.output));
		}
		
		@Override
		public WireCuttingRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf buffer) {
			return new WireCuttingRecipe(identifier,
					IngredientUtils.fromIngredientPacket(buffer),
					IngredientUtils.fromIngredientPacket(buffer),
					StackUtils.fromPacket(buffer));
		}
		
		@Override
		public void write(FriendlyByteBuf buffer, WireCuttingRecipe recipe) {
			IngredientUtils.toIngredientPacket(buffer, recipe.input);
			IngredientUtils.toIngredientPacket(buffer, recipe.tool);
			StackUtils.toPacket(buffer, recipe.output);
		}
	}
	
	public static final class Type implements AMRecipeType<WireCuttingRecipe> {
		public static final Type INSTANCE = new Type();
		
		private Type() {}
	}
	
	public static final class Format {
		JsonObject input;
		
		JsonObject tool;
		
		JsonObject output;
	}
}
