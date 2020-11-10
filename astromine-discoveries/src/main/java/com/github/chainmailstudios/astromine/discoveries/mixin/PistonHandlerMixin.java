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

	@Shadow private static boolean isBlockSticky(Block block) { return false; }

	@Inject(method = "isBlockSticky(Lnet/minecraft/block/Block;)Z", at = @At("HEAD"), cancellable = true)
	private static void isBlockStickyInject(Block block, CallbackInfoReturnable<Boolean> cir) {
		if (block.is(AstromineDiscoveriesBlocks.SPACE_SLIME_BLOCK)) cir.setReturnValue(true);
	}

	@Inject(method = "isAdjacentBlockStuck(Lnet/minecraft/block/Block;Lnet/minecraft/block/Block;)Z", at = @At("HEAD"), cancellable = true)
	private static void isAdjacentBlockStuckInject(Block block, Block block2, CallbackInfoReturnable<Boolean> cir) {
		if (block.is(AstromineDiscoveriesBlocks.SPACE_SLIME_BLOCK) && !isBlockSticky(block2)) cir.setReturnValue(false);
		else if (block2.is(AstromineDiscoveriesBlocks.SPACE_SLIME_BLOCK) && !isBlockSticky(block)) cir.setReturnValue(false);
	}
}
