package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.common.generator.material.MaterialItemType;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsMaterialSets;
import net.minecraft.util.Identifier;

public class BeaconPaymentTagGenerator extends GenericSetTagGenerator {
	public BeaconPaymentTagGenerator(MaterialItemType type) {
		super("beacon_payment", new Identifier("beacon_payment_items"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return super.shouldGenerate(set) && !set.equals(AstromineFoundationsMaterialSets.RAW_NETHERITE) && !set.equals(AstromineFoundationsMaterialSets.COAL) &&
		       !set.equals(AstromineFoundationsMaterialSets.CHARCOAL) && !set.equals(AstromineFoundationsMaterialSets.QUARTZ) && !set.equals(AstromineFoundationsMaterialSets.LAPIS);
	}
}
