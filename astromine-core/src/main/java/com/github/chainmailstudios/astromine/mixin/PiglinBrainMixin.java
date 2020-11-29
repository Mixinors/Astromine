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

import com.github.chainmailstudios.astromine. registry.AstromineConfig;
import com.github.chainmailstudios.astromine. registry.AstromineCriteria;
import com.github.chainmailstudios.astromine. registry.AstromineTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(PiglinAi.class)
public abstract class PiglinBrainMixin {
	@Inject(method = "stopHoldingOffHandItem(Lnet/minecraft/world/entity/monster/piglin/Piglin;Z)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isBarterCurrency(Lnet/minecraft/world/item/Item;)Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private static void astromine_consumeOffHandItem(Piglin entity, boolean bl, CallbackInfo ci, ItemStack stack, boolean bl2) {
		if (bl && bl2 && stack.getItem().is(AstromineTags.TRICKS_PIGLINS)) {
			Optional<Player> optional = entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
			if (optional.isPresent() && optional.get() instanceof ServerPlayer) {
				boolean noticed = entity.getRandom().nextInt(AstromineConfig.get().piglinAngerChance) == 0;
				AstromineCriteria.TRICKED_PIGLIN.trigger((ServerPlayer) optional.get(), !noticed);
				if(noticed) {
					entity.playSound(SoundEvents.PIGLIN_ANGRY, 1.0f, 1.0f);
					PiglinAi.setAngerTarget(entity, optional.get());
					ci.cancel();
				}
			}
		}
	}
}
