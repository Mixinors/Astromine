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

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
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
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.utilities.IngredientUtilities;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Optional;

public final class PressingRecipe implements EnergyConsumingRecipe<Inventory> {
	private final Identifier identifier;
	private final Ingredient firstInput;
	private final ItemStack firstOutput;
	private final double energyInput;
	private final int time;

	public PressingRecipe(Identifier identifier, Ingredient firstInput, ItemStack output, double energy, int time) {
		this.identifier = identifier;
		this.firstInput = firstInput;
		this.firstOutput = output;
		this.energyInput = energy;
		this.time = time;
	}

	public static boolean allows(World world, ItemComponent itemComponent) {
		return world.getRecipeManager().getAllOfType(PressingRecipe.Type.INSTANCE).values().stream().anyMatch(it -> {
			PressingRecipe recipe = ((PressingRecipe) it);

			return recipe.allows(itemComponent);
		});
	}

	public static Optional<PressingRecipe> matching(World world, ItemComponent itemComponent, EnergyComponent energyComponent) {
		return (Optional<PressingRecipe>) (Object) world.getRecipeManager().getAllOfType(PressingRecipe.Type.INSTANCE).values().stream().filter(it -> {
			PressingRecipe recipe = ((PressingRecipe) it);

			return recipe.matches(itemComponent, energyComponent);
		}).findFirst();
	}

	public boolean matches(ItemComponent itemComponent, EnergyComponent energyComponent) {
		if (itemComponent.getSize() < 2) {
			return false;
		}

		if (energyComponent.getAmount() < energyInput) {
			return false;
		}

		if (!firstInput.test(itemComponent.getFirst())) {
			return false;
		}

		return StackUtilities.test(firstOutput, itemComponent.getSecond());
	}

	public boolean allows(ItemComponent itemComponent) {
		if (itemComponent.getSize() < 1) {
			return false;
		}

		return firstInput.test(itemComponent.getFirst());
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
	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.firstInput);
		return defaultedList;
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_PRESSER);
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public Ingredient getFirstInput() {
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

			return new PressingRecipe(identifier, IngredientUtilities.fromIngredientJson(format.firstInput), StackUtilities.fromJson(format.firstOutput), EnergyUtilities.fromJson(format.energyInput), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public PressingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new PressingRecipe(identifier, IngredientUtilities.fromIngredientPacket(buffer), StackUtilities.fromPacket(buffer), EnergyUtilities.fromPacket(buffer), PacketUtilities.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, PressingRecipe recipe) {
			IngredientUtilities.toIngredientPacket(buffer, recipe.firstInput);
			StackUtilities.toPacket(buffer, recipe.firstOutput);
			EnergyUtilities.toPacket(buffer, recipe.energyInput);
			PacketUtilities.toPacket(buffer, recipe.time);
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
