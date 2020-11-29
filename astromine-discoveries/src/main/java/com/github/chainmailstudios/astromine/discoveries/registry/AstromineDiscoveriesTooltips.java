package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.registry.AstromineTooltips;

public class AstromineDiscoveriesTooltips extends AstromineTooltips {
    public static void initialize() {
        registerPrimitive(AstromineDiscoveriesItems.PRIMITIVE_ROCKET_BOOSTER);
        registerPrimitive(AstromineDiscoveriesItems.PRIMITIVE_ROCKET_FUEL_TANK);
        registerPrimitive(AstromineDiscoveriesItems.PRIMITIVE_ROCKET_HULL);
        registerPrimitive(AstromineDiscoveriesItems.PRIMITIVE_ROCKET_PLATING);

        registerSpecial(AstromineDiscoveriesItems.PRIMITIVE_ROCKET);
        registerSpecial(AstromineDiscoveriesItems.SPACE_SUIT_BOOTS);
        registerSpecial(AstromineDiscoveriesItems.SPACE_SUIT_CHESTPLATE);
        registerSpecial(AstromineDiscoveriesItems.SPACE_SUIT_HELMET);
        registerSpecial(AstromineDiscoveriesItems.SPACE_SUIT_LEGGINGS);
    }
}
