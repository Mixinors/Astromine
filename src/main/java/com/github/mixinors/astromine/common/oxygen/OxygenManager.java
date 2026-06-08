package com.github.mixinors.astromine.common.oxygen;

import com.github.mixinors.astromine.common.component.entity.OxygenComponent;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.item.armor.SpaceSuitArmorItem;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMTagKeys;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public final class OxygenManager {
	private OxygenManager() {
	}

	public static void tick(LivingEntity entity) {
		if (entity.level().isClientSide()) {
			return;
		}

		if (!AMWorlds.isVacuum(entity.level().dimensionTypeRegistration())) {
			OxygenComponent.get(entity).tick(true);
			return;
		}

		OxygenComponent.get(entity).tick(canBreatheInVacuum(entity));
	}

	public static boolean canBreatheInVacuum(LivingEntity entity) {
		if (entity.getType().is(AMTagKeys.EntityTypeTags.CAN_BREATHE_IN_SPACE)) {
			return true;
		}

		if (!(entity instanceof Player) && !(entity instanceof Mob)) {
			return false;
		}

		return tryUseSpaceSuit(entity);
	}

	private static boolean tryUseSpaceSuit(LivingEntity entity) {
		var helmet = entity.getItemBySlot(EquipmentSlot.HEAD);
		var chestplate = entity.getItemBySlot(EquipmentSlot.CHEST);
		var leggings = entity.getItemBySlot(EquipmentSlot.LEGS);
		var boots = entity.getItemBySlot(EquipmentSlot.FEET);

		if (!helmet.is(AMItems.SPACE_SUIT_HELMET.get()) || !leggings.is(AMItems.SPACE_SUIT_LEGGINGS.get()) || !boots.is(AMItems.SPACE_SUIT_BOOTS.get())) {
			return false;
		}

		if (!(chestplate.getItem() instanceof SpaceSuitArmorItem.Chestplate suit)) {
			return false;
		}

		return tryUseSuitResources(chestplate, suit);
	}

	private static boolean tryUseSuitResources(ItemStack stack, SpaceSuitArmorItem.Chestplate suit) {
		var fluidAmount = Math.max(1L, AMConfig.get().items.spaceSuitChestplateFluidConsumption);
		var energyAmount = Math.max(1L, AMConfig.get().items.spaceSuitChestplateEnergyConsumption);
		var storedFluid = suit.getStoredFluid(stack);

		if (!isOxygen(storedFluid) || storedFluid.getAmount() < fluidAmount || suit.getStoredEnergy(stack) < energyAmount) {
			return false;
		}

		suit.setStoredFluid(stack, storedFluid.copyWithAmount((int) Math.max(0L, storedFluid.getAmount() - fluidAmount)));
		suit.setStoredEnergy(stack, suit.getStoredEnergy(stack) - energyAmount);

		return true;
	}

	private static boolean isOxygen(FluidStack stack) {
		return !stack.isEmpty() && stack.is(AMTagKeys.FluidTags.OXYGEN);
	}
}
