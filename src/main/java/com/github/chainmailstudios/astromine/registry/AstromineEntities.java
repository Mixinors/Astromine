package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.entity.SpaceSlimeEntity;
import com.github.chainmailstudios.astromine.common.entity.SuperSpaceSlimeEntity;
import com.github.chainmailstudios.astromine.common.entity.projectile.BulletEntity;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AstromineEntities {

	public static final EntityType<BulletEntity> BULLET_ENTITY_TYPE = register("bullet_entity", new EntityType<>(BulletEntity::new, SpawnGroup.MISC, false, true, true, true, ImmutableSet.<Block>builder().build(), EntityDimensions.fixed(0.1875f, 0.125f),

	public static final EntityType<SpaceSlimeEntity> SPACE_SLIME = register(
			"space_slime",
			FabricEntityTypeBuilder
					.create(SpawnGroup.MONSTER, SpaceSlimeEntity::new)
					.dimensions(EntityDimensions.changing(2.04F, 2.04F))
					.trackable(128, 4)
					.build()
	);

	public static final EntityType<SuperSpaceSlimeEntity> SUPER_SPACE_SLIME = register(
			"super_space_slime",
			FabricEntityTypeBuilder
					.create(SpawnGroup.MONSTER, SuperSpaceSlimeEntity::new)
					.dimensions(EntityDimensions.changing(6.125F, 6.125F))
					.trackable(128, 4)
					.build()
	);                                                                                                             4, 20));

	public static void initialize() {
		FabricDefaultAttributeRegistry.register(SPACE_SLIME, HostileEntity.createHostileAttributes());
		FabricDefaultAttributeRegistry.register(SUPER_SPACE_SLIME, SuperSpaceSlimeEntity.createAttributes());

		// register behavior for super space slime spawning minions when hit
		AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
			if(entity instanceof SuperSpaceSlimeEntity) {
				if(world.random.nextInt(10) == 0) {
					SpaceSlimeEntity spaceSlimeEntity = AstromineEntities.SPACE_SLIME.create(world);
					spaceSlimeEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
					world.spawnEntity(spaceSlimeEntity);
				}
			}

			return ActionResult.PASS;
		});
	}

	private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
		return register(new Identifier(AstromineCommon.MOD_ID, id), builder);
	}

	private static <T extends Entity> EntityType<T> register(Identifier id, EntityType.Builder<T> builder) {
		return Registry.register(Registry.ENTITY_TYPE, id, builder.build(id.getPath()));
	}

	/**
	 * @param id            Name of EntityType instance to be registered
	 * @param type      	EntityType instance to register
	 * @return              Registered EntityType
	 */
	private static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
		return register(new Identifier(AstromineCommon.MOD_ID, id), type);
	}

	private static <T extends Entity> EntityType<T> register(Identifier id, EntityType<T> type) {
		return Registry.register(Registry.ENTITY_TYPE, id, type);
	}
}
