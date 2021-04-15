/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

public enum NetworkMemberType {
	/** Requester is a member which may only request contents from a given {@link NetworkType}. */
	REQUESTER,

	/** Requester is a member which may only provide contents for a given {@link NetworkType}. */
	PROVIDER,

	/** Requester is a member which may request or provide contents for a given {@link NetworkType}. */
	BUFFER,

	/** Node is a member which may neither request not provide contents,
	 * existing solely as an intermediary between the other nodes of a given {@link NetworkType}. */
	NODE,

	/** None is a member which is not part of a given {@link NetworkType}. */
	NONE;

	/** Returns this node's string representation.
	 * For example, it may be "Requester". */
	@Override
	public String toString() {
		switch (this) {
			case REQUESTER: return "Requester";
			case PROVIDER: return "Provider";
			case BUFFER: return "Buffer";
			case NODE: return "Node";

			default: return "None";
		}
	}
}
