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

package com.github.mixinors.astromine.common.item.utility;

import com.github.mixinors.astromine.common.block.HoloBridgeProjectorBlock;
import com.github.mixinors.astromine.common.block.entity.HoloBridgeProjectorBlockEntity;
import com.github.mixinors.astromine.registry.common.AMSoundEvents;
import com.github.mixinors.astromine.common.util.ItemDataUtils;
import com.github.mixinors.astromine.common.util.NbtUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public class HolographicConnectorItem extends Item {
	private static final String SELECTOR_CONNECTOR_BLOCK_KEY = "SelectedConnectorBlock";
	
	private static final String WORLD_KEY = "World";
	private static final String POSITION_KEY = "position";
	
	public HolographicConnectorItem(Properties settings) {
		super(settings);
	}
	
	public record Selection(
			ResourceKey<Level> registryKey,
			BlockPos blockPos
	) {}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {

		if (context.isSecondaryUseActive()) {
			return super.useOn(context);
		}
		
		var world = context.getLevel();
		var player = context.getPlayer();
		var stack = context.getItemInHand();
		var hand = context.getHand();
		var pos = context.getClickedPos();
		
		if (world == null || player == null) {
			return super.useOn(context);
		}
		
		if (world.getBlockState(pos).getBlock() instanceof HoloBridgeProjectorBlock && world.getBlockEntity(pos) instanceof HoloBridgeProjectorBlockEntity child) {
			var pair = fromStack(stack);
			
			var childPos = child.getBlockPos();
			
			if (pair == null || !pair.registryKey().equals(world.dimension())) {
				if (!world.isClientSide) {
					player.setItemInHand(hand, select(stack, world.dimension(), childPos));
				} else {
					player.displayClientMessage(Component.translatable("text.astromine.message.holographic_connector_select", toShortString(child.getBlockPos())).withStyle(ChatFormatting.BLUE), true);
					
					world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundSource.PLAYERS, 0.5F, 0.33F);
				}
			} else {
				var blockEntity = world.getBlockEntity(pair.blockPos());
				
				if (!(blockEntity instanceof HoloBridgeProjectorBlockEntity parent)) {
					if (!world.isClientSide) {
						player.setItemInHand(hand, select(stack, world.dimension(), childPos));
					} else {
						player.displayClientMessage(Component.translatable("text.astromine.message.holographic_connector_select", toShortString(child.getBlockPos())).withStyle(ChatFormatting.BLUE), true);
						
						world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundSource.PLAYERS, 0.5F, 0.33F);
					}
					
					return InteractionResult.SUCCESS;
				}
				
				var parentPos = parent.getBlockPos();
				
				if (parentPos.getZ() < childPos.getZ() || parentPos.getX() < childPos.getX()) {
					var previousParent = parent;
					
					parent = child;
					child = previousParent;
				}
				
				if ((parentPos.getX() != childPos.getX() && parentPos.getZ() != childPos.getZ()) || parentPos.distSqr(childPos) > 65536) {
					if (!world.isClientSide) {
						player.setItemInHand(hand, unselect(stack));
					} else {
						player.displayClientMessage(Component.translatable("text.astromine.message.holographic_connection_failed", toShortString(parentPos), toShortString(childPos)).withStyle(ChatFormatting.RED), true);
						
						world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundSource.PLAYERS, 0.5F, 0.33F);
					}
					
					return InteractionResult.SUCCESS;
				} else if (parent.getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite() != child.getBlockState().getValue(HorizontalDirectionalBlock.FACING)) {
					if (!world.isClientSide) {
						player.setItemInHand(hand, unselect(stack));
					} else {
						player.displayClientMessage(Component.translatable("text.astromine.message.holographic_connection_failed", toShortString(parentPos), toShortString(childPos)).withStyle(ChatFormatting.RED), true);
						
						world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundSource.PLAYERS, 0.5F, 0.33F);
					}
					
					return InteractionResult.SUCCESS;
				}
				
				if (!parent.attemptToBuildBridge(child)) {
					if (!world.isClientSide) {
						player.setItemInHand(hand, unselect(stack));
					} else {
						player.displayClientMessage(Component.translatable("text.astromine.message.holographic_connection_failed", toShortString(parentPos), toShortString(childPos)).withStyle(ChatFormatting.RED), true);
						
						world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundSource.PLAYERS, 0.5F, 0.33F);
					}
					
					return InteractionResult.SUCCESS;
				}
				
				if (world.isClientSide) {
					player.displayClientMessage(Component.translatable("text.astromine.message.holographic_connection_successful", toShortString(parentPos), toShortString(childPos)).withStyle(ChatFormatting.GREEN), true);
					
					world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundSource.PLAYERS, 0.5F, 0.33F);
				} else {
					parent.setChild(child);
					child.setParent(parent);
					
					if (parent.getParent() == child.getParent()) {
						parent.setParent(null);
					}
					
					parent.buildBridge();
					
					parent.syncData();
					
					player.setItemInHand(hand, unselect(stack));
				}
			}
		} else {
			if (world.isClientSide) {
				player.displayClientMessage(Component.translatable("text.astromine.message.holographic_connection_clear").withStyle(ChatFormatting.YELLOW), true);
				
				world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundSource.PLAYERS, 0.5F, 0.33F);
			} else {
				player.setItemInHand(hand, unselect(stack));
			}
		}
		
		return InteractionResult.SUCCESS;
	}
	
	private ItemStack unselect(ItemStack stack) {
		stack = stack.copy();
		
		ItemDataUtils.update(stack, nbt -> nbt.remove(SELECTOR_CONNECTOR_BLOCK_KEY));
		
		return stack;
	}
	
	private ItemStack select(ItemStack stack, ResourceKey<Level> registryKey, BlockPos pos) {
		stack = stack.copy();
		
		ItemDataUtils.update(stack, nbt -> {
			nbt.remove(SELECTOR_CONNECTOR_BLOCK_KEY);
			nbt.put(SELECTOR_CONNECTOR_BLOCK_KEY, toNbt(registryKey, pos));
		});
		
		return stack;
	}
	
	public Selection fromStack(ItemStack stack) {
		var nbt = ItemDataUtils.get(stack);
		
		if (!nbt.contains(SELECTOR_CONNECTOR_BLOCK_KEY)) {
			return null;
		}
		
		return fromNbt(nbt.getCompound(SELECTOR_CONNECTOR_BLOCK_KEY));
	}
	
	private CompoundTag toNbt(ResourceKey<Level> registryKey, BlockPos pos) {
		var nbt = new CompoundTag();
		
		NbtUtils.putRegistryKey(nbt, WORLD_KEY, registryKey);
		NbtUtils.putBlockPos(nbt, POSITION_KEY, pos);
		
		return nbt;
	}
	
	private Selection fromNbt(CompoundTag nbt) {
		var registryKey = NbtUtils.<Level>getRegistryKey(nbt, WORLD_KEY);
		var pos = NbtUtils.getBlockPos(nbt, POSITION_KEY);
		
		return new Selection(registryKey, pos);
	}
	
	public String toShortString(BlockPos pos) {
		return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
	}
}
