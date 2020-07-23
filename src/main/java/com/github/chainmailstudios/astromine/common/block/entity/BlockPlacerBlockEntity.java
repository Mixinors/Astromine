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
package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public class BlockPlacerBlockEntity extends DefaultedEnergyItemBlockEntity implements NetworkMember, Tickable {
	private Fraction cooldown = Fraction.empty();

	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	public BlockPlacerBlockEntity() {
		super(AstromineBlockEntityTypes.BLOCK_PLACER);
	}

	@Override
	protected double getEnergySize() {
		return AstromineConfig.get().blockPlacerEnergy;
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(1);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient()) return;

		start:
		if (this.world != null && !this.world.isClient()) {
			if (asEnergy().getEnergy() < AstromineConfig.get().blockPlacerEnergyConsumed) {
				cooldown.resetToEmpty();
				isActive = false;
				break start;
			}

			isActive = true;

			cooldown.add(Fraction.of(1, AstromineConfig.get().blockPlacerTimeConsumed));
			cooldown.simplify();
			if (cooldown.isBiggerOrEqualThan(Fraction.ofWhole(1))) {
				cooldown.resetToEmpty();

				ItemStack stored = itemComponent.getStack(0);

				Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
				BlockPos targetPos = pos.offset(direction);
				BlockState targetState = world.getBlockState(targetPos);

				if (stored.getItem() instanceof BlockItem && targetState.isAir()) {
					BlockState newState = ((BlockItem) stored.getItem()).getBlock().getDefaultState();
					world.setBlockState(targetPos, newState);
					stored.decrement(1);

					asEnergy().extract(AstromineConfig.get().blockPlacerEnergyConsumed);
				}
			}
		}

		if (activity.length - 1 >= 0) System.arraycopy(activity, 1, activity, 0, activity.length - 1);

		activity[4] = isActive;

		if (isActive && !activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, true));
		} else if (!isActive && activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, false));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("cooldown", cooldown.toTag(new CompoundTag()));
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		cooldown = Fraction.fromTag(tag.getCompound("cooldown"));
		super.fromTag(state, tag);
	}

	@Override
	protected @NotNull Map<NetworkType, Collection<NetworkMemberType>> createMemberProperties() {
		return ofTypes(AstromineNetworkTypes.ENERGY, REQUESTER);
	}
}
