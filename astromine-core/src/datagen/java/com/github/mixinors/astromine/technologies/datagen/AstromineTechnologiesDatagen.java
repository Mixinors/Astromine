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

package com.github.mixinors.astromine.technologies.datagen;

import com.github.mixinors.astromine.datagen.entrypoint.DatagenInitializer;
import com.github.mixinors.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.mixinors.astromine.datagen.registry.AstromineMaterialSets;
import com.github.mixinors.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.mixinors.astromine.datagen.registry.AstromineRecipeGenerators;
import com.github.mixinors.astromine.datagen.registry.AstromineTagGenerators;
import com.github.mixinors.astromine.datagen.registry.AstromineWorldGenGenerators;

public class AstromineTechnologiesDatagen implements DatagenInitializer {
	@Override
	public String getModuleId() {
		return "astromine-technologies";
	}

	@Override
	public AstromineMaterialSets getMaterialSets() {
		return null;
	}

	@Override
	public AstromineLootTableGenerators getLootTableGenerators() {
		return new AstromineTechnologiesLootTableGenerators();
	}

	@Override
	public AstromineRecipeGenerators getRecipeGenerators() {
		return new AstromineTechnologiesRecipeGenerators();
	}

	@Override
	public AstromineTagGenerators getTagGenerators() {
		return null;
	}

	@Override
	public AstromineModelStateGenerators getModelStateGenerators() {
		return new AstromineTechnologiesModelStateGenerators();
	}

	@Override
	public AstromineWorldGenGenerators getWorldGenGenerators() {
		return null;
	}
}
