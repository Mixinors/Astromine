/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.client.rei.alloysmelting;

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.client.rei.base.input.DoubleItemInputCategory;
import com.github.mixinors.astromine.client.rei.base.output.SingleItemOutputCategory;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;

public class AlloySmeltingCategory implements DoubleItemInputCategory<AlloySmeltingDisplay>, SingleItemOutputCategory<AlloySmeltingDisplay> {
	@Override
	public CategoryIdentifier<? extends AlloySmeltingDisplay> getCategoryIdentifier() {
		return AMRoughlyEnoughItemsPlugin.ALLOY_SMELTING;
	}
	
	@Override
	public Text getTitle() {
		return Text.translatable("category.astromine.alloy_smelting");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(AMBlocks.ADVANCED_ALLOY_SMELTER.get());
	}
}
