/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.transportations.client.render.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.registry.ConveyorSpecialScaleRegistry;
import com.github.chainmailstudios.astromine.transportations.common.block.property.ConveyorProperties;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.PositionalConveyable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import java.util.Random;

@Environment(EnvType.CLIENT)
public interface ConveyorRenderer<T extends BlockEntity> {
	default void renderSupport(T blockEntity, ConveyorTypes type, float position, float speed, float horizontalPosition, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider) {
		PositionalConveyable conveyor = (PositionalConveyable) blockEntity;
		Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		int rotation = type == ConveyorTypes.DOWN_VERTICAL ? -90 : 90;

		matrixStack.pushPose();

		matrixStack.translate(0.5, 4F / 16F, 0.5);

		if (type == ConveyorTypes.DOWN_VERTICAL) {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
		}

		if (direction == Direction.NORTH && rotation == 90) {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
		} else if (direction == Direction.SOUTH && rotation == -90) {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
		} else if (direction == Direction.EAST) {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
		} else if (direction == Direction.WEST) {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(-rotation));
		}

		matrixStack.translate(-0.5F, -1.001F, -0.5F);

		if (type == ConveyorTypes.VERTICAL && blockEntity.getBlockState().getValue(ConveyorProperties.CONVEYOR) && conveyor.getPosition() == 16)
			matrixStack.translate(0, -1F / 16F, 0);

