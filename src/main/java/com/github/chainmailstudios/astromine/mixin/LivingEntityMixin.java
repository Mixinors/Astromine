package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.component.world.WorldAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.entity.SpaceSlimeEntity;
import com.github.chainmailstudios.astromine.common.fluid.AdvancedFluid;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.item.SpaceSuitItem;
import com.github.chainmailstudios.astromine.common.registry.BreathableRegistry;
import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.github.chainmailstudios.astromine.registry.AstromineTags;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow
	@Final
	private DefaultedList<ItemStack> equippedArmor;

	@Shadow
	protected abstract int getNextAirUnderwater(int air);

	@Shadow
	protected abstract int getNextAirOnLand(int air);

	int oxygen = 180;

	long lastOxygenTick = 0;

	private static int nextOxygen(boolean b, int o) {
		return b ? o < 180 ? o + 1 : 180 : o > -20 ? o - 1 : -20;
	}

	@ModifyConstant(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", constant = @Constant(doubleValue = 0.08D))
	double getGravity(double original) {
		World world = ((Entity) (Object) this).world;

		return GravityRegistry.INSTANCE.get(world.getDimensionRegistryKey());
	}

	@Inject(at = @At("HEAD"), method = "tick()V")
	void onTick(CallbackInfo callbackInformation) {
		Entity entity = (Entity) (Object) this;

		if (!entity.getType().isIn(AstromineTags.DOES_NOT_BREATHE)) {
			ComponentProvider componentProvider = ComponentProvider.fromWorld(entity.world);

			WorldAtmosphereComponent atmosphereComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_ATMOSPHERE_COMPONENT);

			FluidVolume atmosphere = atmosphereComponent.get(entity.getBlockPos().offset(Direction.UP));

			Fluid fluid;

			boolean isBreathing = true;

			if (!SpaceSuitItem.hasFullArmor(equippedArmor) && !(entity instanceof SpaceSlimeEntity)) {
				fluid = atmosphere.getFluid();
			} else {
				//FluidVolume volume = SpaceSuitItem.readVolume(equippedArmor);
				//fluid = volume.getFluid();
//
				//if (volume.isEmpty()) {
				//	isBreathing = false;
				//}

				fluid = AstromineFluids.OXYGEN;
			}

			if (!BreathableRegistry.INSTANCE.get(((Entity) (Object) this).getType()).contains(fluid)) {
				if (fluid instanceof AdvancedFluid && ((AdvancedFluid) fluid).isToxic()) {
					entity.damage(DamageSource.GENERIC, ((AdvancedFluid) fluid).getDamage());
				}

				isBreathing = false;
			}

			BlockState upState = entity.world.getBlockState(entity.getBlockPos().offset(Direction.UP));
			BlockState downState = entity.world.getBlockState(entity.getBlockPos());

			Block upBlock = upState.getBlock();
			Block downBlock = downState.getBlock();

			if (upBlock instanceof FluidBlock) {
				FluidState fluidState = upBlock.getFluidState(upState);

				fluid = fluidState.getFluid();

				if (fluid instanceof AdvancedFluid && ((AdvancedFluid) fluid).isToxic()) {
					entity.damage(DamageSource.GENERIC, ((AdvancedFluid) fluid).getDamage());
				}

				isBreathing = false;
			}

			if (downBlock instanceof FluidBlock) {
				FluidState fluidState = downBlock.getFluidState(downState);

				fluid = fluidState.getFluid();

				if (fluid instanceof AdvancedFluid && ((AdvancedFluid) fluid).isToxic()) {
					entity.damage(DamageSource.GENERIC, ((AdvancedFluid) fluid).getDamage());
				}
			}

			if (!isBreathing && !((Object) this instanceof PlayerEntity && ((PlayerEntity) (Object) this).isCreative())) {
				LivingEntity user = (LivingEntity) (Object) this;
				oxygen = nextOxygen(false, oxygen);

				if (oxygen <= -20) {
					oxygen = 0;

					user.damage(DamageSource.DROWN, 2.0F);
				}
			} else {
				oxygen = nextOxygen(true, oxygen);
			}

			long currentTime = System.currentTimeMillis();

			if (currentTime - lastOxygenTick >= 300000) {
				FluidVolume volume = SpaceSuitItem.readVolume(equippedArmor);

				if (!volume.isEmpty()) {
					volume.setFraction(Fraction.subtract(volume.getFraction(), Fraction.min(volume.getFraction(), Fraction.BOTTLE)));
				}

				lastOxygenTick = currentTime;
			}
		}
	}
}
