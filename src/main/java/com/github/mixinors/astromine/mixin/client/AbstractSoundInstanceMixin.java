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

package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.common.util.SoundUtils;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSoundInstance.class)
public class AbstractSoundInstanceMixin {
	@Shadow
	protected double x;
	
	@Shadow
	protected double y;
	
	@Shadow
	protected double z;
	
	@Shadow
	@Final
	protected SoundCategory category;
	
	@Inject(at = @At("RETURN"), method = "getVolume", cancellable = true)
	private void astromine$getVolume(CallbackInfoReturnable<Float> cir) {
		switch (this.category) {
			case RECORDS, WEATHER, BLOCKS, HOSTILE, NEUTRAL, PLAYERS, AMBIENT -> {
				var client = InstanceUtil.getClient();
				
				if (client == null || client.player == null) {
					return;
				}
				
				var mufflingMultiplier = SoundUtils.getSoundMufflingMultiplier(client.player);
				
				if (mufflingMultiplier != 1.0F) {
					cir.setReturnValue(cir.getReturnValueF() * mufflingMultiplier);
				}
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getPitch", cancellable = true)
	private void astromine$getPitch(CallbackInfoReturnable<Float> cir) {
		switch (this.category) {
			case RECORDS, WEATHER, BLOCKS, HOSTILE, NEUTRAL, PLAYERS, AMBIENT -> {
				var client = InstanceUtil.getClient();
				
				if (client == null || client.player == null) {
					return;
				}
				
				var mufflingMultiplier = SoundUtils.getSoundMufflingMultiplier(client.player);
				
				if (mufflingMultiplier != 1.0F) {
					cir.setReturnValue(cir.getReturnValueF() * mufflingMultiplier);
				}
			}
		}
	}
}
