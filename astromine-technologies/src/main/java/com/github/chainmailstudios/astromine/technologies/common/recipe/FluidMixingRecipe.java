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
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyComponent;
import com.github.chainmailstudios.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.chainmailstudios.astromine.common.utilities.*;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Optional;

public final class FluidMixingRecipe implements Recipe<Inventory>, EnergyConsumingRecipe<Inventory> {
	private final Identifier identifier;
	private final FluidIngredient firstInput;
	private final FluidIngredient secondInput;
	private final FluidVolume firstOutput;
	private final double energyInput;
	private final int time;

	public FluidMixingRecipe(Identifier identifier, FluidIngredient firstInput, FluidIngredient secondIngredient, FluidVolume firstOutput, double energyInput, int time) {
		this.identifier = identifier;
		this.firstInput = firstInput;
		this.secondInput = secondIngredient;
		this.firstOutput = firstOutput;
		this.energyInput = energyInput;
		this.time = time;
	}

	public static boolean allows(World world, FluidComponent fluidComponent) {
		return world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().anyMatch(it -> {
			FluidMixingRecipe recipe = ((FluidMixingRecipe) it);

			return recipe.allows(fluidComponent);
		});
	}

	public static Optional<FluidMixingRecipe> matching(World world, FluidComponent fluidComponent, EnergyComponent energyComponent) {
		return (Optional<FluidMixingRecipe>) (Object) world.getRecipeManager().getAllOfType(FluidMixingRecipe.Type.INSTANCE).values().stream().filter(it -> {
			FluidMixingRecipe recipe = ((FluidMixingRecipe) it);

			return recipe.matches(fluidComponent, energyComponent);
		}).findFirst();
	}

	public boolean matches(FluidComponent fluidComponent, EnergyComponent energyComponent) {
		if (fluidComponent.getSize() < 3) {
			return false;
		}

		if (energyComponent.getAmount() < energyInput) {
			return false;
		}

		if (!firstInput.test(fluidComponent.getFirst()) && !secondInput.test(fluidComponent.getFirst())) {
			return false;
		}

		if (!secondInput.test(fluidComponent.getSecond()) && !firstInput.test(fluidComponent.getSecond())) {
			return false;
		}

		return firstOutput.test(fluidComponent.getThird());
	}

	public boolean allows(FluidComponent fluidComponent) {
		if (fluidComponent.getSize() < 2) {
			return false;
		}

		if (!firstInput.testWeak(fluidComponent.getFirst()) && !secondInput.testWeak(fluidComponent.getFirst())) {
			return false;
		}

		return secondInput.testWeak(fluidComponent.getSecond()) || firstInput.testWeak(fluidComponent.getSecond());
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
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER);
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public FluidIngredient getFirstInput() {
		return firstInput;
	}

	public FluidIngredient getSecondInput() {
		return secondInput;
	}

	public FluidVolume getFirstOutput() {
		return firstOutput.copy();
	}

	public int getTime() {
		return time;
	}

	@Override
	public double getEnergyInput() {
		return energyInput;
	}

	public static final class Serializer implements RecipeSerializer<FluidMixingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("fluid_mixing");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public FluidMixingRecipe read(Identifier identifier, JsonObject object) {
			FluidMixingRecipe.Format format = new Gson().fromJson(object, FluidMixingRecipe.Format.class);

			return new FluidMixingRecipe(
					identifier,
					FluidIngredient.fromJson(format.firstInput),
					FluidIngredient.fromJson(format.secondInput),
					FluidVolume.fromJson(format.firstOutput),
					DoubleUtilities.fromJson(format.energyInput),
					IntegerUtilities.fromJson(format.time)
			);
		}

		@Override
		public FluidMixingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new FluidMixingRecipe(
					identifier,
					FluidIngredient.fromPacket(buffer),
					FluidIngredient.fromPacket(buffer),
					FluidVolume.fromPacket(buffer),
					DoubleUtilities.fromPacket(buffer),
					IntegerUtilities.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, FluidMixingRecipe recipe) {
			recipe.firstInput.toPacket(buffer);
			recipe.secondInput.toPacket(buffer);
			recipe.firstOutput.toPacket(buffer);
			DoubleUtilities.toPacket(buffer, recipe.energyInput);
			IntegerUtilities.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AstromineRecipeType<FluidMixingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		@SerializedName("first_input")
		JsonElement firstInput;

		@SerializedName("second_input")
		JsonElement secondInput;

		@SerializedName("output")
		JsonElement firstOutput;

		@SerializedName("energy_input")
		JsonElement energyInput;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" +
					"firstInput=" + firstInput +
					", secondInput=" + secondInput +
					", firstOutput=" + firstOutput +
					", energyInput=" + energyInput +
					", time=" + time +
					'}';
		}
	}
}
