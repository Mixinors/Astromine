package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.BreathableRegistry;
import net.minecraft.entity.EntityType;

public class AstromineBreathables {
	public static void initialize() {
		BreathableRegistry.INSTANCE.register(EntityType.PLAYER, AstromineFluids.OXYGEN);
	}
}
