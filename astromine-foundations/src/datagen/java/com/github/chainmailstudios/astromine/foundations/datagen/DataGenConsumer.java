package com.github.chainmailstudios.astromine.foundations.datagen;

import me.shedaniel.cloth.api.datagen.v1.DataGeneratorHandler;

public interface DataGenConsumer<T> {
	void accept(DataGeneratorHandler handler, T value);
}
