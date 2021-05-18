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

import com.github.mixinors.astromine.common.component.block.entity.TransferComponent;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.registry.common.AMComponents;
import net.minecraft.block.entity.BlockEntity;

import com.github.mixinors.astromine.common.block.transfer.TransferType;
import com.github.mixinors.astromine.common.network.NetworkInstance;
import com.github.mixinors.astromine.common.network.NetworkMember;
import com.github.mixinors.astromine.common.network.NetworkMemberNode;
import com.github.mixinors.astromine.common.network.NetworkNode;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkMemberRegistry;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;
import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
import it.unimi.dsi.fastutil.objects.Reference2DoubleOpenHashMap;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
