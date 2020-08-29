package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.discoveries.common.advancement.LaunchRocketCriterion;
import com.github.chainmailstudios.astromine.registry.AstromineCriteria;

public class AstromineDiscoveriesCriteria extends AstromineCriteria {
	public static final LaunchRocketCriterion LAUNCH_ROCKET = register(new LaunchRocketCriterion(AstromineCommon.identifier("launch_rocket")));

	public static void initialize() {

	}
}
