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
import net.minecraft.entity.effect.StatusEffects;

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
		var validTarget = slime.getTarget() == null;
		
		var validState = slime.isOnGround() || slime.isTouchingWater() || slime.isInLava() || slime.hasStatusEffect(StatusEffects.LEVITATION);
		
		var hasSlimeMoveControls = slime.getMoveControl() instanceof SuperSpaceSlimeMoveControl;
		
		return validTarget && validState && hasSlimeMoveControls;
	}
	
	@Override
	public void tick() {
		this.timer = this.timer - 1;
		
		if (timer <= 0) {
			timer = 40 + slime.getRandom().nextInt(60);
			
			targetYaw = (float) slime.getRandom().nextInt(360);
		}
		
		((SuperSpaceSlimeMoveControl) slime.getMoveControl()).look(targetYaw, false);
	}
}
