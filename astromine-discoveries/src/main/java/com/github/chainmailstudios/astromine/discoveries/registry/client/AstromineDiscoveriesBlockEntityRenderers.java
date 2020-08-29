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

package com.github.chainmailstudios.astromine.discoveries.registry.client;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

import com.github.chainmailstudios.astromine.discoveries.client.render.block.AltarBlockEntityRenderer;
import com.github.chainmailstudios.astromine.discoveries.client.render.block.AltarPedestalBlockEntityRenderer;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.client.AstromineBlockEntityRenderers;

import java.util.function.Function;

public class AstromineDiscoveriesBlockEntityRenderers {
	public static void initialize() {
		register(AstromineDiscoveriesBlockEntityTypes.ALTAR_PEDESTAL, AltarPedestalBlockEntityRenderer::new);
		register(AstromineDiscoveriesBlockEntityTypes.ALTAR, AltarBlockEntityRenderer::new);
	}

	public static <B extends BlockEntity, C extends BlockEntityType<B>> void register(C c, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<B>> b) {
		AstromineBlockEntityRenderers.register(c, b);
	}
}
