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

import com.github.chainmailstudios.astromine.discoveries.common.entity.SuperSpaceSlimeEntity;

import java.util.EnumSet;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;

public class SuperSpaceSlimeRandomLookGoal extends Goal {

	private final SuperSpaceSlimeEntity slime;
	private float targetYaw;
	private int timer;

	public SuperSpaceSlimeRandomLookGoal(SuperSpaceSlimeEntity slime) {
		this.slime = slime;
		this.setFlags(EnumSet.of(Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		boolean validTarget = this.slime.getTarget() == null;
		boolean validState = this.slime.isOnGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION);
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
