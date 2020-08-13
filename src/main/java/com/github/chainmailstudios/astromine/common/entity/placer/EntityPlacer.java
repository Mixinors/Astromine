package com.github.chainmailstudios.astromine.common.entity.placer;

import net.minecraft.entity.Entity;
import net.minecraft.world.TeleportTarget;

public interface EntityPlacer {
	TeleportTarget placeEntity(Entity entity);
}
