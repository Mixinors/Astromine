package com.github.chainmailstudios.astromine.datagen.registry;

import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

import java.util.ArrayList;
import java.util.List;

public class AstromineMaterialSets {
	public static final List<MaterialSet> MATERIAL_SETS = new ArrayList<>();

	public static MaterialSet register(MaterialSet set) {
		MATERIAL_SETS.add(set);
		System.out.println("registered material " + set.getName());
		return set;
	}

	public static List<MaterialSet> getMaterialSets() {
		return MATERIAL_SETS;
	}
}
