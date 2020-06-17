package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.fluid.AdvancedFluid;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.gas.AtmosphericManager;
import com.github.chainmailstudios.astromine.common.item.SpaceSuitItem;
import com.github.chainmailstudios.astromine.common.registry.BreathableRegistry;
import com.github.chainmailstudios.astromine.common.registry.DimensionLayerRegistry;
import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
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
	@Shadow @Final private DefaultedList<ItemStack> equippedArmor;
	int lastY = 0;

	long lastOxygenTick = 0;

	@ModifyConstant(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", constant = @Constant(doubleValue = 0.08D))
	double getGravity(double original) {
		World world = ((Entity) (Object) this).world;

		return GravityRegistry.INSTANCE.get(world.getDimensionRegistryKey());
	}

	@Inject(at = @At("HEAD"), method = "tick()V")
	void onTick(CallbackInfo callbackInformation) {
		Entity entity = (Entity) (Object) this;


		if ((int) entity.getPos().getY() != lastY && !entity.world.isClient) {
			lastY = (int) entity.getPos().getY();

			int bY = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey());
			int tY = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey());

			if (lastY <= bY && bY != Integer.MIN_VALUE) {
				RegistryKey<World> worldKey = RegistryKey.of(Registry.DIMENSION, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.BOTTOM, entity.world.getDimensionRegistryKey()).getValue());

				ServerWorld serverWorld = entity.world.getServer().getWorld(worldKey);

				FabricDimensions.teleport(entity, serverWorld).updatePosition(entity.getPos().getX(), 256, entity.getPos().getZ());
			} else if (lastY >= tY && tY != Integer.MIN_VALUE) {
				RegistryKey<World> worldKey = RegistryKey.of(Registry.DIMENSION, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.TOP, entity.world.getDimensionRegistryKey()).getValue());

				ServerWorld serverWorld = entity.world.getServer().getWorld(worldKey);

				FabricDimensions.teleport(entity, serverWorld).updatePosition(entity.getPos().getX(), 256, entity.getPos().getZ());
			}
		}

		FluidVolume atmosphere = AtmosphericManager.get(entity.world, entity.getBlockPos());

		Fluid fluid;

		if (!SpaceSuitItem.hasFullArmor(equippedArmor)) {
			fluid = atmosphere.getFluid();
		} else {
			FluidVolume volume = SpaceSuitItem.readVolume(equippedArmor);
			fluid = volume.getFluid();

			if (volume.isEmpty()) {
				fluid = null;
				entity.damage(DamageSource.DROWN, 1);
			}
		}

		if (fluid != null && !BreathableRegistry.INSTANCE.contains(((Entity) (Object) this).getType(), fluid)) {
			if (fluid instanceof AdvancedFluid && ((AdvancedFluid) fluid).isToxic()) {
				entity.damage(DamageSource.DROWN, ((AdvancedFluid) fluid).getDamage());
			} else {
				entity.damage(DamageSource.DROWN, 1);
			}
		}

		BlockState state = entity.world.getBlockState(entity.getBlockPos());

		if (state.getBlock() instanceof FluidBlock) {
			FluidBlock block = (FluidBlock) state.getBlock();

			FluidState fluidState = block.getFluidState(state);

			fluid = fluidState.getFluid();

			if (fluid instanceof AdvancedFluid && ((AdvancedFluid) fluid).isToxic()) {
				entity.damage(DamageSource.LAVA, ((AdvancedFluid) fluid).getDamage());
			}
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
