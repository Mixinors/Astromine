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

import com.github.mixinors.astromine.common.entity.slime.SpaceSlimeEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class SpaceSlimeJumpHoverGoal extends Goal {
	private final SpaceSlimeEntity slime;
	
	private int ticksLeft;
	
	public SpaceSlimeJumpHoverGoal(SpaceSlimeEntity slime) {
		this.slime = slime;
		
		this.setControls(EnumSet.of(Control.JUMP, Control.LOOK, Control.MOVE));
	}
	
	@Override
	public boolean canStart() {
		// TODO: Ensure Slime has space to float in.
		return slime.getFloatingCooldown() <= 0 && slime.world.random.nextInt(10) == 0;
	}
	
	@Override
	public boolean shouldContinue() {
		return --ticksLeft > 0 && !slime.isOnGround() && slime.world.getBlockState(slime.getBlockPos().down()).isAir();
	}
	
	@Override
	public void start() {
		slime.setFloating(true);
		
		ticksLeft = 20 * 10;
		
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
	public void tick() {
		if (slime.isOnGround()) {
			slime.move(MovementType.SELF, new Vec3d(0.0D, 0.1D, 0.0D));
		}
		
		this.slime.setFloatingProgress(slime.getFloatingProgress() + 1);
	}
}
