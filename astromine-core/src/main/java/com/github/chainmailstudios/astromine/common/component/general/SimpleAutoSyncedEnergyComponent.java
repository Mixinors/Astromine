package com.github.chainmailstudios.astromine.common.component.general;

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

/**
 * A {@link SimpleEnergyComponent} that synchronizes itself
 * automatically.
 */
public class SimpleAutoSyncedEnergyComponent extends SimpleEnergyComponent implements AutoSyncedComponent {
    /** Instantiates a {@link SimpleAutoSyncedEnergyComponent}. */
    protected SimpleAutoSyncedEnergyComponent(double size) {
        super(size);
    }

    /** Instantiates a {@link SimpleAutoSyncedEnergyComponent}. */
    protected SimpleAutoSyncedEnergyComponent(EnergyVolume volume) {
        super(volume);
    }

    /** Instantiates a {@link SimpleAutoSyncedEnergyComponent}. */
    public static SimpleAutoSyncedEnergyComponent of(double size) {
        return new SimpleAutoSyncedEnergyComponent(size);
    }

    /** Instantiates a {@link SimpleAutoSyncedEnergyComponent}. */
    public static SimpleAutoSyncedEnergyComponent of(EnergyVolume volume) {
        return new SimpleAutoSyncedEnergyComponent(volume);
    }
}
