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

package com.github.mixinors.astromine.common.network.type;

import com.github.mixinors.astromine.cardinalcomponents.common.component.base.EnergyComponent;

import com.github.mixinors.astromine.common.network.NetworkInstance;
import com.github.mixinors.astromine.common.network.NetworkNode;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;

/**
 * A {@link NetworkType} for energy.
 */
public final class EnergyNetworkType implements NetworkType {
	/** Override behavior to handle attached {@link EnergyComponent}s.
	 *
	 * Performance is dubious at best.
	 */
	@Override
	public void tick(NetworkInstance instance) {
		/** Reimplementation must use {@link EnergyComponent}. */
		throw new UnsupportedOperationException("Pending re-implementation!");
	}

	/** Returns this type's string representation.
	 * It will be "Energy". */
	@Override
	public String toString() {
		return "Energy";
	}

	/**
	 * A speed provider for
	 * attached {@link NetworkNode}s.
	 */
	public interface NodeSpeedProvider {
		/** Returns this node's transfer speed. */
		double getNodeSpeed();
	}
}
