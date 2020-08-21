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

package com.github.chainmailstudios.astromine.common.recipe;

import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import net.minecraft.block.Block;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.base.WrenchableHorizontalFacingEnergyTieredBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.recipe.base.AdvancedRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.EnergyGeneratingRecipe;
import com.github.chainmailstudios.astromine.common.recipe.base.RecipeConsumer;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.utilities.IngredientUtilities;
import com.github.chainmailstudios.astromine.common.utilities.PacketUtilities;
import com.github.chainmailstudios.astromine.common.utilities.ParsingUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class SolidGeneratingRecipe implements AdvancedRecipe<Inventory>, EnergyGeneratingRecipe<Inventory> {
	final Identifier identifier;
	final Ingredient input;
	final int amount;
	final double energyGenerated;
	final int time;

	private static final int OUTPUT_ENERGY_VOLUME = 0;
	private static final int INPUT_ITEM_SLOT = 0;

	public SolidGeneratingRecipe(Identifier identifier, Ingredient input, int amount, double energyGenerated, int time) {
		this.identifier = identifier;
		this.input = input;
		this.amount = amount;
		this.energyGenerated = energyGenerated;
		this.time = time;
	}

	@Override
	public <T extends ComponentBlockEntity> boolean canCraft(T blockEntity) {
		Block block = blockEntity.getWorld().getBlockState(blockEntity.getPos()).getBlock();
		if (!(block instanceof WrenchableHorizontalFacingEnergyTieredBlockWithEntity))
			return false;
		ItemInventoryComponent itemComponent = blockEntity.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT);

		ItemStack itemStack = itemComponent.getStack(0);

		return input.test(itemStack);
	}

	@Override
	public <T extends ComponentBlockEntity> void craft(T blockEntity) {
		if (canCraft(blockEntity)) {
			EnergyHandler energyHandler = Energy.of(blockEntity);
			ItemInventoryComponent itemComponent = blockEntity.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT);
			ItemStack itemStack = itemComponent.getStack(INPUT_ITEM_SLOT);

			if (EnergyUtilities.hasAvailable(energyHandler, energyGenerated)) {
				itemStack.decrement(amount);
				energyHandler.insert(energyGenerated);
			}
		}
	}

	@Override
	public <T extends RecipeConsumer> void tick(T t) {
		if (t.isFinished()) {
			t.reset();

			craft((ComponentBlockEntity) t);
		} else if (canCraft((ComponentBlockEntity) t)) {
			t.setLimit(getTime());
			t.increment();
			t.setActive(true);
		} else {
			t.reset();
		}
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineTechnologiesBlocks.ADVANCED_SOLID_GENERATOR);
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

	public Ingredient getInput() {
		return input;
	}

	public int getAmount() {
		return amount;
	}

	public double getEnergyGenerated() {
		return energyGenerated;
	}

	public int getTime() {
		return time;
	}

	public static final class Serializer implements RecipeSerializer<SolidGeneratingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("solid_generating");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public SolidGeneratingRecipe read(Identifier identifier, JsonObject object) {
			SolidGeneratingRecipe.Format format = new Gson().fromJson(object, SolidGeneratingRecipe.Format.class);

			return new SolidGeneratingRecipe(identifier, IngredientUtilities.fromJson(format.input), ParsingUtilities.fromJson(format.amount, Integer.class), EnergyUtilities.fromJson(format.energyGenerated), ParsingUtilities.fromJson(format.time, Integer.class));
		}

		@Override
		public SolidGeneratingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new SolidGeneratingRecipe(identifier, IngredientUtilities.fromPacket(buffer), PacketUtilities.fromPacket(buffer, Integer.class), EnergyUtilities.fromPacket(buffer), PacketUtilities.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, SolidGeneratingRecipe recipe) {
			IngredientUtilities.toPacket(buffer, recipe.input);
			PacketUtilities.toPacket(buffer, recipe.amount);
			EnergyUtilities.toPacket(buffer, recipe.energyGenerated);
			PacketUtilities.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AstromineRecipeType<SolidGeneratingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		JsonObject input;

		@SerializedName("amount")
		JsonElement amount;

		@SerializedName("energy_generated")
		JsonElement energyGenerated;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" + "input=" + input + ", amount=" + amount + ", energyGenerated=" + energyGenerated + ", time=" + time + '}';
		}
	}
}
