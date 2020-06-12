package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.entity.projectile.BulletEntity;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineEntities {
	public static final EntityType<BulletEntity> BULLET_ENTITY_TYPE = register("bullet_entity", new EntityType<>(BulletEntity::new, SpawnGroup.MISC, false, true, true, true, ImmutableSet.<Block>builder().build(), EntityDimensions.fixed(0.1875f, 0.125f),
	                                                                                                             4, 20));

	public static void initialize() {
		// Unused.
	}

	private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
		return register(new Identifier(AstromineCommon.MOD_ID, id), builder);
	}

	private static <T extends Entity> EntityType<T> register(Identifier id, EntityType.Builder<T> builder) {
		return Registry.register(Registry.ENTITY_TYPE, id, builder.build(id.getPath()));
	}

	private static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
		return register(new Identifier(AstromineCommon.MOD_ID, id), type);
	}

	private static <T extends Entity> EntityType<T> register(Identifier id, EntityType<T> type) {
		return Registry.register(Registry.ENTITY_TYPE, id, type);
	}
}
