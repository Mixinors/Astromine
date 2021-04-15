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

package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.MultiSmeltingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;

import java.util.Arrays;
import java.util.List;

import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.AXE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.BOOTS;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.CHESTPLATE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.EXCAVATOR;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.HAMMER;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.HELMET;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.HOE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.LEGGINGS;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.MATTOCK;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.MINING_TOOL;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.PICKAXE;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.SHOVEL;
import static com.github.chainmailstudios.astromine.datagen.material.MaterialItemType.SWORD;

public class SalvageSmeltingRecipeGenerator extends MultiSmeltingSetRecipeGenerator {
	public static final List<MaterialItemType> SALVAGEABLE = Arrays.asList(
			PICKAXE, AXE, SHOVEL, SWORD, HOE,
			HAMMER, EXCAVATOR, MINING_TOOL, MATTOCK,
			HELMET, CHESTPLATE, LEGGINGS, BOOTS
	);

	public SalvageSmeltingRecipeGenerator(MaterialItemType output) {
		super(SALVAGEABLE, output);
	}
}
