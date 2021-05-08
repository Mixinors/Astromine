/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.client.registry;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import com.github.mixinors.astromine.client.render.sky.skybox.Skybox;
import com.github.mixinors.astromine.common.registry.base.BiRegistry;
import com.github.mixinors.astromine.mixin.client.WorldRendererMixin;

/**
 * A {@link BiRegistry} for registration of
 * {@link RegistryKey<World>}s mapped to {@link Skybox}s.
 *
 * The registered gravity value will then be used for {@link Skybox}
 * rendering, through {@link WorldRendererMixin}.
 */
public class SkyboxRegistry extends BiRegistry<RegistryKey<World>, Skybox> {
	public static final SkyboxRegistry INSTANCE = new SkyboxRegistry();

	/** We only want one instance of this. */
	private SkyboxRegistry() {}
}
