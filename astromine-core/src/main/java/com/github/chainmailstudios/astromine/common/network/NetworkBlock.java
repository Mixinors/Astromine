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

public interface NetworkBlock {
	default NetworkMemberType getMemberType(NetworkType type) {
		if (type.equals(AstromineNetworkTypes.ENERGY))
			return energyType();
		else if (type.equals(AstromineNetworkTypes.FLUID))
			return fluidType();
		else if (type.equals(AstromineNetworkTypes.ITEM))
			return itemType();
		else return NetworkMemberType.NONE;
	}

	default NetworkMemberType fluidType() {
		return NetworkMemberType.NONE;
	}

	default NetworkMemberType energyType() {
		return NetworkMemberType.NONE;
	}

	default NetworkMemberType itemType() {
		return NetworkMemberType.NONE;
	}

	default boolean isMember(NetworkType type) {
		return getMemberType(type) != NetworkMemberType.NONE;
	}

	interface EnergyRequester extends NetworkBlock {
		@Override
		default NetworkMemberType energyType() {
			return NetworkMemberType.REQUESTER;
		}
	}

	interface EnergyProvider extends NetworkBlock {
		@Override
		default NetworkMemberType energyType() {
			return NetworkMemberType.PROVIDER;
		}
	}

	interface EnergyBuffer extends NetworkBlock {
		@Override
		default NetworkMemberType energyType() {
			return NetworkMemberType.BUFFER;
		}
	}

	interface EnergyNode extends NetworkBlock {
		@Override
		default NetworkMemberType energyType() {
			return NetworkMemberType.NODE;
		}
	}

	interface FluidRequester extends NetworkBlock {
		@Override
		default NetworkMemberType fluidType() {
			return NetworkMemberType.REQUESTER;
		}
	}

	interface FluidProvider extends NetworkBlock {
		@Override
		default NetworkMemberType fluidType() {
			return NetworkMemberType.PROVIDER;
		}
	}

	interface FluidBuffer extends NetworkBlock {
		@Override
		default NetworkMemberType fluidType() {
			return NetworkMemberType.BUFFER;
		}
	}

	interface FluidNode extends NetworkBlock {
		@Override
		default NetworkMemberType fluidType() {
			return NetworkMemberType.NODE;
		}
	}
}
