package com.github.chainmailstudios.astromine.datagen.generator.tag.set;

import com.github.chainmailstudios.astromine.datagen.generator.SetGenerator;
import com.github.chainmailstudios.astromine.datagen.generator.tag.TagGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialEntry;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;

/**
 * A Tag Generator intended to run for many {@link MaterialSet}s
 * and their {@link MaterialEntry}s
 */
public interface SetTagGenerator extends SetGenerator<TagData>, TagGenerator {
}
