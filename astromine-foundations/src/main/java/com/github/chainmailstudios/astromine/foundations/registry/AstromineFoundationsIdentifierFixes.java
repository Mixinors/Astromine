package com.github.chainmailstudios.astromine.foundations.registry;

import com.github.chainmailstudios.astromine.registry.AstromineIdentifierFixes;

public class AstromineFoundationsIdentifierFixes extends AstromineIdentifierFixes {
	public static void initialize() {
		register("metite_plates", "metite_plate");
		register("stellum_plates", "stellum_plate");
		register("univite_plates", "univite_plate");
		register("lunum_plates", "lunum_plate");

		register("copper_plates", "copper_plate");
		register("tin_plates", "tin_plate");
		register("silver_plates", "silver_plate");
		register("lead_plates", "lead_plate");

		register("steel_plates", "steel_plate");
		register("bronze_plates", "bronze_plate");
		register("electrum_plates", "electrum_plate");
		register("rose_gold_plates", "rose_gold_plate");
		register("sterling_silver_plates", "sterling_silver_plate");
		register("fools_gold_plates", "fools_gold_plate");
		register("meteoric_steel_plates", "meteoric_steel_plate");

		register("iron_plates", "iron_plate");
		register("gold_plates", "gold_plate");
		register("netherite_plates", "netherite_plate");
	}
}
