/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.registry.common.AMCriteria;
import com.github.mixinors.astromine.registry.common.AMTags;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
	@Inject(method = "consumeOffHandItem(Lnet/minecraft/entity/mob/PiglinEntity;Z)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/mob/PiglinBrain;acceptsForBarter(Lnet/minecraft/item/ItemStack;)Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private static void astromine_consumeOffHandItem(PiglinEntity piglin, boolean barter, CallbackInfo ci, ItemStack stack) {
		if (stack.isIn(AMTags.TRICKS_PIGLINS)) { //TODO: make sure this works correctly
			var optional = piglin.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
			if (optional.isPresent() && optional.get() instanceof ServerPlayerEntity player) {
				var noticed = piglin.getRandom().nextInt(AMConfig.get().piglinAngerChance) == 0;
				AMCriteria.TRICKED_PIGLIN.trigger(player, !noticed);
				if (noticed) {
					piglin.playSound(SoundEvents.ENTITY_PIGLIN_ANGRY, 1.0f, 1.0f);
					PiglinBrain.becomeAngryWith(piglin, player);
					ci.cancel();
				}
			}
		}
	}
}
