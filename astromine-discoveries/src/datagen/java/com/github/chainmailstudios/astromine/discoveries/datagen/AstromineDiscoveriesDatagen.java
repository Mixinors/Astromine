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

package com.github.chainmailstudios.astromine.discoveries.datagen;

import com.github.chainmailstudios.astromine.datagen.entrypoint.DatagenInitializer;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineMaterialSets;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineRecipeGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineTagGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineWorldGenGenerators;
import com.github.chainmailstudios.astromine.discoveries.datagen.registry.AstromineDiscoveriesLootTableGenerators;
import com.github.chainmailstudios.astromine.discoveries.datagen.registry.AstromineDiscoveriesModelStateGenerators;
import com.github.chainmailstudios.astromine.discoveries.datagen.registry.AstromineDiscoveriesRecipeGenerators;
import com.github.chainmailstudios.astromine.discoveries.datagen.registry.AstromineDiscoveriesTagGenerators;

public class AstromineDiscoveriesDatagen implements DatagenInitializer {
	@Override
	public String getModuleId() {
		return "astromine-discoveries";
	}

	@Override
	public AstromineMaterialSets getMaterialSets() {
		return null;
	}

	@Override
	public AstromineLootTableGenerators getLootTableGenerators() {
		return new AstromineDiscoveriesLootTableGenerators();
	}

	@Override
	public AstromineRecipeGenerators getRecipeGenerators() {
		return new AstromineDiscoveriesRecipeGenerators();
	}

	@Override
	public AstromineTagGenerators getTagGenerators() {
		return new AstromineDiscoveriesTagGenerators();
	}

	@Override
	public AstromineModelStateGenerators getModelStateGenerators() {
		return new AstromineDiscoveriesModelStateGenerators();
	}

	@Override
	public AstromineWorldGenGenerators getWorldGenGenerators() {
		return null;
	}
}
