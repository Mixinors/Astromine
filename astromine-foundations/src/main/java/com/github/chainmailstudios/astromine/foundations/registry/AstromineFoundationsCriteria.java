package com.github.chainmailstudios.astromine.foundations.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.common.criterion.MetiteOreUnderestimationCriterion;
import net.minecraft.advancement.criterion.Criteria;

public class AstromineFoundationsCriteria {
	public static final MetiteOreUnderestimationCriterion METITE_ORE_UNDERESTIMATION = Criteria.register(new MetiteOreUnderestimationCriterion(AstromineCommon.identifier("metite_ore_underestimation")));

	public static void initialize() {

	}
}
