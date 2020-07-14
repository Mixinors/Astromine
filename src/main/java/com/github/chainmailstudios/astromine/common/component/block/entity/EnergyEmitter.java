package com.github.chainmailstudios.astromine.common.component.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

public class EnergyEmitter {
	public static void emit(DefaultedBlockEntity blockEntity, int slot) {
		ComponentProvider ourProvider = ComponentProvider.fromBlockEntity(blockEntity);

		EnergyVolume energyVolume = ourProvider.getComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).getVolume(slot);

		for (Direction direction : Direction.values()) {
			if (blockEntity.getSidedComponent(direction, AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT) == null) continue;

			BlockPos position = blockEntity.getPos().offset(direction);

			BlockEntity attached = blockEntity.getWorld().getBlockEntity(position);

			if (attached instanceof ComponentProvider) {
				ComponentProvider provider = ComponentProvider.fromBlockEntity(attached);

				EnergyInventoryComponent inventory = provider.getSidedComponent(direction.getOpposite(), AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);

				if (inventory != null && energyVolume.hasStored(Fraction.bottle())) {
					EnergyVolume attachedVolume = inventory.getFirstInsertableVolume(direction.getOpposite());

					if (attachedVolume != null) {
						attachedVolume.pullVolume(energyVolume, Fraction.bottle());
					}

					if (attached instanceof BlockEntityClientSerializable && !attached.getWorld().isClient) {
						((BlockEntityClientSerializable) attached).sync();
					}
				}
			}
		}
	}
}
