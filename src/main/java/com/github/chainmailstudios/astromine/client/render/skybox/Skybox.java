package com.github.chainmailstudios.astromine.client.render.skybox;

import net.minecraft.client.util.math.MatrixStack;

public interface Skybox {
	void render(MatrixStack matrices, float tickDelta);
}
