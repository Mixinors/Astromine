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
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.ingredient.ItemIngredient;
import com.github.chainmailstudios.astromine.common.utilities.DoubleUtilities;
import com.github.chainmailstudios.astromine.common.utilities.IntegerUtilities;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import it.unimi.dsi.fastutil.ints.Int2BooleanArrayMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.Optional;

public final class AlloySmeltingRecipe implements EnergyConsumingRecipe<Inventory> {
	private final Identifier identifier;
	private final ItemIngredient firstInput;
	private final ItemIngredient secondInput;
	private final ItemStack firstOutput;
	private final double energyInput;
	private final int time;

	private final Int2BooleanArrayMap cache = new Int2BooleanArrayMap();

	public AlloySmeltingRecipe(Identifier identifier, ItemIngredient firstInput, ItemIngredient secondInput, ItemStack firstOutput, double energyInput, int time) {
		this.identifier = identifier;
		this.firstInput = firstInput;
		this.secondInput = secondInput;
		this.firstOutput = firstOutput;
		this.energyInput = energyInput;
		this.time = time;
	}


	public static boolean allows(World world, ItemComponent itemComponent) {
		return world.getRecipeManager().getAllOfType(AlloySmeltingRecipe.Type.INSTANCE).values().stream().anyMatch(it -> {
			AlloySmeltingRecipe recipe = ((AlloySmeltingRecipe) it);

			return recipe.allows(itemComponent);
		});
	}

	public static Optional<AlloySmeltingRecipe> matching(World world, ItemComponent itemComponent, EnergyComponent energyComponent) {
		return (Optional<AlloySmeltingRecipe>) (Object) world.getRecipeManager().getAllOfType(AlloySmeltingRecipe.Type.INSTANCE).values().stream().filter(it -> {
			AlloySmeltingRecipe recipe = ((AlloySmeltingRecipe) it);

			return recipe.matches(itemComponent, energyComponent);
		}).findFirst();
	}

	public boolean matches(ItemComponent itemComponent, EnergyComponent energyComponent) {
		if (itemComponent.getSize() < 3) {
			return false;
		}

		if (energyComponent.getAmount() < energyInput) {
			return false;
		}

		if (!firstInput.test(itemComponent.getFirst()) && !secondInput.test(itemComponent.getFirst())) {
			return false;
		}

		if (!firstInput.test(itemComponent.getSecond()) && !secondInput.test(itemComponent.getSecond())) {
			return false;
		}

		return StackUtilities.test(firstOutput, itemComponent.getThird());
	}

	public boolean allows(ItemComponent itemComponent) {
		if (itemComponent.getSize() < 2) {
			return false;
		}

		if (!firstInput.testWeak(itemComponent.getFirst()) && !secondInput.testWeak(itemComponent.getFirst())) {
			return false;
		}

		return firstInput.testWeak(itemComponent.getSecond()) || secondInput.testWeak(itemComponent.getSecond());
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
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
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
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_ALLOY_SMELTER);
	}

	public Int2BooleanArrayMap getCache() {
		return cache;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public ItemIngredient getFirstInput() {
		return firstInput;
	}

	public ItemIngredient getSecondInput() {
		return secondInput;
	}

	public ItemStack getFirstOutput() {
		return firstOutput.copy();
	}
	
	public int getTime() {
		return time;
	}

	public double getEnergyInput() {
		return energyInput;
	}

	public static final class Serializer implements RecipeSerializer<AlloySmeltingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("alloy_smelting");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public AlloySmeltingRecipe read(Identifier identifier, JsonObject object) {
			AlloySmeltingRecipe.Format format = new Gson().fromJson(object, AlloySmeltingRecipe.Format.class);

			return new AlloySmeltingRecipe(
					identifier,
					ItemIngredient.fromJson(format.firstInput),
					ItemIngredient.fromJson(format.secondInput),
					StackUtilities.fromJson(format.output.getAsJsonObject()),
					DoubleUtilities.fromJson(format.energyInput),
					IntegerUtilities.fromJson(format.time)
			);
		}

		@Override
		public AlloySmeltingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new AlloySmeltingRecipe(
					identifier,
					ItemIngredient.fromPacket(buffer),
					ItemIngredient.fromPacket(buffer),
					StackUtilities.fromPacket(buffer),
					DoubleUtilities.fromPacket(buffer),
					IntegerUtilities.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, AlloySmeltingRecipe recipe) {
			recipe.firstInput.toPacket(buffer);
			recipe.secondInput.toPacket(buffer);
			StackUtilities.toPacket(buffer, recipe.firstOutput);
			DoubleUtilities.toPacket(buffer, recipe.energyInput);
			IntegerUtilities.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AstromineRecipeType<AlloySmeltingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		@SerializedName("first_input")
		JsonElement firstInput;

		@SerializedName("second_input")
		JsonElement secondInput;

		JsonElement output;

		@SerializedName("energy_input")
		JsonElement energyInput;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" +
					"firstInput=" + firstInput +
					", secondInput=" + secondInput +
					", output=" + output +
					", energyInput=" + energyInput +
					", time=" + time +
					'}';
		}
	}
}
