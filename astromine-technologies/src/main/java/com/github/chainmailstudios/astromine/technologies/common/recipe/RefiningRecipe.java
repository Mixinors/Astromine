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

import com.github.chainmailstudios.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.chainmailstudios.astromine.common.utilities.*;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
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

public class RefiningRecipe implements Recipe<Inventory>, EnergyConsumingRecipe<Inventory> {
	final Identifier identifier;
	final FluidIngredient input;
	final FluidVolume firstOutput;
	final FluidVolume secondOutput;
	final FluidVolume thirdOutput;
	final FluidVolume fourthOutput;
	final FluidVolume fifthOutput;
	final FluidVolume sixthOutput;
	final FluidVolume seventhOutput;
	final EnergyVolume energy;
	final int time;

	public RefiningRecipe(Identifier identifier, FluidIngredient input, FluidVolume firstOutput, FluidVolume secondOutput, FluidVolume thirdOutput, FluidVolume fourthOutput, FluidVolume fifthOutput, FluidVolume sixthOutput, FluidVolume seventhOutput, EnergyVolume energy,
		int time) {
		this.identifier = identifier;
		this.input = input;
		this.firstOutput = firstOutput;
		this.secondOutput = secondOutput;
		this.thirdOutput = thirdOutput;
		this.fourthOutput = fourthOutput;
		this.fifthOutput = fifthOutput;
		this.sixthOutput = sixthOutput;
		this.seventhOutput = seventhOutput;
		this.energy = energy;
		this.time = time;
	}

	public static boolean allows(World world, Fluid inserting, Fluid existing) {
		return world.getRecipeManager().getAllOfType(RefiningRecipe.Type.INSTANCE).values().stream().anyMatch(it -> {
			RefiningRecipe recipe = ((RefiningRecipe) it);

			return (existing == inserting || existing == Fluids.EMPTY) && (recipe.input.test(inserting));
		});
	}

