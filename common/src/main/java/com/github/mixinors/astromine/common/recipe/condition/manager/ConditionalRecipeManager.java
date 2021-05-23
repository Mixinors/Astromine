package com.github.mixinors.astromine.common.recipe.condition.manager;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.condition.RecipeCondition;
import com.github.mixinors.astromine.mixin.common.common.RecipeManagerAccessor;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.HashMap;
import java.util.Map;

public class ConditionalRecipeManager extends RecipeManager {
	private final ServerResourceManager resourceManager;
	
	public ConditionalRecipeManager(ServerResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler) {
		var allowed = new HashMap<Identifier, JsonElement>();
		
		for (var entry : map.entrySet()) {
			if (entry.getValue() instanceof JsonObject object) {
				var condition = AMCommon.GSON.fromJson(object.get("condition"), RecipeCondition.class);
				
				if (condition.isAllowed()) {
					allowed.put(new Identifier(entry.getKey().getNamespace(), "conditional_" + entry.getKey().getPath()), object.get("recipe"));
				}
			}
		}
		
		var existing = ((RecipeManagerAccessor) this.resourceManager.getRecipeManager()).getRecipes();
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
		
		((RecipeManagerAccessor) this.resourceManager.getRecipeManager()).setRecipes(combined);
	}
	
	public ImmutableMap<? extends RecipeType<?>, ImmutableMap<Identifier, Recipe<?>>> parse(Map<Identifier, JsonElement> map) {
		var recipeMap = new HashMap<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>>();
		
		for (var entry : map.entrySet()) {
			var identifier = entry.getKey();
			
			try {
				var recipe = deserialize(identifier, JsonHelper.asObject(entry.getValue(), "top element"));
				recipeMap.computeIfAbsent(recipe.getType(), (recipeType) -> ImmutableMap.builder()).put(identifier, recipe);
			} catch (IllegalArgumentException | JsonParseException e) {
				AMCommon.LOGGER.error("Parsing error loading conditional recipe {}", identifier, e);
			}
		}
		
		AMCommon.LOGGER.info("Loaded {} conditional recipes", recipeMap.size());
		
		return recipeMap.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, (entry) -> (entry.getValue()).build()));
	}
}
