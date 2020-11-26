package com.github.chainmailstudios.astromine.common.component.general.provider;

import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;

/**
 * An interface meant to be implemented
 * by objects which provide an {@link ItemComponent}.
 */
public interface ItemComponentProvider {
    /** Returns this object's {@link ItemComponent}. */
    ItemComponent getItemComponent();
}
