package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.common.item.PatchouliItems;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
	@Inject(at = @At("RETURN"), method = "onUse(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;")
	void astromine_onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		if ((Object) this == Blocks.BOOKSHELF) {
			if (player.getStackInHand(hand).getItem() == AstromineItems.METITE_AXE) {
				ItemStack stack = new ItemStack(PatchouliItems.book);
				stack.setTag(stack.getOrCreateTag());
				stack.getTag().putString("patchouli:book", "astromine:manual");
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.breakBlock(pos, false);
			}
		}
	}
}
