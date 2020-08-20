package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSets;
import net.minecraft.util.Identifier;

public class BeaconPaymentTagGenerator extends GenericSetTagGenerator {
	public BeaconPaymentTagGenerator(MaterialItemType type) {
		super("beacon_payment", new Identifier("beacon_payment_items"), type);
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return super.shouldGenerate(set) && !set.equals(MaterialSets.RAW_NETHERITE) && !set.equals(MaterialSets.COAL) &&
		       !set.equals(MaterialSets.CHARCOAL) && !set.equals(MaterialSets.QUARTZ) && !set.equals(MaterialSets.LAPIS);
	}
}
