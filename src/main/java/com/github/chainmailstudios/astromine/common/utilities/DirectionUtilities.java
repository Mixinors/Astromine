package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.util.math.Direction;

public class DirectionUtilities {
    public static Direction byNameOrId(String name) {
        if(name.equals("down")) return Direction.DOWN;
        else if(name.equals("up")) return Direction.UP;
        else if(name.equals("north")) return Direction.NORTH;
        else if(name.equals("south")) return Direction.SOUTH;
        else if(name.equals("west")) return Direction.WEST;
        else if(name.equals("east")) return Direction.EAST;
        else return Direction.byId(Integer.parseInt(name));
    }
}
