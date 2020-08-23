package com.github.chainmailstudios.astromine.common.generator.tag;

import com.github.chainmailstudios.astromine.common.generator.SetGenerator;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialEntry;
import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.TagData;

/**
 * A Tag Generator intended to run for many {@link MaterialSet}s
 * and their {@link MaterialEntry}s
 */
public interface SetTagGenerator extends SetGenerator<TagData> {
}
