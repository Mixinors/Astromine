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

package com.github.mixinors.astromine.client.screen.base.block;

import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import com.github.mixinors.astromine.client.screen.base.CustomForegroundBaseHandledScreen;
import com.github.mixinors.astromine.common.screenhandler.base.block.ComponentBlockEntityScreenHandler;

/**
 * A {@link CustomForegroundBaseHandledScreen} used by
 * {@link ComponentBlockEntityScreenHandler}.
 */
public class ComponentBlockEntityHandledScreen<T extends ComponentBlockEntityScreenHandler> extends CustomForegroundBaseHandledScreen<T> {
	/** Instantiates a {@link ComponentBlockEntityHandledScreen}. */
	public ComponentBlockEntityHandledScreen( BaseScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
}
