package com.github.chainmailstudios.astromine.common.registry.base;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.Identifier;

import java.util.Collection;

public abstract class BiDirectionalRegistry<T, U> {
	private final BiMap<T, U> entries = HashBiMap.create();

	public U get(T t) {
		return entries.get(t);
	}

	public T getKey(U u) {
		return entries.inverse().get(u);
	}

	public U set(T t, U u) {
		entries.putIfAbsent(t, u);
		return u;
	}

	public U add(T t, U u) {
		return set(t, u);
	}

	public U remove(T t, U u) {
		entries.remove(t, u);
		return u;
	}

	public void removeKey(T t) {
		entries.remove(t);
	}

	public void removeValue(U u) {
		entries.inverse().remove(u);
	}

	public U register(T t, U u) {
		return set(t, u);
	}

	public void unregister(T t, U u) {
		remove(t, u);
	}

	public void unregisterKey(T t) {
		removeKey(t);
	}

	public void unregisterValue(U u) {
		removeValue(u);
	}

	public boolean contains(T t, U u) {
		return containsKey(t) && get(t).equals(u);
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
