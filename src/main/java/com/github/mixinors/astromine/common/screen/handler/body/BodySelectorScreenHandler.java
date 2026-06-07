package com.github.mixinors.astromine.common.screen.handler.body;

import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class BodySelectorScreenHandler extends AbstractContainerMenu {
	public static final float SELECT_BUTTON_WIDTH = 18.0F;
	public static final float SELECT_BUTTON_HEIGHT = 18.0F;
	
	public BodySelectorScreenHandler(int syncId, Player player) {
		super(AMScreenHandlers.BODY_SELECTOR.get(), syncId);
	}
	
	@Override
	public boolean stillValid(Player player) {
		return player.isAlive();
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}
}
