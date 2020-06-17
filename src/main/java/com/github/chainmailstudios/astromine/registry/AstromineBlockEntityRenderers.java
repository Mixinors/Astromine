package com.github.chainmailstudios.astromine.registry;

import java.util.function.Function;

import com.github.chainmailstudios.astromine.client.render.block.HolographicBridgeBlockEntityRenderer;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public class AstromineBlockEntityRenderers {
	public static void initialize() {
		register(AstromineBlockEntityTypes.HOLOGRAPHIC_BRIDGE, HolographicBridgeBlockEntityRenderer::new);
	}

	public static <B extends BlockEntity, C extends BlockEntityType<B>> void register(C c, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<B>> b) {
		BlockEntityRendererRegistry.INSTANCE.register(c, b);
	}
}
