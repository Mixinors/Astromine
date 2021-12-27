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

package com.github.mixinors.astromine.common.recipe.condition.manager;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.condition.RecipeCondition;
import com.github.mixinors.astromine.mixin.common.RecipeManagerAccessor;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

public class ConditionalRecipeManager extends RecipeManager {
	private final ServerResourceManager resourceManager;
	
	public ConditionalRecipeManager(ServerResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler) {
		Map<Identifier, JsonElement> allowed = new HashMap<>();
		
		for (Map.Entry<Identifier, JsonElement> entry : map.entrySet()) {
			if (entry.getValue() instanceof JsonObject) {
				var object = entry.getValue().getAsJsonObject();
				
				var condition = AMCommon.GSON.fromJson(object.get("condition"), RecipeCondition.class);
				
				if (condition.isAllowed()) {
					allowed.put(new Identifier(entry.getKey().getNamespace(), "conditional_" + entry.getKey().getPath()), object.get("recipe"));
				}
			}
		}
		
		Map<RecipeType<?>, Map<Identifier, Recipe<?>>> existing = ((RecipeManagerAccessor) this.resourceManager.getRecipeManager()).getRecipes();
		ImmutableMap<? extends RecipeType<?>, ImmutableMap<Identifier, Recipe<?>>> parsed = parse(allowed);
		HashMap<RecipeType<?>, Map<Identifier, Recipe<?>>> combined = new HashMap<>();
		
		for (Map.Entry<RecipeType<?>, Map<Identifier, Recipe<?>>> entry : existing.entrySet()) {
			if (!combined.containsKey(entry.getKey())) {
				combined.put(entry.getKey(), new HashMap<>());
			}
			
			combined.get(entry.getKey()).putAll(entry.getValue());
		}
		
		for (Map.Entry<? extends RecipeType<?>, ImmutableMap<Identifier, Recipe<?>>> entry : parsed.entrySet()) {
			if (!combined.containsKey(entry.getKey())) {
				combined.put(entry.getKey(), new HashMap<>());
			}
			
			combined.get(entry.getKey()).putAll(entry.getValue());
		}
		
		((RecipeManagerAccessor) this.resourceManager.getRecipeManager()).setRecipes(combined);
	}
	
	public ImmutableMap<? extends RecipeType<?>, ImmutableMap<Identifier, Recipe<?>>> parse(Map<Identifier, JsonElement> map) {
		Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> recipeMap = new HashMap<>();
		
		for (Map.Entry<Identifier, JsonElement> identifierJsonElementEntry : map.entrySet()) {
			var identifier = identifierJsonElementEntry.getKey();
			
			try {
				Recipe<?> recipe = deserialize(identifier, JsonHelper.asObject(identifierJsonElementEntry.getValue(), "top element"));
				recipeMap.computeIfAbsent(recipe.getType(), (recipeType) -> ImmutableMap.builder()).put(identifier, recipe);
			} catch (IllegalArgumentException | JsonParseException var9) {
				AMCommon.LOGGER.error("Parsing error loading conditional recipe {}", identifier, var9);
			}
		}
		
		AMCommon.LOGGER.info("Loaded {} conditional recipes", recipeMap.size());
		return recipeMap.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, (entry) -> (entry.getValue()).build()));
	}
}
