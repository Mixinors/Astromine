package com.github.chainmailstudios.astromine.common.registry.base;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

public class UniRegistry<T, U> {
	private final Map<T, U> entries = Maps.newHashMap();

	public U get(T t) {
		return entries.get(t);
	}

	public U set(T t, U u) {
		entries.put(t, u);
		return u;
	}

	public U add(T t, U u) {
		return set(t, u);
	}

	public U remove(T t, U u) {
		entries.remove(t, u);
		return u;
	}

	public U register(T t, U u) {
		return set(t, u);
	}

	public void unregister(T t, U u) {
		remove(t, u);
	}

	public boolean contains(T t) {
		return containsKey(t);
	}

	public boolean containsKey(T t) {
		return entries.containsKey(t);
	}

	public boolean containsValue(U u) {
		return entries.containsValue(u);
	}

	public Collection<T> getKeys() {
		return entries.keySet();
	}

	public Collection<U> getValues() {
		return entries.values();
	}
}
