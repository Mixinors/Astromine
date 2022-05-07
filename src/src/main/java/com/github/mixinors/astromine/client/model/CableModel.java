package com.github.mixinors.astromine.client.model;

import com.github.mixinors.astromine.common.block.entity.cable.CableBlockEntity;
import com.mojang.datafixers.util.Pair;
import dev.vini2003.hammer.core.api.client.util.InstanceUtils;
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
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class CableModel implements FabricBakedModel, BakedModel, UnbakedModel {
	private Identifier centerModelId;
	private Identifier sideModelId;
	private Identifier connectorModelId;
	private Identifier insertConnectorModelId;
	private Identifier extractConnectorModelId;
	private Identifier insertExtractConnectorModelId;
	
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
		var client = InstanceUtils.getClient();
		
		var centerModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), centerModelId);
		var sideModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), sideModelId);
		var connectorModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), connectorModelId);
		var insertConnectorModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), insertConnectorModelId);
		var extractConnectorModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), extractConnectorModelId);
		var insertExtractConnectorModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), insertExtractConnectorModelId);
		
		var connections = (CableBlockEntity.Connections) ((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos);
		
		if (connections == null) return;
		
		// Emit Center
		centerModel.emitBlockQuads(blockView, state, pos, randomSupplier, context);
		
		for (var direction : Direction.values()) {
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
		var client = InstanceUtils.getClient();
		
		var centerModel = (FabricBakedModel) BakedModelManagerHelper.getModel(client.getBakedModelManager(), centerModelId);
		
		centerModel.emitItemQuads(stack, randomSupplier, context);
	}
	
	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList();
	}
	
	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return Collections.emptyList();
	}
	
	@Nullable
	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		return this;
	}
	
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
		return Collections.emptyList();
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
		return BakedModelManagerHelper.getModel(InstanceUtils.getClient().getBakedModelManager(), centerModelId).getParticleSprite();
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
