package com.github.mixinors.astromine;

import net.fabricmc.api.ModInitializer;

public class AMFCommon implements ModInitializer {
	@Override
	public void onInitialize() {
		AMCommon.init();
	}
}
