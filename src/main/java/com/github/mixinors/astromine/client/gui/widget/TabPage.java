/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record TabPage(Supplier<ItemStack> icon, Supplier<Component> title, List<Widget> children) {
	public TabPage(Supplier<ItemStack> icon, Supplier<Component> title) {
		this(icon, title, new ArrayList<>());
	}
	
	public <T extends Widget> T add(T widget) {
		children.add(widget);
		return widget;
	}
}
