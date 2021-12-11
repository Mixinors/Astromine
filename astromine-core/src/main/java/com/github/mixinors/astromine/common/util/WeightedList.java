package com.github.mixinors.astromine.common.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

public class WeightedList<U> {
	protected final List<WeightedList.Entry<U>> entries;

	public WeightedList() {
		this.entries = Lists.newArrayList();
	}

	private WeightedList(List<WeightedList.Entry<U>> list) {
		this.entries = Lists.newArrayList(list);
	}

	public static <U> Codec<WeightedList<U>> createCodec(Codec<U> codec) {
		return WeightedList.Entry.createCodec(codec).listOf().xmap(WeightedList::new, (weightedList) -> {
			return weightedList.entries;
		});
	}

	public WeightedList<U> add(U data, int weight) {
		this.entries.add(new WeightedList.Entry<>(data, weight));
		return this;
	}

	public WeightedList<U> shuffle(Random random) {
		this.entries.forEach((entry) -> {
			entry.setShuffledOrder(random.nextFloat());
		});
		this.entries.sort(Comparator.comparingDouble(WeightedList.Entry::getShuffledOrder));
		return this;
	}

	public Stream<U> stream() {
		return this.entries.stream().map(WeightedList.Entry::getElement);
	}

	public String toString() {
		return "ShufflingList[" + this.entries + "]";
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
					Dynamic<T> dynamic = new Dynamic<>(dynamicOps, object);
					return dynamic.get("data").flatMap(codec::parse).map((data) -> {
						return new WeightedList.Entry<>(data, dynamic.get("weight").asInt(1));
					}).map((entry) -> {
						return Pair.of(entry, dynamicOps.empty());
					});
				}

				@Override
				public <T> DataResult<T> encode(WeightedList.Entry<E> entry, DynamicOps<T> dynamicOps, T object) {
					return dynamicOps.mapBuilder().add("weight", dynamicOps.createInt(entry.weight)).add("data", codec.encodeStart(dynamicOps, entry.data)).build(object);
				}
			};
		}
	}
}
