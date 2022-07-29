package com.github.mixinors.astromine.common.registry.base;

import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * <p>A lazily-loaded supplier of {@link Registry} entries that is invalidated
 * during resource reloads.</p>
 */
public class RegistryEntry<V> implements Supplier<V> {
	private final Supplier<V> supplier;
	
	private final Identifier key;
	private V value;
	
	public RegistryEntry(Identifier key, Supplier<V> supplier) {
		this.key = key;
		this.supplier = supplier;
	}
	
	@Override
	public V get() {
		return getValue();
	}
	
	@Nullable
	public V getValue() {
		if (value == null) {
			value = supplier.get();
		}
		
		return value;
	}
	
	public Identifier getKey() {
		return key;
	}
	
	public void invalidate() {
		this.value = null;
	}
}
