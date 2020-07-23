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

import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
