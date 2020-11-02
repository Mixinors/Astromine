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

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergyStorage;

public class TransportUtilities {
	public static boolean isExtractingEnergy(BlockEntity entity, @Nullable BlockEntityTransferComponent transferComponent, Direction direction) {
		if (!(entity instanceof EnergyStorage))
			return false;
		TransferType transferType = transferComponent != null ? transferComponent.getEnergy(direction) : TransferType.NONE;
		return transferType.canExtract() || (!transferType.isNone() && NetworkMemberRegistry.get(entity, direction).isProvider(AstromineNetworkTypes.ENERGY));
	}

	public static boolean isInsertingEnergy(BlockEntity entity, @Nullable BlockEntityTransferComponent transferComponent, Direction direction) {
		if (!(entity instanceof EnergyStorage))
			return false;
		TransferType transferType = transferComponent != null ? transferComponent.getEnergy(direction) : TransferType.NONE;
		return transferType.canInsert() || (!transferType.isNone() && NetworkMemberRegistry.get(entity, direction).isRequester(AstromineNetworkTypes.ENERGY));
	}

	public static boolean isExtractingItem(@Nullable BlockEntityTransferComponent transferComponent, Direction direction, boolean defaultValue) {
		TransferType transferType = transferComponent != null ? transferComponent.getItem(direction) : TransferType.NONE;
		return transferType.canExtract() || (defaultValue && transferType.isNone());
	}

	public static boolean isInsertingItem(@Nullable BlockEntityTransferComponent transferComponent, Direction direction, boolean defaultValue) {
		TransferType transferType = transferComponent != null ? transferComponent.getItem(direction) : TransferType.NONE;
		return transferType.canInsert() || (defaultValue && transferType.isNone());
	}
}
