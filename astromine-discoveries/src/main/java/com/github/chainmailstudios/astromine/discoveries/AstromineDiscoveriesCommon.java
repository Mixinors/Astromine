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

package com.github.chainmailstudios.astromine.discoveries;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.discoveries.registry.*;

public class AstromineDiscoveriesCommon extends AstromineCommon {
	@Override
	public void onInitialize() {
		AstromineDiscoveriesDecorators.initialize();
		AstromineDiscoveriesBiomes.initialize();
		AstromineDiscoveriesBiomeSources.initialize();
		AstromineDiscoveriesChunkGenerators.initialize();
		AstromineDiscoveriesCommonCallbacks.initialize();
		AstromineDiscoveriesDimensionLayers.initialize();
		AstromineDiscoveriesOres.initialize();
		AstromineDiscoveriesGravities.initialize();
		AstromineDiscoveriesBlocks.initialize();
		AstromineDiscoveriesBlockEntityTypes.initialize();
		AstromineDiscoveriesItems.initialize();
		AstromineDiscoveriesItemGroups.initialize();
		AstromineDiscoveriesAtmospheres.initialize();
		AstromineDiscoveriesDimensions.initialize();
		AstromineDiscoveriesFeatures.initialize();
		AstromineDiscoveriesEntityTypes.initialize();
		AstromineDiscoveriesArmorMaterials.initialize();
		AstromineDiscoveriesCriteria.initialize();
		AstromineDiscoveriesSoundEvents.initialize();
		AstromineDiscoveriesRecipeSerializers.initialize();
		AstromineDiscoveriesScreenHandlers.initialize();
		AstromineDiscoveriesParticles.initialize();
	}
}
