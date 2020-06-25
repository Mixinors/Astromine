package com.github.chainmailstudios.astromine.common.network;

public interface NetworkMember {
	<T extends NetworkType> boolean acceptsType(T type);

	default <T extends NetworkType> boolean isNode(T type) {
		return true;
	}

	default <T extends NetworkType> boolean isRequester(T type) {
		return false;
	}

	default <T extends NetworkType> boolean isProvider(T type) {
		return false;
	}

	default <T extends NetworkType> boolean isBuffer(T type) {
		return false;
	}

	default <T extends NetworkType> boolean isVisual(T type) {
		return false;
	}
}
