package com.github.chainmailstudios.astromine.common.entity;

import com.github.chainmailstudios.astromine.common.entity.ai.JumpHoverGoal;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class SpaceSlimeEntity extends SlimeEntity {

	private static final TrackedData<Integer> FLOATING_PROGRESS = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> FLOATING = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private int floatingCooldown;

	public SpaceSlimeEntity(EntityType<? extends SlimeEntity> entityType, World world) {
		super(entityType, world);
		this.floatingCooldown = world.random.nextInt(200);
	}

	@Override
	public void initGoals() {
		super.initGoals();
		this.goalSelector.add(3, new JumpHoverGoal(this));
	}

	@Override
	public void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(FLOATING, false);
		this.dataTracker.startTracking(FLOATING_PROGRESS, 0);
	}

	@Override
	protected ParticleEffect getParticles() {
		return AstromineParticles.SPACE_SLIME;
	}

	@Override
	public void tick() {
		if (this.floatingCooldown > 0) {
			this.floatingCooldown--;
		}

		super.tick();
	}

	@Override
	protected int computeFallDamage(float fallDistance, float damageMultiplier) {
		return 0;
	}

	@Override
	public boolean hasNoGravity() {
		return this.dataTracker.get(FLOATING);
	}

	public int getFloatingCooldown() {
		return this.floatingCooldown;
	}

	public void setFloatingCooldown(int cooldown) {
		this.floatingCooldown = cooldown;
	}

	public boolean isFloating() {
		return this.dataTracker.get(FLOATING);
	}

	public void setFloating(boolean floating) {
		this.dataTracker.set(FLOATING, floating);
	}

	public int getFloatingProgress() {
		return this.dataTracker.get(FLOATING_PROGRESS);
	}

	public void setFloatingProgress(int progress) {
		this.dataTracker.set(FLOATING_PROGRESS, progress);
	}

	public static boolean canSpawnInDark(EntityType<? extends SpaceSlimeEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && isSpawnDark(world, pos, random) && canMobSpawn(type, world, spawnReason, pos, random) && random.nextDouble() <= .15;
	}

	public static boolean isSpawnDark(WorldAccess world, BlockPos pos, Random random) {
		if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
			return false;
		} else {
			int i = world.getWorld().isThundering() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos);
			return i <= random.nextInt(8);
		}
	}
}
