package com.github.chainmailstudios.astromine.access;

import net.minecraft.entity.Entity;

public interface EntityAccess {
	Entity astromine_getLastVehicle();

	void astromine_setLastVehicle(Entity lastVehicle);
}
