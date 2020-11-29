package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.registry.AstromineTooltips;

public class AstromineTechnologiesTooltips extends AstromineTooltips {
    public static void initialize() {
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_ALLOY_SMELTER.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_BUFFER.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_CAPACITOR.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_ELECTRIC_FURNACE.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_ELECTROLYZER.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_FLUID_MIXER.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_FLUID_GENERATOR.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_MELTER.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_REFINERY.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_SOLID_GENERATOR.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_SOLIDIFIER.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_TANK.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_TRITURATOR.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_WIRE_MILL.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.PRIMITIVE_PRESS.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.VENT.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.BLOCK_PLACER.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.BLOCK_BREAKER.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.FLUID_PLACER.asItem());
        registerPrimitive(AstromineTechnologiesBlocks.FLUID_COLLECTOR.asItem());
        registerPrimitive(AstromineTechnologiesItems.PRIMITIVE_BATTERY);
        registerPrimitive(AstromineTechnologiesItems.PRIMITIVE_BATTERY_PACK);
        registerPrimitive(AstromineTechnologiesItems.PRIMITIVE_CIRCUIT);
        registerPrimitive(AstromineTechnologiesItems.PRIMITIVE_DRILL);
        registerPrimitive(AstromineTechnologiesItems.PRIMITIVE_DRILL_BASE);
        registerPrimitive(AstromineTechnologiesItems.PRIMITIVE_PLATING);
        registerPrimitive(AstromineTechnologiesItems.MACHINE_CHASSIS);
        registerPrimitive(AstromineTechnologiesItems.PORTABLE_TANK);
        registerPrimitive(AstromineTechnologiesItems.DRILL_HEAD);

