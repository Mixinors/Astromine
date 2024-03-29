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

import com.github.mixinors.astromine.common.block.entity.cable.CableBlockEntity;
import com.github.mixinors.astromine.common.util.DirectionUtils;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class CableModel implements FabricBakedModel, BakedModel, UnbakedModel {
	private final Identifier centerModelId;
	private final Identifier sideModelId;
	private final Identifier connectorModelId;
	private final Identifier insertConnectorModelId;
	private final Identifier extractConnectorModelId;
	private final Identifier insertExtractConnectorModelId;
	
	public CableModel(Identifier centerModelId, Identifier sideModelId, Identifier connectorModelId, Identifier insertConnectorModelId, Identifier extractConnectorModelId, Identifier insertConnectorExtractModelid) {
		this.centerModelId = centerModelId;
		this.sideModelId = sideModelId;
		this.connectorModelId = connectorModelId;
		this.insertConnectorModelId = insertConnectorModelId;
		this.extractConnectorModelId = extractConnectorModelId;
		this.insertExtractConnectorModelId = insertConnectorExtractModelid;
	}
	
	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		var client = InstanceUtil.getClient();
		
		var centerModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), centerModelId);
		var sideModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), sideModelId);
		var connectorModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), connectorModelId);
		var insertConnectorModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), insertConnectorModelId);
		var extractConnectorModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), extractConnectorModelId);
		var insertExtractConnectorModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), insertExtractConnectorModelId);
		
		var connections = (CableBlockEntity.Connections) ((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos);
		
		if (connections == null) {
			return;
		}
		
		// Emit Center
		centerModel.emitBlockQuads(blockView, state, pos, randomSupplier, context);
		
		for (var direction : DirectionUtils.VALUES) {
			var hasSide = connections.hasSide(direction);
			var hasConnector = connections.hasConnector(direction);
			
			if (hasSide || hasConnector) {
				pushTransform(direction, context);
				
				// Emit Side
				if (hasSide) {
					sideModel.emitBlockQuads(blockView, state, pos, randomSupplier, context);
				}
				
				// Emit Connector
				if (hasConnector) {
					if (connections.isInsert(direction)) {
						insertConnectorModel.emitBlockQuads(blockView, state, pos, randomSupplier, context);
					} else if (connections.isExtract(direction)) {
						extractConnectorModel.emitBlockQuads(blockView, state, pos, randomSupplier, context);
					} else if (connections.isInsertExtract(direction)) {
						insertExtractConnectorModel.emitBlockQuads(blockView, state, pos, randomSupplier, context);
					} else {
						connectorModel.emitBlockQuads(blockView, state, pos, randomSupplier, context);
					}
				}
				
				context.popTransform();
			}
		}
	}
	
	private void pushTransform(Direction direction, RenderContext context) {
		var angle = switch (direction) {
			case SOUTH -> Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F);
			case WEST -> Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F);
			case EAST -> Vec3f.POSITIVE_Y.getDegreesQuaternion(270.0F);
			case UP -> Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F);
			case DOWN -> Vec3f.POSITIVE_X.getDegreesQuaternion(270.0F);
			
			default -> Vec3f.ZERO.getDegreesQuaternion(0.0F);
		};
		
		context.pushTransform(new RotationQuadTransform(angle));
	}
	
	private static class RotationQuadTransform implements RenderContext.QuadTransform {
		private final Quaternion rotation;
		
		public RotationQuadTransform(Quaternion rotation) {
			this.rotation = rotation;
		}
		
		@Override
		public boolean transform(MutableQuadView quad) {
			for (var i = 0; i < 4; ++i) {
				var pos = quad.copyPos(i, null);
				pos.subtract(new Vec3f(8.0F / 16.0F, 8.0F / 16.0F, 8.0F / 16.0F));
				pos.rotate(rotation);
				pos.add(new Vec3f(8.0F / 16.0F, 8.0F / 16.0F, 8.0F / 16.0F));
				
				quad.pos(i, pos);
			}
			
			return true;
		}
	}
	
	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		var client = InstanceUtil.getClient();
		
		var centerModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), centerModelId);
		
		centerModel.emitItemQuads(stack, randomSupplier, context);
	}
	
	@Override
	public Collection<Identifier> getModelDependencies() {
		return ImmutableList.of();
	}
	
	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return ImmutableList.of();
	}
	
	@Nullable
	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		return this;
	}
	
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
		return ImmutableList.of();
	}
	
	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}
	
	@Override
	public boolean hasDepth() {
		return false;
	}
	
	@Override
	public boolean isSideLit() {
		return false;
	}
	
	@Override
	public boolean isBuiltin() {
		return false;
	}
	
	@Override
	public Sprite getParticleSprite() {
		return BakedModelManagerHelper.getModel(InstanceUtil.getClient().getBakedModelManager(), centerModelId).getParticleSprite();
	}
	
	@Override
	public ModelTransformation getTransformation() {
		return ModelTransformation.NONE;
	}
	
	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}
	
	@Override
	public boolean isVanillaAdapter() {
		return false;
	}
}
