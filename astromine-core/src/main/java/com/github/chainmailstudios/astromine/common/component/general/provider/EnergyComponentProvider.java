package com.github.chainmailstudios.astromine.common.component.general.provider;

import com.github.chainmailstudios.astromine.common.component.general.base.EnergyComponent;

/**
 * An interface meant to be implemented
 * by objects which provide an {@link EnergyComponent}.
 */
public interface EnergyComponentProvider {
    /** Returns this object's {@link EnergyComponent}. */
    EnergyComponent getEnergyComponent();
}
