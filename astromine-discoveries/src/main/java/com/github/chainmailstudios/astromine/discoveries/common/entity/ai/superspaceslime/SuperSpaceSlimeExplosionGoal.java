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
import net.minecraft.world.entity.ai.goal.Goal;

public class SuperSpaceSlimeExplosionGoal extends Goal {

	private final SuperSpaceSlimeEntity slime;
	private int ticksLeft = 0;

	public SuperSpaceSlimeExplosionGoal(SuperSpaceSlimeEntity slime) {
		this.slime = slime;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	/**
	 * Returns whether this goal is valid for the current {@link SuperSpaceSlimeEntity}.
	 * <p>
	 * This goal can only start when:
	 * <ul>
	 * <li>The Super Space Slime's health is 50% or less</li>
	 * <li>The Super Space Slime's has not exploded yet</li>
	 * </ul>
	 *
	 * @return whether the current {@link SuperSpaceSlimeEntity} can explode
	 */
	@Override
	public boolean canUse() {
		return this.slime.getHealth() <= this.slime.getMaxHealth() * .5 && !this.slime.hasExploded();
	}

	@Override
	public boolean canContinueToUse() {
		return this.ticksLeft > 0;
	}

	@Override
	public void start() {
		this.slime.setExploding(true);
		this.ticksLeft = 100;
		this.slime.setExplodingProgress(100);
		super.start();
	}

	@Override
	public void stop() {
		this.slime.setExploding(false);
		this.slime.setExplodingProgress(0);
		this.slime.setHasExploded(true);
		this.slime.explode();

		super.stop();
	}

	@Override
	public void tick() {
		this.ticksLeft--;
		this.slime.setExplodingProgress(this.ticksLeft);
		super.tick();
	}
}
