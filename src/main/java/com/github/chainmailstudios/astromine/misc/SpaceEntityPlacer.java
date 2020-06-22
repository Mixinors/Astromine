package com.github.chainmailstudios.astromine.misc;

import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;

public class SpaceEntityPlacer implements EntityPlacer {
	public static final SpaceEntityPlacer TO_PLANET = new SpaceEntityPlacer(1024);
	public static final SpaceEntityPlacer TO_SPACE = new SpaceEntityPlacer(1);

	public final int y;

	public SpaceEntityPlacer(int y) {
		this.y = y;
	}

	@Override
	public BlockPattern.TeleportTarget placeEntity(Entity entity, ServerWorld serverWorld, Direction direction, double v, double v1) {
		return new BlockPattern.TeleportTarget(entity.getPos().add(0, y, 0), entity.getVelocity(), (int) entity.getHeadYaw());
	}
}
