package com.github.mixinors.astromine.client.fluid.render;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public class ExtendedFluidRenderHandler implements FluidRenderHandler {
	private final Sprite[] sprites;
	
	private final int tint;
	
	public ExtendedFluidRenderHandler(Sprite[] sprites, int tint) {
		this.sprites = sprites;
		this.tint = tint;
	}
	
	@Override
	public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
		return sprites;
	}
	
	@Override
	public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
		return tint;
	}
}
