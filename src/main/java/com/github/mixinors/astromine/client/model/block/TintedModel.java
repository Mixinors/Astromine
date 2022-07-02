/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.client.model.block;

import com.github.mixinors.astromine.client.accessor.BakedQuadAccessor;
import com.github.mixinors.astromine.mixin.client.JsonUnbakedModelAccessor;
import com.mojang.datafixers.util.Pair;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class TintedModel extends JsonUnbakedModel {
	private final JsonUnbakedModel unbakedModel;
	
	private static Map<Identifier, JsonUnbakedModel> UNBAKED_MODELS = new HashMap<>();
	
	private static JsonUnbakedModel getUnbakedModel(Identifier modelId) {
		var unbakedModel = UNBAKED_MODELS.get(modelId);
		
		if (unbakedModel == null) {
			var client = InstanceUtil.getClient();
			
			var resourceManager = client.getResourceManager();
			
			Resource resource = null;
			
			try {
				if (modelId.getPath().contains("models/")) {
					resource = resourceManager.getResource(modelId);
				} else {
					resource = resourceManager.getResource(new Identifier(modelId.getNamespace(), "models/" + modelId.getPath() + ".json"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			
			unbakedModel = JsonUnbakedModel.deserialize(reader);
			
			UNBAKED_MODELS.put(modelId, unbakedModel);
		}
		
		return unbakedModel;
	}
	
	public TintedModel(Identifier modelId) {
		super(
				((JsonUnbakedModelAccessor) getUnbakedModel(modelId)).getParentId(),
				getUnbakedModel(modelId).getElements(),
				((JsonUnbakedModelAccessor) getUnbakedModel(modelId)).getTextureMap(),
				getUnbakedModel(modelId).useAmbientOcclusion(),
				getUnbakedModel(modelId).getGuiLight(),
				getUnbakedModel(modelId).getTransformations(),
				getUnbakedModel(modelId).getOverrides()
		);
		
		this.unbakedModel = getUnbakedModel(modelId);
	}
	
	@Override
	public Collection<Identifier> getModelDependencies() {
		return unbakedModel.getModelDependencies();
	}
	
	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return unbakedModel.getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences);
	}
	
	@Nullable
	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		try {
			var model = unbakedModel.bake(loader, textureGetter, rotationContainer, modelId);
			
			for (var dir : Direction.values()) {
				for (var quad : model.getQuads(Blocks.STONE.getDefaultState(), dir, new Random())) {
					((BakedQuadAccessor) quad).setColorIndex(0);
				}
			}
			
			return new Baked(model);
		} catch (Exception ignore) {
			return null;
		}
	}
	
	public static class Baked implements FabricBakedModel, BakedModel {
		private final BakedModel model;
		
		public Baked(BakedModel model) {
			this.model = model;
		}
		
		@Override
		public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
			for (var quad : model.getQuads(state, null, randomSupplier.get())) {
				((BakedQuadAccessor) quad).setColorIndex(0);
			}
			
			((FabricBakedModel) model).emitBlockQuads(blockView, state, pos, randomSupplier, context);
		}
		
		@Override
		public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
			for (var dir : Direction.values()) {
				for (var quad : model.getQuads(((BlockItem) stack.getItem()).getBlock().getDefaultState(), dir, new Random())) {
					((BakedQuadAccessor) quad).setColorIndex(0);
				}
			}
			
			((FabricBakedModel) model).emitItemQuads(stack, randomSupplier, context);
		}
		
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
			return model.getQuads(state, face, random);
		}
		
		@Override
		public boolean useAmbientOcclusion() {
			return model.useAmbientOcclusion();
		}
		
		@Override
		public boolean hasDepth() {
			return model.hasDepth();
		}
		
		@Override
		public boolean isSideLit() {
			return model.isSideLit();
		}
		
		@Override
		public boolean isBuiltin() {
			return model.isBuiltin();
		}
		
		@Override
		public Sprite getParticleSprite() {
			return model.getParticleSprite();
		}
		
		@Override
		public ModelTransformation getTransformation() {
			return model.getTransformation();
		}
		
		@Override
		public ModelOverrideList getOverrides() {
			return model.getOverrides();
		}
		
		@Override
		public boolean isVanillaAdapter() {
			return false;
		}
	}
}
