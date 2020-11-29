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

package com.github.chainmailstudios.astromine.discoveries.common.entity.ai.superspaceslime;

import com.github.chainmailstudios.astromine.discoveries.common.entity.SpaceSlimeEntity;

import java.util.EnumSet;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class SpaceSlimeJumpHoverGoal extends Goal {

	private final SpaceSlimeEntity slime;
	private int ticksLeft;

	public SpaceSlimeJumpHoverGoal(SpaceSlimeEntity slime) {
		this.slime = slime;
		this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		// todo: ensure slime has space
		return this.slime.getFloatingCooldown() <= 0 && this.slime.level.random.nextInt(10) == 0;
	}

	@Override
	public boolean canContinueToUse() {
		return --this.ticksLeft > 0 && !this.slime.isOnGround() && this.slime.level.getBlockState(this.slime.blockPosition().below()).isAir();
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
			this.slime.move(MoverType.SELF, new Vec3(0, 0.1, 0));
		}

		this.slime.setFloatingProgress(this.slime.getFloatingProgress() + 1);
	}
}
