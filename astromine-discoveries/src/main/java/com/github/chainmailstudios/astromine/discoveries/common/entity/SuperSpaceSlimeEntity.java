/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.discoveries.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.discoveries.common.entity.ai.superspaceslime.SuperSpaceSlimeExplosionGoal;
import com.github.chainmailstudios.astromine.discoveries.common.entity.ai.superspaceslime.SuperSpaceSlimeFaceTowardTargetGoal;
import com.github.chainmailstudios.astromine.discoveries.common.entity.ai.superspaceslime.SuperSpaceSlimeMoveControl;
import com.github.chainmailstudios.astromine.discoveries.common.entity.ai.superspaceslime.SuperSpaceSlimeMoveGoal;
import com.github.chainmailstudios.astromine.discoveries.common.entity.ai.superspaceslime.SuperSpaceSlimeRandomLookGoal;
import com.github.chainmailstudios.astromine.discoveries.common.entity.ai.superspaceslime.SuperSpaceSlimeSwimmingGoal;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesEntityTypes;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesParticles;

public class SuperSpaceSlimeEntity extends MobEntity implements Monster {

	// data for slime explosion mechanic
	private static final TrackedData<Integer> EXPLOSION_PROGRESS = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> IS_EXPLODING = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> HAS_EXPLODED = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private final ServerBossBar bossBar;
	public float targetStretch;
	public float stretch;
	public float lastStretch;
	private boolean onGroundLastTick;

	public SuperSpaceSlimeEntity(EntityType<? extends SuperSpaceSlimeEntity> entityType, World world) {
		super(entityType, world);
		this.bossBar = (ServerBossBar) (new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true);
		this.moveControl = new SuperSpaceSlimeMoveControl(this);
	}

