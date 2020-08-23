package com.github.chainmailstudios.astromine.common.generator;

import com.github.chainmailstudios.astromine.common.generator.material.MaterialSet;

/**
 * A generator intended to be run multiple times for many {@link MaterialSet}s
 * and their {@link com.github.chainmailstudios.astromine.common.generator.material.MaterialEntry}s
 */
public interface SetGenerator<T> extends Generator<T> {
	boolean shouldGenerate(MaterialSet set);
	void generate(T data, MaterialSet set);
	default void generate(T data) { }
}