        registerBasic(AstromineTechnologiesBlocks.BASIC_ALLOY_SMELTER.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_BUFFER.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_CAPACITOR.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_ELECTRIC_FURNACE.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_ELECTROLYZER.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_FLUID_MIXER.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_FLUID_GENERATOR.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_MELTER.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_REFINERY.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_SOLID_GENERATOR.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_SOLIDIFIER.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_TANK.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_TRITURATOR.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_WIRE_MILL.asItem());
        registerBasic(AstromineTechnologiesBlocks.BASIC_PRESS.asItem());
        registerBasic(AstromineTechnologiesItems.BASIC_BATTERY);
        registerBasic(AstromineTechnologiesItems.BASIC_BATTERY_PACK);
        registerBasic(AstromineTechnologiesItems.BASIC_CIRCUIT);
        registerBasic(AstromineTechnologiesItems.BASIC_DRILL);
        registerBasic(AstromineTechnologiesItems.BASIC_DRILL_BASE);
        registerBasic(AstromineTechnologiesItems.BASIC_PLATING);
        registerBasic(AstromineTechnologiesItems.BASIC_MACHINE_UPGRADE_KIT);
        registerBasic(AstromineTechnologiesItems.LARGE_PORTABLE_TANK);

        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_ALLOY_SMELTER.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_BUFFER.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_CAPACITOR.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_ELECTRIC_FURNACE.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_FLUID_GENERATOR.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_MELTER.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_REFINERY.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_SOLID_GENERATOR.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_SOLIDIFIER.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_TANK.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_TRITURATOR.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_WIRE_MILL.asItem());
        registerAdvanced(AstromineTechnologiesBlocks.ADVANCED_PRESS.asItem());
        registerAdvanced(AstromineTechnologiesItems.ADVANCED_BATTERY);
        registerAdvanced(AstromineTechnologiesItems.ADVANCED_BATTERY_PACK);
        registerAdvanced(AstromineTechnologiesItems.ADVANCED_CIRCUIT);
        registerAdvanced(AstromineTechnologiesItems.ADVANCED_DRILL);
        registerAdvanced(AstromineTechnologiesItems.ADVANCED_DRILL_BASE);
        registerAdvanced(AstromineTechnologiesItems.ADVANCED_PLATING);
        registerAdvanced(AstromineTechnologiesItems.ADVANCED_MACHINE_UPGRADE_KIT);

        registerElite(AstromineTechnologiesBlocks.ELITE_ALLOY_SMELTER.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_BUFFER.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_CAPACITOR.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_ELECTRIC_FURNACE.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_ELECTROLYZER.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_FLUID_MIXER.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_FLUID_GENERATOR.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_MELTER.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_REFINERY.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_SOLID_GENERATOR.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_SOLIDIFIER.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_TANK.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_TRITURATOR.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_WIRE_MILL.asItem());
        registerElite(AstromineTechnologiesBlocks.ELITE_PRESS.asItem());
        registerElite(AstromineTechnologiesItems.ELITE_BATTERY);
        registerElite(AstromineTechnologiesItems.ELITE_BATTERY_PACK);
        registerElite(AstromineTechnologiesItems.ELITE_CIRCUIT);
        registerElite(AstromineTechnologiesItems.ELITE_DRILL);
        registerElite(AstromineTechnologiesItems.ELITE_DRILL_BASE);
        registerElite(AstromineTechnologiesItems.ELITE_PLATING);
        registerElite(AstromineTechnologiesItems.ELITE_MACHINE_UPGRADE_KIT);

        registerCreative(AstromineTechnologiesBlocks.CREATIVE_BUFFER.asItem());
        registerCreative(AstromineTechnologiesBlocks.CREATIVE_CAPACITOR.asItem());
        registerCreative(AstromineTechnologiesBlocks.CREATIVE_TANK.asItem());
        registerCreative(AstromineTechnologiesItems.CREATIVE_BATTERY);
        registerCreative(AstromineTechnologiesItems.CREATIVE_BATTERY_PACK);

        registerSpecial(AstromineTechnologiesBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR.asItem());
        registerSpecial(AstromineTechnologiesBlocks.AIRLOCK.asItem());
        registerSpecial(AstromineTechnologiesBlocks.NUCLEAR_WARHEAD.asItem());
        registerSpecial(AstromineTechnologiesItems.HOLOGRAPHIC_CONNECTOR);
        registerSpecial(AstromineTechnologiesItems.GRAVITY_GAUNTLET);

        registerDescription("text.astromine.tooltip.solid_generator_description",
                AstromineTechnologiesBlocks.PRIMITIVE_SOLID_GENERATOR.asItem(),
                AstromineTechnologiesBlocks.BASIC_SOLID_GENERATOR.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_SOLID_GENERATOR.asItem(),
                AstromineTechnologiesBlocks.ELITE_SOLID_GENERATOR.asItem()
        );

        registerDescription("text.astromine.tooltip.fluid_generator_description",
                AstromineTechnologiesBlocks.PRIMITIVE_FLUID_GENERATOR.asItem(),
                AstromineTechnologiesBlocks.BASIC_FLUID_GENERATOR.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_FLUID_GENERATOR.asItem(),
                AstromineTechnologiesBlocks.ELITE_FLUID_GENERATOR.asItem()
        );

        registerDescription("text.astromine.tooltip.electric_furnace_description",
                AstromineTechnologiesBlocks.PRIMITIVE_ELECTRIC_FURNACE.asItem(),
                AstromineTechnologiesBlocks.BASIC_ELECTRIC_FURNACE.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_ELECTRIC_FURNACE.asItem(),
                AstromineTechnologiesBlocks.ELITE_ELECTRIC_FURNACE.asItem()
        );

        registerDescription("text.astromine.tooltip.alloy_smelter_description",
                AstromineTechnologiesBlocks.PRIMITIVE_ALLOY_SMELTER.asItem(),
                AstromineTechnologiesBlocks.BASIC_ALLOY_SMELTER.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_ALLOY_SMELTER.asItem(),
                AstromineTechnologiesBlocks.ELITE_ALLOY_SMELTER.asItem()
        );

        registerDescription("text.astromine.tooltip.triturator_description",
                AstromineTechnologiesBlocks.PRIMITIVE_TRITURATOR.asItem(),
                AstromineTechnologiesBlocks.BASIC_TRITURATOR.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_TRITURATOR.asItem(),
                AstromineTechnologiesBlocks.ELITE_TRITURATOR.asItem()
        );

        registerDescription("text.astromine.tooltip.press_description",
                AstromineTechnologiesBlocks.PRIMITIVE_PRESS.asItem(),
                AstromineTechnologiesBlocks.BASIC_PRESS.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_PRESS.asItem(),
                AstromineTechnologiesBlocks.ELITE_PRESS.asItem()
        );

        registerDescription("text.astromine.tooltip.wire_mill_description",
                AstromineTechnologiesBlocks.PRIMITIVE_WIRE_MILL.asItem(),
                AstromineTechnologiesBlocks.BASIC_WIRE_MILL.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_WIRE_MILL.asItem(),
                AstromineTechnologiesBlocks.ELITE_WIRE_MILL.asItem()
        );

        registerDescription("text.astromine.tooltip.electrolyzer_description",
                AstromineTechnologiesBlocks.PRIMITIVE_ELECTROLYZER.asItem(),
                AstromineTechnologiesBlocks.BASIC_ELECTROLYZER.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER.asItem(),
                AstromineTechnologiesBlocks.ELITE_ELECTROLYZER.asItem()
        );

        registerDescription("text.astromine.tooltip.refinery_description",
                AstromineTechnologiesBlocks.PRIMITIVE_REFINERY.asItem(),
                AstromineTechnologiesBlocks.BASIC_REFINERY.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_REFINERY.asItem(),
                AstromineTechnologiesBlocks.ELITE_REFINERY.asItem()
        );

        registerDescription("text.astromine.tooltip.fluid_mixer_description",
                AstromineTechnologiesBlocks.PRIMITIVE_FLUID_MIXER.asItem(),
                AstromineTechnologiesBlocks.BASIC_FLUID_MIXER.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER.asItem(),
                AstromineTechnologiesBlocks.ELITE_FLUID_MIXER.asItem()
        );

        registerDescription("text.astromine.tooltip.solidifer_description",
                AstromineTechnologiesBlocks.PRIMITIVE_SOLIDIFIER.asItem(),
                AstromineTechnologiesBlocks.BASIC_SOLIDIFIER.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_SOLIDIFIER.asItem(),
                AstromineTechnologiesBlocks.ELITE_SOLIDIFIER.asItem()
        );

        registerDescription("text.astromine.tooltip.melter_description",
                AstromineTechnologiesBlocks.PRIMITIVE_MELTER.asItem(),
                AstromineTechnologiesBlocks.BASIC_MELTER.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_MELTER.asItem(),
                AstromineTechnologiesBlocks.ELITE_MELTER.asItem()
        );

        registerDescription("text.astromine.tooltip.buffer_description",
                AstromineTechnologiesBlocks.PRIMITIVE_BUFFER.asItem(),
                AstromineTechnologiesBlocks.BASIC_BUFFER.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_BUFFER.asItem(),
                AstromineTechnologiesBlocks.ELITE_BUFFER.asItem(),
                AstromineTechnologiesBlocks.CREATIVE_BUFFER.asItem()
        );

        registerDescription("text.astromine.tooltip.capacitor_description",
                AstromineTechnologiesBlocks.PRIMITIVE_CAPACITOR.asItem(),
                AstromineTechnologiesBlocks.BASIC_CAPACITOR.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_CAPACITOR.asItem(),
                AstromineTechnologiesBlocks.ELITE_CAPACITOR.asItem(),
                AstromineTechnologiesBlocks.CREATIVE_CAPACITOR.asItem()
        );

        registerDescription("text.astromine.tooltip.tank_description",
                AstromineTechnologiesBlocks.PRIMITIVE_TANK.asItem(),
                AstromineTechnologiesBlocks.BASIC_TANK.asItem(),
                AstromineTechnologiesBlocks.ADVANCED_TANK.asItem(),
                AstromineTechnologiesBlocks.ELITE_TANK.asItem(),
                AstromineTechnologiesBlocks.CREATIVE_TANK.asItem()
        );

        registerDescription("text.astromine.tooltip.drill_description",
                AstromineTechnologiesItems.PRIMITIVE_DRILL,
                AstromineTechnologiesItems.BASIC_DRILL,
                AstromineTechnologiesItems.ADVANCED_DRILL,
                AstromineTechnologiesItems.ELITE_DRILL
        );

        registerDescription("text.astromine.tooltip.machine_upgrade_kit_description",
                AstromineTechnologiesItems.BASIC_MACHINE_UPGRADE_KIT,
                AstromineTechnologiesItems.ADVANCED_MACHINE_UPGRADE_KIT,
                AstromineTechnologiesItems.ELITE_MACHINE_UPGRADE_KIT
        );

        registerDescription("text.astromine.tooltip.vent_description",
                AstromineTechnologiesBlocks.VENT.asItem()
        );

        registerDescription("text.astromine.tooltip.fluid_placer_description",
                AstromineTechnologiesBlocks.FLUID_PLACER.asItem()
        );

        registerDescription("text.astromine.tooltip.fluid_collector_description",
                AstromineTechnologiesBlocks.FLUID_COLLECTOR.asItem()
        );

        registerDescription("text.astromine.tooltip.block_placer_description",
                AstromineTechnologiesBlocks.BLOCK_PLACER.asItem()
        );

        registerDescription("text.astromine.tooltip.block_breaker_description",
                AstromineTechnologiesBlocks.BLOCK_BREAKER.asItem()
        );

        registerDescription("text.astromine.tooltip.nuclear_warhead_description",
                AstromineTechnologiesBlocks.NUCLEAR_WARHEAD.asItem()
        );

        registerDescription("text.astromine.tooltip.airlock_door_description",
                AstromineTechnologiesBlocks.AIRLOCK.asItem()
        );

        registerDescription("text.astromine.tooltip.holographic_connector_description",
                AstromineTechnologiesItems.HOLOGRAPHIC_CONNECTOR.asItem()
        );

        registerDescription("text.astromine.tooltip.gravity_gauntlet_description",
                AstromineTechnologiesItems.GRAVITY_GAUNTLET.asItem()
        );

        registerDescription("text.astromine.tooltip.holographic_bridge_projector_description",
                AstromineTechnologiesBlocks.HOLOGRAPHIC_BRIDGE_PROJECTOR.asItem()
        );
    }
}
