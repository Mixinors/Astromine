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

package com.github.chainmailstudios.astromine.transportations.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsSoundEvents;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ShredderBlockEntity extends ComponentBlockEntity implements Conveyable {
	public ShredderBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.INCINERATOR);
	}

	public ShredderBlockEntity(BlockEntityType type) {
		super(type);
	}

	@Override
	public int accepts(ItemStack stack) {
		if (tickRedstone()) {
			return stack.getCount();
		} else {
			return 0;
		}
	}

	@Override
	public boolean canInsert(Direction direction) {
		return direction != Direction.UP && direction != Direction.DOWN;
	}

	@Override
	public boolean canExtract(Direction direction, ConveyorTypes type) {
		return false;
	}

	@Override
	public void give(ItemStack stack) {
		float min = 0F;
		float max = 0.4F;
		float random = min + ((float) Math.random()) * (max - min);
		random = random - (random / 2);
		level.playSound(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), AstromineTransportationsSoundEvents.INCINERATE, SoundSource.BLOCKS, 0.25F, 1.0F + random);
	}
}
