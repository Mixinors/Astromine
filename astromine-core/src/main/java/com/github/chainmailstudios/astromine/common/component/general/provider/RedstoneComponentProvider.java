package com.github.chainmailstudios.astromine.common.component.general.provider;

import com.github.chainmailstudios.astromine.common.component.block.entity.RedstoneComponent;

/**
 * An interface meant to be implemented
 * by objects which provide a {@link RedstoneComponent}.
 */
public interface RedstoneComponentProvider {
    /** Returns this object's {@link RedstoneComponent}. */
    RedstoneComponent getRedstoneComponent();
}
