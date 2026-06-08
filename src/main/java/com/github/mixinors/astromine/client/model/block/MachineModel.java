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

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import com.github.mixinors.astromine.common.util.DirectionUtils;
import com.github.mixinors.astromine.registry.client.AMModels;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MachineModel implements IUnbakedGeometry<MachineModel> {
	private static final ChunkRenderTypeSet OVERLAY_RENDER_TYPES = ChunkRenderTypeSet.of(RenderType.cutout());
	private static final List<ResourceLocation> OVERLAY_MODEL_IDS = List.of(
			AMModels.SIDING_OVERLAY_INSERT_MODEL_ID,
			AMModels.SIDING_OVERLAY_EXTRACT_MODEL_ID,
			AMModels.SIDING_OVERLAY_INSERT_EXTRACT_MODEL_ID,
			AMModels.SIDING_OVERLAY_ITEM_INSERT_MODEL_ID,
			AMModels.SIDING_OVERLAY_ITEM_EXTRACT_MODEL_ID,
			AMModels.SIDING_OVERLAY_ITEM_INSERT_EXTRACT_MODEL_ID,
			AMModels.SIDING_OVERLAY_FLUID_INSERT_MODEL_ID,
			AMModels.SIDING_OVERLAY_FLUID_EXTRACT_MODEL_ID,
			AMModels.SIDING_OVERLAY_FLUID_INSERT_EXTRACT_MODEL_ID
	);

	private final ResourceLocation baseModelId;

	public MachineModel(ResourceLocation baseModelId) {
		this.baseModelId = baseModelId;
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
		for (var dependency : dependencies(baseModelId)) {
			var model = modelGetter.apply(dependency);

			if (model instanceof BlockModel blockModel) {
				blockModel.resolveParents(modelGetter);
			}
		}
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		var baseModel = baker.bake(baseModelId, modelState, spriteGetter);
		var overlayModels = new HashMap<ResourceLocation, BakedModel>();

		for (var overlayModelId : OVERLAY_MODEL_IDS) {
			overlayModels.put(overlayModelId, baker.bake(overlayModelId, BlockModelRotation.X0_Y0, spriteGetter));
		}

		return new Baked(baseModel, Map.copyOf(overlayModels));
	}

	private static List<ResourceLocation> dependencies(ResourceLocation baseModelId) {
		return ImmutableList.<ResourceLocation>builder()
				.add(baseModelId)
				.addAll(OVERLAY_MODEL_IDS)
				.build();
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

	public static class Loader implements IGeometryLoader<MachineModel> {
		public static final Loader INSTANCE = new Loader();

		private Loader() {
		}

		@Override
		public MachineModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
			return new MachineModel(ResourceLocation.parse(GsonHelper.getAsString(jsonObject, "base")));
		}
	}

	private static class Baked implements IDynamicBakedModel {
		private final BakedModel baseModel;
		private final Map<ResourceLocation, BakedModel> overlayModels;

		private Baked(BakedModel baseModel, Map<ResourceLocation, BakedModel> overlayModels) {
			this.baseModel = baseModel;
			this.overlayModels = overlayModels;
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, RandomSource random, ModelData modelData, @Nullable RenderType renderType) {
			var quads = new ArrayList<BakedQuad>();

			quads.addAll(baseModel.getQuads(state, face, random, modelData, renderType));

			if (renderType != null && !renderType.equals(RenderType.cutout())) {
				return quads;
			}

			var sidingData = modelData.get(ExtendedBlockEntity.SIDING_MODEL_DATA);

			if (sidingData == null) {
				return quads;
			}

			var split = sidingData.hasItemSidings() && sidingData.hasFluidSidings();

			for (var direction : DirectionUtils.VALUES) {
				addOverlay(quads, StorageType.ITEM, sidingData.itemSiding(direction), direction, split, state, face, random, modelData, renderType);
				addOverlay(quads, StorageType.FLUID, sidingData.fluidSiding(direction), direction, split, state, face, random, modelData, renderType);
			}

			return quads;
		}

		private void addOverlay(List<BakedQuad> quads, StorageType storageType, StorageSiding siding, Direction direction, boolean split, @Nullable BlockState state, @Nullable Direction face, RandomSource random, ModelData modelData, @Nullable RenderType renderType) {
			if (siding == StorageSiding.NONE) {
				return;
			}

			var overlayModel = overlayModels.get(AMModels.sidingOverlayModel(storageType, siding, split));

			if (overlayModel == null) {
				return;
			}

			for (var quad : overlayModel.getQuads(state, null, random, modelData, renderType)) {
				var rotated = rotateQuad(quad, direction);

				if (face == null || rotated.getDirection() == face) {
					quads.add(rotated);
				}
			}
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, RandomSource random) {
			return getQuads(state, face, random, ModelData.EMPTY, null);
		}

		@Override
		public boolean useAmbientOcclusion() {
			return baseModel.useAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return baseModel.isGui3d();
		}

		@Override
		public boolean usesBlockLight() {
			return baseModel.usesBlockLight();
		}

		@Override
		public boolean isCustomRenderer() {
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleIcon() {
			return baseModel.getParticleIcon();
		}

		@Override
		public ItemTransforms getTransforms() {
			return baseModel.getTransforms();
		}

		@Override
		public ItemOverrides getOverrides() {
			return baseModel.getOverrides();
		}

		@Override
		public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource random, ModelData modelData) {
			return ChunkRenderTypeSet.union(baseModel.getRenderTypes(state, random, modelData), OVERLAY_RENDER_TYPES);
		}
	}
}
