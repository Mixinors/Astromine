package com.github.chainmailstudios.astromine.common.utilities;

import java.util.Map;

public class MapUtilities {
	public static <K, V> Map.Entry<K, V> entryOf(K k, V v) {
		return new Map.Entry<K, V>() {
			@Override
			public K getKey() {
				return k;
			}

			@Override
			public V getValue() {
				return v;
			}

			@Override
			public V setValue(V value) {
				return null;
			}
		};
	}
}
