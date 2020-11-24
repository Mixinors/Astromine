package com.github.chainmailstudios.astromine.common.component.inventory;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.item.ItemStack;

/**
 * A {@link SimpleItemComponent} that synchronizes itself
 * automatically.
 */
public class SimpleAutoSyncedItemComponent extends SimpleItemComponent implements AutoSyncedComponent {
    /** Instantiates a {@link SimpleAutoSyncedItemComponent}. */
    protected SimpleAutoSyncedItemComponent(int size) {
        super(size);
    }

    /** Instantiates a {@link SimpleAutoSyncedItemComponent}. */
    protected SimpleAutoSyncedItemComponent(ItemStack... stacks) {
        super(stacks);
    }

    /** Instantiates a {@link SimpleAutoSyncedItemComponent}. */
    public static SimpleAutoSyncedItemComponent of(int size) {
        return new SimpleAutoSyncedItemComponent(size);
    }

    /** Instantiates a {@link SimpleAutoSyncedItemComponent}. */
    public static SimpleAutoSyncedItemComponent of(ItemStack... stacks) {
        return new SimpleAutoSyncedItemComponent(stacks);
    }
}
