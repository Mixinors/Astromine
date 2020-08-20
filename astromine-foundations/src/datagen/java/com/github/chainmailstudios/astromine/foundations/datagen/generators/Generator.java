package com.github.chainmailstudios.astromine.foundations.datagen.generators;

public interface Generator<T> {
	String getGeneratorName();
	void generate(T data);
}
