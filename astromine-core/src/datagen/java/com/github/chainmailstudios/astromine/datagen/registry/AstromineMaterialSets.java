package com.github.chainmailstudios.astromine.datagen.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

import java.util.ArrayList;
import java.util.List;

public abstract class AstromineMaterialSets {
	public static final List<MaterialSet> MATERIAL_SETS = new ArrayList<>();

	public static MaterialSet register(MaterialSet set) {
		MATERIAL_SETS.add(set);
		AstromineCommon.LOGGER.info("Registered " + set.getName() + ".");
		return set;
	}

	public static List<MaterialSet> getMaterialSets() {
		return MATERIAL_SETS;
	}
}
