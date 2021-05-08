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
import com.github.mixinors.astromine.registry.common.AMBlocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.util.DoubleUtils;
import com.github.mixinors.astromine.common.util.IntegerUtils;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class RefiningRecipe implements Recipe<Inventory>, EnergyConsumingRecipe<Inventory> {
	private final Identifier identifier;
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

	private static final Map<World, RefiningRecipe[]> RECIPE_CACHE = new HashMap<>();

	public RefiningRecipe(Identifier identifier, FluidIngredient firstInput, FluidVolume firstOutput, FluidVolume secondOutput, FluidVolume thirdOutput, FluidVolume fourthOutput, FluidVolume fifthOutput, FluidVolume sixthOutput, FluidVolume seventhOutput, double energyInput,
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

	public static boolean allows(World world, FluidComponent fluidComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (RefiningRecipe) it).toArray(RefiningRecipe[]::new));
		}

		for (RefiningRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(fluidComponent)) {
				return true;
			}
		}

		return false;
	}
	
	public static Optional<RefiningRecipe> matching(World world, FluidComponent fluidComponent) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (RefiningRecipe) it).toArray(RefiningRecipe[]::new));
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
		return new ItemStack(AMBlocks.ADVANCED_ELECTROLYZER);
	}

	public Identifier getIdentifier() {
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
		public static final Identifier ID = AMCommon.id("refining");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public RefiningRecipe read(Identifier identifier, JsonObject object) {
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
					DoubleUtils.fromJson(format.energyInput),
					IntegerUtils.fromJson(format.time)
			);
		}

		@Override
		public RefiningRecipe read(Identifier identifier, PacketByteBuf buffer) {
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
					DoubleUtils.fromPacket(buffer),
					IntegerUtils.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, RefiningRecipe recipe) {
			recipe.firstInput.toPacket(buffer);
			recipe.firstOutput.toPacket(buffer);
			recipe.secondOutput.toPacket(buffer);
			recipe.thirdOutput.toPacket(buffer);
			recipe.fourthOutput.toPacket(buffer);
			recipe.fifthOutput.toPacket(buffer);
			recipe.sixthOutput.toPacket(buffer);
			recipe.seventhOutput.toPacket(buffer);
			DoubleUtils.toPacket(buffer, recipe.energyInput);
			IntegerUtils.toPacket(buffer, recipe.time);
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
