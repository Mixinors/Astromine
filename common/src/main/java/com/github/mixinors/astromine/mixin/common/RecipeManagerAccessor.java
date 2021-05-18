package com.github.mixinors.astromine.mixin.common;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collections;
import java.util.Map;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor {
	@Accessor
	Map<RecipeType<?>, Map<Identifier, Recipe<?>>> getRecipes();
	
	@Accessor
	void setRecipes(Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes);
	
	@Invoker
	<C extends Inventory, T extends Recipe<C>> Map<Identifier, Recipe<C>> getAllOfType(RecipeType<T> recipeType);
}