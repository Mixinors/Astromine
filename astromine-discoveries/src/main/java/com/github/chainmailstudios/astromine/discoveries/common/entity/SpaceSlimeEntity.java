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

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import com.github.chainmailstudios.astromine.discoveries.common.entity.ai.superspaceslime.SpaceSlimeJumpHoverGoal;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesParticles;

import java.util.Random;

public class SpaceSlimeEntity extends SlimeEntity {
	private static final TrackedData<Integer> FLOATING_PROGRESS = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> FLOATING = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private int floatingCooldown;

	public SpaceSlimeEntity(EntityType<? extends SlimeEntity> entityType, World world) {
		super(entityType, world);
		this.floatingCooldown = world.random.nextInt(200);
	}

	public static boolean canSpawnInDark(EntityType<? extends SpaceSlimeEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && isSpawnDark(world, pos, random) && canMobSpawn(type, world, spawnReason, pos, random) && random.nextDouble() <= .15;
	}

	public static boolean isSpawnDark(WorldAccess world, BlockPos pos, Random random) {
		if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
			return false;
		} else {
			int i = ((ServerWorld) world).isThundering() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos);
			return i <= random.nextInt(8);
		}
	}

	@Override
	public void initGoals() {
		super.initGoals();
		this.goalSelector.add(3, new SpaceSlimeJumpHoverGoal(this));
	}

	@Override
	public void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(FLOATING, false);
		this.dataTracker.startTracking(FLOATING_PROGRESS, 0);
	}

	@Override
	protected ParticleEffect getParticles() {
		return AstromineDiscoveriesParticles.SPACE_SLIME;
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

	@Override
	public SoundEvent getSquishSound() {
		return SoundEvents.BLOCK_GLASS_BREAK;
	}
}
