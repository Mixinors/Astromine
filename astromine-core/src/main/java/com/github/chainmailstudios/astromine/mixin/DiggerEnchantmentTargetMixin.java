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

package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.Item;

import com.github.chainmailstudios.astromine.common.item.DiggerTool;

/**
 * for future reference, the inner class numbers of the EnchantmentTarget enum: 1 = armor 2 = breakable 3 = bow 4 =
 * wearable 5 = crossbow 6 = vanishable 7 = boots 8 = not sure if chestplate or leggings 9 = not sure if chestplate or
 * leggings 10 = helmet 11 = sword 12 = digger 13 = fishing rod 14 = trident Note that these are for some reason in a
 * different order to what they appear in the class itself...
 */
@Mixin(targets = { "net/minecraft/enchantment/EnchantmentTarget$12" })
public class DiggerEnchantmentTargetMixin {
	@Inject(at = @At("HEAD"), method = "isAcceptableItem(Lnet/minecraft/item/Item;)Z", cancellable = true)
	public void astromine_makeMultiToolEnchantable(Item item, CallbackInfoReturnable<Boolean> cir) {
		if (item instanceof DiggerTool)
			cir.setReturnValue(true);
	}
}
