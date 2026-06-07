/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.screen.base.block.entity;

import com.github.mixinors.astromine.client.screen.base.CustomForegroundBaseHandledScreen;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ExtendedBlockEntityHandledScreen<T extends ExtendedBlockEntityScreenHandler> extends CustomForegroundBaseHandledScreen<T> {
	public ExtendedBlockEntityHandledScreen(T handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
	}
}
