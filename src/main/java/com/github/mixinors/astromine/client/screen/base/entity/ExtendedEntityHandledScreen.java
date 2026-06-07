/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.screen.base.entity;

import com.github.mixinors.astromine.client.screen.base.CustomForegroundBaseHandledScreen;
import com.github.mixinors.astromine.common.screen.handler.base.entity.ExtendedEntityScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ExtendedEntityHandledScreen<T extends ExtendedEntityScreenHandler> extends CustomForegroundBaseHandledScreen<T> {
	public ExtendedEntityHandledScreen(T handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
	}
}
