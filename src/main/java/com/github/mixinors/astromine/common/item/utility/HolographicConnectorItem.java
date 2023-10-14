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
import dev.architectury.hooks.block.BlockEntityHooks;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class HolographicConnectorItem extends Item {
	private static final String SELECTOR_CONNECTOR_BLOCK_KEY = "SelectedConnectorBlock";
	
	private static final String WORLD_KEY = "World";
	private static final String POSITION_KEY = "position";
	
	public HolographicConnectorItem(Settings settings) {
		super(settings);
	}
	
	public record Selection(
			RegistryKey<World> registryKey,
			BlockPos blockPos
	) {}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.shouldCancelInteraction()) {
			return super.useOnBlock(context);
		}
		
		var world = context.getWorld();
		var player = context.getPlayer();
		var stack = context.getStack();
		var hand = context.getHand();
		var pos = context.getBlockPos();
		
		if (world == null || player == null) {
			return super.useOnBlock(context);
		}
		
		if (world.getBlockState(pos).getBlock() instanceof HoloBridgeProjectorBlock && world.getBlockEntity(pos) instanceof HoloBridgeProjectorBlockEntity child) {
			var pair = fromStack(stack);
			
			var childPos = child.getPos();
			
			if (pair == null || !pair.registryKey().equals(world.getRegistryKey())) {
				if (!world.isClient) {
					player.setStackInHand(hand, select(stack, world.getRegistryKey(), childPos));
				} else {
					player.sendMessage(Text.translatable("text.astromine.message.holographic_connector_select", toShortString(child.getPos())).formatted(Formatting.BLUE), true);
					
					world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundCategory.PLAYERS, 0.5F, 0.33F);
				}
			} else {
				var blockEntity = world.getBlockEntity(pair.blockPos());
				
				if (!(blockEntity instanceof HoloBridgeProjectorBlockEntity parent)) {
					if (!world.isClient) {
						player.setStackInHand(hand, select(stack, world.getRegistryKey(), childPos));
					} else {
						player.sendMessage(Text.translatable("text.astromine.message.holographic_connector_select", toShortString(child.getPos())).formatted(Formatting.BLUE), true);
						
						world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundCategory.PLAYERS, 0.5F, 0.33F);
					}
					
					return ActionResult.SUCCESS;
				}
				
				var parentPos = parent.getPos();
				
				if (parentPos.getZ() < childPos.getZ() || parentPos.getX() < childPos.getX()) {
					var temporary = parent;
					
					parent = child;
					child = temporary;
				}
				
				if (parentPos.getSquaredDistance(childPos) > 65536) {
					if (!world.isClient) {
						player.setStackInHand(hand, unselect(stack));
					} else {
						player.sendMessage(Text.translatable("text.astromine.message.holographic_connection_failed", toShortString(parentPos), toShortString(childPos)).formatted(Formatting.RED), true);
						
						world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundCategory.PLAYERS, 0.5F, 0.33F);
					}
					
					return ActionResult.SUCCESS;
				} else if (parent.getCachedState().get(HorizontalFacingBlock.FACING).getOpposite() != child.getCachedState().get(HorizontalFacingBlock.FACING)) {
					if (!world.isClient) {
						player.setStackInHand(hand, unselect(stack));
					} else {
						player.sendMessage(Text.translatable("text.astromine.message.holographic_connection_failed", toShortString(parentPos), toShortString(childPos)).formatted(Formatting.RED), true);
						
						world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundCategory.PLAYERS, 0.5F, 0.33F);
					}
					
					return ActionResult.SUCCESS;
				}
				
				// TODO: Reimplement. Might be a good idea having a safety check, y'know?
//				if (!parent.attemptToBuildBridge(child)) {
//					if (!world.isClient) {
//						player.setStackInHand(hand, unselect(stack));
//					} else {
//						player.sendMessage(Text.translatable("text.astromine.message.holographic_connection_failed", toShortString(parentPos), toShortString(childPos)).formatted(Formatting.RED), true);
//
//						world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundCategory.PLAYERS, 0.5F, 0.33F);
//					}
//
//					return ActionResult.SUCCESS;
//				}
				
				if (world.isClient) {
					player.sendMessage(Text.translatable("text.astromine.message.holographic_connection_successful", toShortString(parentPos), toShortString(childPos)).formatted(Formatting.GREEN), true);
					
					world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundCategory.PLAYERS, 0.5F, 0.33F);
				} else {
					parent.setChild(child);
					child.setParent(parent);
					
					if (parent.getParent() == child.getParent()) {
						parent.setParent(null);
					}
					
					parent.buildBridge();
					
					BlockEntityHooks.syncData(parent);
					
					player.setStackInHand(hand, unselect(stack));
				}
			}
		} else {
			if (world.isClient) {
				player.sendMessage(Text.translatable("text.astromine.message.holographic_connection_clear").formatted(Formatting.YELLOW), true);
				
				world.playSound(player, pos, AMSoundEvents.HOLOGRAPHIC_CONNECTOR_CLICK.get(), SoundCategory.PLAYERS, 0.5F, 0.33F);
			} else {
				player.setStackInHand(hand, unselect(stack));
			}
		}
		
		return ActionResult.SUCCESS;
	}
	
	private ItemStack unselect(ItemStack stack) {
		stack = stack.copy();
		
		var nbt = stack.getOrCreateNbt();
		
		nbt.remove(SELECTOR_CONNECTOR_BLOCK_KEY);
		
		return stack;
	}
	
	private ItemStack select(ItemStack stack, RegistryKey<World> registryKey, BlockPos pos) {
		stack = stack.copy();
		
		var nbt = stack.getOrCreateNbt();
		
		nbt.remove(SELECTOR_CONNECTOR_BLOCK_KEY);
		
		nbt.put(SELECTOR_CONNECTOR_BLOCK_KEY, toNbt(registryKey, pos));
		
		return stack;
	}
	
	public Selection fromStack(ItemStack stack) {
		var nbt = stack.getNbt();
		
		if (nbt == null) {
			return null;
		}
		
		if (!nbt.contains(SELECTOR_CONNECTOR_BLOCK_KEY)) {
			return null;
		}
		
		return fromNbt(nbt.getCompound(SELECTOR_CONNECTOR_BLOCK_KEY));
	}
	
	private NbtCompound toNbt(RegistryKey<World> registryKey, BlockPos pos) {
		var nbt = new NbtCompound();
		
		NbtUtil.putRegistryKey(nbt, WORLD_KEY, registryKey);
		NbtUtil.putBlockPos(nbt, POSITION_KEY, pos);
		
		return nbt;
	}
	
	private Selection fromNbt(NbtCompound nbt) {
		var registryKey = NbtUtil.<World>getRegistryKey(nbt, WORLD_KEY);
		var pos = NbtUtil.getBlockPos(nbt, POSITION_KEY);
		
		return new Selection(registryKey, pos);
	}
	
	public String toShortString(BlockPos pos) {
		return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
	}
}
