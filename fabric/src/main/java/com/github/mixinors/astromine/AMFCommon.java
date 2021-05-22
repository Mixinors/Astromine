package com.github.mixinors.astromine;

import com.github.mixinors.astromine.common.registry.*;
import net.fabricmc.api.ModInitializer;

public class AMFCommon implements ModInitializer {
	@Override
	public void onInitialize() {
		AMCommon.init();
		
		AMFComponents.init();
		AMFNetworkMembers.init();
		AMFContainerInfoHandlers.init();
		AMFEntityTypes.init();
		AMFFeatures.init();
	}
}
