package com.github.chainmailstudios.astromine.technologies.client.rei;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.server.ContainerInfo;
import net.minecraft.screen.ScreenHandler;

import java.util.List;

public abstract class SimpleTransferRecipeDisplay implements TransferRecipeDisplay {
	private final int width;
	private final int height;

	public SimpleTransferRecipeDisplay(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<ScreenHandler> containerInfo, ScreenHandler container) {
		return getInputEntries();
	}
}
