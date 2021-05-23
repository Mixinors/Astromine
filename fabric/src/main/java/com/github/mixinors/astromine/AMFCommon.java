package com.github.mixinors.astromine;

import com.github.mixinors.astromine.common.registry.*;
import net.fabricmc.api.ModInitializer;

public class AMFCommon implements ModInitializer {
	@Override
	public void onInitialize() {
		AMCommon.init();
		
		AMFCallbacks.init();
		AMFComponents.init();
		AMFContainerInfoHandlers.init();
		AMFEntityTypes.init();
		AMFFeatures.init();
		AMFItems.init();
		AMFNetworkMembers.init();
		AMFOres.init();
	}
}
