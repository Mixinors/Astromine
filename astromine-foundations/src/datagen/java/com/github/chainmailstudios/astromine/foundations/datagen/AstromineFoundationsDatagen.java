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

package com.github.chainmailstudios.astromine.foundations.datagen;

import com.github.chainmailstudios.astromine.datagen.entrypoint.DatagenInitializer;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineLootTableGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineMaterialSets;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineRecipeGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineTagGenerators;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineWorldGenGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsLootTableGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsMaterialSets;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsModelStateGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsRecipeGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsTagGenerators;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsWorldGenGenerators;

public class AstromineFoundationsDatagen implements DatagenInitializer {
	@Override
	public String getModuleId() {
		return "astromine-foundations";
	}

	@Override
	public AstromineMaterialSets getMaterialSets() {
		return new AstromineFoundationsMaterialSets();
	}

	@Override
	public AstromineLootTableGenerators getLootTableGenerators() {
		return new AstromineFoundationsLootTableGenerators();
	}

	@Override
	public AstromineRecipeGenerators getRecipeGenerators() {
		return new AstromineFoundationsRecipeGenerators();
	}

	@Override
	public AstromineTagGenerators getTagGenerators() {
		return new AstromineFoundationsTagGenerators();
	}

	@Override
	public AstromineModelStateGenerators getModelStateGenerators() {
		return new AstromineFoundationsModelStateGenerators();
	}

	@Override
	public AstromineWorldGenGenerators getWorldGenGenerators() {
		return new AstromineFoundationsWorldGenGenerators();
	}
}
