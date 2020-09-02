package com.github.chainmailstudios.astromine.common.utilities.data;

import java.util.function.Consumer;

public class Holder<T> {
	T t;

	private Holder(T t) {
		this.t = t;
	}

	public Holder<T> with(Consumer<T> consumer) {
		consumer.accept(t);

		return this;
	}

	public T get() {
		return t;
	}

	public void set(T t) {
		this.t = t;
	}

	public static <Y> Holder<Y> of(Y y) {
		return new Holder<>(y);
	}
}
