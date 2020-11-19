package com.github.chainmailstudios.astromine.access;

import com.github.chainmailstudios.astromine.mixin.EntityNavigationMixin;
import com.github.chainmailstudios.astromine.mixin.WorldChunkMixin;

/**
 * A mixin helper class, used by {@link EntityNavigationMixin}.
 */
public interface EntityAccess {
	boolean astromine_isInIndustrialFluid();
}
