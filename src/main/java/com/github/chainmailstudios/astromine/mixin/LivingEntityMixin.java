package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.atmosphere.AtmosphereRegistry;
import com.github.chainmailstudios.astromine.common.component.entity.EntityOxygenComponent;
import com.github.chainmailstudios.astromine.common.dimension.base.AtmosphericDimensionType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

import java.util.stream.Collectors;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow
	@Final
	private DefaultedList<ItemStack> equippedArmor;

	@ModifyConstant(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", constant = @Constant(doubleValue = 0.08D))
	double getGravity(double original) {
		World world = ((Entity) (Object) this).world;

		return GravityRegistry.INSTANCE.get(world.getDimensionRegistryKey());
	}

	@Inject(at = @At("HEAD"), method = "tick()V")
	void onTick(CallbackInfo callbackInformation) {
		Entity entity = (Entity) (Object) this;

		if (AtmosphereRegistry.INSTANCE.containsKey(entity.world.getDimensionRegistryKey()) && !entity.getType().isIn(AstromineTags.DOES_NOT_BREATHE)) {
			ComponentProvider worldProvider = ComponentProvider.fromWorld(entity.world);

			WorldAtmosphereComponent atmosphereComponent = worldProvider.getComponent(AstromineComponentTypes.WORLD_ATMOSPHERE_COMPONENT);

			FluidVolume atmosphereVolume = atmosphereComponent.get(entity.getBlockPos().offset(Direction.UP));

			if (SpaceSuitItem.hasFullArmor(equippedArmor)) return;

			boolean isSubmerged = false;

			Box collisionBox = entity.getBoundingBox();

			for (BlockPos blockPos : BlockPos.method_29715(collisionBox).collect(Collectors.toList())) {
				BlockState blockState = entity.world.getBlockState(blockPos);

				if (blockState.getBlock() instanceof FluidBlock) {
					isSubmerged = true;

					FluidState fluidState = blockState.getFluidState();
					Fluid collidingFluid = fluidState.getFluid();

					if (collidingFluid instanceof AdvancedFluid) {
						AdvancedFluid advancedFluid = (AdvancedFluid) collidingFluid;

						if (advancedFluid.isToxic()) {
							entity.damage(advancedFluid.getSource(), advancedFluid.getDamage());

							break;
						}
					}
				}
			}

			if (!isSubmerged) {
				ComponentProvider entityProvider = ComponentProvider.fromEntity(entity);

				EntityOxygenComponent oxygenComponent = entityProvider.getComponent(AstromineComponentTypes.ENTITY_OXYGEN_COMPONENT);

				oxygenComponent.simulate(atmosphereVolume);
			}
		}
	}
}
