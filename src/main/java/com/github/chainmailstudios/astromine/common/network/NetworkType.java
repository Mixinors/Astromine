package com.github.chainmailstudios.astromine.common.network;

public abstract class NetworkType {
	public static final NetworkType EMPTY = new NetworkType() {
		@Override
		public void simulate(NetworkInstance controller) {
			// Unused.
		}
	};

	public abstract void simulate(NetworkInstance controller);
}
