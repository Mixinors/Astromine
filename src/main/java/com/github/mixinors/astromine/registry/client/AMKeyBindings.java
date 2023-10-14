package com.github.mixinors.astromine.registry.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AMKeyBindings {
	public static final KeyBinding toggleCamera = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.trucc.togglecamera",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_J,
			"category.trucc.main"
	));
	public static final KeyBinding rotateCameraLeft = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.trucc.rotateleft",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_COMMA,
			"category.trucc.main"
	));
	public static final KeyBinding rotateCameraRight = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.trucc.rotateright",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_PERIOD,
			"category.trucc.main"
	));
	
	public static void init() {
	
	}
}
