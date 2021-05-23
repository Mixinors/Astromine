package com.github.mixinors.astromine;

import com.github.mixinors.astromine.AMDedicated;
import net.fabricmc.api.DedicatedServerModInitializer;

public class AMFDedicated implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		AMDedicated.init();
	}
}
