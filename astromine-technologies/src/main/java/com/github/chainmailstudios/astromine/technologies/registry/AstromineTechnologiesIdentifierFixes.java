package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.registry.AstromineIdentifierFixes;

public class AstromineTechnologiesIdentifierFixes extends AstromineIdentifierFixes {
    public static void initialize() {
        register("fluid_cable", "fluid_pipe");

        register("gas_canister", "large_portable_tank");
        register("pressurized_gas_canister", "portable_tank");

        register("fluid_inserter", "fluid_placer");
        register("fluid_extractor", "fluid_collector");

        register("primitive_presser", "primitive_press");
        register("basic_presser", "basic_press");
        register("advanced_presser", "advanced_press");
        register("elite_presser", "elite_press");

        register("primitive_liquid_generator", "primitive_fluid_generator");
        register("basic_liquid_generator", "basic_fluid_generator");
        register("advanced_liquid_generator", "advanced_fluid_generator");
        register("elite_liquid_generator", "elite_fluid_generator");

        register("primitive_electric_smelter", "primitive_electric_furnace");
        register("basic_electric_smelter", "basic_electric_furnace");
        register("advanced_electric_smelter", "advanced_electric_furnace");
        register("elite_electric_smelter", "elite_electric_furnace");
    }
}
