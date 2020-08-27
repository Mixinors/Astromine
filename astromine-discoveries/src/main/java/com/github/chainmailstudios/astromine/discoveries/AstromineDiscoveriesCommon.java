package com.github.chainmailstudios.astromine.discoveries;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.discoveries.registry.*;

public class AstromineDiscoveriesCommon extends AstromineCommon {
	@Override
	public void onInitialize() {
		AstromineDiscoveriesBiomeSources.initialize();
		AstromineDiscoveriesChunkGenerators.initialize();
		AstromineDiscoveriesCommonCallbacks.initialize();
		AstromineDiscoveriesCriteria.initialize();
		AstromineDiscoveriesDimensionLayers.initialize();
		AstromineDiscoveriesDimensions.initialize();
		AstromineDiscoveriesFeatures.initialize();
		AstromineDiscoveriesGravities.initialize();
	}
}
