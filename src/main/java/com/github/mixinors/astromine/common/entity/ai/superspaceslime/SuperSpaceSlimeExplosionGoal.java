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

import java.util.EnumSet;

public class SuperSpaceSlimeExplosionGoal extends Goal {
	private final SuperSpaceSlimeEntity slime;
	
	private int ticksLeft = 0;
	
	public SuperSpaceSlimeExplosionGoal(SuperSpaceSlimeEntity slime) {
		this.slime = slime;
		
		this.setControls(EnumSet.of(Control.MOVE, Control.JUMP));
	}
	
	@Override
	public boolean canStart() {
		return slime.getHealth() <= slime.getMaxHealth() * 0.5D && !slime.hasExploded();
	}
	
	@Override
	public boolean shouldContinue() {
		return ticksLeft > 0;
	}
	
	@Override
	public void start() {
		slime.setExploding(true);
		slime.setExplodingProgress(100);
		
		ticksLeft = 100;
		
		super.start();
	}
	
	@Override
	public void stop() {
		slime.setExploding(false);
		slime.setExplodingProgress(0);
		slime.setHasExploded(true);
		
		this.slime.explode();
		
		super.stop();
	}
	
	@Override
	public void tick() {
		ticksLeft--;
		
		slime.setExplodingProgress(ticksLeft);
		
		super.tick();
	}
}
