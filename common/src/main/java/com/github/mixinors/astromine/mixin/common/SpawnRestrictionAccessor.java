package com.github.mixinors.astromine.mixin.common;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(SpawnRestriction.class)
public interface SpawnRestrictionAccessor {
	@Invoker
	static <T extends MobEntity> void callRegister(EntityType<T> entityType, SpawnRestriction.Location location, Heightmap.Type type, SpawnRestriction.SpawnPredicate<T> spawnPredicate) {
		throw new UnsupportedOperationException("Cannot invoke @Invoker method!");
	}
}
