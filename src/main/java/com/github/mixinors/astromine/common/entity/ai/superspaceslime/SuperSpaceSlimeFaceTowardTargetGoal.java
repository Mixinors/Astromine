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

package com.github.mixinors.astromine.common.entity.ai.superspaceslime;

import com.github.mixinors.astromine.common.entity.slime.SuperSpaceSlimeEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class SuperSpaceSlimeFaceTowardTargetGoal extends Goal {
	private final SuperSpaceSlimeEntity slime;
	
	private int ticksLeft;
	
	public SuperSpaceSlimeFaceTowardTargetGoal(SuperSpaceSlimeEntity slime) {
		this.slime = slime;
		
		this.setControls(EnumSet.of(Goal.Control.LOOK));
	}
	
	@Override
	public boolean canStart() {
		var target = slime.getTarget();
		
		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else {
			return (!(target instanceof PlayerEntity player)) || !(player.getAbilities().invulnerable) && slime.getMoveControl() instanceof SuperSpaceSlimeMoveControl;
		}
	}
	
	@Override
	public boolean shouldContinue() {
		var target = slime.getTarget();
		
		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else if (target instanceof PlayerEntity player && player.getAbilities().invulnerable) {
			return false;
		} else {
			return --this.ticksLeft > 0;
		}
	}
	
	@Override
	public void start() {
		ticksLeft = 300;
		
		super.start();
	}
	
	@Override
	public void tick() {
		slime.lookAtEntity(slime.getTarget(), 10.0F, 10.0F);
		
		((SuperSpaceSlimeMoveControl) slime.getMoveControl()).look(slime.getYaw(), true);
	}
}
