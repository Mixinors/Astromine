package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;

public class BeaconPaymentTagGenerator extends GenericTagGenerator {
	public BeaconPaymentTagGenerator(MaterialItemType type) {
		super("beacon_payment", new Identifier("beacon_payment_items"), type);
	}
}