	public boolean matches(FluidComponent fluidComponent) {
		FluidVolume inputVolume = fluidComponent.getFirst();
		FluidVolume firstOutputVolume = fluidComponent.getSecond();
		FluidVolume secondOutputVolume = fluidComponent.getThird();
		FluidVolume thirdOutputVolume = fluidComponent.getFourth();
		FluidVolume fourthOutputVolume = fluidComponent.getFifth();
		FluidVolume fifthOutputVolume = fluidComponent.getSixth();
		FluidVolume sixthOutputVolume = fluidComponent.getSeventh();
		FluidVolume seventhOutputVolume = fluidComponent.getEighth();

		if (!input.test(inputVolume)) {
			return false;
		}

		if (!firstOutput.test(firstOutputVolume)) {
			return false;
		}

		if (!secondOutput.test(secondOutputVolume)) {
			return false;
		}

		if (!thirdOutput.test(thirdOutputVolume)) {
			return false;
		}

		if (!fourthOutput.test(fourthOutputVolume)) {
			return false;
		}

		if (!fifthOutput.test(fifthOutputVolume)) {
			return false;
		}

		if (!sixthOutput.test(sixthOutputVolume)) {
			return false;
		}

		return seventhOutput.test(seventhOutputVolume);
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
		return null;
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

	public FluidIngredient getIngredient() {
		return input;
	}

	public FluidVolume getFirstOutputVolume() {
		return firstOutput.copy();
	}

	public FluidVolume getSecondOutputVolume() {
		return secondOutput.copy();
	}

	public FluidVolume getThirdOutputVolume() {
		return thirdOutput.copy();
	}

	public FluidVolume getFourthOutputVolume() {
		return fourthOutput.copy();
	}

	public FluidVolume getFifthOutputVolume() {
		return fifthOutput.copy();
	}

	public FluidVolume getSixthOutputVolume() {
		return sixthOutput.copy();
	}

	public FluidVolume getSeventhOutputVolume() {
		return seventhOutput.copy();
	}

	public EnergyVolume getEnergyVolume() {
		return energy.copy();
	}

	public int getTime() {
		return time;
	}

	@Override
	public double getEnergy() {
		return energy.getAmount();
	}

	public static final class Serializer implements RecipeSerializer<RefiningRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("refining");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public RefiningRecipe read(Identifier identifier, JsonObject object) {
			RefiningRecipe.Format format = new Gson().fromJson(object, RefiningRecipe.Format.class);

			return new RefiningRecipe(identifier, IngredientUtilities.fromFluidIngredientJson(format.input), VolumeUtilities.fromFluidVolumeJson(format.firstOutput), VolumeUtilities.fromFluidVolumeJson(format.secondOutput), VolumeUtilities.fromFluidVolumeJson(format.thirdOutput),
				VolumeUtilities.fromFluidVolumeJson(format.fourthOutput), VolumeUtilities.fromFluidVolumeJson(format.fifthOutput), VolumeUtilities.fromFluidVolumeJson(format.sixthOutput), VolumeUtilities.fromFluidVolumeJson(format.seventhOutput), VolumeUtilities
					.fromEnergyVolumeJson(format.energy), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public RefiningRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new RefiningRecipe(identifier, IngredientUtilities.fromFluidIngredientPacket(buffer), VolumeUtilities.fromFluidVolumePacket(buffer), VolumeUtilities.fromFluidVolumePacket(buffer), VolumeUtilities.fromFluidVolumePacket(buffer), VolumeUtilities
				.fromFluidVolumePacket(buffer), VolumeUtilities.fromFluidVolumePacket(buffer), VolumeUtilities.fromFluidVolumePacket(buffer), VolumeUtilities.fromFluidVolumePacket(buffer), VolumeUtilities.fromEnergyVolumePacket(buffer), PacketUtilities.fromPacket(buffer,
					Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, RefiningRecipe recipe) {
			IngredientUtilities.toFluidIngredientPacket(buffer, recipe.getIngredient());
			VolumeUtilities.toFluidVolumePacket(buffer, recipe.getFirstOutputVolume());
			VolumeUtilities.toFluidVolumePacket(buffer, recipe.getSecondOutputVolume());
			VolumeUtilities.toFluidVolumePacket(buffer, recipe.getSecondOutputVolume());
			VolumeUtilities.toFluidVolumePacket(buffer, recipe.getSecondOutputVolume());
			VolumeUtilities.toFluidVolumePacket(buffer, recipe.getSecondOutputVolume());
			VolumeUtilities.toFluidVolumePacket(buffer, recipe.getSecondOutputVolume());
			VolumeUtilities.toFluidVolumePacket(buffer, recipe.getSecondOutputVolume());
			VolumeUtilities.toEnergyVolumePacket(buffer, recipe.getEnergyVolume());
			PacketUtilities.toPacket(buffer, recipe.getTime());
		}
	}

	public static final class Type implements AstromineRecipeType<RefiningRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		JsonElement input;

		@SerializedName("first_output")
		JsonElement firstOutput;

		@SerializedName("second_output")
		JsonElement secondOutput;

		@SerializedName("third_output")
		JsonElement thirdOutput;

		@SerializedName("fourth_output")
		JsonElement fourthOutput;

		@SerializedName("fifth_output")
		JsonElement fifthOutput;

		@SerializedName("sixth_output")
		JsonElement sixthOutput;

		@SerializedName("seventh_output")
		JsonElement seventhOutput;

		JsonElement energy;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "input=" + input + ", firstOutput=" + firstOutput + ", secondOutput=" + secondOutput + ", thirdOutput=" + thirdOutput + ", fourthOutput=" + fourthOutput + ", fifthOutput=" + fifthOutput + ", sixthOutput=" + sixthOutput + ", seventhOutput=" +
				seventhOutput + ", energy=" + energy + ", time=" + time + '}';
		}
	}
}
