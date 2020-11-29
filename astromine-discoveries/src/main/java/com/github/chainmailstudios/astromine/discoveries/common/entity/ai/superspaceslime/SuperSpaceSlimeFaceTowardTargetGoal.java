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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

/**
 * This goal instructs the given {@link SuperSpaceSlimeEntity} to look at its target for up to 300 ticks.
 */
public class SuperSpaceSlimeFaceTowardTargetGoal extends Goal {

	private final SuperSpaceSlimeEntity slime;
	private int ticksLeft;

	public SuperSpaceSlimeFaceTowardTargetGoal(SuperSpaceSlimeEntity slime) {
		this.slime = slime;
		this.setFlags(EnumSet.of(Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity livingEntity = this.slime.getTarget();

		if (livingEntity == null) {
			return false;
		} else if (!livingEntity.isAlive()) {
			return false;
		} else {
			return (!(livingEntity instanceof Player) || !((Player) livingEntity).abilities.invulnerable) && this.slime.getMoveControl() instanceof SuperSpaceSlimeMoveControl;
		}
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity livingEntity = this.slime.getTarget();

		if (livingEntity == null) {
			return false;
		} else if (!livingEntity.isAlive()) {
			return false;
		} else if (livingEntity instanceof Player && ((Player) livingEntity).abilities.invulnerable) {
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
		this.slime.lookAt(this.slime.getTarget(), 10.0F, 10.0F);
		((SuperSpaceSlimeMoveControl) this.slime.getMoveControl()).look(this.slime.yRot, true);
	}
}
