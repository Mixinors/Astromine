/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.condition.RecipeCondition;
import com.github.mixinors.astromine.mixin.common.RecipeManagerAccessor;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.HashMap;
import java.util.Map;

public class ConditionalRecipeManager extends RecipeManager {
	private final RecipeManager recipeManager;
	public ConditionalRecipeManager(RecipeManager recipeManager) {
		this.recipeManager = recipeManager;
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler) {
		var allowed = new HashMap<Identifier, JsonElement>();
		
		for (var entry : map.entrySet()) {
			if (entry.getValue() instanceof JsonObject jsonObject) {
				var condition = AMCommon.GSON.fromJson(jsonObject.get("condition"), RecipeCondition.class);
				
				if (condition.isAllowed()) {
					allowed.put(new Identifier(entry.getKey().getNamespace(), "conditional_" + entry.getKey().getPath()), jsonObject.get("recipe"));
				}
			}
		}
		
		var existing = ((RecipeManagerAccessor) recipeManager).getRecipes();
		var parsed = parse(allowed);
		var combined = new HashMap<RecipeType<?>, Map<Identifier, Recipe<?>>>();
		
		for (var entry : existing.entrySet()) {
			if (!combined.containsKey(entry.getKey())) {
				combined.put(entry.getKey(), new HashMap<>());
			}
			
			combined.get(entry.getKey()).putAll(entry.getValue());
		}
		
		for (var entry : parsed.entrySet()) {
			if (!combined.containsKey(entry.getKey())) {
				combined.put(entry.getKey(), new HashMap<>());
			}
			
			combined.get(entry.getKey()).putAll(entry.getValue());
		}
		
		((RecipeManagerAccessor) recipeManager).setRecipes(combined);
	}
	
	public ImmutableMap<? extends RecipeType<?>, ImmutableMap<Identifier, Recipe<?>>> parse(Map<Identifier, JsonElement> map) {
		var recipeMap = new HashMap<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>>();
		
		for (var identifierJsonElementEntry : map.entrySet()) {
			var identifier = identifierJsonElementEntry.getKey();
			
			try {
				var recipe = deserialize(identifier, JsonHelper.asObject(identifierJsonElementEntry.getValue(), "top element"));
				recipeMap.computeIfAbsent(recipe.getType(), (recipeType) -> ImmutableMap.builder()).put(identifier, recipe);
			} catch (IllegalArgumentException | JsonParseException var9) {
				AMCommon.LOGGER.error("Parsing error loading conditional recipe {}", identifier, var9);
			}
		}
		
		AMCommon.LOGGER.info("Loaded {} conditional recipes", recipeMap.size());
		return recipeMap.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, (entry) -> (entry.getValue()).build()));
	}
}
