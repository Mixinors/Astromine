package com.github.chainmailstudios.astromine.common.callback;

import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface TransferEntryCallback {
    Event<TransferEntryCallback> EVENT = EventFactory.createArrayBacked(TransferEntryCallback.class, (listeners) -> (entry) -> {
        for (TransferEntryCallback listener : listeners) {
            listener.handle(entry);
        }
    });

    void handle(BlockEntityTransferComponent.TransferEntry entry);
}
