package com.github.chainmailstudios.astromine.common.generator;

public interface Generator<T> {
	String getGeneratorName();
	void generate(T data);
}
