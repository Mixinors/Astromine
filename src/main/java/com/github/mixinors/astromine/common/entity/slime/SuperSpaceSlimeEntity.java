/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.entity.slime;

import com.github.mixinors.astromine.common.entity.ai.superspaceslime.*;
import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import com.github.mixinors.astromine.registry.common.AMParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class SuperSpaceSlimeEntity extends Mob implements Enemy {
	private static final String HAS_EXPLODED_KEY = "HasExploded";
	private static final String WAS_ON_GROUND_KEY = "WasOnGround";
	
	private static final EntityDataAccessor<Integer> EXPLOSION_PROGRESS = SynchedEntityData.defineId(SuperSpaceSlimeEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> IS_EXPLODING = SynchedEntityData.defineId(SuperSpaceSlimeEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> HAS_EXPLODED = SynchedEntityData.defineId(SuperSpaceSlimeEntity.class, EntityDataSerializers.BOOLEAN);
	
	private final ServerBossEvent bossBar;
	
	public float targetStretch;
	public float stretch;
	public float prevStretch;
	
	private boolean onGroundLastTick;
	
	public float prevExplodingProgress = 0.0F;
	
	public SuperSpaceSlimeEntity(EntityType<? extends SuperSpaceSlimeEntity> entityType, Level world) {
		super(entityType, world);
		
		this.bossBar = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
		
		this.moveControl = new SuperSpaceSlimeMoveControl(this);
	}
	
	/**
	 * Creates a {@link AttributeSupplier.Builder} instance used for registering this entity's default attributes.
	 *
	 * @return a {@link AttributeSupplier.Builder} with default attribute information
	 */
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MAX_HEALTH, 300);
	}
	
	@Override
	public void registerGoals() {
		this.goalSelector.addGoal(0, new SuperSpaceSlimeExplosionGoal(this));
		this.goalSelector.addGoal(1, new SuperSpaceSlimeSwimmingGoal(this));
		this.goalSelector.addGoal(2, new SuperSpaceSlimeFaceTowardTargetGoal(this));
		this.goalSelector.addGoal(3, new SuperSpaceSlimeRandomLookGoal(this));
		this.goalSelector.addGoal(5, new SuperSpaceSlimeMoveGoal(this));
		
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (livingEntity) -> true));
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		
		builder.define(HAS_EXPLODED, false);
		builder.define(IS_EXPLODING, false);
		builder.define(EXPLOSION_PROGRESS, 0);
	}
	
	@Override
	public void tick() {
		this.stretch += (this.targetStretch - this.stretch) * 0.5F;
		
		this.prevStretch = this.stretch;
		
		super.tick();
		
		if (this.onGround() && !this.onGroundLastTick) {
			var size = 10;
			
			// spawn random landing particles around this entity's hitbox base
			for (var j = 0; j < size * 8; ++j) {
				var oX = this.random.nextFloat() * 6.2831855F;
				var oY = this.random.nextFloat() * 0.5F + 0.5F;
				
				var particleX = Mth.sin(oX) * (float) size * 0.5F * oY;
				var particleZ = Mth.cos(oX) * (float) size * 0.5F * oY;
				
				this.level().addParticle(this.getParticles(), this.getX() + (double) particleX, this.getY(), this.getZ() + (double) particleZ, 0.0D, 0.0D, 0.0D);
			}
			
			this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			this.playSound(SoundEvents.GLASS_BREAK, this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			
			this.targetStretch = -0.5F;
		} else if (!this.onGround() && this.onGroundLastTick) {
			this.targetStretch = 1.0F;
		}
		
		this.onGroundLastTick = this.onGround();
		
		this.updateStretch();
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		
		nbt.putBoolean(HAS_EXPLODED_KEY, this.hasExploded());
		nbt.putBoolean(WAS_ON_GROUND_KEY, this.onGroundLastTick);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		
		this.setHasExploded(nbt.getBoolean(HAS_EXPLODED_KEY));
		
		this.onGroundLastTick = nbt.getBoolean(WAS_ON_GROUND_KEY);
	}
	
	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		
		this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());
	}
	
	public void setHasExploded(boolean exploded) {
		this.entityData.set(HAS_EXPLODED, exploded);
	}
	
	public boolean hasExploded() {
		return this.entityData.get(HAS_EXPLODED);
	}
	
	protected ParticleOptions getParticles() {
		return AMParticles.SPACE_SLIME.get();
	}
	
	protected SoundEvent getSquishSound() {
		return SoundEvents.SLIME_SQUISH;
	}
	
	protected void updateStretch() {
		this.targetStretch *= 0.6F;
	}
	
	/**
	 * Called at the end of {@link SuperSpaceSlimeExplosionGoal}.
	 * <p>
	 * Spawns a large number of Space Slime around the Super Space Slime. Note that entity hitbox mechanics remove the need to set random velocities on the new slimes.
	 */
	public void explode() {
		for (var i = 0; i < 50; i++) {
			var level = level();
			var spaceSlime = AMEntityTypes.SPACE_SLIME.get().create(level);
			
			spaceSlime.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.NATURAL, null);
			
			level.addFreshEntity(spaceSlime);
			
			spaceSlime.teleportTo(getX(), getY(), getZ());
		}
	}
	
	@Override
	public void playerTouch(Player player) {
		this.damage(player);
	}
	
	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		
		this.bossBar.addPlayer(player);
	}
	
	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		
		this.bossBar.removePlayer(player);
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.SLIME_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SLIME_DEATH;
	}
	
	@Override
	public float getSoundVolume() {
		return 1;
	}
	
	@Override
	public void push(Entity entity) {
		super.push(entity);
		
		if (entity instanceof IronGolem) {
			this.damage((LivingEntity) entity);
		}
	}
	
	protected void damage(LivingEntity target) {
		if (this.isAlive()) {
			var size = 10;
			
			if (this.distanceToSqr(target) < 0.6D * (double) size * 0.6D * (double) size && this.hasLineOfSight(target) && target.hurt(this.damageSources().mobAttack(this), this.getDamageAmount())) {
				this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
			}
		}
	}
	
	protected float getDamageAmount() {
		return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
	}
	
	@Override
	public void jumpFromGround() {
		var vec3d = this.getDeltaMovement();
		this.setDeltaMovement(vec3d.x, this.getJumpPower(), vec3d.z);
		this.hasImpulse = true;
	}
	
	/**
	 * To prevent the Super Space Slime from dying through entity cramming during its explosion attack, its cramming functionality is disabled.
	 */
	@Override
	protected void pushEntities() {
	
	}
	
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		return 0.625F * dimensions.height();
	}
	
	public float getJumpSoundPitch() {
		return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F;
	}
	
	public SoundEvent getJumpSound() {
		return SoundEvents.SLIME_JUMP;
	}
	
	public int getTicksUntilNextJump() {
		return this.random.nextInt(20) + 10;
	}
	
	public boolean isExploding() {
		return this.entityData.get(IS_EXPLODING);
	}
	
	public void setExploding(boolean exploding) {
		this.entityData.set(IS_EXPLODING, exploding);
	}
	
	public int getExplodingProgress() {
		return this.entityData.get(EXPLOSION_PROGRESS);
	}
	
	public void setExplodingProgress(int progress) {
		this.entityData.set(EXPLOSION_PROGRESS, progress);
	}
}
