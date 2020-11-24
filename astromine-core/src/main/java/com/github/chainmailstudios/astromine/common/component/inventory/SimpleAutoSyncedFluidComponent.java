package com.github.chainmailstudios.astromine.common.component.inventory;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

/**
 * A {@link SimpleFluidComponent} that synchronizes itself
 * automatically.
 */
public class SimpleAutoSyncedFluidComponent extends SimpleFluidComponent implements AutoSyncedComponent {
    /** Instantiates a {@link SimpleAutoSyncedItemComponent}. */
    protected SimpleAutoSyncedFluidComponent(int size) {
        super(size);
    }

    /** Instantiates a {@link SimpleAutoSyncedItemComponent}. */
    protected SimpleAutoSyncedFluidComponent(FluidVolume... volumes) {
        super(volumes);
    }

    /** Instantiates a {@link SimpleAutoSyncedFluidComponent}. */
    public static SimpleAutoSyncedFluidComponent of(int size) {
        return new SimpleAutoSyncedFluidComponent(size);
    }

    /** Instantiates a {@link SimpleAutoSyncedFluidComponent}. */
    public static SimpleAutoSyncedFluidComponent of(FluidVolume... volumes) {
        return new SimpleAutoSyncedFluidComponent(volumes);
    }
}
