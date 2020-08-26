package com.github.chainmailstudios.astromine.registry;

import net.minecraft.advancement.criterion.Criteria;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.advancement.LaunchRocketCriterion;
import com.github.chainmailstudios.astromine.advancement.TrickedPiglinCriterion;

public class AstromineCriteria {
	public static final TrickedPiglinCriterion TRICKED_PIGLIN = Criteria.register(new TrickedPiglinCriterion(AstromineCommon.identifier("tricked_piglin")));
	public static final LaunchRocketCriterion LAUNCH_ROCKET = Criteria.register(new LaunchRocketCriterion(AstromineCommon.identifier("launch_rocket")));

	public static void initialize() {

	}
}
