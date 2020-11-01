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

package com.github.chainmailstudios.astromine.technologies.common.recipe;

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
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemComponentFromInventory;
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.utilities.IngredientUtilities;
import com.github.chainmailstudios.astromine.common.utilities.PacketUtilities;
import com.github.chainmailstudios.astromine.common.utilities.ParsingUtilities;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class TrituratingRecipe implements EnergyConsumingRecipe<Inventory> {
	final Identifier identifier;
	final Ingredient input;
	final ItemStack output;
	final double energy;
	final int time;

	public TrituratingRecipe(Identifier identifier, Ingredient input, ItemStack output, double energy, int time) {
		this.identifier = identifier;
		this.input = input;
		this.output = output;
		this.energy = energy;
		this.time = time;
	}

	public static boolean allows(World world, Inventory inventory) {
		return world.getRecipeManager().getAllOfType(TrituratingRecipe.Type.INSTANCE).values().stream().anyMatch(it -> {
			TrituratingRecipe recipe = ((TrituratingRecipe) it);

			return recipe.matches(inventory, world);
		});
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		return ItemComponentFromInventory.of(inventory).getContents().values().stream().anyMatch(input);
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		ItemComponent component = ItemComponentFromInventory.of(inventory);
		List<ItemStack> matching = Lists.newArrayList(component.getStacks(input));

		ItemStack stack = matching.isEmpty() ? ItemStack.EMPTY : matching.get(0);

		for (Map.Entry<Integer, ItemStack> entry : component.getContents().entrySet()) {
			if (entry.getValue() == stack && !stack.isEmpty()) {
				component.getStack(entry.getKey()).decrement(1);

				break;
			}
		}

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
		defaultedList.add(this.input);
		return defaultedList;
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_TRITURATOR);
	}

	public int getTime() {
		return time;
	}

	public double getEnergy() {
		return energy;
	}

	public static final class Serializer implements RecipeSerializer<TrituratingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("triturating");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public TrituratingRecipe read(Identifier identifier, JsonObject object) {
			TrituratingRecipe.Format format = new Gson().fromJson(object, TrituratingRecipe.Format.class);

			return new TrituratingRecipe(identifier, IngredientUtilities.fromIngredientJson(format.input), StackUtilities.fromJson(format.output), EnergyUtilities.fromJson(format.energy), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public TrituratingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new TrituratingRecipe(identifier, IngredientUtilities.fromIngredientPacket(buffer), StackUtilities.fromPacket(buffer), EnergyUtilities.fromPacket(buffer), PacketUtilities.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, TrituratingRecipe recipe) {
			IngredientUtilities.toIngredientPacket(buffer, recipe.input);
			StackUtilities.toPacket(buffer, recipe.output);
			EnergyUtilities.toPacket(buffer, recipe.energy);
			PacketUtilities.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AstromineRecipeType<TrituratingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		JsonObject input;
		JsonObject output;
		@SerializedName("time")
		JsonPrimitive time;
		JsonElement energy;

		@Override
		public String toString() {
			return "Format{" + "input=" + input + ", output=" + output + ", time=" + time + ", energy=" + energy + '}';
		}
	}
}
