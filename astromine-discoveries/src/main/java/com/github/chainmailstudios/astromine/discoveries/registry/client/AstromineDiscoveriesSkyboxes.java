/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.discoveries.registry.client;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.registry.SkyboxRegistry;
import com.github.chainmailstudios.astromine.discoveries.client.render.sky.skybox.SpaceSkybox;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesDimensions;
import com.github.chainmailstudios.astromine.registry.client.AstromineSkyboxes;

public class AstromineDiscoveriesSkyboxes extends AstromineSkyboxes {
	public static void initialize() {
		SkyboxRegistry.INSTANCE.register(AstromineDiscoveriesDimensions.EARTH_SPACE_WORLD, new SpaceSkybox.Builder().up(AstromineCommon.identifier("textures/skybox/earth_space_up.png")).down(AstromineCommon.identifier("textures/skybox/earth_space_down.png")).north(AstromineCommon
			.identifier("textures/skybox/earth_space_north.png")).south(AstromineCommon.identifier("textures/skybox/earth_space_south.png")).west(AstromineCommon.identifier("textures/skybox/earth_space_west.png")).east(AstromineCommon.identifier(
				"textures/skybox/earth_space_east.png")).planet(AstromineCommon.identifier("textures/skybox/earth.png")).cloud(AstromineCommon.identifier("textures/skybox/earth_cloud.png")).build());
	}
}
