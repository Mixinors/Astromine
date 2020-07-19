package com.github.chainmailstudios.astromine.common.network;

public enum NetworkMemberType {
	/**
	 * Requester is a member that requests without special handling defined by the user (e.g. siding)
	 */
	REQUESTER,
	/**
	 * Provider is a member that provides without special handling defined by the user (e.g. siding)
	 */
	PROVIDER,
	/**
	 * Buffer is a member that stores with special handling defined by the user (e.g. siding)
	 */
	BUFFER,
	NODE
}
