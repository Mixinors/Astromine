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

package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.patchouli.AlloySmeltingPage;
import com.github.mixinors.astromine.client.patchouli.PressingPage;
import com.github.mixinors.astromine.client.patchouli.TrituratingPage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import vazkii.patchouli.client.book.ClientBookRegistry;

@Environment(EnvType.CLIENT)
public class AMPatchouliPages {
	@Environment(EnvType.CLIENT)
	public static void init() {
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.identifier("triturating"), TrituratingPage.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.identifier("pressing"), PressingPage.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.identifier("alloy_smelting"), AlloySmeltingPage.class);
	}
}
