package com.github.chainmailstudios.astromine.recipe;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.IngredientUtilities;
import com.github.chainmailstudios.astromine.common.utilities.PacketUtilities;
import com.github.chainmailstudios.astromine.common.utilities.ParsingUtilities;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.*;

public class SortingRecipe implements Recipe<ItemInventoryFromInventoryComponent> {
	final Identifier identifier;
	final Ingredient input;
	final ItemStack output;

	final int timeTotal;
	final int energyTotal;

	public SortingRecipe(Identifier identifier, Ingredient input, ItemStack output, int timeTotal, int energyTotal) {
		this.identifier = identifier;
		this.input = input;
		this.output = output;
		this.timeTotal = timeTotal;
		this.energyTotal = energyTotal;
	}

	@Override
	public boolean matches(ItemInventoryFromInventoryComponent component, World world) {
		return component.getComponent().getItemContents().values().stream().anyMatch(input);
	}

	@Override
	public ItemStack craft(ItemInventoryFromInventoryComponent component) {
		List<ItemStack> matching = Lists.newArrayList(component.getComponent().getContentsMatching(input));

		ItemStack stack = matching.isEmpty() ? ItemStack.EMPTY : matching.get(0);

		for (Map.Entry<Integer, ItemStack> entry : component.getComponent().getItemContents().entrySet()) {
			if (entry.getValue() == stack && !stack.isEmpty()) {
				component.removeStack(entry.getKey());

				break;
			}
		}

		return output.copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return output.copy();
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

	public Identifier getIdentifier() {
		return identifier;
	}

	public Ingredient getInput() {
		return input;
	}

	public int getTimeTotal() {
		return timeTotal;
	}

	public int getEnergyTotal() {
		return energyTotal;
	}

	public static final class Serializer implements RecipeSerializer<SortingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("sorting_recipe_serializer");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public SortingRecipe read(Identifier identifier, JsonObject object) {
			SortingRecipe.Format format = new Gson().fromJson(object, SortingRecipe.Format.class);

			return new SortingRecipe(ID,
					IngredientUtilities.fromJson(format.input),
					StackUtilities.fromJson(format.output),
					ParsingUtilities.fromJson(format.timeTotal, Integer.class),
					ParsingUtilities.fromJson(format.energyTotal, Integer.class));
		}

		@Override
		public SortingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new SortingRecipe(identifier,
					IngredientUtilities.fromPacket(buffer),
					StackUtilities.fromPacket(buffer),
					PacketUtilities.fromPacket(buffer, Integer.class),
					PacketUtilities.fromPacket(buffer, Integer.class));
		}

		@Override
		public void write(PacketByteBuf buffer, SortingRecipe recipe) {
			IngredientUtilities.toPacket(buffer, recipe.input);
			StackUtilities.toPacket(buffer, recipe.output);
			PacketUtilities.toPacket(buffer, recipe.timeTotal);
			PacketUtilities.toPacket(buffer, recipe.energyTotal);
		}
	}

	public static final class Type implements RecipeType<SortingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		JsonArray input;
		JsonObject output;
		@SerializedName("time_total")
		JsonPrimitive timeTotal;
		@SerializedName("energy_total")
		JsonPrimitive energyTotal;
	}
}
