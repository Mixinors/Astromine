package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import com.github.chainmailstudios.astromine.world.AstromineDimensionType;

public class AstromineGravities {
    public static void initialize() {
        GravityRegistry.INSTANCE.register(AstromineDimensionType.KEY_ID, 0.01D);
    }
}
