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

package com.github.chainmailstudios.astromine.common.network;

import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.Block;

/**
 * A {@link Block} which
 * provides a {@link NetworkMemberType} for one or more
 * {@link NetworkType}(s).
 */
public interface NetworkBlock {
	/**
	 * A {@link NetworkBlock} whose
	 * {@link #getEnergyNetworkMemberType()}} defaults to
	 * {@link NetworkMemberType#REQUESTER}.
	 */
	interface EnergyRequester extends NetworkBlock {
		@Override
		default NetworkMemberType getEnergyNetworkMemberType() {
			return NetworkMemberType.REQUESTER;
		}
	}

	/**
	 * A {@link NetworkBlock} whose
	 * {@link #getEnergyNetworkMemberType()}} defaults to
	 * {@link NetworkMemberType#PROVIDER}.
	 */
	interface EnergyProvider extends NetworkBlock {
		@Override
		default NetworkMemberType getEnergyNetworkMemberType() {
			return NetworkMemberType.PROVIDER;
		}
	}

	/**
	 * A {@link NetworkBlock} whose
	 * {@link #getEnergyNetworkMemberType()}} defaults to
	 * {@link NetworkMemberType#BUFFER}.
	 */
	interface EnergyBuffer extends NetworkBlock {
		@Override
		default NetworkMemberType getEnergyNetworkMemberType() {
			return NetworkMemberType.BUFFER;
		}
	}

	/**
	 * A {@link NetworkBlock} whose
	 * {@link #getEnergyNetworkMemberType()}} defaults to
	 * {@link NetworkMemberType#NODE}.
	 */
	interface EnergyNode extends NetworkBlock {
		@Override
		default NetworkMemberType getEnergyNetworkMemberType() {
			return NetworkMemberType.NODE;
		}
	}

	/**
	 * A {@link NetworkBlock} whose
	 * {@link #getFluidNetworkMemberType()}} defaults to
	 * {@link NetworkMemberType#REQUESTER}.
	 */
	interface FluidRequester extends NetworkBlock {
		@Override
		default NetworkMemberType getFluidNetworkMemberType() {
			return NetworkMemberType.REQUESTER;
		}
	}

	/**
	 * A {@link NetworkBlock} whose
	 * {@link #getFluidNetworkMemberType()}} defaults to
	 * {@link NetworkMemberType#PROVIDER}.
	 */
	interface FluidProvider extends NetworkBlock {
		@Override
		default NetworkMemberType getFluidNetworkMemberType() {
			return NetworkMemberType.PROVIDER;
		}
	}

	/**
	 * A {@link NetworkBlock} whose
	 * {@link #getFluidNetworkMemberType()}} defaults to
	 * {@link NetworkMemberType#BUFFER}.
	 */
	interface FluidBuffer extends NetworkBlock {
		@Override
		default NetworkMemberType getFluidNetworkMemberType() {
			return NetworkMemberType.BUFFER;
		}
	}

	/**
	 * A {@link NetworkBlock} whose
	 * {@link #getFluidNetworkMemberType()}} defaults to
	 * {@link NetworkMemberType#NODE}.
	 */
	interface FluidNode extends NetworkBlock {
		@Override
		default NetworkMemberType getFluidNetworkMemberType() {
			return NetworkMemberType.NODE;
		}
	}

	/** Returns the {@link NetworkMemberType} of the given
	 * {@link NetworkType}, or {@link NetworkMemberType#NODE}
	 * if the type is unknown. */
	default NetworkMemberType getMemberType(NetworkType type) {
		if (type.equals(AstromineNetworkTypes.ENERGY)) {
			return getEnergyNetworkMemberType();
		} else if (type.equals(AstromineNetworkTypes.FLUID)) {
			return getFluidNetworkMemberType();
		} else if (type.equals(AstromineNetworkTypes.ITEM)) {
			return getItemNetworkMemberType();
		}

		return NetworkMemberType.NODE;
	}

	/** Returns the default {@link NetworkMemberType} for {@link AstromineNetworkTypes#FLUID}. */
	default NetworkMemberType getFluidNetworkMemberType() {
		return NetworkMemberType.NONE;
	}

	/** Returns the default {@link NetworkMemberType} for {@link AstromineNetworkTypes#ENERGY}. */
	default NetworkMemberType getEnergyNetworkMemberType() {
		return NetworkMemberType.NONE;
	}

	/** Returns the default {@link NetworkMemberType} for {@link AstromineNetworkTypes#ITEM}. */
	default NetworkMemberType getItemNetworkMemberType() {
		return NetworkMemberType.NONE;
	}

	/** Asserts whether this block is a a {@link NetworkMember}
	 * of a network with the given {@link NetworkType} or not. */
	default boolean isMember(NetworkType type) {
		return getMemberType(type) != NetworkMemberType.NONE;
	}
}
