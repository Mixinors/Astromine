package com.github.chainmailstudios.astromine.common.multiblock;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public abstract class MultiblockControllerBlockEntityRenderer<T extends MultiblockControllerBlockEntity> extends BlockEntityRenderer<T> {
	private final ModelPart model;

	public MultiblockControllerBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher, ModelPart model) {
		super(dispatcher);
		this.model = model;
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay) {
		model.render(matrices, provider.getBuffer(RenderLayer.getSolid()), light, overlay);
	}
}
