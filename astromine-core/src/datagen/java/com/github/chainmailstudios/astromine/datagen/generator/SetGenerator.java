package com.github.chainmailstudios.astromine.datagen.generator;

import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;

/**
 * A generator intended to be run multiple times for many {@link MaterialSet}s
 * and their {@link com.github.chainmailstudios.astromine.datagen.material.MaterialEntry}s
 */
public interface SetGenerator<T> extends Generator<T> {
	boolean shouldGenerate(MaterialSet set);

	void generate(T data, MaterialSet set);

	default void generate(T data) {
	}
}