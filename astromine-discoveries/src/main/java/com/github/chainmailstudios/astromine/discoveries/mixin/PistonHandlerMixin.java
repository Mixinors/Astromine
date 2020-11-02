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

package com.github.chainmailstudios.astromine.discoveries.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonHandler;

import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;

@Mixin(PistonHandler.class)
public abstract class PistonHandlerMixin {

	@Shadow
	private static boolean isBlockSticky(Block block) {
		return false;
	}

	@Inject(method = "isBlockSticky(Lnet/minecraft/block/Block;)Z", at = @At("HEAD"), cancellable = true)
	private static void isBlockStickyInject(Block block, CallbackInfoReturnable<Boolean> cir) {
		if (block.is(AstromineDiscoveriesBlocks.SPACE_SLIME_BLOCK))
			cir.setReturnValue(true);
	}

	@Inject(method = "isAdjacentBlockStuck(Lnet/minecraft/block/Block;Lnet/minecraft/block/Block;)Z", at = @At("HEAD"), cancellable = true)
	private static void isAdjacentBlockStuckInject(Block block, Block block2, CallbackInfoReturnable<Boolean> cir) {
		if (block.is(AstromineDiscoveriesBlocks.SPACE_SLIME_BLOCK) && !isBlockSticky(block2))
			cir.setReturnValue(false);
		else if (block2.is(AstromineDiscoveriesBlocks.SPACE_SLIME_BLOCK) && !isBlockSticky(block))
			cir.setReturnValue(false);
	}
}
