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

package com.github.chainmailstudios.astromine.discoveries.common.world.layer.moon;

import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBiomes;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;
import net.minecraft.world.level.newbiome.layer.traits.DimensionOffset0Transformer;

public class MoonBiomeLayer implements AreaTransformer0, DimensionOffset0Transformer {
	private final Registry<Biome> biomeRegistry;

	public MoonBiomeLayer(Registry<Biome> biomeRegistry) {
		this.biomeRegistry = biomeRegistry;
	}

	@Override
	public int applyPixel(Context context, int x, int y) {
		switch (context.nextRandom(3)) {
			case 0:
				return biomeRegistry.getId(biomeRegistry.get(AstromineDiscoveriesBiomes.LUNAR_PLAINS));
			case 1:
				return biomeRegistry.getId(biomeRegistry.get(AstromineDiscoveriesBiomes.LUNAR_HILLS));
			case 2:
				return biomeRegistry.getId(biomeRegistry.get(AstromineDiscoveriesBiomes.LUNAR_LOWLANDS));
		}

		return biomeRegistry.getId(biomeRegistry.get(AstromineDiscoveriesBiomes.LUNAR_PLAINS));
	}
}
