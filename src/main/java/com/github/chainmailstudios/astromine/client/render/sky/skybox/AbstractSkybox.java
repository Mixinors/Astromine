package com.github.chainmailstudios.astromine.client.render.sky.skybox;

import net.minecraft.client.util.math.MatrixStack;

public abstract class AbstractSkybox {
	public abstract void render(MatrixStack matrices, float tickDelta);
}
