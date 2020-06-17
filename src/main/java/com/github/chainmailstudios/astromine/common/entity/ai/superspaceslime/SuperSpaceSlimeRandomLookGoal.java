package com.github.chainmailstudios.astromine.common.entity.ai.superspaceslime;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffects;

import com.github.chainmailstudios.astromine.common.entity.SuperSpaceSlimeEntity;

import java.util.EnumSet;

public class SuperSpaceSlimeRandomLookGoal extends Goal {

	private final SuperSpaceSlimeEntity slime;
	private float targetYaw;
	private int timer;

	public SuperSpaceSlimeRandomLookGoal(SuperSpaceSlimeEntity slime) {
		this.slime = slime;
		this.setControls(EnumSet.of(Goal.Control.LOOK));
	}

	@Override
	public boolean canStart() {
		boolean validTarget = this.slime.getTarget() == null;
		boolean validState = this.slime.isOnGround() || this.slime.isTouchingWater() || this.slime.isInLava() || this.slime.hasStatusEffect(StatusEffects.LEVITATION);
		boolean hasSlimeMoveControls = this.slime.getMoveControl() instanceof SuperSpaceSlimeMoveControl;

		return validTarget && validState && hasSlimeMoveControls;
	}

	@Override
	public void tick() {
		this.timer = this.timer - 1;

		if (this.timer <= 0) {
			this.timer = 40 + this.slime.getRandom().nextInt(60);
			this.targetYaw = (float) this.slime.getRandom().nextInt(360);
		}

		((SuperSpaceSlimeMoveControl) this.slime.getMoveControl()).look(this.targetYaw, false);
	}
}
