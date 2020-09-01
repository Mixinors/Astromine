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

package com.github.chainmailstudios.astromine.transportations.registry.client;

import com.github.chainmailstudios.astromine.registry.client.AstromineBlockEntityRenderers;
import com.github.chainmailstudios.astromine.transportations.client.render.block.AbstractConveyableBlockEntityRenderer;
import com.github.chainmailstudios.astromine.transportations.client.render.block.ConveyorBlockEntityRenderer;
import com.github.chainmailstudios.astromine.transportations.client.render.block.DownwardVerticalConveyorBlockEntityRenderer;
import com.github.chainmailstudios.astromine.transportations.client.render.block.InserterBlockEntityRenderer;
import com.github.chainmailstudios.astromine.transportations.client.render.block.VerticalConveyorBlockEntityRenderer;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;

public class AstromineTransportationsBlockEntityRenderers extends AstromineBlockEntityRenderers {
	public static void initialize() {
		register(AstromineTransportationsBlockEntityTypes.ALTERNATOR, AbstractConveyableBlockEntityRenderer::new);
		register(AstromineTransportationsBlockEntityTypes.SPLITTER, AbstractConveyableBlockEntityRenderer::new);
		register(AstromineTransportationsBlockEntityTypes.INSERTER, InserterBlockEntityRenderer::new);

		register(AstromineTransportationsBlockEntityTypes.CONVEYOR, ConveyorBlockEntityRenderer::new);
		register(AstromineTransportationsBlockEntityTypes.VERTICAL_CONVEYOR, VerticalConveyorBlockEntityRenderer::new);
		register(AstromineTransportationsBlockEntityTypes.DOWNWARD_VERTICAL_CONVEYOR, DownwardVerticalConveyorBlockEntityRenderer::new);
	}
}
