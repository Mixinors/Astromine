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

package com.github.mixinors.astromine.client.screen;

import com.github.mixinors.astromine.common.screen.handler.RecipeCreatorScreenHandler;
import dev.vini2003.hammer.gui.api.client.screen.base.BaseHandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link BaseHandledScreen} used by {@link RecipeCreatorScreenHandler}.
 */
public class RecipeCreatorHandledScreen extends BaseHandledScreen<RecipeCreatorScreenHandler> {
	/** Instantiates a {@link RecipeCreatorHandledScreen}. */
	public RecipeCreatorHandledScreen(@NotNull RecipeCreatorScreenHandler handler, @NotNull PlayerInventory inventory, @NotNull Text title) {
		super(handler, inventory, title);
		
		playerInventoryTitleX = -Integer.MAX_VALUE;
		playerInventoryTitleY = -Integer.MAX_VALUE;
	}
}
