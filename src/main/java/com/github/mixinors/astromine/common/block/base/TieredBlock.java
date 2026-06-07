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

package com.github.mixinors.astromine.common.block.base;

import com.github.mixinors.astromine.common.item.utility.MachineUpgradeKitItem;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public interface TieredBlock {
	String X_KEY = "x";
	String Y_KEY = "y";
	String Z_KEY = "z";
	
	Tier getTier();
	
	@Nullable
	Block getForTier(Tier tier);
	
	default boolean hasTier(Tier tier) {
		return getForTier(tier) != null;
	}
	
	default InteractionResult tryUpgrade(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		var stack = player.getItemInHand(hand);
		
		if (stack.getItem() instanceof MachineUpgradeKitItem upgradeKitItem && upgradeKitItem.isValidFor(this)) {
			var newBlock = upgradeKitItem.getUpgrade(this);
			
			if (newBlock != null) {
				if (world.isClientSide) {
					var random = world.random;
					
					var x = pos.getX() - 0.3F;
					var y = pos.getY() - 0.3F;
					var z = pos.getZ() - 0.3F;
					
					for (var i = 0; i < 20; i++) {
						world.addParticle(ParticleTypes.COMPOSTER, x + random.nextDouble() * 1.6F, y + random.nextDouble() * 1.6F, z + random.nextDouble() * 1.6F, -0.2F + random.nextDouble() * 0.4F, -0.2F + random.nextDouble() * 0.4F, -0.2F + random.nextDouble() * 0.4F);
					}
					
					world.playLocalSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F, false);
				} else {
					if (!player.isCreative()) {
						stack.shrink(1);
					}
					
					var blockEntity = world.getBlockEntity(pos);
					
					var blockEntityNbt = (CompoundTag) null;
					
					if (blockEntity != null) {
						blockEntityNbt = blockEntity.saveWithId(world.registryAccess());
						
						blockEntityNbt.putInt(X_KEY, pos.getX());
						blockEntityNbt.putInt(Y_KEY, pos.getY());
						blockEntityNbt.putInt(Z_KEY, pos.getZ());
					}
					
					world.removeBlockEntity(pos);
					
					var newState = newBlock.withPropertiesOf(state);
					
					world.setBlock(pos, newState, Block.UPDATE_ALL, 512);
					
					var newBlockEntity = world.getBlockEntity(pos);
					
					if (newBlockEntity != null && blockEntityNbt != null) {
						newBlockEntity.loadWithComponents(blockEntityNbt, world.registryAccess());
					}
				}
				
				return InteractionResult.SUCCESS;
			}
		}
		
		return InteractionResult.PASS;
	}
}
