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
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.chainmailstudios.astromine.common.utilities.DoubleUtilities;
import com.github.chainmailstudios.astromine.common.utilities.IntegerUtilities;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.Optional;

public final class ElectrolyzingRecipe implements Recipe<Inventory>, EnergyConsumingRecipe<Inventory> {
	private final Identifier identifier;
	private final FluidIngredient firstInput;
	private final FluidVolume firstOutput;
	private final FluidVolume secondOutput;
	private final double energyInput;
	private final int time;

	public ElectrolyzingRecipe(Identifier identifier, FluidIngredient firstInput, FluidVolume firstOutput, FluidVolume secondOutput, double energyInput, int time) {
		this.identifier = identifier;
		this.firstInput = firstInput;
		this.firstOutput = firstOutput;
		this.secondOutput = secondOutput;
		this.energyInput = energyInput;
		this.time = time;
	}

	public static boolean allows(World world, FluidComponent fluidComponent) {
		return world.getRecipeManager().getAllOfType(ElectrolyzingRecipe.Type.INSTANCE).values().stream().anyMatch(it -> {
			ElectrolyzingRecipe recipe = ((ElectrolyzingRecipe) it);

			return recipe.allows(fluidComponent);
		});
	}

	public static Optional<ElectrolyzingRecipe> matching(World world, FluidComponent fluidComponent, EnergyComponent energyComponent) {
		return (Optional<ElectrolyzingRecipe>) (Object) world.getRecipeManager().getAllOfType(ElectrolyzingRecipe.Type.INSTANCE).values().stream().filter(it -> {
			ElectrolyzingRecipe recipe = ((ElectrolyzingRecipe) it);

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

		if (!firstInput.test(fluidComponent.getFirst())) {
			return false;
		}

		if (!firstOutput.test(fluidComponent.getSecond())) {
			return false;
		}

		return secondOutput.test(fluidComponent.getThird());
	}

	public boolean allows(FluidComponent fluidComponent) {
		if (fluidComponent.getSize() < 1) {
			return false;
		}

		return firstInput.testWeak(fluidComponent.getFirst());
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
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER);
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public FluidIngredient getFirstInput() {
		return firstInput;
	}

	public FluidVolume getFirstOutput() {
		return firstOutput.copy();
	}

	public FluidVolume getSecondOutput() {
		return secondOutput.copy();
	}

	public int getTime() {
		return time;
	}

	@Override
	public double getEnergyInput() {
		return energyInput;
	}

	public static final class Serializer implements RecipeSerializer<ElectrolyzingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("electrolyzing");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public ElectrolyzingRecipe read(Identifier identifier, JsonObject object) {
			ElectrolyzingRecipe.Format format = new Gson().fromJson(object, ElectrolyzingRecipe.Format.class);

			return new ElectrolyzingRecipe(identifier, FluidIngredient.fromJson(format.firstInput), FluidVolume.fromJson(format.firstOutput), FluidVolume.fromJson(format.secondOutput), DoubleUtilities.fromJson(
				format.energyInput), IntegerUtilities.fromJson(format.time)
			);
		}

		@Override
		public ElectrolyzingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new ElectrolyzingRecipe(identifier, FluidIngredient.fromPacket(buffer), FluidVolume.fromPacket(buffer), FluidVolume.fromPacket(buffer), DoubleUtilities.fromPacket(buffer), IntegerUtilities.fromPacket(
				buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, ElectrolyzingRecipe recipe) {
			recipe.firstInput.toPacket(buffer);
			recipe.firstOutput.toPacket(buffer);
			recipe.secondOutput.toPacket(buffer);
			DoubleUtilities.toPacket(buffer, recipe.energyInput);
			IntegerUtilities.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AstromineRecipeType<ElectrolyzingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		@SerializedName("input")
		JsonElement firstInput;

		@SerializedName("first_output")
		JsonElement firstOutput;

		@SerializedName("second_output")
		JsonElement secondOutput;

		@SerializedName("energy_input")
		JsonElement energyInput;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "input=" + firstInput + ", firstOutput=" + firstOutput + ", secondOutput=" + secondOutput + ", energy=" + energyInput + ", time=" + time + '}';
		}
	}
}
