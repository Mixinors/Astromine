/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.common.screen.handler.base.entity;

import com.github.mixinors.astromine.common.entity.base.ExtendedEntity;
import com.github.mixinors.astromine.common.screen.handler.base.MenuQuickMove;
import java.util.function.Supplier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public abstract class ExtendedEntityScreenHandler extends AbstractContainerMenu {
	protected final ExtendedEntity entity;
	protected final Player player;
	
	public ExtendedEntityScreenHandler(Supplier<? extends MenuType<?>> type, int syncId, Player player, int entityId) {
		super(type.get(), syncId);
		
		this.player = player;
		this.entity = (ExtendedEntity) player.level().getEntity(entityId);
		
		if (entity != null && !player.level().isClientSide) {
			entity.setSyncItemStorage(true);
			entity.setSyncFluidStorage(true);
		}
		
		addEntitySlots();
		addPlayerInventory(player.getInventory(), 8, 84);
	}
	
	public abstract ItemStack getSymbol();
	
	public int getDefaultFluidSlotForBar() {
		return 0;
	}
	
	@Override
	public boolean stillValid(Player player) {
		return entity != null && entity.isAlive() && entity.distanceTo(player) < 8.0F;
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return MenuQuickMove.moveWithPlayerSlotsLast(this, player, index, this::moveItemStackTo);
	}
	
	public ExtendedEntity getEntity() {
		return entity;
	}
	
	public void init(int width, int height) {
	}
	
	private void addEntitySlots() {
		if (entity == null || !entity.hasItemStorage()) {
			return;
		}
		
		var storage = entity.getItemStorage();
		
		for (var slot = 0; slot < storage.getSlots(); ++slot) {
			addSlot(new SlotItemHandler(storage, slot, 8 + (slot % 9) * 18, 17 + (slot / 9) * 18));
		}
	}
	
	private void addPlayerInventory(Inventory inventory, int x, int y) {
		for (var row = 0; row < 3; ++row) {
			for (var column = 0; column < 9; ++column) {
				addSlot(new Slot(inventory, column + row * 9 + 9, x + column * 18, y + row * 18));
			}
		}
		
		for (var column = 0; column < 9; ++column) {
			addSlot(new Slot(inventory, column, x + column * 18, y + 58));
		}
	}
}
