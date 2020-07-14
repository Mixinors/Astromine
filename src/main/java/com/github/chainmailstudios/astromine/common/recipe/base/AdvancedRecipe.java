package com.github.chainmailstudios.astromine.common.recipe.base;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;

public interface AdvancedRecipe<C extends Inventory> extends Recipe<C> {
	<T extends DefaultedBlockEntity> boolean canCraft(T t);

	<T extends DefaultedBlockEntity> void craft(T t);

	<T extends RecipeConsumer> void tick(T t);

	@Override
	default boolean matches(C inv, World world) {
		return false;
	}

	@Override
	default ItemStack craft(C inv) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean fits(int width, int height) {
		return true;
	}

	@Override
	default ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	default DefaultedList<ItemStack> getRemainingStacks(C inventory) {
		return DefaultedList.of();
	}

	@Override
	default DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.of();
	}

	@Override
	default boolean isIgnoredInRecipeBook() {
		return false;
	}

	@Override
	default String getGroup() {
		return "";
	}

	@Override
	default ItemStack getRecipeKindIcon() {
		return ItemStack.EMPTY;
	}
}
