package com.github.chainmailstudios.astromine.common.entity.placer;

import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class SpaceEntityPlacer implements EntityPlacer {
	public static final SpaceEntityPlacer TO_PLANET = new SpaceEntityPlacer(AstromineConfig.get().overworldSpawnYLevel);
	public static final SpaceEntityPlacer TO_SPACE = new SpaceEntityPlacer(AstromineConfig.get().spaceSpawnYLevel);

	public final int y;


	public SpaceEntityPlacer(int y) {
		this.y = y;
	}

	@Override
	public BlockPattern.TeleportTarget placeEntity(Entity entity, ServerWorld serverWorld, Direction direction, double v, double v1) {
		return new BlockPattern.TeleportTarget(new Vec3d(entity.getX(), y, entity.getZ()), entity.getVelocity(), (int) entity.getHeadYaw());
	}
}
