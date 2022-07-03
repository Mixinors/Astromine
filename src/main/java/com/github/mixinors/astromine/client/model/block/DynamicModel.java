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
import com.github.mixinors.astromine.registry.common.AMProperties;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class DynamicModel extends JsonUnbakedModel {
	private JsonUnbakedModel staticUnbakedModel = null;
	private JsonUnbakedModel dynamicUnbakedModel = null;
	
	private final boolean item;
	
	private static Map<Identifier, JsonUnbakedModel> DYNAMIC_UNBAKED_MODELS = new HashMap<>();
	private static Map<Identifier, JsonUnbakedModel> STATIC_UNBAKED_MODELS = new HashMap<>();
	
	private static final String MODELS_PREFIX = "models/";
	private static final String BLOCK_PREFIX = "block/";
	
	private static final String BLOCK_DYNAMIC_PREFIX = "block/dynamic_";
	private static final String BLOCK_STATIC_PREFIX = "block/static_";
	
	private static final String JSON_SUFFIX = ".json";
	
	private static final String ALL = "all";
	
	private static Either<JsonUnbakedModel, Pair<JsonUnbakedModel, JsonUnbakedModel>> getUnbakedModel(Identifier modelId) {
		var item = modelId.toString().contains("item/");
		
		var dynamicUnbakedModel = DYNAMIC_UNBAKED_MODELS.get(modelId);
		var staticUnbakedModel = STATIC_UNBAKED_MODELS.get(modelId);
		
		DYNAMIC_UNBAKED_MODELS.clear();
		STATIC_UNBAKED_MODELS.clear();
		
		if (dynamicUnbakedModel == null || staticUnbakedModel == null) {
			var client = InstanceUtil.getClient();
			
			var manager = client.getResourceManager();
			
			Resource resource;
			
			try {
				if (modelId.getPath().contains(MODELS_PREFIX)) {
					resource = manager.getResource(modelId);
				} else {
					resource = manager.getResource(new Identifier(modelId.getNamespace(), MODELS_PREFIX + modelId.getPath() + JSON_SUFFIX));
				}
			} catch (Exception e) {
				e.printStackTrace();
				
				return null;
			}
			
			if (resource == null) {
				System.out.println("Resource null!");
			}
			
			String string;
			
			try {
				string = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
				
				return null;
			}
			
			staticUnbakedModel = JsonUnbakedModel.deserialize(string);
			
			if (item) {
				return Either.left(staticUnbakedModel);
			}
			
			var staticTextures = ((JsonUnbakedModelAccessor) staticUnbakedModel).getTextureMap();
			
			for (var entry : staticTextures.entrySet()) {
				var id = entry.getKey();
				var either = entry.getValue();
				
				if (either.left().isPresent()) {
					var spriteId = either.left().get();
					var textureId = spriteId.getTextureId();
					
					if (textureId.getPath().contains("moon") && !textureId.getPath().contains("static") && !textureId.getPath().contains("dynamic")) {
						staticTextures.put(id, Either.left(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(textureId.getNamespace(), textureId.getPath().replace("block/", "block/static_").replace("item/", "item/static_")))));
					}
				}
			}
			
			STATIC_UNBAKED_MODELS.put(modelId, staticUnbakedModel);
			
			dynamicUnbakedModel = JsonUnbakedModel.deserialize(string);
			
			var dynamicTextures = ((JsonUnbakedModelAccessor) dynamicUnbakedModel).getTextureMap();
			
			for (var entry : dynamicTextures.entrySet()) {
				var id = entry.getKey();
				var either = entry.getValue();
				
				if (either.left().isPresent()) {
					var spriteId = either.left().get();
					var textureId = spriteId.getTextureId();
					
					if (textureId.getPath().contains("moon") && !textureId.getPath().contains("dynamic") && !textureId.getPath().contains("dynamic")) {
						dynamicTextures.put(id, Either.left(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(textureId.getNamespace(), textureId.getPath().replace("block/", "block/dynamic_").replace("item/", "item/dynamic_")))));
					}
				}
			}
			
			DYNAMIC_UNBAKED_MODELS.put(modelId, dynamicUnbakedModel);
		}
		
		return Either.right(Pair.of(dynamicUnbakedModel, staticUnbakedModel));
	}
	
	public static JsonUnbakedModel getWrappedModel(Identifier modelId) {
		var either = getUnbakedModel(modelId);
		
		if (either.left().isPresent()) {
			return either.left().get();
		} else {
			return either.right().get().getSecond();
		}
	}
	
	public DynamicModel(Identifier modelId) {
		super(
				((JsonUnbakedModelAccessor) getWrappedModel(modelId)).getParentId(),
				getWrappedModel(modelId).getElements(),
				((JsonUnbakedModelAccessor) getWrappedModel(modelId)).getTextureMap(),
				getWrappedModel(modelId).useAmbientOcclusion(),
				getWrappedModel(modelId).getGuiLight(),
				getWrappedModel(modelId).getTransformations(),
				getWrappedModel(modelId).getOverrides()
		);
		
		this.item = modelId.getPath().contains("item/");
		
		if (!item) {
			this.dynamicUnbakedModel = getUnbakedModel(modelId).right().get().getFirst();
			this.staticUnbakedModel = getUnbakedModel(modelId).right().get().getSecond();
		} else {
			this.staticUnbakedModel = getUnbakedModel(modelId).left().get();
		}
	}
	
	@Override
	public Collection<Identifier> getModelDependencies() {
		var dependencies = new ArrayList<Identifier>();
		
		if (!item) {
			dependencies.addAll(dynamicUnbakedModel.getModelDependencies());
		}
		
		dependencies.addAll(staticUnbakedModel.getModelDependencies());
		
		return dependencies;
	}
	
	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		var dependencies = new ArrayList<SpriteIdentifier>();
		
		if (!item) {
			dependencies.addAll(dynamicUnbakedModel.getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences));
		}
		
		dependencies.addAll(staticUnbakedModel.getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences));
		
		return dependencies;
	}
	
	@Nullable
	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		if (!item) {
			var dynamicBakedModel = dynamicUnbakedModel.bake(loader, textureGetter, rotationContainer, modelId);
			var staticBakedModel = staticUnbakedModel.bake(loader, textureGetter, rotationContainer, modelId);
			
			return new Baked(dynamicBakedModel, staticBakedModel);
		} else {
			var staticBakedModel = staticUnbakedModel.bake(loader, textureGetter, rotationContainer, modelId);
			
			return new Baked(staticBakedModel);
		}
	}
	
	public static class Baked implements FabricBakedModel, BakedModel {
		private final BakedModel dynamicBakedModel;
		private final BakedModel staticBakedModel;
		
		public Baked(BakedModel staticBakedModel) {
			this.staticBakedModel = staticBakedModel;
			
			this.dynamicBakedModel = null;
		}
		
		public Baked(BakedModel dynamicBakedModel, BakedModel staticBakedModel) {
			this.dynamicBakedModel = dynamicBakedModel;
			this.staticBakedModel = staticBakedModel;
			
			var random = new Random();
			random.setSeed(42L);
			
			for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
				var cullFace = ModelHelper.faceFromIndex(i);
				
				var quads = dynamicBakedModel.getQuads(null, cullFace, random);
				
				for (var quad : quads) {
					((BakedQuadAccessor) quad).setColorIndex(0);
				}
			}
		}
		
		@Override
		public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
			BakedModel model;
			
			if (state == null || (state.contains(AMProperties.DYNAMIC) && state.get(AMProperties.DYNAMIC)) || dynamicBakedModel == null) {
				model = staticBakedModel;
			} else {
				model = dynamicBakedModel;
			}
			
			((FabricBakedModel) model).emitBlockQuads(blockView, state, pos, randomSupplier, context);
		}
		
		@Override
		public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
			((FabricBakedModel) staticBakedModel).emitItemQuads(stack, randomSupplier, context);
		}
		
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
			return dynamicBakedModel != null ? dynamicBakedModel.getQuads(state, face, random) : staticBakedModel.getQuads(state, face, random);
		}
		
		@Override
		public boolean useAmbientOcclusion() {
			return dynamicBakedModel != null ? dynamicBakedModel.useAmbientOcclusion() : staticBakedModel.useAmbientOcclusion();
		}
		
		@Override
		public boolean hasDepth() {
			return dynamicBakedModel != null ? dynamicBakedModel.hasDepth() : staticBakedModel.hasDepth();
		}
		
		@Override
		public boolean isSideLit() {
			return dynamicBakedModel != null ? dynamicBakedModel.isSideLit() : staticBakedModel.isSideLit();
		}
		
		@Override
		public boolean isBuiltin() {
			return dynamicBakedModel != null ? dynamicBakedModel.isBuiltin() : staticBakedModel.isBuiltin();
		}
		
		@Override
		public Sprite getParticleSprite() {
			return dynamicBakedModel != null ? dynamicBakedModel.getParticleSprite() : staticBakedModel.getParticleSprite();
		}
		
		@Override
		public ModelTransformation getTransformation() {
			return dynamicBakedModel != null ? dynamicBakedModel.getTransformation() : staticBakedModel.getTransformation();
		}
		
		@Override
		public ModelOverrideList getOverrides() {
			return dynamicBakedModel != null ? dynamicBakedModel.getOverrides() : staticBakedModel.getOverrides();
		}
		
		@Override
		public boolean isVanillaAdapter() {
			return false;
		}
	}
}
