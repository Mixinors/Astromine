package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.entity.SpaceSlimeEntity;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends MobEntity {

	private SlimeEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SlimeEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
	private void playDoubleSound(SlimeEntity slimeEntity, SoundEvent sound, float volume, float pitch) {
		if ((Object) this instanceof SpaceSlimeEntity) {
			this.playSound(SoundEvents.BLOCK_GLASS_BREAK, this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
		} else {
			this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
		}
	}

	@Shadow
	protected abstract SoundEvent getSquishSound();
}
