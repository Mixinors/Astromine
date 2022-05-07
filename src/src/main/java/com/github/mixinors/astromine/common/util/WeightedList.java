/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.util;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

public class WeightedList<U> {
	private static final String DATA_KEY = "data";
	private static final String WEIGHT_KEY = "weight";
	
	protected final List<WeightedList.Entry<U>> entries;

	public WeightedList() {
		this.entries = Lists.newArrayList();
	}

	private WeightedList(List<WeightedList.Entry<U>> list) {
		this.entries = Lists.newArrayList(list);
	}

	public static <U> Codec<WeightedList<U>> createCodec(Codec<U> codec) {
		return WeightedList.Entry.createCodec(codec).listOf().xmap(WeightedList::new, (weightedList) -> weightedList.entries);
	}

	public WeightedList<U> add(U data, int weight) {
		this.entries.add(new WeightedList.Entry<>(data, weight));
		return this;
	}

	public WeightedList<U> shuffle(Random random) {
		this.entries.forEach((entry) -> entry.setShuffledOrder(random.nextFloat()));
		this.entries.sort(Comparator.comparingDouble(WeightedList.Entry::getShuffledOrder));
		return this;
	}

	public Stream<U> stream() {
		return this.entries.stream().map(WeightedList.Entry::getElement);
	}

	public String toString() {
		return "WeightedList[" + this.entries + "]";
	}

	public boolean isEmpty() {
		return this.entries.isEmpty();
	}

	public static class Entry<T> {
		final T data;
		final int weight;
		private double shuffledOrder;

		Entry(T object, int i) {
			this.weight = i;
			this.data = object;
		}

		private double getShuffledOrder() {
			return this.shuffledOrder;
		}

		void setShuffledOrder(float random) {
			this.shuffledOrder = -Math.pow(random, 1.0F / (float) this.weight);
		}

		public T getElement() {
			return this.data;
		}

		public int getWeight() {
			return this.weight;
		}

		public String toString() {
			return this.weight + ":" + this.data;
		}

		public static <E> Codec<WeightedList.Entry<E>> createCodec(Codec<E> codec) {
			return new Codec<>() {
				@Override
				public <T> DataResult<Pair<WeightedList.Entry<E>, T>> decode(DynamicOps<T> dynamicOps, T object) {
					Objects.requireNonNull(codec);
					
					var dynamic = new Dynamic<T>(dynamicOps, object);
					
					return dynamic.get(DATA_KEY).flatMap(codec::parse).map((data) -> {
						return new WeightedList.Entry<>(data, dynamic.get(WEIGHT_KEY).asInt(1));
					}).map((entry) -> Pair.of(entry, dynamicOps.empty()));
				}

				@Override
				public <T> DataResult<T> encode(WeightedList.Entry<E> entry, DynamicOps<T> dynamicOps, T object) {
					return dynamicOps.mapBuilder().add(WEIGHT_KEY, dynamicOps.createInt(entry.weight)).add(DATA_KEY, codec.encodeStart(dynamicOps, entry.data)).build(object);
				}
			};
		}
	}
}
