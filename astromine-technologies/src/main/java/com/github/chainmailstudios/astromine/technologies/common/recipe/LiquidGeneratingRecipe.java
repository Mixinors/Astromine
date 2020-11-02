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
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LiquidGeneratingRecipe implements Recipe<Inventory>, EnergyGeneratingRecipe<Inventory> {
	final Identifier identifier;
	final FluidIngredient input;
	final EnergyVolume energy;
	final int time;

	public LiquidGeneratingRecipe(Identifier identifier, FluidIngredient input, EnergyVolume energy, int time) {
		this.identifier = identifier;
		this.input = input;
		this.energy = energy;
		this.time = time;
	}

	public static boolean allows(World world, Fluid inserting, Fluid existing) {
		return world.getRecipeManager().getAllOfType(LiquidGeneratingRecipe.Type.INSTANCE).values().stream().anyMatch(it -> {
			LiquidGeneratingRecipe recipe = ((LiquidGeneratingRecipe) it);

			return (existing == inserting || existing == Fluids.EMPTY) && (recipe.input.test(inserting));
		});
	}

	public boolean matches(FluidComponent fluidComponent) {
		FluidVolume inputVolume = fluidComponent.getFirst();

		return input.test(inputVolume);
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
		return DefaultedList.of(); // we are not dealing with items
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_LIQUID_GENERATOR);
	}

	public FluidIngredient getIngredient() {
		return input;
	}

	public EnergyVolume getEnergyVolume() {
		return energy.copy();
	}

	public int getTime() {
		return time;
	}

	@Override
	public double getEnergyGenerated() {
		return energy.getAmount();
	}

	public static final class Serializer implements RecipeSerializer<LiquidGeneratingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("liquid_generating");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public LiquidGeneratingRecipe read(Identifier identifier, JsonObject object) {
			LiquidGeneratingRecipe.Format format = new Gson().fromJson(object, LiquidGeneratingRecipe.Format.class);

			return new LiquidGeneratingRecipe(identifier, IngredientUtilities.fromFluidIngredientJson(format.input), VolumeUtilities.fromEnergyVolumeJson(format.energy), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public LiquidGeneratingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new LiquidGeneratingRecipe(identifier, IngredientUtilities.fromFluidIngredientPacket(buffer), VolumeUtilities.fromEnergyVolumePacket(buffer), PacketUtilities.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, LiquidGeneratingRecipe recipe) {
			IngredientUtilities.toFluidIngredientPacket(buffer, recipe.getIngredient());
			VolumeUtilities.toEnergyVolumePacket(buffer, recipe.getEnergyVolume());
			PacketUtilities.toPacket(buffer, recipe.getTime());
		}
	}

	public static final class Type implements AstromineRecipeType<LiquidGeneratingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		JsonElement input;

		JsonElement energy;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "input=" + input + ", energy=" + energy + ", time=" + time + '}';
		}
	}
}
