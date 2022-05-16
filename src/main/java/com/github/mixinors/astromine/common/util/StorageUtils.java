package com.github.mixinors.astromine.common.util;

import com.google.common.collect.Streams;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StorageUtils {
	@Nullable
	public static <T> StorageView<T> first(Storage<T> storage, Transaction transaction, Predicate<StorageView<T>> predicate) {
		return Streams.stream(storage.iterable(transaction)).filter(predicate).findFirst().orElse(null);
	}
	
	@Nonnull
	public static <T> Stream<? extends StorageView<T>> stream(Storage<T> storage, Transaction transaction) {
		return Streams.stream(storage.iterable(transaction));
	}
}
