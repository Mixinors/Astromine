package com.github.chainmailstudios.astromine.discoveries;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.discoveries.registry.*;
import com.github.chainmailstudios.astromine.registry.AstromineDimensions;
import com.github.chainmailstudios.astromine.registry.AstromineFeatures;

public class AstromineDiscoveriesCommon extends AstromineCommon {
	@Override
	public void onInitialize() {
		AstromineDiscoveriesBiomes.initialize();
		AstromineDiscoveriesBiomeSources.initialize();
		AstromineDiscoveriesChunkGenerators.initialize();
		AstromineDiscoveriesCommonCallbacks.initialize();
		AstromineDiscoveriesDimensionLayers.initialize();
		AstromineDiscoveriesOres.initialize();
		AstromineDiscoveriesGravities.initialize();
		AstromineDiscoveriesBlocks.initialize();
		AstromineDiscoveriesBlockEntityTypes.initialize();
		AstromineDiscoveriesItems.initialize();
		AstromineDiscoveriesItemGroups.initialize();
		AstromineDiscoveriesAtmospheres.initialize();
		AstromineDiscoveriesDimensions.initialize();
		AstromineDiscoveriesFeatures.initialize();
		AstromineDiscoveriesEntityTypes.initialize();
		AstromineDiscoveriesArmorMaterials.initialize();
		AstromineDiscoveriesCriteria.initialize();
		AstromineDiscoveriesSoundEvents.initialize();
		AstromineDiscoveriesRecipeSerializers.initialize();
		AstromineDiscoveriesScreenHandlers.initialize();
		AstromineDiscoveriesParticles.initialize();
	}
}
