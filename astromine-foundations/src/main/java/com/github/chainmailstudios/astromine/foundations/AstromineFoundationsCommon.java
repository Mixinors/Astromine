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

package com.github.chainmailstudios.astromine.foundations;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsCriteria;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsFeatures;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsFluidEffects;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsFluids;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsIdentifierFixes;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsItems;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsOres;

public class AstromineFoundationsCommon extends AstromineCommon {
	public static String appendId(String id) {
		return MOD_ID + ":" + id;
	}

	@Override
	public void onInitialize() {
		AstromineFoundationsBlocks.initialize();
		AstromineFoundationsItems.initialize();
		AstromineFoundationsOres.initialize();
		AstromineFoundationsFluids.initialize();
		AstromineFoundationsCriteria.initialize();
		AstromineFoundationsFluidEffects.initialize();
		AstromineFoundationsFeatures.initialize();
		AstromineFoundationsIdentifierFixes.initialize();
	}
}
