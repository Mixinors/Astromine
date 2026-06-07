/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 */

package com.github.mixinors.astromine.client.gui;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.resources.ResourceLocation;

public final class GuiSprites {
	public static final int TEXT = 0xFF404040;
	public static final int BUTTON_TEXT = 0xFFFFFFFF;
	public static final int SLOT_SIZE = 18;
	public static final int BAR_WIDTH = 24;
	public static final int BAR_HEIGHT = 48;
	public static final int ARROW_WIDTH = 22;
	public static final int ARROW_HEIGHT = 16;
	public static final int TAB_WIDTH = 26;
	public static final int TAB_HEIGHT = 25;
	
	public static final ResourceLocation PANEL = AMCommon.id("textures/widget/panel.png");
	public static final ResourceLocation SLOT = AMCommon.id("textures/widget/slot.png");
	public static final ResourceLocation BUTTON_ENABLED = AMCommon.id("textures/widget/button_enabled.png");
	public static final ResourceLocation BUTTON_DISABLED = AMCommon.id("textures/widget/button_disabled.png");
	public static final ResourceLocation BUTTON_FOCUSED = AMCommon.id("textures/widget/button_focused.png");
	public static final ResourceLocation BAR_BACKGROUND = AMCommon.id("textures/widget/bar_background.png");
	public static final ResourceLocation BAR_FOREGROUND = AMCommon.id("textures/widget/bar_foreground.png");
	public static final ResourceLocation ENERGY_BAR_BACKGROUND = AMCommon.id("textures/widget/energy_bar_background.png");
	public static final ResourceLocation ENERGY_BAR_FILLED = AMCommon.id("textures/widget/energy_bar_filled.png");
	public static final ResourceLocation ARROW_BACKGROUND = AMCommon.id("textures/widget/horizontal_arrow_background.png");
	public static final ResourceLocation ARROW_FOREGROUND = AMCommon.id("textures/widget/horizontal_arrow_foreground.png");
	public static final ResourceLocation TAB_LEFT_ACTIVE = AMCommon.id("textures/widget/tab_left_active.png");
	public static final ResourceLocation TAB_MIDDLE_ACTIVE = AMCommon.id("textures/widget/tab_middle_active.png");
	public static final ResourceLocation TAB_RIGHT_ACTIVE = AMCommon.id("textures/widget/tab_right_active.png");
	public static final ResourceLocation TAB_LEFT_INACTIVE = AMCommon.id("textures/widget/tab_left_inactive.png");
	public static final ResourceLocation TAB_MIDDLE_INACTIVE = AMCommon.id("textures/widget/tab_middle_inactive.png");
	public static final ResourceLocation TAB_RIGHT_INACTIVE = AMCommon.id("textures/widget/tab_right_inactive.png");
	public static final ResourceLocation FLUID_FILTER_BACKGROUND = AMCommon.id("textures/widget/fluid_filter_background.png");
	
	private GuiSprites() {
	}
}
