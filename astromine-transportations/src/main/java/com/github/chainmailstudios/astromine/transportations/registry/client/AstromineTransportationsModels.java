package com.github.chainmailstudios.astromine.transportations.registry.client;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.transportations.common.block.ConveyorBlock;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlocks;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.block.Block;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AstromineTransportationsModels {
	private static final Map<Identifier, Function<ModelProviderContext, UnbakedModel>> MODELS = new HashMap<>();

	public static void initialize() {
		if (true) return;
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(ResourceProvider::new);
		registerConveyors(
			AstromineTransportationsBlocks.BASIC_CONVEYOR,
			AstromineTransportationsBlocks.ADVANCED_CONVEYOR,
			AstromineTransportationsBlocks.ELITE_CONVEYOR
		);
	}

	private static void registerConveyors(Block... blocks) {
		for (Block block : blocks) {
			registerConveyor(block);
		}
	}

	private static void registerConveyor(Block block) {
		if (!(block instanceof ConveyorBlock)) {
			throw new IllegalArgumentException("Not a conveyor!");
		}
		Identifier id = Registry.BLOCK.getId(block);
		MODELS.put(new Identifier(id.getNamespace(), "block/" + id.getPath()), buildConveyor((ConveyorBlock) block));
	}

	private static Function<ModelProviderContext, UnbakedModel> buildConveyor(ConveyorBlock block) {
		Identifier blockId = Registry.BLOCK.getId(block);
		return context -> new JsonUnbakedModel(AstromineCommon.identifier("block/conveyor"), Collections.emptyList(),
			ImmutableMap.of(
				"particle", Either.left(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(blockId.getNamespace(), "block/" + blockId.getPath()))),
				"conveyor", Either.left(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(blockId.getNamespace(), "block/" + blockId.getPath())))
			), false, null, ModelTransformation.NONE, Collections.emptyList()) {
			@Override
			public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
				BasicBakedModel parent = (BasicBakedModel) super.bake(loader, textureGetter, rotationContainer, modelId);
				parent.getQuads(null, null, null).forEach(quad -> {
//					if (quad.sprite.getName().equals(new Identifier(blockId.getNamespace(), "block/" + blockId.getPath()))) {
//
//					}
				});
				return new ConveyorBakedModel(parent);
			}
		};
	}

	public static class ConveyorBakedModel extends ForwardingBakedModel implements FabricBakedModel {
		public ConveyorBakedModel(BakedModel parent) {
			this.wrapped = parent;
		}

		/*@Override
		public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
			Renderer renderer = RendererAccess.INSTANCE.getRenderer();
			Identifier id = Registry.BLOCK.getId(state.getBlock());

			for (int i = 0; i < 6; ++i) {
				Direction face = ModelHelper.faceFromIndex(i);
				List<BakedQuad> quads = wrapped.getQuads(state, face,  randomSupplier.get());
				for (BakedQuad q : quads) {
					context.getEmitter().fromVanilla(q, renderer.materialFinder().find(), face);
				}
			}

			List<BakedQuad> quads = wrapped.getQuads(state,  null,  randomSupplier.get());
			for (BakedQuad q : quads) {
				if (!q.sprite.getName().equals(new ResourceLocation(id.getNamespace(), "block/" + id.getPath()))) {
					context.getEmitter().fromVanilla(q, renderer.materialFinder().find(), null);
				}
			}
		}*/
	}

	public static class ResourceProvider implements ModelResourceProvider {
		private final ResourceManager manager;

		public ResourceProvider(ResourceManager manager) {
			this.manager = manager;
		}

		@Override
		public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) throws ModelProviderException {
			Function<ModelProviderContext, UnbakedModel> function = MODELS.get(resourceId);
			return function == null ? null : function.apply(context);
		}
	}
}
