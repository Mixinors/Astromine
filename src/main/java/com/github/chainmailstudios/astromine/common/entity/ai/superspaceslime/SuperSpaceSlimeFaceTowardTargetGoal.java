package com.github.chainmailstudios.astromine.common.entity.ai.superspaceslime;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import com.github.chainmailstudios.astromine.common.entity.SuperSpaceSlimeEntity;

import java.util.EnumSet;

/**
 * This goal instructs the given {@link SuperSpaceSlimeEntity} to look at its target for up to 300 ticks.
 */
public class SuperSpaceSlimeFaceTowardTargetGoal extends Goal {

	private final SuperSpaceSlimeEntity slime;
	private int ticksLeft;

	public SuperSpaceSlimeFaceTowardTargetGoal(SuperSpaceSlimeEntity slime) {
		this.slime = slime;
		this.setControls(EnumSet.of(Goal.Control.LOOK));
	}

	@Override
	public boolean canStart() {
		LivingEntity livingEntity = this.slime.getTarget();

		if (livingEntity == null) {
			return false;
		} else if (!livingEntity.isAlive()) {
			return false;
		} else {
			return (!(livingEntity instanceof PlayerEntity) || !((PlayerEntity) livingEntity).abilities.invulnerable) && this.slime.getMoveControl() instanceof SuperSpaceSlimeMoveControl;
		}
	}

	@Override
	public boolean shouldContinue() {
		LivingEntity livingEntity = this.slime.getTarget();

		if (livingEntity == null) {
			return false;
		} else if (!livingEntity.isAlive()) {
			return false;
		} else if (livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).abilities.invulnerable) {
			return false;
		} else {
			return --this.ticksLeft > 0;
		}
	}

	@Override
	public void start() {
		this.ticksLeft = 300;
		super.start();
	}

	@Override
	public void tick() {
		this.slime.lookAtEntity(this.slime.getTarget(), 10.0F, 10.0F);
		((SuperSpaceSlimeMoveControl) this.slime.getMoveControl()).look(this.slime.yaw, true);
	}
}