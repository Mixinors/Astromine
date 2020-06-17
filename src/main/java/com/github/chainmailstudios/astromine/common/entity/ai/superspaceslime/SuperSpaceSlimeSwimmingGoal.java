package com.github.chainmailstudios.astromine.common.entity.ai.superspaceslime;

import java.util.EnumSet;

import com.github.chainmailstudios.astromine.common.entity.SuperSpaceSlimeEntity;

import net.minecraft.entity.ai.goal.Goal;

public class SuperSpaceSlimeSwimmingGoal extends Goal {

	private final SuperSpaceSlimeEntity slime;

	public SuperSpaceSlimeSwimmingGoal(SuperSpaceSlimeEntity slime) {
		this.slime = slime;
		this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
		slime.getNavigation().setCanSwim(true);
	}

	@Override
	public boolean canStart() {
		boolean validState = this.slime.isTouchingWater() || this.slime.isInLava();
		boolean hasSlimeMoveControls = this.slime.getMoveControl() instanceof SuperSpaceSlimeMoveControl;

		return validState && hasSlimeMoveControls;
	}

	@Override
	public void tick() {
		if (this.slime.getRandom().nextFloat() < 0.8F) {
			this.slime.getJumpControl().setActive();
		}

		((SuperSpaceSlimeMoveControl) this.slime.getMoveControl()).move(1.2D);
	}
}
