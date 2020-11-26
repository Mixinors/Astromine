package com.github.chainmailstudios.astromine.common.component.general.compatibility;

import com.github.chainmailstudios.astromine.common.component.general.base.EnergyComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.energy.WrappedEnergyVolume;
import team.reborn.energy.EnergyStorage;

import java.util.Collections;
import java.util.List;

/**
 * An {@link EnergyComponent} wrapped over an {@link EnergyStorage}.
 */
public class EnergyComponentFromEnergyStorage implements EnergyComponent {
    private final EnergyVolume volume;

    private EnergyComponentFromEnergyStorage(EnergyStorage storage) {
        this.volume = WrappedEnergyVolume.of(storage);
    }

    public static EnergyComponentFromEnergyStorage of(EnergyStorage storage) {
        return new EnergyComponentFromEnergyStorage(storage);
    }

    @Override
    public List<Runnable> getListeners() {
        return Collections.emptyList();
    }

    @Override
    public EnergyVolume getVolume() {
        return volume;
    }
}
