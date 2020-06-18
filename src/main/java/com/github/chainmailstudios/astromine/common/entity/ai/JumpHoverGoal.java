package com.github.chainmailstudios.astromine.common.entity.ai;

import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import com.github.chainmailstudios.astromine.common.entity.SpaceSlimeEntity;

import java.util.EnumSet;

public class JumpHoverGoal extends Goal {

	private final SpaceSlimeEntity slime;
	private int ticksLeft;

	public JumpHoverGoal(SpaceSlimeEntity slime) {
		this.slime = slime;
		this.setControls(EnumSet.of(Control.JUMP, Control.LOOK, Control.MOVE));
	}

	@Override
	public boolean canStart() {
		// todo: ensure slime has space
		return this.slime.getFloatingCooldown() <= 0 && this.slime.world.random.nextInt(10) == 0;
	}

	@Override
	public boolean shouldContinue() {
		return --this.ticksLeft > 0 && !this.slime.isOnGround() && this.slime.world.getBlockState(this.slime.getBlockPos().down()).isAir();
	}

	@Override
	public void start() {
		this.slime.setFloating(true);
		this.ticksLeft = 20 * 10;
		super.start();
	}

	@Override
	public void stop() {
		this.slime.setFloating(false);
		this.slime.setFloatingCooldown(500);
		this.slime.setFloatingProgress(0);
		super.stop();
	}

	@Override
	public void tick() {
		// wait till slime is on ground
		if (this.slime.isOnGround()) {
			this.slime.move(MovementType.SELF, new Vec3d(0, 0.1, 0));
		}

		this.slime.setFloatingProgress(this.slime.getFloatingProgress() + 1);
	}
}
