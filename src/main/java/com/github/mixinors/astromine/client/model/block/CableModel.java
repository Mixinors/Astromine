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
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

public class CableModel implements IDynamicBakedModel, IUnbakedGeometry<CableModel> {
	private final ResourceLocation centerModelId;
	private final ResourceLocation sideModelId;
	private final ResourceLocation connectorModelId;
	private final ResourceLocation insertConnectorModelId;
	private final ResourceLocation extractConnectorModelId;
	private final ResourceLocation insertExtractConnectorModelId;
	
	public CableModel(ResourceLocation centerModelId, ResourceLocation sideModelId, ResourceLocation connectorModelId, ResourceLocation insertConnectorModelId, ResourceLocation extractConnectorModelId, ResourceLocation insertConnectorExtractModelid) {
		this.centerModelId = centerModelId;
		this.sideModelId = sideModelId;
		this.connectorModelId = connectorModelId;
		this.insertConnectorModelId = insertConnectorModelId;
		this.extractConnectorModelId = extractConnectorModelId;
		this.insertExtractConnectorModelId = insertConnectorExtractModelid;
	}
	
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, RandomSource random, ModelData modelData, @Nullable RenderType renderType) {
		var quads = new ArrayList<BakedQuad>();
		var connections = modelData.get(CableBlockEntity.CONNECTIONS);
		
		addQuads(quads, model(centerModelId), state, face, random, modelData, renderType);
		
		if (connections == null) {
			return quads;
		}
		
		for (var direction : DirectionUtils.VALUES) {
			var hasSide = connections.hasSide(direction);
			var hasConnector = connections.hasConnector(direction);
			
			if (!hasSide && !hasConnector) {
				continue;
			}
			
			if (hasSide) {
				addRotatedQuads(quads, model(sideModelId), direction, state, face, random, modelData, renderType);
			}
			
			if (hasConnector) {
				var connectorModel = model(connectorModelId);
				
				if (connections.isInsert(direction)) {
					connectorModel = model(insertConnectorModelId);
				} else if (connections.isExtract(direction)) {
					connectorModel = model(extractConnectorModelId);
				} else if (connections.isInsertExtract(direction)) {
					connectorModel = model(insertExtractConnectorModelId);
				}
				
				addRotatedQuads(quads, connectorModel, direction, state, face, random, modelData, renderType);
			}
		}
		
		return quads;
	}
	
	private static BakedModel model(ResourceLocation id) {
		return Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(id));
	}
	
	private static void addQuads(List<BakedQuad> out, BakedModel model, @Nullable BlockState state, @Nullable Direction face, RandomSource random, ModelData modelData, @Nullable RenderType renderType) {
		out.addAll(model.getQuads(state, face, random, modelData, renderType));
	}
	
	private static void addRotatedQuads(List<BakedQuad> out, BakedModel model, Direction direction, @Nullable BlockState state, @Nullable Direction face, RandomSource random, ModelData modelData, @Nullable RenderType renderType) {
		for (var quad : model.getQuads(state, null, random, modelData, renderType)) {
			var rotated = rotateQuad(quad, direction);
			
			if (face == null || rotated.getDirection() == face) {
				out.add(rotated);
			}
		}
	}
	
	private static BakedQuad rotateQuad(BakedQuad quad, Direction direction) {
		var rotation = rotation(direction);
		var vertices = quad.getVertices().clone();
		var vertexSize = vertices.length / 4;
		
		for (var i = 0; i < 4; ++i) {
			var offset = i * vertexSize;
			var position = new Vector3f(
					Float.intBitsToFloat(vertices[offset]),
					Float.intBitsToFloat(vertices[offset + 1]),
					Float.intBitsToFloat(vertices[offset + 2])
			);
			
			position.sub(0.5F, 0.5F, 0.5F).rotate(rotation).add(0.5F, 0.5F, 0.5F);
			
			vertices[offset] = Float.floatToRawIntBits(position.x());
			vertices[offset + 1] = Float.floatToRawIntBits(position.y());
			vertices[offset + 2] = Float.floatToRawIntBits(position.z());
		}
		
		return new BakedQuad(vertices, quad.getTintIndex(), rotateDirection(quad.getDirection(), rotation), quad.getSprite(), quad.isShade(), quad.hasAmbientOcclusion());
	}
	
	private static Direction rotateDirection(Direction direction, Quaternionf rotation) {
		var normal = new Vector3f(direction.getStepX(), direction.getStepY(), direction.getStepZ()).rotate(rotation);
		var nearest = direction;
		var nearestDot = Float.NEGATIVE_INFINITY;
		
		for (var candidate : Direction.values()) {
			var dot = normal.x() * candidate.getStepX() + normal.y() * candidate.getStepY() + normal.z() * candidate.getStepZ();
			
			if (dot > nearestDot) {
				nearest = candidate;
				nearestDot = dot;
			}
		}
		
		return nearest;
	}
	
	private static Quaternionf rotation(Direction direction) {
		return switch (direction) {
			case SOUTH -> new Quaternionf().rotateY((float) Math.toRadians(180.0F));
			case WEST -> new Quaternionf().rotateY((float) Math.toRadians(90.0F));
			case EAST -> new Quaternionf().rotateY((float) Math.toRadians(270.0F));
			case UP -> new Quaternionf().rotateX((float) Math.toRadians(90.0F));
			case DOWN -> new Quaternionf().rotateX((float) Math.toRadians(270.0F));
			default -> new Quaternionf();
		};
	}
	
	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
		for (var dependency : ImmutableList.of(centerModelId, sideModelId, connectorModelId, insertConnectorModelId, extractConnectorModelId, insertExtractConnectorModelId)) {
			var model = modelGetter.apply(dependency);
			
			if (model instanceof BlockModel blockModel) {
				blockModel.resolveParents(modelGetter);
			}
		}
	}
	
	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		return this;
	}
	
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, RandomSource random) {
		return getQuads(state, face, random, ModelData.EMPTY, null);
	}
	
	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}
	
	@Override
	public boolean isGui3d() {
		return false;
	}
	
	@Override
	public boolean usesBlockLight() {
		return false;
	}
	
	@Override
	public boolean isCustomRenderer() {
		return false;
	}
	
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return model(centerModelId).getParticleIcon();
	}
	
	@Override
	public ItemTransforms getTransforms() {
		return ItemTransforms.NO_TRANSFORMS;
	}
	
	@Override
	public ItemOverrides getOverrides() {
		return ItemOverrides.EMPTY;
	}
	
	public static class Loader implements IGeometryLoader<CableModel> {
		public static final Loader INSTANCE = new Loader();
		
		private Loader() {
		}
		
		@Override
		public CableModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
			var center = ResourceLocation.parse(GsonHelper.getAsString(jsonObject, "center"));
			var side = resource(jsonObject, "side", "block/cable_side");
			var connector = resource(jsonObject, "connector", "block/cable_connector");
			var insertConnector = resource(jsonObject, "insert_connector", "block/cable_connector_insert");
			var extractConnector = resource(jsonObject, "extract_connector", "block/cable_connector_extract");
			var insertExtractConnector = resource(jsonObject, "insert_extract_connector", "block/cable_connector_insert_extract");
			
			return new CableModel(center, side, connector, insertConnector, extractConnector, insertExtractConnector);
		}
		
		private static ResourceLocation resource(JsonObject jsonObject, String key, String fallback) {
			return jsonObject.has(key) ? ResourceLocation.parse(GsonHelper.getAsString(jsonObject, key)) : ResourceLocation.fromNamespaceAndPath("astromine", fallback);
		}
	}
}
