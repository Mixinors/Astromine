package com.github.chainmailstudios.astromine.common.callback;

import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * A callback called when a {@link BlockEntityTransferComponent.TransferEntry}
 * is added to a {@link BlockEntityTransferComponent}.
 */
public interface TransferEntryCallback {
    Event<TransferEntryCallback> EVENT = EventFactory.createArrayBacked(TransferEntryCallback.class, (listeners) -> (entry) -> {
        for (TransferEntryCallback listener : listeners) {
            listener.handle(entry);
        }
    });

    /** Handle the added {@link BlockEntityTransferComponent.TransferEntry}. */
    void handle(BlockEntityTransferComponent.TransferEntry entry);
}
