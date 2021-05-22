package com.github.mixinors.astromine.common.registry;

import com.github.mixinors.astromine.common.entity.SuperSpaceSlimeEntity;
import com.github.mixinors.astromine.registry.common.AMCallbacks;
import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.util.ActionResult;

public class AMFCallbacks extends AMCallbacks {
	public static void init() {
		AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
			if (entity instanceof SuperSpaceSlimeEntity) {
				if (world.random.nextInt(10) == 0) {
					var spaceSlimeEntity = AMEntityTypes.SPACE_SLIME.get().create(world);
					spaceSlimeEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
					
					world.spawnEntity(spaceSlimeEntity);
				}
			}
			
			return ActionResult.PASS;
		});
	}
}
