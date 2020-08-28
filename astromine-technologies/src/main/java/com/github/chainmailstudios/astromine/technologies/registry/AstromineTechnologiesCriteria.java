package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.technologies.common.advancement.LaunchRocketCriterion;
import com.github.chainmailstudios.astromine.registry.AstromineCriteria;

public class AstromineTechnologiesCriteria extends AstromineCriteria {
	public static final LaunchRocketCriterion LAUNCH_ROCKET = register(new LaunchRocketCriterion(AstromineCommon.identifier("launch_rocket")));

	public static void initialize() {

	}
}
