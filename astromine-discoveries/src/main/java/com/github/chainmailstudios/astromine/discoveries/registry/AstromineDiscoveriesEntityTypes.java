package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.discoveries.common.entity.SpaceSlimeEntity;
import com.github.chainmailstudios.astromine.discoveries.common.entity.SuperSpaceSlimeEntity;
import com.github.chainmailstudios.astromine.registry.AstromineEntityTypes;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.Heightmap;

public class AstromineDiscoveriesEntityTypes extends AstromineEntityTypes {
	public static final EntityType<SpaceSlimeEntity> SPACE_SLIME = register("space_slime", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SpaceSlimeEntity::new).dimensions(EntityDimensions.changing(2.04F, 2.04F)).trackable(128, 4).build());

	public static final EntityType<SuperSpaceSlimeEntity> SUPER_SPACE_SLIME = register("super_space_slime", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SuperSpaceSlimeEntity::new).dimensions(EntityDimensions.changing(6.125F, 6.125F)).trackable(128, 4).build());

	public static void initialize() {
		FabricDefaultAttributeRegistry.register(SPACE_SLIME, HostileEntity.createHostileAttributes());
		FabricDefaultAttributeRegistry.register(SUPER_SPACE_SLIME, SuperSpaceSlimeEntity.createAttributes());

		AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
			if (entity instanceof SuperSpaceSlimeEntity) {
				if (world.random.nextInt(10) == 0) {
					SpaceSlimeEntity spaceSlimeEntity = AstromineDiscoveriesEntityTypes.SPACE_SLIME.create(world);
					spaceSlimeEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
					world.spawnEntity(spaceSlimeEntity);
				}
			}

			return ActionResult.PASS;
		});

		SpawnRestriction.register(AstromineDiscoveriesEntityTypes.SPACE_SLIME, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpaceSlimeEntity::canSpawnInDark);

		FabricDefaultAttributeRegistry.register(SPACE_SLIME, SpaceSlimeEntity.createMobAttributes());
		FabricDefaultAttributeRegistry.register(SUPER_SPACE_SLIME, SuperSpaceSlimeEntity.createMobAttributes());
	}
}
