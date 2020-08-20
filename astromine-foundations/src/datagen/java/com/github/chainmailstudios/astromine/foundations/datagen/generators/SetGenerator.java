package com.github.chainmailstudios.astromine.foundations.datagen.generators;

import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;

/**
 * A generator intended to be run multiple times for many {@link MaterialSet}s
 * and their {@link com.github.chainmailstudios.astromine.foundations.datagen.MaterialEntry}s
 */
public interface SetGenerator<T> extends Generator<T> {
	boolean shouldGenerate(MaterialSet set);
	void generate(T data, MaterialSet set);
	default void generate(T data) { }
}