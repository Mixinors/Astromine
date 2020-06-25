package com.github.chainmailstudios.astromine.common.entity.ai.superspaceslime;

import net.minecraft.entity.ai.goal.Goal;

import com.github.chainmailstudios.astromine.common.entity.SuperSpaceSlimeEntity;

import java.util.EnumSet;

public class SuperSpaceSlimeExplosionGoal extends Goal {

	private final SuperSpaceSlimeEntity slime;
	private int ticksLeft = 0;

	public SuperSpaceSlimeExplosionGoal(SuperSpaceSlimeEntity slime) {
		this.slime = slime;
		this.setControls(EnumSet.of(Control.MOVE, Control.JUMP));
	}

	/**
	 * Returns whether this goal is valid for the current {@link SuperSpaceSlimeEntity}.
	 *
	 * <p>This goal can only start when:
	 * <ul>
	 *  <li>The Super Space Slime's health is 50% or less</li>
	 *  <li>The Super Space Slime's has not exploded yet</li>
	 * </ul>
	 *
	 * @return whether the current {@link SuperSpaceSlimeEntity} can explode
	 */
	@Override
	public boolean canStart() {
		return this.slime.getHealth() <= this.slime.getMaxHealth() * .5 && !this.slime.hasExploded();
	}

	@Override
	public boolean shouldContinue() {
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
