package com.github.chainmailstudios.astromine.common.network;

public interface NetworkMember {
	default NetworkTicker getNetworkType() {
		return NetworkTicker.EMPTY;
	}

	boolean accepts(Object... objects);

	default boolean refuses(NetworkTicker type) {
		return !accepts(type);
	}

	default boolean isNode() {
		return true;
	}

	default boolean isRequester() {
		return false;
	}

	default boolean isProvider() {
		return false;
	}

	default boolean isBuffer() {
		return false;
	}

	default boolean isVisual() {
		return false;
	}
}
