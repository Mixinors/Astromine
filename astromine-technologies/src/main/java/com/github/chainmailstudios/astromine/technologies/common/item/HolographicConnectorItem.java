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

package com.github.chainmailstudios.astromine.technologies.common.item;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.technologies.common.block.HolographicBridgeProjectorBlock;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.HolographicBridgeProjectorBlockEntity;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesSoundEvents;

public class HolographicConnectorItem extends Item {
	public HolographicConnectorItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();

		BlockPos position = context.getBlockPos();

		if (context.shouldCancelInteraction())
			return super.useOnBlock(context);

		if (world.getBlockState(position).getBlock() instanceof HolographicBridgeProjectorBlock) {
			HolographicBridgeProjectorBlockEntity entity = (HolographicBridgeProjectorBlockEntity) world.getBlockEntity(position);

			Pair<RegistryKey<World>, BlockPos> pair = readBlock(context.getStack());
			if (pair == null || !pair.getLeft().getValue().equals(world.getRegistryKey().getValue())) {
				if (!world.isClient) {
					context.getPlayer().setStackInHand(context.getHand(), selectBlock(context.getStack(), entity.getWorld().getRegistryKey(), entity.getPos()));
				} else {
					context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connector_select", toShortString(entity.getPos())).formatted(Formatting.BLUE), true);
					world.playSound(context.getPlayer(), context.getBlockPos(), AstromineTechnologiesSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
				}
			} else {
				BlockEntity blockEntity = world.getBlockEntity(pair.getRight());
				if (!(blockEntity instanceof HolographicBridgeProjectorBlockEntity)) {
					if (!world.isClient) {
						context.getPlayer().setStackInHand(context.getHand(), selectBlock(context.getStack(), entity.getWorld().getRegistryKey(), entity.getPos()));
					} else {
						context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connector_select", toShortString(entity.getPos())).formatted(Formatting.BLUE), true);
						world.playSound(context.getPlayer(), context.getBlockPos(), AstromineTechnologiesSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
					}
					return ActionResult.SUCCESS;
				}
				HolographicBridgeProjectorBlockEntity parent = (HolographicBridgeProjectorBlockEntity) blockEntity;

				BlockPos nP = entity.getPos();
				BlockPos oP = parent.getPos();

				if (parent.getPos().getZ() < entity.getPos().getZ() || parent.getPos().getX() < entity.getPos().getX()) {
					HolographicBridgeProjectorBlockEntity temporary = parent;
					parent = entity;
					entity = temporary;
				}

				if ((parent.getPos().getX() != entity.getPos().getX() && parent.getPos().getZ() != entity.getPos().getZ()) || oP.getSquaredDistance(nP) > 65536) {
					if (!world.isClient) {
						context.getPlayer().setStackInHand(context.getHand(), unselect(context.getStack()));
					} else {
						context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connection_failed", toShortString(parent.getPos()), toShortString(entity.getPos())).formatted(Formatting.RED), true);
						world.playSound(context.getPlayer(), context.getBlockPos(), AstromineTechnologiesSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
					}
					return ActionResult.SUCCESS;
				} else if (parent.getCachedState().get(HorizontalFacingBlock.FACING).getOpposite() != entity.getCachedState().get(HorizontalFacingBlock.FACING)) {
					if (!world.isClient) {
						context.getPlayer().setStackInHand(context.getHand(), unselect(context.getStack()));
					} else {
						context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connection_failed", toShortString(parent.getPos()), toShortString(entity.getPos())).formatted(Formatting.RED), true);
						world.playSound(context.getPlayer(), context.getBlockPos(), AstromineTechnologiesSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
					}
					return ActionResult.SUCCESS;
				}

				if (world.isClient) {
					context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connection_successful", toShortString(parent.getPos()), toShortString(entity.getPos())).formatted(Formatting.GREEN), true);
					world.playSound(context.getPlayer(), context.getBlockPos(), AstromineTechnologiesSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
				} else {
					parent.setChild(entity);
					entity.setParent(parent);

					if (parent.getParent() == entity.getParent()) {
						parent.setParent(null);
					}

					parent.buildBridge();
					parent.sync();
					context.getPlayer().setStackInHand(context.getHand(), unselect(context.getStack()));
				}
			}
		} else {
			if (world.isClient) {
				context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connection_clear").formatted(Formatting.YELLOW), true);
				world.playSound(context.getPlayer(), context.getBlockPos(), AstromineTechnologiesSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
			} else {
				context.getPlayer().setStackInHand(context.getHand(), unselect(context.getStack()));
			}
		}
		return ActionResult.SUCCESS;
	}

	private ItemStack unselect(ItemStack stack) {
		stack = stack.copy();
		CompoundTag tag = stack.getOrCreateTag();
		tag.remove("SelectedConnectorBlock");
		return stack;
	}

	private ItemStack selectBlock(ItemStack stack, RegistryKey<World> registryKey, BlockPos pos) {
		stack = stack.copy();
		CompoundTag tag = stack.getOrCreateTag();
		tag.remove("SelectedConnectorBlock");
		tag.put("SelectedConnectorBlock", writePos(registryKey, pos));
		return stack;
	}

	public Pair<RegistryKey<World>, BlockPos> readBlock(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag == null)
			return null;
		if (!tag.contains("SelectedConnectorBlock"))
			return null;
		return readPos(tag.getCompound("SelectedConnectorBlock"));
	}

	private CompoundTag writePos(RegistryKey<World> registryKey, BlockPos pos) {
		CompoundTag tag = new CompoundTag();
		tag.putString("World", registryKey.getValue().toString());
		tag.putInt("X", pos.getX());
		tag.putInt("Y", pos.getY());
		tag.putInt("Z", pos.getZ());
		return tag;
	}

	private Pair<RegistryKey<World>, BlockPos> readPos(CompoundTag tag) {
		RegistryKey<World> registryKey = RegistryKey.of(Registry.DIMENSION, Identifier.tryParse(tag.getString("World")));
		int x = tag.getInt("X");
		int y = tag.getInt("Y");
		int z = tag.getInt("Z");
		return new Pair<>(registryKey, new BlockPos(x, y, z));
	}

	public String toShortString(BlockPos pos) {
		return "" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
	}
}
