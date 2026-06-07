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
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class SuperSpaceSlimeFaceTowardTargetGoal extends Goal {
	private final SuperSpaceSlimeEntity slime;
	
	private int ticksLeft;
	
	public SuperSpaceSlimeFaceTowardTargetGoal(SuperSpaceSlimeEntity slime) {
		this.slime = slime;
		
		this.setFlags(EnumSet.of(Goal.Flag.LOOK));
	}
	
	@Override
	public boolean canUse() {
		var target = slime.getTarget();
		
		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else {
			return (!(target instanceof Player player)) || !(player.getAbilities().invulnerable) && slime.getMoveControl() instanceof SuperSpaceSlimeMoveControl;
		}
	}
	
	@Override
	public boolean canContinueToUse() {
		var target = slime.getTarget();
		
		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else if (target instanceof Player player && player.getAbilities().invulnerable) {
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
		slime.lookAt(slime.getTarget(), 10.0F, 10.0F);
		
		((SuperSpaceSlimeMoveControl) slime.getMoveControl()).look(slime.getYRot(), true);
	}
}
