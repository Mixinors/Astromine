package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.*;

public abstract class DefaultedEnergyBlockEntity extends DefaultedBlockEntity implements ComponentProvider, EnergyStorage {
	private final EnergyVolume volume = EnergyVolume.empty();

	public DefaultedEnergyBlockEntity(BlockEntityType<?> type) {
		super(type);
		transferComponent.add(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);
	}

	public EnergyHandler asEnergy() {
		return Energy.of(this);
	}

	@Override
	public double getStored(EnergySide energySide) {
		return volume.getAmount();
	}

	@Override
	public void setStored(double v) {
		volume.setAmount(v);
	}

	@Override
	public double getMaxStoredPower() {
		return volume.getMaxAmount();
	}

	public void setMaxStoredPower(double v) {
		volume.setMaxAmount(v);
	}

	@Override
	public EnergyTier getTier() {
		return EnergyTier.INFINITE;
	}

	public final EnergyVolume getEnergyVolume() {
		return volume;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("energy", getEnergyVolume().toTag(new CompoundTag()));
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		super.fromTag(state, tag);
		volume.setAmount(0);
		if (tag.contains("energy")) {
			EnergyVolume energy = EnergyVolume.fromTag(tag.getCompound("energy"));
			volume.setAmount(energy.getAmount());
		}
	}
}
