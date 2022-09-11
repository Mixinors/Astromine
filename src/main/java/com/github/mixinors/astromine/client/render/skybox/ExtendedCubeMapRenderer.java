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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.util.Identifier;

/**
 * {@link CubeMapRenderer} with some changes to make it work better internally
 */
public class ExtendedCubeMapRenderer extends CubeMapRenderer {
	
	public ExtendedCubeMapRenderer() {
		super(new Identifier("empty"));
	}
	
	public void swapTextures(SkyboxTextures textures) {
		this.faces[0] = textures.north();
		this.faces[1] = textures.west();
		this.faces[2] = textures.south();
		this.faces[3] = textures.east();
		this.faces[4] = textures.up();
		this.faces[5] = textures.down();
	}
	
	public void render() {
		var client = MinecraftClient.getInstance();
		var camera = client.gameRenderer.getCamera();
		super.draw(client, camera.getPitch(), -camera.getYaw(), 1f);
	}
}
