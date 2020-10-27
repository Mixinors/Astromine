package com.github.chainmailstudios.astromine.common.network;

import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

public interface NetworkBlock {
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
	default NetworkMemberType getMemberType(NetworkType type) {
		if(type.equals(AstromineNetworkTypes.ENERGY)) return energyType();
		else if(type.equals(AstromineNetworkTypes.FLUID)) return fluidType();
		else if(type.equals(AstromineNetworkTypes.ITEM)) return itemType();
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
}
