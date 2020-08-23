package com.github.chainmailstudios.astromine.foundations.common.recipe;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import com.github.chainmailstudios.astromine.common.recipe.base.AstromineRecipe;
import com.github.chainmailstudios.astromine.common.utilities.IngredientUtilities;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.foundations.common.block.altar.entity.AltarBlockEntity;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AltarRecipe implements AstromineRecipe<AltarBlockEntity> {
	private final List<Ingredient> ingredients;
	private final ItemStack output;
	private final Identifier id;

	public AltarRecipe(Identifier id, List<Ingredient> ingredients, ItemStack output) {
		this.ingredients = ingredients;
		this.output = output;
		this.id = id;
	}

	@Override
	public boolean matches(AltarBlockEntity inv, World world) {
		List<Ingredient> ingredients = Lists.newArrayList(this.ingredients);
		a:
		for (ItemStack stack : inv.children.stream().map(blockEntity -> blockEntity.get().getStack(0)).collect(Collectors.toList())) {
			Iterator<Ingredient> iterator = ingredients.iterator();

			while (iterator.hasNext()) {
				Ingredient ingredient = iterator.next();

				if (ingredient.test(stack)) {
					iterator.remove();
					continue a;
				}
			}
			return false;
		}
		return ingredients.isEmpty();
	}

	@Override
	public ItemStack craft(AltarBlockEntity inv) {
		return null;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= ingredients.size();
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AstromineFoundationsBlocks.ALTAR);
	}

	@Override
	public Identifier getId() {
		return id;
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
		return DefaultedList.copyOf(Ingredient.EMPTY, ingredients.toArray(new Ingredient[0]));
	}

	public static final class Serializer implements RecipeSerializer<AltarRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("altar");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {
			// Locked.
		}

		@Override
		public AltarRecipe read(Identifier identifier, JsonObject object) {
			AltarRecipe.Format format = new Gson().fromJson(object, AltarRecipe.Format.class);

			return new AltarRecipe(identifier, format.inputs.stream().map(IngredientUtilities::fromJson).collect(Collectors.toList()), StackUtilities.fromJson(format.output));
		}

		@Override
		public AltarRecipe read(Identifier identifier, PacketByteBuf buffer) {
			int size = buffer.readInt();
			List<Ingredient> inputs = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				inputs.add(IngredientUtilities.fromPacket(buffer));
			}
			return new AltarRecipe(identifier, inputs, StackUtilities.fromPacket(buffer));
		}

		@Override
		public void write(PacketByteBuf buffer, AltarRecipe recipe) {
			buffer.writeInt(recipe.ingredients.size());
			for (Ingredient ingredient : recipe.ingredients) {
				IngredientUtilities.toPacket(buffer, ingredient);
			}
			StackUtilities.toPacket(buffer, recipe.output);
		}
	}

	public static final class Type implements AstromineRecipeType<AltarRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {
			// Locked.
		}
	}

	public static final class Format {
		List<JsonObject> inputs;
		JsonObject output;

		@Override
		public String toString() {
			return "Format{" +
			       "inputs=" + inputs +
			       ", output=" + output +
			       '}';
		}
	}
}