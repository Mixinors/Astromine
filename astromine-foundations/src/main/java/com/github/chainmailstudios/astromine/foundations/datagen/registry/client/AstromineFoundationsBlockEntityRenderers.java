package com.github.chainmailstudios.astromine.foundations.datagen.registry.client;

import com.github.chainmailstudios.astromine.foundations.client.render.block.AltarBlockEntityRenderer;
import com.github.chainmailstudios.astromine.foundations.client.render.block.ItemDisplayerBlockEntityRenderer;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.client.AstromineBlockEntityRenderers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

import java.util.function.Function;

public class AstromineFoundationsBlockEntityRenderers {
	public static void initialize() {
		register(AstromineFoundationsBlockEntityTypes.ITEM_DISPLAYER, ItemDisplayerBlockEntityRenderer::new);
		register(AstromineFoundationsBlockEntityTypes.ALTAR, AltarBlockEntityRenderer::new);
	}

	public static <B extends BlockEntity, C extends BlockEntityType<B>> void register(C c, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<B>> b) {
		AstromineBlockEntityRenderers.register(c, b);
	}
}
