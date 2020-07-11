package com.github.chainmailstudios.astromine.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.registry.AstromineItems;

import java.util.Iterator;
import java.util.List;

@Mixin(FallingBlockEntity.class)
public abstract class FlamingAnvilCraftingMixin extends Entity {
	@Shadow
	private BlockState block;
	
	public FlamingAnvilCraftingMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Environment(EnvType.CLIENT)
	@Inject(method = "doesRenderOnFire", at = @At("RETURN"), cancellable = true)
	private void ret(CallbackInfoReturnable<Boolean> cir) {
		if (this.isOnFire()) {
			if (this.block.isIn(BlockTags.ANVIL)) {
				cir.setReturnValue(true);
			}
		}
	}

	@Inject(method = "handleFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/Tag;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void handle(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Boolean> cir, int i, List<Entity> list) {
		if (this.isOnFire() && this.block.isIn(BlockTags.ANVIL)) {
			Iterator<Entity> iterator = list.iterator();
			while (iterator.hasNext()) {
				Entity entity = iterator.next();
				if (entity instanceof ItemEntity) {
					ItemEntity item = (ItemEntity) entity;
					ItemStack stack = item.getStack();
					if (stack.getItem() == Items.BREAD) {
						iterator.remove();
						item.setStack(new ItemStack(AstromineItems.YEAST, stack.getCount()));
					}
				}
			}
		}
	}
}
