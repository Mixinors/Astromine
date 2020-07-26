package com.github.chainmailstudios.astromine.common.conveyor;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public interface Conveyable {
	/**
     * @return Whether or not the interactable accepts the stack to be inputted.
     */
    boolean accepts(ItemStack stack);

    boolean validInputSide(Direction direction);

	boolean isOutputSide(Direction direction, ConveyorType type);

    /**
     * @return Gives the interactable the stack.
     */
    void give(ItemStack stack);

    boolean hasBeenRemoved();

    void setRemoved(boolean hasBeenRemoved);
}
