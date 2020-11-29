package com.github.chainmailstudios.astromine.transportations.registry;

import com.github.chainmailstudios.astromine.registry.AstromineTooltips;

public class AstromineTransportationsTooltips extends AstromineTooltips {
    public static void initialize() {
        registerPrimitive(AstromineTransportationsBlocks.PRIMITIVE_ENERGY_CABLE.asItem());

        registerPrimitive(AstromineTransportationsBlocks.SPLITTER.asItem());
        registerPrimitive(AstromineTransportationsBlocks.ALTERNATOR.asItem());
        registerPrimitive(AstromineTransportationsBlocks.DRAIN.asItem());
        registerPrimitive(AstromineTransportationsBlocks.SHREDDER.asItem());
        registerPrimitive(AstromineTransportationsBlocks.INSERTER.asItem());
        registerPrimitive(AstromineTransportationsBlocks.FLUID_PIPE.asItem());

        registerBasic(AstromineTransportationsBlocks.BASIC_CONVEYOR.asItem());
        registerBasic(AstromineTransportationsBlocks.BASIC_DOWNWARD_VERTICAL_CONVEYOR.asItem());
        registerBasic(AstromineTransportationsBlocks.BASIC_ENERGY_CABLE.asItem());
        registerBasic(AstromineTransportationsBlocks.BASIC_VERTICAL_CONVEYOR.asItem());
        registerBasic(AstromineTransportationsBlocks.FAST_INSERTER.asItem());

        registerAdvanced(AstromineTransportationsBlocks.ADVANCED_CONVEYOR.asItem());
        registerAdvanced(AstromineTransportationsBlocks.ADVANCED_DOWNWARD_VERTICAL_CONVEYOR.asItem());
        registerAdvanced(AstromineTransportationsBlocks.ADVANCED_ENERGY_CABLE.asItem());
        registerAdvanced(AstromineTransportationsBlocks.ADVANCED_VERTICAL_CONVEYOR.asItem());

        registerElite(AstromineTransportationsBlocks.ELITE_CONVEYOR.asItem());
        registerElite(AstromineTransportationsBlocks.ELITE_DOWNWARD_VERTICAL_CONVEYOR.asItem());
        registerElite(AstromineTransportationsBlocks.ELITE_ENERGY_CABLE.asItem());
        registerElite(AstromineTransportationsBlocks.ELITE_VERTICAL_CONVEYOR.asItem());

        registerSpecial(AstromineTransportationsItems.ENERGY);
        registerSpecial(AstromineTransportationsItems.FLUID);
        registerSpecial(AstromineTransportationsItems.ITEM);
        registerSpecial(AstromineTransportationsItems.MANUAL);

        registerDescription("text.astromine.tooltip.alternator_description",
            AstromineTransportationsBlocks.ALTERNATOR.asItem()
        );

        registerDescription("text.astromine.tooltip.splitter_description",
                AstromineTransportationsBlocks.SPLITTER.asItem()
        );

        registerDescription("text.astromine.tooltip.shredder_description",
                AstromineTransportationsBlocks.SHREDDER.asItem()
        );

        registerDescription("text.astromine.tooltip.drain_description",
                AstromineTransportationsBlocks.DRAIN.asItem()
        );

        registerDescription("text.astromine.conveyor_description",
                AstromineTransportationsBlocks.BASIC_CONVEYOR.asItem(),
                AstromineTransportationsBlocks.ADVANCED_CONVEYOR.asItem(),
                AstromineTransportationsBlocks.ELITE_CONVEYOR.asItem()
        );

        registerDescription("text.astromine.vertical_conveyor_description",
            AstromineTransportationsBlocks.BASIC_VERTICAL_CONVEYOR.asItem(),
            AstromineTransportationsBlocks.ADVANCED_VERTICAL_CONVEYOR.asItem(),
            AstromineTransportationsBlocks.ELITE_VERTICAL_CONVEYOR.asItem()
        );

        registerDescription("text.astromine.downwards_vertical_conveyor_description",
            AstromineTransportationsBlocks.BASIC_DOWNWARD_VERTICAL_CONVEYOR.asItem(),
            AstromineTransportationsBlocks.ADVANCED_DOWNWARD_VERTICAL_CONVEYOR.asItem(),
            AstromineTransportationsBlocks.ELITE_DOWNWARD_VERTICAL_CONVEYOR.asItem()
        );

        registerDescription("text.astromine.tooltip.inserter",
            AstromineTransportationsBlocks.INSERTER.asItem(),
            AstromineTransportationsBlocks.FAST_INSERTER.asItem()
        );
    }
}
