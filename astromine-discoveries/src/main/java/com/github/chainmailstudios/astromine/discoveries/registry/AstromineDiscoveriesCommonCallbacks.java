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

package com.github.chainmailstudios.astromine.discoveries.registry;

import net.minecraft.server.world.ServerWorld;

import com.github.chainmailstudios.astromine.common.callback.ServerChunkManagerCallback;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.glacios.GlaciosChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.mars.MarsChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.moon.MoonChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.space.EarthSpaceChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.vulcan.VulcanChunkGenerator;
import com.github.chainmailstudios.astromine.registry.AstromineCommonCallbacks;

public class AstromineDiscoveriesCommonCallbacks extends AstromineCommonCallbacks {
	public static void initialize() {
		ServerChunkManagerCallback.EVENT.register(manager -> {
			if (manager.chunkGenerator instanceof EarthSpaceChunkGenerator) {
				manager.chunkGenerator = ((EarthSpaceChunkGenerator) manager.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			}
		});

		ServerChunkManagerCallback.EVENT.register(manager -> {
			if (manager.chunkGenerator instanceof MoonChunkGenerator) {
				manager.chunkGenerator = ((MoonChunkGenerator) manager.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			}
		});

		ServerChunkManagerCallback.EVENT.register(manager -> {
			if (manager.chunkGenerator instanceof MarsChunkGenerator) {
				manager.chunkGenerator = ((MarsChunkGenerator) manager.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			}
		});

		ServerChunkManagerCallback.EVENT.register(manager -> {
			if (manager.chunkGenerator instanceof VulcanChunkGenerator) {
				manager.chunkGenerator = ((VulcanChunkGenerator) manager.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			}
		});

		ServerChunkManagerCallback.EVENT.register(manager -> {
			if (manager.chunkGenerator instanceof GlaciosChunkGenerator) {
				manager.chunkGenerator = ((GlaciosChunkGenerator) manager.chunkGenerator).withSeedCommon(((ServerWorld) manager.getWorld()).getSeed());
			}
		});
	}
}
