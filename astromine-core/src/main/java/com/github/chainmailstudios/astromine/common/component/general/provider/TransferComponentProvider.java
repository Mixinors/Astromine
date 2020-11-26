package com.github.chainmailstudios.astromine.common.component.general.provider;

import com.github.chainmailstudios.astromine.common.component.block.entity.TransferComponent;

/**
 * An interface meant to be implemented
 * by objects which provide a {@link TransferComponent}.
 */
public interface TransferComponentProvider {
    /** Returns this object's {@link TransferComponent}. */
    TransferComponent getTransferComponent();
}
