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
		Map<Identifier, JsonElement> allowed = new HashMap<>();
		
		for (Map.Entry<Identifier, JsonElement> entry : map.entrySet()) {
			if (entry.getValue() instanceof JsonObject) {
				JsonObject object = entry.getValue().getAsJsonObject();
				
				RecipeCondition condition = AMCommon.GSON.fromJson(object.get("condition"), RecipeCondition.class);
				
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
			Identifier identifier = identifierJsonElementEntry.getKey();
			
			try {
				Recipe<?> recipe = deserialize(identifier, JsonHelper.asObject(identifierJsonElementEntry.getValue(), "top element"));
				recipeMap.computeIfAbsent(recipe.getType(), (recipeType) -> ImmutableMap.builder()).put(identifier, recipe);
			} catch (IllegalArgumentException | JsonParseException var9) {
				AMCommon.LOGGER.error("Parsing error loading conditional recipe {}", identifier, var9);
			}
		}
		
		AMCommon.LOGGER.info("Loaded {} conditional recipes", recipeMap.size());
		return recipeMap.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, (entryx) -> (entryx.getValue()).build()));
	}
}
