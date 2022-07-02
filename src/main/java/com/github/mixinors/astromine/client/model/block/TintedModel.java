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
import com.mojang.datafixers.util.Pair;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class TintedModel extends ForwardingBakedModel implements UnbakedModel {
	private final Identifier modelId;
	
	private FabricBakedModel model;
	
	private UnbakedModel unbakedModel;
	
	public TintedModel(Identifier modelId) {
		this.modelId = modelId;
		
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
		
		this.unbakedModel = JsonUnbakedModel.deserialize(reader);
	}
	
	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		if (model != null) {
			for (var quad : ((BakedModel) model).getQuads(state, null, randomSupplier.get())) {
				((BakedQuadAccessor) quad).setColorIndex(0);
			}
		}
	}
	
	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		if (model != null) {
			model.emitItemQuads(stack, randomSupplier, context);
		}
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
			this.model = (FabricBakedModel) unbakedModel.bake(loader, textureGetter, rotationContainer, modelId);
			this.wrapped = (BakedModel) model;
			
			for (var dir : Direction.values()) {
				for (var quad : ((BakedModel) model).getQuads(Blocks.STONE.getDefaultState(), dir, new Random())) {
					((BakedQuadAccessor) quad).setColorIndex(0);
				}
			}
		} catch (Exception ignore) {
			return null;
		}
		
		return this;
	}
}
