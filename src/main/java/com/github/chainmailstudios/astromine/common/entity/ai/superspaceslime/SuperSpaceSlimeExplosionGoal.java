package com.github.chainmailstudios.astromine.common.entity.ai.superspaceslime;

import com.github.chainmailstudios.astromine.common.entity.SuperSpaceSlimeEntity;
import net.minecraft.entity.ai.goal.Goal;

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
        return slime.getHealth() <= slime.getMaxHealth() * .5 && !slime.hasExploded();
    }

    @Override
    public void start() {
        slime.setExploding(true);
        ticksLeft = 100;
        slime.setExplodingProgress(100);
        super.start();
    }

    @Override
    public void stop() {
        slime.setExploding(false);
        slime.setExplodingProgress(0);
        slime.setHasExploded(true);
        slime.explode();

        super.stop();
    }

    @Override
    public void tick() {
        ticksLeft--;
        slime.setExplodingProgress(ticksLeft);
        super.tick();
    }

    @Override
    public boolean shouldContinue() {
        return ticksLeft > 0;
    }
}