	/**
	 * Creates a {@link DefaultAttributeContainer.Builder} instance used for registering this entities' default
	 * attributes.
	 *
	 * @return a {@link DefaultAttributeContainer.Builder} with default attribute information
	 */
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5).add(EntityAttributes.GENERIC_MAX_HEALTH, 300);
	}

	@Override
	public void initGoals() {
		this.goalSelector.add(0, new SuperSpaceSlimeExplosionGoal(this));
		this.goalSelector.add(1, new SuperSpaceSlimeSwimmingGoal(this));
		this.goalSelector.add(2, new SuperSpaceSlimeFaceTowardTargetGoal(this));
		this.goalSelector.add(3, new SuperSpaceSlimeRandomLookGoal(this));
		this.goalSelector.add(5, new SuperSpaceSlimeMoveGoal(this));

		this.targetSelector.add(1, new FollowTargetGoal<>(this, PlayerEntity.class, 10, true, false, (livingEntity) -> true));
	}

	@Override
	public void initDataTracker() {
		super.initDataTracker();

		this.dataTracker.startTracking(HAS_EXPLODED, false);
		this.dataTracker.startTracking(IS_EXPLODING, false);
		this.dataTracker.startTracking(EXPLOSION_PROGRESS, 0);
	}

	@Override
	public void tick() {
		this.stretch += (this.targetStretch - this.stretch) * 0.5F;
		this.lastStretch = this.stretch;

		super.tick();

		if (this.onGround && !this.onGroundLastTick) {
			int size = 10;

			// spawn random landing particles around this entity's hitbox base
			for (int j = 0; j < size * 8; ++j) {
				float f = this.random.nextFloat() * 6.2831855F;
				float g = this.random.nextFloat() * 0.5F + 0.5F;
				float particleX = MathHelper.sin(f) * (float) size * 0.5F * g;
				float particleZ = MathHelper.cos(f) * (float) size * 0.5F * g;
				this.world.addParticle(this.getParticles(), this.getX() + (double) particleX, this.getY(), this.getZ() + (double) particleZ, 0.0D, 0.0D, 0.0D);
			}

			this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			this.playSound(SoundEvents.BLOCK_GLASS_BREAK, this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			this.targetStretch = -0.5F;
		} else if (!this.onGround && this.onGroundLastTick) {
			this.targetStretch = 1.0F;
		}

		this.onGroundLastTick = this.onGround;
		this.updateStretch();
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putBoolean("hasExploded", this.hasExploded());
		tag.putBoolean("wasOnGround", this.onGroundLastTick);
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		this.setHasExploded(tag.getBoolean("hasExploded"));
		this.onGroundLastTick = tag.getBoolean("wasOnGround");
	}

	@Override
	protected void mobTick() {
		super.mobTick();
		this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
	}

	public void setHasExploded(boolean exploded) {
		this.dataTracker.set(HAS_EXPLODED, exploded);
	}

	public boolean hasExploded() {
		return this.dataTracker.get(HAS_EXPLODED);
	}

	protected ParticleEffect getParticles() {
		return AstromineDiscoveriesParticles.SPACE_SLIME;
	}

	protected SoundEvent getSquishSound() {
		return SoundEvents.ENTITY_SLIME_SQUISH;
	}

	protected void updateStretch() {
		this.targetStretch *= 0.6F;
	}

	/**
	 * Called at the end of {@link SuperSpaceSlimeExplosionGoal}.
	 * <p>
	 * Spawns a large number of Space Slime around the Super Space Slime. Note that entity hitbox mechanics remove the
	 * need to set random velocities on the new slimes.
	 */
	public void explode() {
		for (int i = 0; i < 50; i++) {
			SpaceSlimeEntity spaceSlime = AstromineDiscoveriesEntityTypes.SPACE_SLIME.create(this.world);
			spaceSlime.initialize((ServerWorldAccess) this.world, this.world.getLocalDifficulty(this.getBlockPos()), SpawnReason.NATURAL, null, null);
			this.world.spawnEntity(spaceSlime);
			spaceSlime.requestTeleport(this.getX(), this.getY(), this.getZ());
		}
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		this.damage(player);
	}

	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		this.bossBar.addPlayer(player);
	}

	@Override
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		this.bossBar.removePlayer(player);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SLIME_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SLIME_DEATH;
	}

	@Override
	public float getSoundVolume() {
		return 1;
	}

	@Override
	public void pushAwayFrom(Entity entity) {
		super.pushAwayFrom(entity);

		if (entity instanceof IronGolemEntity) {
			this.damage((LivingEntity) entity);
		}
	}

	protected void damage(LivingEntity target) {
		if (this.isAlive()) {
			int size = 10;

			if (this.squaredDistanceTo(target) < 0.6D * (double) size * 0.6D * (double) size && this.canSee(target) && target.damage(DamageSource.mob(this), this.getDamageAmount())) {
				this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				this.dealDamage(this, target);
			}
		}
	}

	protected float getDamageAmount() {
		return (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
	}

	@Override
	public void jump() {
		Vec3d vec3d = this.getVelocity();
		this.setVelocity(vec3d.x, this.getJumpVelocity(), vec3d.z);
		this.velocityDirty = true;
	}

	/**
	 * To prevent the Super Space Slime from dying through entity cramming during its explosion attack, its cramming
	 * functionality is disabled.
	 */
	@Override
	protected void tickCramming() {

	}

	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.625F * dimensions.height;
	}

	public float getJumpSoundPitch() {
		float f = 0.8F;
		return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
	}

	public SoundEvent getJumpSound() {
		return SoundEvents.ENTITY_SLIME_JUMP;
	}

	public int getTicksUntilNextJump() {
		return this.random.nextInt(20) + 10;
	}

	public boolean isExploding() {
		return this.dataTracker.get(IS_EXPLODING);
	}

	public void setExploding(boolean exploding) {
		this.dataTracker.set(IS_EXPLODING, exploding);
	}

	public int getExplodingProgress() {
		return this.dataTracker.get(EXPLOSION_PROGRESS);
	}

	public void setExplodingProgress(int progress) {
		this.dataTracker.set(EXPLOSION_PROGRESS, progress);
	}
}
