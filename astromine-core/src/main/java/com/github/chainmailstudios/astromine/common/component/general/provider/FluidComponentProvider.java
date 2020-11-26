package com.github.chainmailstudios.astromine.common.component.general.provider;

import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;

/**
 * An interface meant to be implemented
 * by objects which provide a {@link FluidComponent}.
 */
public interface FluidComponentProvider {
    /** Returns this object's {@link FluidComponent}. */
    FluidComponent getFluidComponent();
}
