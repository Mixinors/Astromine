package com.github.chainmailstudios.astromine.common.entity.ai.superspaceslime;

import com.github.chainmailstudios.astromine.common.entity.SuperSpaceSlimeEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SuperSpaceSlimeMoveGoal extends Goal {

    private final SuperSpaceSlimeEntity slime;

    public SuperSpaceSlimeMoveGoal(SuperSpaceSlimeEntity slime) {
        this.slime = slime;
        this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return !this.slime.hasVehicle();
    }

    @Override
    public void tick() {
        ((SuperSpaceSlimeMoveControl) this.slime.getMoveControl()).move(1.0D);
    }
}