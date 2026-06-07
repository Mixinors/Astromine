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

package com.github.mixinors.astromine.common.block.entity.utility;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.config.entry.utility.UtilityConfig;
import com.github.mixinors.astromine.common.provider.config.UtilityConfigProvider;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import com.github.mixinors.astromine.common.transfer.storage.LongEnergyStorage;

public class BlockPlacerBlockEntity extends ExtendedBlockEntity implements UtilityConfigProvider<UtilityConfig> {
	public static final String COOLDOWN_KEY = "Cooldown";
	
	public static final int INPUT_SLOT = 0;
	
	public static final int[] INSERT_SLOTS = new int[] { INPUT_SLOT };
	
	public static final int[] EXTRACT_SLOTS = new int[] { };
	
	private long cooldown = 0L;
	
	public BlockPlacerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.BLOCK_PLACER, blockPos, blockState);
		
		energyStorage = new LongEnergyStorage(getEnergyStorageSize(), getMaxTransferRate(), 0L);
		
		itemStorage = new SimpleItemStorage(1).extractPredicate((variant, slot) ->
				false
		).insertPredicate((variant, slot) -> {
			if (slot != INPUT_SLOT) {
				return false;
			}
			
			return variant.getItem() instanceof BlockItem;
		}).listener(() -> {
			setChanged();
		}).insertSlots(INSERT_SLOTS).extractSlots(EXTRACT_SLOTS);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (level == null || level.isClientSide || !shouldRun()) {
			return;
		}
		
		if (itemStorage != null && itemStorage != null) {
			var consumed = getEnergyConsumed();
			
			if (energyStorage.getAmount() < consumed) {
				cooldown = 0L;
				
				active = false;
			} else {
				var stored = itemStorage.getItem(0);
				var direction = getBlockState().getValue(HorizontalDirectionalBlock.FACING);
				var targetPos = worldPosition.relative(direction);
				var targetState = level.getBlockState(targetPos);
				
				if (stored.getItem() instanceof BlockItem blockItem) {
					var storedBlock = blockItem.getBlock();
					var storedState = blockItem.getBlock().defaultBlockState();
					
					if (storedBlock instanceof DoorBlock || storedBlock instanceof SlabBlock || storedBlock instanceof BushBlock || storedBlock instanceof BedBlock || storedBlock instanceof BannerBlock) {
						return;
					}
					
					if (storedState.canSurvive(level, targetPos) && targetState.isAir()) {
						if (cooldown >= getSpeed()) {
							level.setBlockAndUpdate(targetPos, storedState);
							
							blockItem.getBlock().setPlacedBy(level, targetPos, storedState, null, stored);
							
							itemStorage.removeItem(INPUT_SLOT, 1);
							
							energyStorage.amount -= consumed;
							
							cooldown = 0;
						} else {
							++cooldown;
							
							active = true;
						}
					} else {
						active = false;
					}
				}
			}
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		nbt.putLong(COOLDOWN_KEY, cooldown);
		
		super.saveAdditional(nbt, registries);
	}
	
	@Override
	protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
		cooldown = nbt.getLong(COOLDOWN_KEY);
		
		super.loadAdditional(nbt, registries);
	}
	
	@Override
	public UtilityConfig getConfig() {
		return AMConfig.get().blocks.utilities.blockPlacer;
	}
}
