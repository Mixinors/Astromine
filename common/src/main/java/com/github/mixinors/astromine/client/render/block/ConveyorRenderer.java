/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.client.render.block;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.ConveyorBlock;
import com.github.mixinors.astromine.common.util.ClientUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TallBlockItem;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;

import com.github.mixinors.astromine.common.registry.ConveyorSpecialScaleRegistry;
import com.github.mixinors.astromine.common.conveyor.ConveyorTypes;
import com.github.mixinors.astromine.common.conveyor.PositionalConveyable;

import java.util.Random;

@Environment(EnvType.CLIENT)
public interface ConveyorRenderer<T extends BlockEntity> {
	default void renderSupport(T blockEntity, ConveyorTypes type, float position, float speed, float horizontalPosition, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider) {
		PositionalConveyable conveyor = (PositionalConveyable) blockEntity;
		var direction = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);
		int rotation = type == ConveyorTypes.DOWN_VERTICAL ? -90 : 90;

		matrixStack.push();

		matrixStack.translate(0.5, 4F / 16F, 0.5);

		if (type == ConveyorTypes.DOWN_VERTICAL) {
			matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
		}

		if (direction == Direction.NORTH && rotation == 90) {
			matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
		} else if (direction == Direction.SOUTH && rotation == -90) {
			matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
		} else if (direction == Direction.EAST) {
			matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
		} else if (direction == Direction.WEST) {
			matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-rotation));
		}

		matrixStack.translate(-0.5F, -1.001F, -0.5F);

		if (type == ConveyorTypes.VERTICAL && blockEntity.getCachedState().get(ConveyorBlock.CONVEYOR) && conveyor.getPosition() == 16)
			matrixStack.translate(0, -1F / 16F, 0);

		if (type == ConveyorTypes.NORMAL) {
			matrixStack.translate(0, 0, position / speed);
		} else if (type == ConveyorTypes.VERTICAL) {
			matrixStack.translate(0, position / speed, horizontalPosition / speed);
		} else if (type == ConveyorTypes.DOWN_VERTICAL) {
			matrixStack.translate(0, (position / (speed)) + (blockEntity.getCachedState().get(ConveyorBlock.CONVEYOR) ? 1 : 0), horizontalPosition / speed);
		}

		ClientUtils.getInstance().getTextureManager().bindTexture(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);

		BakedModel model = ClientUtils.getInstance().getBakedModelManager().getModel(new ModelIdentifier(new Identifier(AMCommon.MOD_ID, "conveyor_supports"), ""));

		int light = LightmapTextureManager.pack(blockEntity.getWorld().getLightLevel(LightType.BLOCK, blockEntity.getPos()), blockEntity.getWorld().getLightLevel(LightType.SKY, blockEntity.getPos()));
		ClientUtils.getInstance().getBlockRenderManager().getModelRenderer().render(matrixStack.peek(), vertexConsumerProvider.getBuffer(RenderLayer.getCutout()), null, model, blockEntity.getPos().getX(), blockEntity.getPos().getY(), blockEntity.getPos().getZ(), light,
			OverlayTexture.DEFAULT_UV);

		matrixStack.pop();
	}

	default void renderItem(T blockEntity, ItemStack stack, float position, int speed, float horizontalPosition, ConveyorTypes type, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider) {
		Random random = new Random();
		var direction = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);
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

		int seed = stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getDamage();
		random.setSeed(seed);

		if (!stack.isEmpty() && stack.getItem() instanceof BlockItem && !ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem())) {
			int light = LightmapTextureManager.pack(blockEntity.getWorld().getLightLevel(LightType.BLOCK, blockEntity.getPos()), blockEntity.getWorld().getLightLevel(LightType.SKY, blockEntity.getPos()));
			Block block = ((BlockItem) stack.getItem()).getBlock();

			for (var i = 0; i < int_1; i++) {
				matrixStack.push();
				matrixStack.translate(0.5F, 4F / 16F, 0.5F);
				if (direction == Direction.NORTH && rotation == 90) {
					matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
				} else if (direction == Direction.SOUTH && rotation == -90) {
					matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
				} else if (direction == Direction.EAST) {
					matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
				} else if (direction == Direction.WEST) {
					matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-rotation));
				}

				if (type == ConveyorTypes.NORMAL) {
					matrixStack.translate(0, 0, position / speed);
				} else if (type == ConveyorTypes.VERTICAL) {
					matrixStack.translate(0, position / speed, horizontalPosition / speed);
				} else if (type == ConveyorTypes.DOWN_VERTICAL) {
					matrixStack.translate(0, (position / (speed)) + (blockEntity.getCachedState().get(ConveyorBlock.CONVEYOR) ? 1 : 0), horizontalPosition / speed);
				}
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				matrixStack.translate(-0.5F, 0, -0.5F);

				if (i > 0) {
					float x = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float y = (random.nextFloat() * 2.0F) * 0.15F;
					float z = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					matrixStack.translate(x * 2, y * 0.5F, z * 2);
				}

				ClientUtils.getInstance().getBlockRenderManager().renderBlockAsEntity(block.getDefaultState(), matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV);
				if (stack.getItem() instanceof TallBlockItem) {
					matrixStack.translate(0, 1, 0);
					ClientUtils.getInstance().getBlockRenderManager().renderBlockAsEntity(block.getDefaultState().with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV);
				}

				matrixStack.pop();
			}
		} else if (!stack.isEmpty()) {
			int light = LightmapTextureManager.pack(blockEntity.getWorld().getLightLevel(LightType.BLOCK, blockEntity.getPos()), blockEntity.getWorld().getLightLevel(LightType.SKY, blockEntity.getPos()));

			matrixStack.push();
			matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
			matrixStack.translate(0.5, 0.5, -4.5F / 16F);

			if (stack.getItem() instanceof BlockItem && ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem()) && ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getRight()) {
				matrixStack.translate(0, 0, -3.5F / 16F);
			}

			if (direction == Direction.NORTH && rotation == 90) {
				matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
			} else if (direction == Direction.SOUTH && rotation == -90) {
				matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
			} else if (direction == Direction.EAST) {
				matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-rotation));
			} else if (direction == Direction.WEST) {
				matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));
			}

			if (type == ConveyorTypes.NORMAL) {
				matrixStack.translate(0, position / speed, 0);
			} else if (type == ConveyorTypes.VERTICAL) {
				matrixStack.translate(0, horizontalPosition / speed, -position / speed);
			} else if (type == ConveyorTypes.DOWN_VERTICAL) {
				matrixStack.translate(0, horizontalPosition / speed, -(position / (speed)) + (blockEntity.getCachedState().get(ConveyorBlock.CONVEYOR) ? -1 : 0));
			}

			if (ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem())) {
				float scale = ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getLeft();
				matrixStack.scale(scale, scale, scale);

				for (var i = 1; i < int_1; i++) {
					matrixStack.push();
					if (ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getRight()) {
						float x = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
						float y = (random.nextFloat() * 2.0F) * 0.15F;
						float z = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
						matrixStack.translate(x, z, y * 0.5F);
					}
					ClientUtils.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider);
					matrixStack.pop();
				}
			} else {
				matrixStack.scale(0.8F, 0.8F, 0.8F);
			}

			ClientUtils.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider);
			matrixStack.pop();
		}
	}

	default void renderItem(T blockEntity, ItemStack stack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider) {
		if (!stack.isEmpty()) {
			int light = LightmapTextureManager.pack(blockEntity.getWorld().getLightLevel(LightType.BLOCK, blockEntity.getPos()), blockEntity.getWorld().getLightLevel(LightType.SKY, blockEntity.getPos()));

			matrixStack.push();
			if (!(stack.getItem() instanceof BlockItem))
				matrixStack.scale(0.8F, 0.8F, 0.8F);
			ClientUtils.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider);
			matrixStack.pop();
		}
	}

	default void renderItem(T blockEntity, Direction direction, ItemStack stack, float position, int speed, float horizontalPosition, ConveyorTypes type, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider) {
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

		int seed = stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getDamage();
		random.setSeed(seed);

		if (!stack.isEmpty() && stack.getItem() instanceof BlockItem && !ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem())) {
			int light = LightmapTextureManager.pack(blockEntity.getWorld().getLightLevel(LightType.BLOCK, blockEntity.getPos().offset(direction)), blockEntity.getWorld().getLightLevel(LightType.SKY, blockEntity.getPos().offset(direction)));
			Block block = ((BlockItem) stack.getItem()).getBlock();

			for (var i = 0; i < int_1; i++) {
				matrixStack.push();
				matrixStack.translate(0.5F, 4F / 16F, 0.5F);
				if (direction == Direction.NORTH && rotation == 90) {
					matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
				} else if (direction == Direction.SOUTH && rotation == -90) {
					matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
				} else if (direction == Direction.EAST) {
					matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
				} else if (direction == Direction.WEST) {
					matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-rotation));
				}

				if (type == ConveyorTypes.NORMAL) {
					matrixStack.translate(0, 0, position / speed);
				} else if (type == ConveyorTypes.VERTICAL) {
					matrixStack.translate(0, position / speed, horizontalPosition / speed);
				} else if (type == ConveyorTypes.DOWN_VERTICAL) {
					matrixStack.translate(0, (position / (speed)) + (blockEntity.getCachedState().get(ConveyorBlock.CONVEYOR) ? 1 : 0), horizontalPosition / speed);
				}
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				matrixStack.translate(-0.5F, 0, -0.5F);

				if (i > 0) {
					float x = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float y = (random.nextFloat() * 2.0F) * 0.15F;
					float z = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					matrixStack.translate(x * 2, y * 0.5F, z * 2);
				}

				ClientUtils.getInstance().getBlockRenderManager().renderBlockAsEntity(block.getDefaultState(), matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV);
				if (stack.getItem() instanceof TallBlockItem) {
					matrixStack.translate(0, 1, 0);
					ClientUtils.getInstance().getBlockRenderManager().renderBlockAsEntity(block.getDefaultState().with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV);
				}

				matrixStack.pop();
			}
		} else if (!stack.isEmpty()) {
			int light = LightmapTextureManager.pack(blockEntity.getWorld().getLightLevel(LightType.BLOCK, blockEntity.getPos()), blockEntity.getWorld().getLightLevel(LightType.SKY, blockEntity.getPos()));

			matrixStack.push();
			matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
			matrixStack.translate(0.5, 0.5, -4.5F / 16F);

			if (stack.getItem() instanceof BlockItem && ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem()) && ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getRight()) {
				matrixStack.translate(0, 0, -3.5F / 16F);
			}

			if (direction == Direction.NORTH && rotation == 90) {
				matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
			} else if (direction == Direction.SOUTH && rotation == -90) {
				matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
			} else if (direction == Direction.EAST) {
				matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-rotation));
			} else if (direction == Direction.WEST) {
				matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));
			}

			if (type == ConveyorTypes.NORMAL) {
				matrixStack.translate(0, position / speed, 0);
			} else if (type == ConveyorTypes.VERTICAL) {
				matrixStack.translate(0, horizontalPosition / speed, -position / speed);
			} else if (type == ConveyorTypes.DOWN_VERTICAL) {
				matrixStack.translate(0, horizontalPosition / speed, -(position / (speed)) + (blockEntity.getCachedState().get(ConveyorBlock.CONVEYOR) ? -1 : 0));
			}

			if (ConveyorSpecialScaleRegistry.INSTANCE.containsKey(stack.getItem())) {
				float scale = ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getLeft();
				matrixStack.scale(scale, scale, scale);

				for (var i = 1; i < int_1; i++) {
					matrixStack.push();
					if (ConveyorSpecialScaleRegistry.INSTANCE.get(stack.getItem()).getRight()) {
						float x = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
						float y = (random.nextFloat() * 2.0F) * 0.15F;
						float z = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
						matrixStack.translate(x, z, y * 0.5F);
					}
					ClientUtils.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider);
					matrixStack.pop();
				}
			} else {
				matrixStack.scale(0.8F, 0.8F, 0.8F);
			}

			ClientUtils.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider);
			matrixStack.pop();
		}
	}
}
