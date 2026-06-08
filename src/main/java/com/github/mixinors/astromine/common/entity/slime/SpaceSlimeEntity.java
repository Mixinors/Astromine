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

import com.github.mixinors.astromine.common.entity.ai.superspaceslime.SpaceSlimeJumpHoverGoal;
import com.github.mixinors.astromine.registry.common.AMParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;

public class SpaceSlimeEntity extends Slime {
	private static final EntityDataAccessor<Integer> FLOATING_PROGRESS = SynchedEntityData.defineId(SpaceSlimeEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> FLOATING = SynchedEntityData.defineId(SpaceSlimeEntity.class, EntityDataSerializers.BOOLEAN);
	
	private int floatingCooldown;
	
	public float prevFloatingProgress = 0.0F;
	
	public SpaceSlimeEntity(EntityType<? extends Slime> entityType, Level world) {
		super(entityType, world);
		
		this.floatingCooldown = world.random.nextInt(200);
	}
	
	public static boolean canSpawnInDark(EntityType<? extends SpaceSlimeEntity> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && isSpawnDark(world, pos, random) && checkMobSpawnRules(type, world, spawnReason, pos, random) && random.nextDouble() <= 0.15D;
	}
	
	public static boolean isSpawnDark(LevelAccessor world, BlockPos pos, RandomSource random) {
		if (world.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
			return false;
		} else {
			var light = ((ServerLevel) world).isThundering() ? world.getMaxLocalRawBrightness(pos, 10) : world.getMaxLocalRawBrightness(pos);
			
			return light <= random.nextInt(8);
		}
	}
	
	@Override
	public void registerGoals() {
		super.registerGoals();
		
		this.goalSelector.addGoal(3, new SpaceSlimeJumpHoverGoal(this));
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		
		builder.define(FLOATING, false);
		builder.define(FLOATING_PROGRESS, 0);
	}
	
	@Override
	protected ParticleOptions getParticleType() {
		return AMParticles.SPACE_SLIME.get();
	}
	
	@Override
	public void tick() {
		this.prevFloatingProgress = this.getFloatingProgress();
		
		if (this.floatingCooldown > 0) {
			this.floatingCooldown--;
		}
		
		super.tick();
	}
	
	@Override
	protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
		return 0;
	}
	
	@Override
	public boolean isNoGravity() {
		return this.entityData.get(FLOATING);
	}
	
	public int getFloatingCooldown() {
		return this.floatingCooldown;
	}
	
	public void setFloatingCooldown(int cooldown) {
		this.floatingCooldown = cooldown;
	}
	
	public boolean isFloating() {
		return this.entityData.get(FLOATING);
	}
	
	public void setFloating(boolean floating) {
		this.entityData.set(FLOATING, floating);
	}
	
	public int getFloatingProgress() {
		return this.entityData.get(FLOATING_PROGRESS);
	}
	
	public void setFloatingProgress(int progress) {
		this.entityData.set(FLOATING_PROGRESS, progress);
	}
	
	@Override
	public SoundEvent getSquishSound() {
		return SoundEvents.GLASS_BREAK;
	}
}
