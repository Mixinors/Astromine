package com.github.chainmailstudios.astromine.common.volume.energy;

import team.reborn.energy.EnergyStorage;

public class WrappedEnergyVolume extends EnergyVolume {
    private final EnergyStorage storage;

    private WrappedEnergyVolume(EnergyStorage storage) {
        super(storage.getStored(null), storage.getMaxStoredPower());
        this.storage = storage;
    }

    private WrappedEnergyVolume(EnergyStorage storage, Runnable runnable) {
        super(storage.getStored(null), storage.getMaxStoredPower(), runnable);
        this.storage = storage;
    }

    public static WrappedEnergyVolume of(EnergyStorage storage) {
        return new WrappedEnergyVolume(storage);
    }

    @Override
    public Double getAmount() {
        return storage.getStored(null);
    }

    @Override
    public void setAmount(Double amount) {
        storage.setStored(amount);
    }

    @Override
    public Double getSize() {
        return storage.getMaxStoredPower();
    }

    @Override
    public void setSize(Double size) {
        // Unsupported, because of the fucking API.
    }
}
