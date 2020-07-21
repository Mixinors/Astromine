package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.*;

import java.util.List;

public abstract class DefaultedEnergyBlockEntity extends DefaultedBlockEntity implements ComponentProvider, EnergyStorage {
	private final List<Runnable> energyListeners = Lists.newArrayList();
	private final EnergyVolume volume = new EnergyVolume(0, () -> {
		for (Runnable listener : energyListeners) {
			listener.run();
		}
	});

	public DefaultedEnergyBlockEntity(BlockEntityType<?> type) {
		super(type);
		transferComponent.add(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);
		addComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, new SimpleEnergyInventoryComponent());
		setMaxStoredPower(getEnergySize());
	}

	protected void addEnergyListener(Runnable listener) {
		this.energyListeners.add(listener);
	}

	public EnergyHandler asEnergy() {
		return Energy.of(this);
	}

	protected abstract int getEnergySize();

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
		return EnergyTier.HIGH;
	}

	@Override
	public double getMaxInput(EnergySide side) {
		return 1024;
	}

	@Override
	public double getMaxOutput(EnergySide side) {
		return 1024;
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
