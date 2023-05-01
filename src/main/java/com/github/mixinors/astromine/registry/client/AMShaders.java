package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.AMCommon;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.render.VertexFormats;

public class AMShaders {
	public static final ManagedCoreShader BLUR = ShaderEffectManager.getInstance().manageCoreShader(AMCommon.id("blurred_interface"), VertexFormats.POSITION_COLOR);
	
	public static void init() {
	
	}
}
