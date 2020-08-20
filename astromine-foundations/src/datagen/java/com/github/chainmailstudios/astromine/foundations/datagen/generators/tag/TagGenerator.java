package com.github.chainmailstudios.astromine.foundations.datagen.generators.tag;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.datagen.generators.Generator;
import me.shedaniel.cloth.api.datagen.v1.TagData;

public interface TagGenerator extends Generator {
	void generateTag(TagData tags, MaterialSet set);
}
