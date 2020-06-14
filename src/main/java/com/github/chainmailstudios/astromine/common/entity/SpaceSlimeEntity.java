package com.github.chainmailstudios.astromine.common.entity;

import com.github.chainmailstudios.astromine.common.entity.ai.JumpHoverGoal;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;

public class SpaceSlimeEntity extends SlimeEntity {

	private int floatingCooldown;
	private static final TrackedData<Integer> FLOATING_PROGRESS = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> FLOATING = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public SpaceSlimeEntity(EntityType<? extends SlimeEntity> entityType, World world) {
		super(entityType, world);
		floatingCooldown = world.random.nextInt(200);
	}

	@Override
	public void initGoals() {
		super.initGoals();
		this.goalSelector.add(3, new JumpHoverGoal(this));
	}

	@Override
	public void tick() {
		if (floatingCooldown > 0) {
			floatingCooldown--;
		}

		super.tick();
	}

	@Override
	public void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(FLOATING, false);
		this.dataTracker.startTracking(FLOATING_PROGRESS, 0);
	}

	@Override
	protected int computeFallDamage(float fallDistance, float damageMultiplier) {
		return 0;
	}

	@Override
	protected ParticleEffect getParticles() {
		return AstromineParticles.SPACE_SLIME;
	}

	@Override
	public boolean hasNoGravity() {
		return this.dataTracker.get(FLOATING);
	}

	public int getFloatingCooldown() {
		return floatingCooldown;
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
}
