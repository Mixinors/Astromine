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

package com.github.mixinors.astromine.foundations.datagen.generators.tag;

import net.minecraft.util.Identifier;

import com.github.mixinors.astromine.datagen.generator.tag.set.SetTagGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.material.MaterialSet;
import com.github.mixinors.astromine.foundations.datagen.registry.AstromineFoundationsMaterialSets;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public class CarbonDustTagGenerator implements SetTagGenerator {
	@Override
	public void generate(TagData tags, MaterialSet set) {
		tags.item(new Identifier("c", "carbon_dusts")).appendTag(set.getItemTagId(MaterialItemType.DUST));
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return (set.equals(AstromineFoundationsMaterialSets.COAL) || set.equals(AstromineFoundationsMaterialSets.CHARCOAL)) && set.hasType(MaterialItemType.DUST);
	}

	@Override
	public String getGeneratorName() {
		return "carbon_dust";
	}
}
