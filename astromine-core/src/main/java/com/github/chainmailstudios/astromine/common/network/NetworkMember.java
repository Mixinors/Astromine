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

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;

/**
 * A member of a network.
 */
public interface NetworkMember {
	Collection<NetworkMemberType> REQUESTER_PROVIDER = Sets.newHashSet(NetworkMemberType.REQUESTER, NetworkMemberType.PROVIDER);

	Collection<NetworkMemberType> REQUESTER = Collections.singleton(NetworkMemberType.REQUESTER);

	Collection<NetworkMemberType> PROVIDER = Collections.singleton(NetworkMemberType.PROVIDER);

	Collection<NetworkMemberType> BUFFER = Collections.singleton(NetworkMemberType.BUFFER);

	Collection<NetworkMemberType> NODE = Collections.singleton(NetworkMemberType.NODE);

	Collection<NetworkMemberType> NONE = Collections.emptySet();

	/** Returns the {@link NetworkMemberType}s of this member
	 * for the given {@link NetworkType}. */
	Collection<NetworkMemberType> getNetworkMemberTypes(NetworkType type);

	/** Asserts whether this member accepts the
	 * given {@link NetworkType} or not. */
	default boolean acceptsType(NetworkType type) {
		return !getNetworkMemberTypes(type).isEmpty();
	}

	/** Asserts whether this member is a provider
	 * for the given {@link NetworkType} or not. */
	default boolean isProvider(NetworkType type) {
		return getNetworkMemberTypes(type).contains(NetworkMemberType.PROVIDER);
	}

	/** Asserts whether this member is a requester
	 * for the given {@link NetworkType} or not. */
	default boolean isRequester(NetworkType type) {
		return getNetworkMemberTypes(type).contains(NetworkMemberType.REQUESTER);
	}

	/** Asserts whether this member is a buffer
	 * for the given {@link NetworkType} or not. */
	default boolean isBuffer(NetworkType type) {
		return getNetworkMemberTypes(type).contains(NetworkMemberType.BUFFER);
	}

	/** Asserts whether this member is a node
	 * for the given {@link NetworkType} or not. */
	default boolean isNode(NetworkType type) {
		return getNetworkMemberTypes(type).contains(NetworkMemberType.NODE);
	}
}
