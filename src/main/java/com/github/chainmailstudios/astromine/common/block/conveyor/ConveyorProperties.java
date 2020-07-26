package com.github.chainmailstudios.astromine.common.block.conveyor;

import net.minecraft.state.property.BooleanProperty;

public class ConveyorProperties {
    public static final BooleanProperty FRONT = BooleanProperty.of("front");
    public static final BooleanProperty LEFT = BooleanProperty.of("left");
    public static final BooleanProperty RIGHT = BooleanProperty.of("right");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty CONVEYOR = BooleanProperty.of("conveyor");
    public static final BooleanProperty FLOOR = BooleanProperty.of("floor");
}