		if (type == ConveyorTypes.NORMAL) {
			matrixStack.translate(0, 0, position / speed);
		} else if (type == ConveyorTypes.VERTICAL) {
			matrixStack.translate(0, position / speed, horizontalPosition / speed);
		} else if (type == ConveyorTypes.DOWN_VERTICAL) {
			matrixStack.translate(0, (position / (speed)) + (blockEntity.getBlockState().getValue(ConveyorProperties.CONVEYOR) ? 1 : 0), horizontalPosition / speed);
		}

		Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);

		BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(AstromineCommon.MOD_ID, "conveyor_supports"), ""));

		int light = LightTexture.pack(blockEntity.getLevel().getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos()), blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos()));
		Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), vertexConsumerProvider.getBuffer(RenderType.cutout()), null, model, blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ(), light,
			OverlayTexture.NO_OVERLAY);

		matrixStack.popPose();
	}

	default void renderItem(T blockEntity, ItemStack stack, float position, int speed, float horizontalPosition, ConveyorTypes type, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider) {
		Random random = new Random();
		Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		int rotation = type == ConveyorTypes.DOWN_VERTICAL ? -90 : 90;
		int int_1 = 1;
		if (stack.getCount() > 48) {
			int_1 = 5;
		} else if (stack.getCount() > 32) {
			int_1 = 4;
		} else if (stack.getCount() > 16) {
			int_1 = 3;
		} else if (stack.getCount() > 1) {
			int_1 = 2;
		}

		int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
		random.setSeed(seed);

		if (!stack.isEmpty() && stack.getItem() instanceof BlockItem && !ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem())) {
			int light = LightTexture.pack(blockEntity.getLevel().getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos()), blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos()));
			Block block = ((BlockItem) stack.getItem()).getBlock();

			for (int i = 0; i < int_1; i++) {
				matrixStack.pushPose();
				matrixStack.translate(0.5F, 4F / 16F, 0.5F);
				if (direction == Direction.NORTH && rotation == 90) {
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
				} else if (direction == Direction.SOUTH && rotation == -90) {
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
				} else if (direction == Direction.EAST) {
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
				} else if (direction == Direction.WEST) {
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(-rotation));
				}

				if (type == ConveyorTypes.NORMAL) {
					matrixStack.translate(0, 0, position / speed);
				} else if (type == ConveyorTypes.VERTICAL) {
					matrixStack.translate(0, position / speed, horizontalPosition / speed);
				} else if (type == ConveyorTypes.DOWN_VERTICAL) {
					matrixStack.translate(0, (position / (speed)) + (blockEntity.getBlockState().getValue(ConveyorProperties.CONVEYOR) ? 1 : 0), horizontalPosition / speed);
				}
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				matrixStack.translate(-0.5F, 0, -0.5F);

				if (i > 0) {
					float x = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float y = (random.nextFloat() * 2.0F) * 0.15F;
					float z = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					matrixStack.translate(x * 2, y * 0.5F, z * 2);
				}

				Minecraft.getInstance().getBlockRenderer().renderSingleBlock(block.defaultBlockState(), matrixStack, vertexConsumerProvider, light, OverlayTexture.NO_OVERLAY);
				if (stack.getItem() instanceof DoubleHighBlockItem) {
					matrixStack.translate(0, 1, 0);
					Minecraft.getInstance().getBlockRenderer().renderSingleBlock(block.defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), matrixStack, vertexConsumerProvider, light, OverlayTexture.NO_OVERLAY);
				}

				matrixStack.popPose();
			}
		} else if (!stack.isEmpty()) {
			int light = LightTexture.pack(blockEntity.getLevel().getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos()), blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos()));

			matrixStack.pushPose();
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
			matrixStack.translate(0.5, 0.5, -4.5F / 16F);

			if (stack.getItem() instanceof BlockItem && ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem()) && ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getB()) {
				matrixStack.translate(0, 0, -3.5F / 16F);
			}

			if (direction == Direction.NORTH && rotation == 90) {
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
			} else if (direction == Direction.SOUTH && rotation == -90) {
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
			} else if (direction == Direction.EAST) {
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-rotation));
			} else if (direction == Direction.WEST) {
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(rotation));
			}

			if (type == ConveyorTypes.NORMAL) {
				matrixStack.translate(0, position / speed, 0);
			} else if (type == ConveyorTypes.VERTICAL) {
				matrixStack.translate(0, horizontalPosition / speed, -position / speed);
			} else if (type == ConveyorTypes.DOWN_VERTICAL) {
				matrixStack.translate(0, horizontalPosition / speed, -(position / (speed)) + (blockEntity.getBlockState().getValue(ConveyorProperties.CONVEYOR) ? -1 : 0));
			}

			if (ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem())) {
				float scale = ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getA();
				matrixStack.scale(scale, scale, scale);

				for (int i = 1; i < int_1; i++) {
					matrixStack.pushPose();
					if (ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getB()) {
						float x = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
						float y = (random.nextFloat() * 2.0F) * 0.15F;
						float z = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
						matrixStack.translate(x, z, y * 0.5F);
					}
					Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, vertexConsumerProvider);
					matrixStack.popPose();
				}
			} else {
				matrixStack.scale(0.8F, 0.8F, 0.8F);
			}

			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, vertexConsumerProvider);
			matrixStack.popPose();
		}
	}

	default void renderItem(T blockEntity, ItemStack stack, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider) {
		if (!stack.isEmpty()) {
			int light = LightTexture.pack(blockEntity.getLevel().getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos()), blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos()));

			matrixStack.pushPose();
			if (!(stack.getItem() instanceof BlockItem))
				matrixStack.scale(0.8F, 0.8F, 0.8F);
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, vertexConsumerProvider);
			matrixStack.popPose();
		}
	}

	default void renderItem(T blockEntity, Direction direction, ItemStack stack, float position, int speed, float horizontalPosition, ConveyorTypes type, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider) {
		Random random = new Random();
		int rotation = type == ConveyorTypes.DOWN_VERTICAL ? -90 : 90;
		int int_1 = 1;
		if (stack.getCount() > 48) {
			int_1 = 5;
		} else if (stack.getCount() > 32) {
			int_1 = 4;
		} else if (stack.getCount() > 16) {
			int_1 = 3;
		} else if (stack.getCount() > 1) {
			int_1 = 2;
		}

		int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
		random.setSeed(seed);

		if (!stack.isEmpty() && stack.getItem() instanceof BlockItem && !ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem())) {
			int light = LightTexture.pack(blockEntity.getLevel().getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos().relative(direction)), blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos().relative(direction)));
			Block block = ((BlockItem) stack.getItem()).getBlock();

			for (int i = 0; i < int_1; i++) {
				matrixStack.pushPose();
				matrixStack.translate(0.5F, 4F / 16F, 0.5F);
				if (direction == Direction.NORTH && rotation == 90) {
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
				} else if (direction == Direction.SOUTH && rotation == -90) {
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
				} else if (direction == Direction.EAST) {
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
				} else if (direction == Direction.WEST) {
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(-rotation));
				}

				if (type == ConveyorTypes.NORMAL) {
					matrixStack.translate(0, 0, position / speed);
				} else if (type == ConveyorTypes.VERTICAL) {
					matrixStack.translate(0, position / speed, horizontalPosition / speed);
				} else if (type == ConveyorTypes.DOWN_VERTICAL) {
					matrixStack.translate(0, (position / (speed)) + (blockEntity.getBlockState().getValue(ConveyorProperties.CONVEYOR) ? 1 : 0), horizontalPosition / speed);
				}
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				matrixStack.translate(-0.5F, 0, -0.5F);

				if (i > 0) {
					float x = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float y = (random.nextFloat() * 2.0F) * 0.15F;
					float z = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					matrixStack.translate(x * 2, y * 0.5F, z * 2);
				}

				Minecraft.getInstance().getBlockRenderer().renderSingleBlock(block.defaultBlockState(), matrixStack, vertexConsumerProvider, light, OverlayTexture.NO_OVERLAY);
				if (stack.getItem() instanceof DoubleHighBlockItem) {
					matrixStack.translate(0, 1, 0);
					Minecraft.getInstance().getBlockRenderer().renderSingleBlock(block.defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), matrixStack, vertexConsumerProvider, light, OverlayTexture.NO_OVERLAY);
				}

				matrixStack.popPose();
			}
		} else if (!stack.isEmpty()) {
			int light = LightTexture.pack(blockEntity.getLevel().getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos()), blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos()));

			matrixStack.pushPose();
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
			matrixStack.translate(0.5, 0.5, -4.5F / 16F);

			if (stack.getItem() instanceof BlockItem && ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem()) && ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getB()) {
				matrixStack.translate(0, 0, -3.5F / 16F);
			}

			if (direction == Direction.NORTH && rotation == 90) {
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
			} else if (direction == Direction.SOUTH && rotation == -90) {
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
			} else if (direction == Direction.EAST) {
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-rotation));
			} else if (direction == Direction.WEST) {
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(rotation));
			}

			if (type == ConveyorTypes.NORMAL) {
				matrixStack.translate(0, position / speed, 0);
			} else if (type == ConveyorTypes.VERTICAL) {
				matrixStack.translate(0, horizontalPosition / speed, -position / speed);
			} else if (type == ConveyorTypes.DOWN_VERTICAL) {
				matrixStack.translate(0, horizontalPosition / speed, -(position / (speed)) + (blockEntity.getBlockState().getValue(ConveyorProperties.CONVEYOR) ? -1 : 0));
			}

			if (ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem())) {
				float scale = ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getA();
				matrixStack.scale(scale, scale, scale);

				for (int i = 1; i < int_1; i++) {
					matrixStack.pushPose();
					if (ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getB()) {
						float x = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
						float y = (random.nextFloat() * 2.0F) * 0.15F;
						float z = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
						matrixStack.translate(x, z, y * 0.5F);
					}
					Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, vertexConsumerProvider);
					matrixStack.popPose();
				}
			} else {
				matrixStack.scale(0.8F, 0.8F, 0.8F);
			}

			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, vertexConsumerProvider);
			matrixStack.popPose();
		}
	}
}
