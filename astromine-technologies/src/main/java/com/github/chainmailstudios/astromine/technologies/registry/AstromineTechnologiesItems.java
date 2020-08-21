package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.common.item.DrillItem;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.minecraft.item.Item;

public class AstromineTechnologiesItems extends AstromineItems {
	public static void initialize() {
		public static final Item BASIC_DRILL = register("basic_drill", new DrillItem(AstromineFoundationsToolMaterials.BASIC_DRILL, 1, -2.8F, 1, 90000, getBasicSettings().maxCount(1)));
		public static final Item ADVANCED_DRILL = register("advanced_drill", new DrillItem(AstromineFoundationsToolMaterials.ADVANCED_DRILL, 1, -2.8F, 1, 240000, getBasicSettings().maxCount(1)));
		public static final Item ELITE_DRILL = register("elite_drill", new DrillItem(AstromineFoundationsToolMaterials.ELITE_DRILL, 1, -2.8F, 1, 640000, getBasicSettings().maxCount(1)));
	}
}
