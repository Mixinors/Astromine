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

package com.github.chainmailstudios.astromine.common.utilities;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.ItemExtractable;
import alexiil.mc.lib.attributes.item.ItemInsertable;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergyStorage;

public class TransportUtilities {
	public static boolean isExtractingEnergy(BlockEntity entity, @Nullable BlockEntityTransferComponent transferComponent, Direction direction) {
		if (!(entity instanceof EnergyStorage))
			return false;
		TransferType transferType = transferComponent != null ? transferComponent.get(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).get(direction) : TransferType.NONE;
		return transferType.canExtract() || (!transferType.isDisabled() && NetworkMemberRegistry.get(entity).isProvider(AstromineNetworkTypes.ENERGY));
	}

	public static boolean isInsertingEnergy(BlockEntity entity, @Nullable BlockEntityTransferComponent transferComponent, Direction direction) {
		if (!(entity instanceof EnergyStorage))
			return false;
		TransferType transferType = transferComponent != null ? transferComponent.get(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).get(direction) : TransferType.NONE;
		return transferType.canInsert() || (!transferType.isDisabled() && NetworkMemberRegistry.get(entity).isRequester(AstromineNetworkTypes.ENERGY));
	}

	public static boolean isExtractingItem(BlockEntity entity, @Nullable BlockEntityTransferComponent transferComponent, Direction direction, boolean defaultValue) {
		if (!(entity instanceof EnergyStorage))
			return false;
		TransferType transferType = transferComponent != null ? transferComponent.get(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).get(direction) : TransferType.DISABLED;
		return transferType.canExtract() || (defaultValue && transferType.isDefault());
	}

	public static boolean isInsertingItem(BlockEntity entity, @Nullable BlockEntityTransferComponent transferComponent, Direction direction, boolean defaultValue) {
		if (!(entity instanceof EnergyStorage))
			return false;
		TransferType transferType = transferComponent != null ? transferComponent.get(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).get(direction) : TransferType.DISABLED;
		return transferType.canInsert() || (defaultValue && transferType.isDefault());
	}

	public static void move(ItemExtractable extractable, ItemInsertable insertable, int count) {
		ItemStack extracted = extractable.attemptExtraction(stack -> {
			ItemStack copy = stack.copy();
			copy.setCount(Math.min(count, copy.getCount()));
			return insertable.attemptInsertion(copy, Simulation.SIMULATE).isEmpty();
		}, count, Simulation.SIMULATE);
		if (!extracted.isEmpty() && insertable.attemptInsertion(extracted, Simulation.SIMULATE).isEmpty()) {
			insertable.insert(extractable.extract(stack -> {
				ItemStack copy = stack.copy();
				copy.setCount(Math.min(count, copy.getCount()));
				return insertable.attemptInsertion(copy, Simulation.SIMULATE).isEmpty();
			}, count));
		}
	}
}
