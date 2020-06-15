package com.github.chainmailstudios.astromine.common.entity.ai;

import com.github.chainmailstudios.astromine.common.entity.SpaceSlimeEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

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
		return slime.getFloatingCooldown() <= 0 && slime.world.random.nextInt(10) == 0;
	}

	@Override
	public void start() {
		this.slime.setFloating(true);
		this.ticksLeft = 20 * 10;
		super.start();
	}

	@Override
	public void stop() {
		slime.setFloating(false);
		slime.setFloatingCooldown(500);
		slime.setFloatingProgress(0);
		super.stop();
	}

	@Override
	public boolean shouldContinue() {
		return --this.ticksLeft > 0 && !slime.isOnGround() && slime.world.getBlockState(slime.getBlockPos().down()).isAir();
	}

	@Override
	public void tick() {
		// wait till slime is on ground
		if (slime.isOnGround()) {
			slime.move(MovementType.SELF, new Vec3d(0, 0.1, 0));
		}

		slime.setFloatingProgress(slime.getFloatingProgress() + 1);
	}
}
