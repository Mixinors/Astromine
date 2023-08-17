/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.mixinors.astromine.client.render.skybox;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;

public class SpaceSkyRenderer {
	private static final ExtendedCubeMapRenderer cubeMapRenderer = new ExtendedCubeMapRenderer();
	private static final SkyboxTextures SPACE_SKYBOX = new SkyboxTextures(
			AMCommon.id("textures/skybox/earth_orbit_up.png"),
			AMCommon.id("textures/skybox/space_down.png"),
			AMCommon.id("textures/skybox/space_north.png"),
			AMCommon.id("textures/skybox/space_south.png"),
			AMCommon.id("textures/skybox/space_east.png"),
			AMCommon.id("textures/skybox/space_west.png")
	);
	
	public static void render(WorldRenderContext ctx) {
		var client = MinecraftClient.getInstance();
		var worldKey = client.player.getWorld().getRegistryKey();
		var body = BodyManager.getBodyDimensionOfWorld(worldKey);
		
		if (body != null && body.skybox() != null) {
			cubeMapRenderer.swapTextures(body.skybox());
			cubeMapRenderer.render();
		} else if (AMWorlds.isAstromine(worldKey)) {
			cubeMapRenderer.swapTextures(SPACE_SKYBOX);
			cubeMapRenderer.render();
		}
	}
}
