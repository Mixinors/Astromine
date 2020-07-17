package com.github.chainmailstudios.astromine.common.network;

public interface NetworkMember {
	<T extends NetworkType> boolean acceptsType(T type);

	default <T extends NetworkType> boolean isNode(T type) {
		return true;
	}

	/**
	 * Requester is a member that requests without special handling defined by the user (e.g. siding)
	 */
	default <T extends NetworkType> boolean isRequester(T type) {
		return false;
	}

	/**
	 * Provider is a member that provides without special handling defined by the user (e.g. siding)
	 */
	default <T extends NetworkType> boolean isProvider(T type) {
		return false;
	}

	/**
	 * Buffer is a member that stores with special handling defined by the user (e.g. siding)
	 */
	default <T extends NetworkType> boolean isBuffer(T type) {
		return false;
	}

	default <T extends NetworkType> boolean isVisual(T type) {
		return false;
	}
}
