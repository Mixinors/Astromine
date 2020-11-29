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

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public final class RefiningRecipe implements Recipe<Container>, EnergyConsumingRecipe<Container> {
	private final ResourceLocation identifier;
	private final FluidIngredient firstInput;
	private final FluidVolume firstOutput;
	private final FluidVolume secondOutput;
	private final FluidVolume thirdOutput;
	private final FluidVolume fourthOutput;
	private final FluidVolume fifthOutput;
	private final FluidVolume sixthOutput;
	private final FluidVolume seventhOutput;
	private final double energyInput;
	private final int time;

	private static final Map<Level, RefiningRecipe[]> RECIPE_CACHE = new HashMap<>();

	public RefiningRecipe(ResourceLocation identifier, FluidIngredient firstInput, FluidVolume firstOutput, FluidVolume secondOutput, FluidVolume thirdOutput, FluidVolume fourthOutput, FluidVolume fifthOutput, FluidVolume sixthOutput, FluidVolume seventhOutput, double energyInput,
		int time) {
		this.identifier = identifier;
		this.firstInput = firstInput;
		this.firstOutput = firstOutput;
		this.secondOutput = secondOutput;
		this.thirdOutput = thirdOutput;
		this.fourthOutput = fourthOutput;
		this.fifthOutput = fifthOutput;
		this.sixthOutput = sixthOutput;
		this.seventhOutput = seventhOutput;
		this.energyInput = energyInput;
		this.time = time;
	}

	public static boolean allows(Level world, FluidComponent fluidComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().byType(Type.INSTANCE).values().stream().map(it -> (RefiningRecipe) it).toArray(RefiningRecipe[]::new));
		}

		for (RefiningRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(fluidComponent)) {
				return true;
			}
		}

		return false;
	}
	
	public static Optional<RefiningRecipe> matching(Level world, FluidComponent fluidComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().byType(Type.INSTANCE).values().stream().map(it -> (RefiningRecipe) it).toArray(RefiningRecipe[]::new));
		}

		for (RefiningRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(fluidComponent)) {
				return Optional.of(recipe);
			}
		}

		return Optional.empty();
	}

	public boolean matches(FluidComponent fluidComponent) {
		if (fluidComponent.getSize() < 8) {
			return false;
		}

		if (!firstInput.test(fluidComponent.getFirst())) {
			return false;
		}

		if (!firstOutput.test(fluidComponent.getSecond())) {
			return false;
		}

		if (!secondOutput.test(fluidComponent.getThird())) {
			return false;
		}

		if (!thirdOutput.test(fluidComponent.getFourth())) {
			return false;
		}

		if (!fourthOutput.test(fluidComponent.getFifth())) {
			return false;
		}

		if (!fifthOutput.test(fluidComponent.getSixth())) {
			return false;
		}

		if (!sixthOutput.test(fluidComponent.getSeventh())) {
			return false;
		}

		return seventhOutput.test(fluidComponent.getEighth());
	}

	public boolean allows(FluidComponent fluidComponent) {
		if (fluidComponent.getSize() < 1) {
			return false;
		}

		return firstInput.testWeak(fluidComponent.getFirst());
	}

	@Override
	public ResourceLocation getId() {
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
	public boolean matches(Container inventory, Level world) {
		return false;
	}

	@Override
	public ItemStack assemble(Container inventory) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER);
	}

	public ResourceLocation getIdentifier() {
		return identifier;
	}

	public FluidIngredient getIngredient() {
		return firstInput;
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

	public int getTime() {
		return time;
	}

	@Override
	public double getEnergyInput() {
		return energyInput;
	}

	public static final class Serializer implements RecipeSerializer<RefiningRecipe> {
		public static final ResourceLocation ID = AstromineCommon.identifier("refining");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public RefiningRecipe fromJson(ResourceLocation identifier, JsonObject object) {
			RefiningRecipe.Format format = new Gson().fromJson(object, RefiningRecipe.Format.class);

			return new RefiningRecipe(
					identifier,
					FluidIngredient.fromJson(format.firstInput),
					FluidVolume.fromJson(format.firstOutput),
					FluidVolume.fromJson(format.secondOutput),
					FluidVolume.fromJson(format.thirdOutput),
					FluidVolume.fromJson(format.fourthOutput),
					FluidVolume.fromJson(format.fifthOutput),
					FluidVolume.fromJson(format.sixthOutput),
					FluidVolume.fromJson(format.seventhOutput),
					DoubleUtilities.fromJson(format.energyInput),
					IntegerUtilities.fromJson(format.time)
			);
		}

		@Override
		public RefiningRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf buffer) {
			return new RefiningRecipe(
					identifier,
					FluidIngredient.fromPacket(buffer),
					FluidVolume.fromPacket(buffer),
					FluidVolume.fromPacket(buffer),
					FluidVolume.fromPacket(buffer),
					FluidVolume.fromPacket(buffer),
					FluidVolume.fromPacket(buffer),
					FluidVolume.fromPacket(buffer),
					FluidVolume.fromPacket(buffer),
					DoubleUtilities.fromPacket(buffer),
					IntegerUtilities.fromPacket(buffer)
			);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, RefiningRecipe recipe) {
			recipe.firstInput.toPacket(buffer);
			recipe.firstOutput.toPacket(buffer);
			recipe.secondOutput.toPacket(buffer);
			recipe.thirdOutput.toPacket(buffer);
			recipe.fourthOutput.toPacket(buffer);
			recipe.fifthOutput.toPacket(buffer);
			recipe.sixthOutput.toPacket(buffer);
			recipe.seventhOutput.toPacket(buffer);
			DoubleUtilities.toPacket(buffer, recipe.energyInput);
			IntegerUtilities.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AstromineRecipeType<RefiningRecipe> {
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

		@SerializedName("energy_input")
		JsonElement energyInput;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "firstInput=" + firstInput + ", firstOutput=" + firstOutput + ", secondOutput=" + secondOutput + ", thirdOutput=" + thirdOutput + ", fourthOutput=" + fourthOutput + ", fifthOutput=" + fifthOutput + ", sixthOutput=" + sixthOutput + ", seventhOutput=" +
				seventhOutput + ", energyInput=" + energyInput + ", time=" + time + '}';
		}
	}
}
