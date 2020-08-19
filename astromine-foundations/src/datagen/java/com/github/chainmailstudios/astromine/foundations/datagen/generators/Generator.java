package com.github.chainmailstudios.astromine.foundations.datagen.generators;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;

public interface Generator {
	boolean shouldGenerate(MaterialSet set);

	String getGeneratorName();
}
