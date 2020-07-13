package com.github.chainmailstudios.astromine.common.dimension.base;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

import java.util.OptionalLong;

public abstract class AtmosphericDimensionType extends DimensionType {
	protected AtmosphericDimensionType(OptionalLong fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, boolean shrunk, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, Identifier infiniburn, float ambientLight) {
		super(fixedTime, hasSkylight, hasCeiling, ultrawarm, natural, shrunk, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, logicalHeight, infiniburn, ambientLight);
	}

	public AtmosphericDimensionType(OptionalLong fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, boolean shrunk, boolean hasEnderDragonFight, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, BiomeAccessType biomeAccessType, Identifier infiniburn, float ambientLight) {
		super(fixedTime, hasSkylight, hasCeiling, ultrawarm, natural, shrunk, hasEnderDragonFight, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, logicalHeight, biomeAccessType, infiniburn, ambientLight);
	}
}
