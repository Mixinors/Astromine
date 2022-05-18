package com.github.mixinors.astromine.common.util;

import com.github.mixinors.astromine.common.access.MobEntityAccessor;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.jetbrains.annotations.Nullable;

public class EntityStorageUtils {
	@Nullable
	public static ContainerItemContext getContextForSlot(LivingEntity entity, EquipmentSlot slot) {
		if (entity instanceof PlayerEntity player) {
			var slotId = switch (slot) {
				case MAINHAND -> 0;
				case HEAD, CHEST, LEGS, FEET -> PlayerInventory.MAIN_SIZE + slot.getEntitySlotId();
				case OFFHAND -> PlayerInventory.OFF_HAND_SLOT;
			};
			
			return ContainerItemContext.ofPlayerSlot(player, PlayerInventoryStorage.of(player).getSlot(slotId));
		} else if (entity instanceof MobEntityAccessor mobEntity) {
			return ContainerItemContext.ofSingleSlot(mobEntity.astromine$getEquipmentStorage().getSlot(slot));
		}
		// TODO: handle armor stands
		return null;
	}
}
