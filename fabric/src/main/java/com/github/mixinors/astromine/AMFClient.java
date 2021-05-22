package com.github.mixinors.astromine;

import net.fabricmc.api.ClientModInitializer;

public class AMFClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AMClient.init();
	}
}
