package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.container.LiquidGeneratorContainer;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class LiquidGeneratorBlockEntity extends DefaultedEnergyFluidBlockEntity implements NetworkMember, Tickable {
	public LiquidGeneratorBlockEntity() {
		super(AstromineBlockEntityTypes.LIQUID_GENERATOR);

		energyComponent.getVolume(0).setSize(new Fraction(16, 1));
		fluidComponent.getVolume(0).setSize(new Fraction(16, 1));
	}

	@Override
	public void tick() {
		if (fluidComponent.getVolume(0).getFraction().isBiggerThan(Fraction.BOTTLE) && energyComponent.getVolume(0).hasAvailable(Fraction.BUCKET) && fluidComponent.getVolume(0).getFluid() == AstromineFluids.ROCKET_FUEL) {
			fluidComponent.getVolume(0).setFraction(Fraction.subtract(fluidComponent.getVolume(0).getFraction(), Fraction.BOTTLE));
			energyComponent.getVolume(0).setFraction(Fraction.add(energyComponent.getVolume(0).getFraction(), Fraction.BUCKET));
		}

		for (PlayerEntity playerEntity : world.getPlayers()) {
			if (playerEntity.currentScreenHandler instanceof LiquidGeneratorContainer) {
				LiquidGeneratorContainer container = (LiquidGeneratorContainer) playerEntity.currentScreenHandler;

				if (container.blockEntity == this && !world.isClient) {
					sync();
				}
			}
		}

		for (Direction direction : Direction.values()) {
			BlockPos position = getPos().offset(direction);

			BlockEntity attached = world.getBlockEntity(position);

			if (attached instanceof ComponentProvider) {
				ComponentProvider provider = ComponentProvider.fromBlockEntity(attached);

				EnergyInventoryComponent inventory = provider.getSidedComponent(direction, AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);

				if (inventory != null && energyComponent.getVolume(0).hasStored(Fraction.BOTTLE)) {
					if (inventory.canInsert(energyComponent.getVolume(0))) {
						inventory.getVolume(0).pullVolume(energyComponent.getVolume(0), Fraction.BOTTLE);
					}

					if (attached instanceof BlockEntityClientSerializable && !world.isClient) {
						((BlockEntityClientSerializable) attached).sync();
					}
				}
			}
		}
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID || type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public boolean isBuffer() {
		return true;
	}
}
