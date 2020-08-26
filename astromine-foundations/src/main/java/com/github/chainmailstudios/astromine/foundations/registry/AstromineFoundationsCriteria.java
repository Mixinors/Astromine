package com.github.chainmailstudios.astromine.foundations.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.common.criterion.MetiteOreUnderestimationCriterion;
import com.github.chainmailstudios.astromine.foundations.common.criterion.ProperlyUseFireExtinguisherCriterion;
import com.github.chainmailstudios.astromine.foundations.common.criterion.UseFireExtinguisherCriterion;

import net.minecraft.advancement.criterion.Criteria;

public class AstromineFoundationsCriteria {
	public static final MetiteOreUnderestimationCriterion UNDERESTIMATE_METITE = Criteria.register(new MetiteOreUnderestimationCriterion(AstromineCommon.identifier("underestimate_metite")));
	public static final UseFireExtinguisherCriterion USE_FIRE_EXTINGUISHER = Criteria.register(new UseFireExtinguisherCriterion(AstromineCommon.identifier("use_fire_extinguisher")));
	public static final ProperlyUseFireExtinguisherCriterion PROPERLY_USE_FIRE_EXTINGUISHER = Criteria.register(new ProperlyUseFireExtinguisherCriterion(AstromineCommon.identifier("properly_use_fire_extinguisher")));

	public static void initialize() {

	}
}
