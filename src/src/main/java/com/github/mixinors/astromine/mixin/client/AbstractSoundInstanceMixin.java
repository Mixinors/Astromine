package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.common.util.SoundUtils;
import dev.vini2003.hammer.core.api.client.util.InstanceUtils;
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
				var client = InstanceUtils.getClient();
				
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
				var client = InstanceUtils.getClient();
				
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
