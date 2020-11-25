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

package com.github.chainmailstudios.astromine.common.component.inventory;

import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.EnergyComponentFromEnergyStorage;
import net.fabricmc.fabric.api.util.NbtType;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergyStorage;

import java.util.List;
import java.util.function.Consumer;

import static java.lang.Math.min;

/**
 * A {@link IdentifiableComponent} representing an energy reserve.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag(CompoundTag)} and {@link #fromTag(CompoundTag)}.
 */
public interface EnergyComponent extends IdentifiableComponent {
	/** Instantiates an {@link EnergyComponent}. */
	static EnergyComponent of(double size) {
		return SimpleEnergyComponent.of(size);
	}

	/** Instantiates an {@link EnergyComponent}. */
	static EnergyComponent of(EnergyVolume volume) {
		return SimpleEnergyComponent.of(volume);
	}

	/** Instantiates an {@link EnergyComponent}. */
	static EnergyComponent of(EnergyStorage storage) {
		return EnergyComponentFromEnergyStorage.of(storage);
	}

	/** Instantiates an {@link EnergyComponent} and synchronization. */
	static EnergyComponent ofSynced(double size) {
		return SimpleAutoSyncedEnergyComponent.of(size);
	}

	/** Instantiates an {@link EnergyComponent} and synchronization. */
	static EnergyComponent ofSynced(EnergyVolume volume) {
		return SimpleAutoSyncedEnergyComponent.of(volume);
	}

	/** Returns this component's {@link Item} symbol. */
	default Item getSymbol() {
		return AstromineItems.ENERGY.asItem();
	}

	/** Returns this component's {@link Text} name. */
	default Text getName() {
		return new TranslatableText("text.astromine.energy");
	}

	/** Returns this component's listeners. */
	List<Runnable> getListeners();

	/** Adds a listener to this component. */
	default void addListener(Runnable listener) {
		this.getListeners().add(listener);
	}

	/** Removes a listener from this component. */
	default void removeListener(Runnable listener) {
		this.getListeners().remove(listener);
	}

	/** Triggers this component's listeners. */
	default void updateListeners() {
		this.getListeners().forEach(Runnable::run);
	}

	/** Returns this component with an added listener. */
	default EnergyComponent withListener(Consumer<EnergyComponent> listener) {
		addListener(() -> listener.accept(this));
		return this;
	}

	/** Returns this component's volume. */
	EnergyVolume getVolume();

	/** Sets this component's volume to the specified value. */
	default void setVolume(EnergyVolume volume) {
		getVolume().setSize(volume.getSize());
		getVolume().setAmount(volume.getAmount());
	}

	/** Returns this component's volume's amount. */
	default double getAmount() {
		return getVolume().getAmount();
	}

	/** Sets this component's volume's amount. */
	default void setAmount(double amount) {
		getVolume().setAmount(amount);
	}

	/** Returns this component's volume's size. */
	default double getSize() {
		return getVolume().getSize();
	}

	/** Sets this component's volume's size. */
	default void setSize(double amount) {
		getVolume().setSize(amount);
	}

	/** Asserts whether this component's volume is empty or not. */
	default boolean isEmpty() {
		return this.getVolume().isEmpty();
	}

	/** Asserts whether this component's volume is not empty or not. */
	default boolean isNotEmpty() {
		return !isEmpty();
	}

	/** Clears this component's volume. */
	default void clear() {
		this.getVolume().setAmount(0.0);
	}

	default void into(EnergyComponent component, double amount) {
		EnergyVolume ourVolume = getVolume();
		EnergyVolume theirVolume = component.getVolume();

		double transferable = min(theirVolume.getSize() - theirVolume.getAmount(), amount);

		ourVolume.give(theirVolume, transferable);
	}

	/** Serializes this {@link EnergyComponent} to a {@link CompoundTag}. */
	@Override
	default void writeToNbt(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		dataTag.putDouble("energy", getVolume().getAmount());

		tag.put(AstromineComponents.ENERGY_INVENTORY_COMPONENT.getId().toString(), dataTag);
	}

	/** Deserializes this {@link EnergyComponent} from a {@link CompoundTag}. */
	@Override
	default void readFromNbt(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound(AstromineComponents.ENERGY_INVENTORY_COMPONENT.getId().toString());

		EnergyVolume volume = getVolume();

		if (dataTag.contains("energy", NbtType.COMPOUND)) {
			EnergyVolume energy = EnergyVolume.fromTag(dataTag.getCompound("energy"));
			volume.setAmount(energy.getAmount());
		} else if (dataTag.contains("energy", NbtType.DOUBLE)) {
			double energy = dataTag.getDouble("energy");
			volume.setAmount(energy);
		}
	}

	/** Returns the {@link EnergyComponent} of the given {@link V}. */
	@Nullable
	static <V> EnergyComponent get(V v) {
		try {
			return AstromineComponents.ENERGY_INVENTORY_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			if (v instanceof EnergyStorage) {
				return of((EnergyStorage) v);
			}

			return null;
		}
	}
}
