package com.github.chainmailstudios.astromine.datagen.generator;

public interface Generator<T> {
	String getGeneratorName();

	void generate(T data);
}
