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

public class FluidMixingRecipe implements Recipe<Inventory>, EnergyConsumingRecipe<Inventory> {
	final Identifier identifier;
	final FluidIngredient firstIngredient;
	final FluidIngredient secondIngredient;
	final FluidVolume output;
	final EnergyVolume energy;
	final int time;

	public FluidMixingRecipe(Identifier identifier, FluidIngredient firstIngredient, FluidIngredient secondIngredient, FluidVolume output, EnergyVolume energy, int time) {
		this.identifier = identifier;
		this.firstIngredient = firstIngredient;
		this.secondIngredient = secondIngredient;
		this.output = output;
		this.energy = energy;
		this.time = time;
	}

	public static boolean allows(World world, Fluid inserting, Fluid existing) {
		return world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().anyMatch(it -> {
			FluidMixingRecipe recipe = ((FluidMixingRecipe) it);

			return (existing == inserting || existing == Fluids.EMPTY) && (recipe.firstIngredient.test(inserting) || recipe.secondIngredient.test(inserting));
		});
	}

	public boolean matches(FluidComponent fluidComponent) {
		FluidVolume firstInputVolume = fluidComponent.getFirst();
		FluidVolume secondInputVolume = fluidComponent.getSecond();
		FluidVolume outputVolume = fluidComponent.getThird();

		if (!firstIngredient.test(firstInputVolume) && !secondIngredient.test(firstInputVolume)) {
			return false;
		}

		if (!secondIngredient.test(secondInputVolume) && !firstIngredient.test(secondInputVolume)) {
			return false;
		}

		return output.test(outputVolume);
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
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.of();
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER);
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public FluidIngredient getFirstIngredient() {
		return firstIngredient;
	}

	public FluidIngredient getSecondIngredient() {
		return secondIngredient;
	}

	public FluidVolume getOutputVolume() {
		return output.copy();
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

	public static final class Serializer implements RecipeSerializer<FluidMixingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("fluid_mixing");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public FluidMixingRecipe read(Identifier identifier, JsonObject object) {
			FluidMixingRecipe.Format format = new Gson().fromJson(object, FluidMixingRecipe.Format.class);

			return new FluidMixingRecipe(identifier, IngredientUtilities.fromFluidIngredientJson(format.firstInput), IngredientUtilities.fromFluidIngredientJson(format.secondInput), VolumeUtilities.fromFluidVolumeJson(format.output), VolumeUtilities.fromEnergyVolumeJson(
				format.energy), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public FluidMixingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new FluidMixingRecipe(identifier, IngredientUtilities.fromFluidIngredientPacket(buffer), IngredientUtilities.fromFluidIngredientPacket(buffer), VolumeUtilities.fromFluidVolumePacket(buffer), VolumeUtilities.fromEnergyVolumePacket(buffer), PacketUtilities
				.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, FluidMixingRecipe recipe) {
			IngredientUtilities.toFluidIngredientPacket(buffer, recipe.getFirstIngredient());
			IngredientUtilities.toFluidIngredientPacket(buffer, recipe.getSecondIngredient());
			VolumeUtilities.toFluidVolumePacket(buffer, recipe.getOutputVolume());
			VolumeUtilities.toEnergyVolumePacket(buffer, recipe.getEnergyVolume());
			PacketUtilities.toPacket(buffer, recipe.getTime());
		}
	}

	public static final class Type implements AstromineRecipeType<FluidMixingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		@SerializedName("first_input")
		JsonElement firstInput;

		@SerializedName("second_input")
		JsonElement secondInput;

		JsonElement output;

		JsonElement energy;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "firstInput=" + firstInput + ", secondInput=" + secondInput + ", output=" + output + ", energy=" + energy + ", time=" + time + '}';
		}
	}
}
