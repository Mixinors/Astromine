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

package com.github.chainmailstudios.astromine.common.multiblock;

import com.github.chainmailstudios.astromine.common.utilities.MapUtilities;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultiblockType {
	private final ImmutableMap<BlockPos, Block> blocks;
	private final ImmutableMultimap<BlockPos, ComponentType<?>> components;
	private final ImmutableMap<ComponentType<?>, Supplier<Component>> suppliers;

	public MultiblockType(Builder builder) {
		this.blocks = builder.blocks.build();
		this.components = builder.components.build();
		this.suppliers = builder.suppliers.build();
	}

	public ImmutableMap<BlockPos, Block> getBlocks() {
		return blocks;
	}

	public ImmutableMultimap<BlockPos, ComponentType<?>> getComponents() {
		return components;
	}

	public ImmutableMap<ComponentType<?>, Component> getSuppliers() {
		return ImmutableMap.copyOf(suppliers.entrySet().stream().map(entry -> MapUtilities.entryOf(entry.getKey(), entry.getValue().get())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
	}

	public static class Builder {
		private final ImmutableMap.Builder<BlockPos, Block> blocks = ImmutableMap.builder();
		private final ImmutableMultimap.Builder<BlockPos, ComponentType<?>> components = ImmutableMultimap.builder();
		private final ImmutableMap.Builder<ComponentType<?>, Supplier<Component>> suppliers = ImmutableMap.builder();

		public Builder with(BlockPos relative, Block block) {
			blocks.put(relative, block);
			return this;
		}

		public Builder with(BlockPos relative, ComponentType<?> type) {
			components.put(relative, type);
			return this;
		}

		public Builder with(ComponentType<?> type, Supplier<Component> supplier) {
			suppliers.put(type, supplier);
			return this;
		}

		public MultiblockType build() {
			return new MultiblockType(this);
		}
	}
}
