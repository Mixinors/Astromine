package com.github.chainmailstudios.astromine.discoveries.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.ModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.GenericItemModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.onetime.SimpleBlockItemModelStateGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.modelstate.set.*;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.registry.AstromineModelStateGenerators;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesItems;

public class AstromineDiscoveriesModelStateGenerators extends AstromineModelStateGenerators {
	public final ModelStateGenerator ASTEROID_ORE = register(new ColumnBlockSetModelStateGenerator(MaterialItemType.ASTEROID_ORE, AstromineCommon.identifier("block/asteroid_stone")));

	public final ModelStateGenerator ASTEROID_CLUSTER = register(new GenericItemSetModelStateGenerator(MaterialItemType.ASTEROID_CLUSTER));

	public final ModelStateGenerator SPACE_SUIT = register(new GenericItemModelStateGenerator(
			AstromineDiscoveriesItems.SPACE_SUIT_HELMET,
			AstromineDiscoveriesItems.SPACE_SUIT_CHESTPLATE,
			AstromineDiscoveriesItems.SPACE_SUIT_LEGGINGS,
			AstromineDiscoveriesItems.SPACE_SUIT_BOOTS
	));

	public final ModelStateGenerator MISC_BLOCKSTATES = register(new SimpleBlockItemModelStateGenerator(AstromineDiscoveriesBlocks.ALTAR, AstromineDiscoveriesBlocks.ALTAR_PEDESTAL));
}
