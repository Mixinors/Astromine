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

package com.github.chainmailstudios.astromine.discoveries.registry;

import net.minecraft.block.entity.BlockEntityType;

import com.github.chainmailstudios.astromine.discoveries.common.block.entity.AltarBlockEntity;
import com.github.chainmailstudios.astromine.discoveries.common.block.entity.AltarPedestalBlockEntity;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;

public class AstromineDiscoveriesBlockEntityTypes extends AstromineBlockEntityTypes {
	public static final BlockEntityType<AltarPedestalBlockEntity> ALTAR_PEDESTAL = register("altar_pedestal", AltarPedestalBlockEntity::new, AstromineDiscoveriesBlocks.ALTAR_PEDESTAL);
	public static final BlockEntityType<AltarBlockEntity> ALTAR = register("altar", AltarBlockEntity::new, AstromineDiscoveriesBlocks.ALTAR);

	public static void initialize() {

	}
}
