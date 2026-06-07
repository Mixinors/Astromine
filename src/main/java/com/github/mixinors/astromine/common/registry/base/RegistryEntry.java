package com.github.mixinors.astromine.common.registry.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

/**
 * <p>A lazily-loaded supplier of {@link Registry} entries that is invalidated
 * during resource reloads.</p>
 */
public class RegistryEntry<V> implements Supplier<V> {
	public static <V> Codec<RegistryEntry<V>> createCodec(Registry<V> registry) {
		return RecordCodecBuilder.create(
				instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("key").forGetter(RegistryEntry::getKey)
				).apply(instance, key -> new RegistryEntry<>(key, registry))
		);
	}
	
	private final Supplier<V> supplier;
	
	private final ResourceLocation key;
	private V value;
	
	public RegistryEntry(ResourceLocation key, Registry<V> registry) {
		this.key = key;
		this.supplier = () -> registry.get(key);
	}
	
	public RegistryEntry(ResourceLocation key, Supplier<V> supplier) {
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
	
	public ResourceLocation getKey() {
		return key;
	}
	
	public void invalidate() {
		this.value = null;
	}
}
