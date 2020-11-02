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

import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
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
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ElectrolyzingRecipe implements Recipe<Inventory>, EnergyConsumingRecipe<Inventory> {
	final Identifier identifier;
	final FluidIngredient input;
	final FluidVolume firstOutput;
	final FluidVolume secondOutput;
	final EnergyVolume energy;
	final int time;

	public ElectrolyzingRecipe(Identifier identifier, FluidIngredient input, FluidVolume firstOutput, FluidVolume secondOutput, EnergyVolume energy, int time) {
		this.identifier = identifier;
		this.input = input;
		this.firstOutput = firstOutput;
		this.secondOutput = secondOutput;
		this.energy = energy;
		this.time = time;
	}

	public static boolean allows(World world, Fluid inserting, Fluid existing) {
		return world.getRecipeManager().getAllOfType(ElectrolyzingRecipe.Type.INSTANCE).values().stream().anyMatch(it -> {
			ElectrolyzingRecipe recipe = ((ElectrolyzingRecipe) it);

			return (existing == inserting || existing == Fluids.EMPTY) && (recipe.input.test(inserting));
		});
	}

	public boolean matches(FluidComponent fluidComponent) {
		FluidVolume inputVolume = fluidComponent.getFirst();
		FluidVolume firstOutputVolume = fluidComponent.getSecond();
		FluidVolume secondOutputVolume = fluidComponent.getThird();

		if (!input.test(inputVolume)) {
			return false;
		}

		if (!firstOutput.test(firstOutputVolume)) {
			return false;
		}

		return secondOutput.test(secondOutputVolume);
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

	public static final class Serializer implements RecipeSerializer<ElectrolyzingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("electrolyzing");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public ElectrolyzingRecipe read(Identifier identifier, JsonObject object) {
			ElectrolyzingRecipe.Format format = new Gson().fromJson(object, ElectrolyzingRecipe.Format.class);

			return new ElectrolyzingRecipe(identifier, IngredientUtilities.fromFluidIngredientJson(format.input), VolumeUtilities.fromFluidVolumeJson(format.firstOutput), VolumeUtilities.fromFluidVolumeJson(format.secondOutput), VolumeUtilities.fromEnergyVolumeJson(
				format.energy), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public ElectrolyzingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new ElectrolyzingRecipe(identifier, IngredientUtilities.fromFluidIngredientPacket(buffer), VolumeUtilities.fromFluidVolumePacket(buffer), VolumeUtilities.fromFluidVolumePacket(buffer), VolumeUtilities.fromEnergyVolumePacket(buffer), PacketUtilities.fromPacket(
				buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, ElectrolyzingRecipe recipe) {
			IngredientUtilities.toFluidIngredientPacket(buffer, recipe.getIngredient());
			VolumeUtilities.toFluidVolumePacket(buffer, recipe.getFirstOutputVolume());
			VolumeUtilities.toFluidVolumePacket(buffer, recipe.getSecondOutputVolume());
			VolumeUtilities.toEnergyVolumePacket(buffer, recipe.getEnergyVolume());
			PacketUtilities.toPacket(buffer, recipe.getTime());
		}
	}

	public static final class Type implements AstromineRecipeType<ElectrolyzingRecipe> {
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

		JsonElement energy;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "input=" + input + ", firstOutput=" + firstOutput + ", secondOutput=" + secondOutput + ", energy=" + energy + ", time=" + time + '}';
		}
	}
}
