/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.component.base;

import com.github.mixinors.astromine.common.component.general.miscellaneous.IdentifiableComponent;
import com.github.mixinors.astromine.common.component.general.provider.EnergyComponentProvider;
import com.github.mixinors.astromine.registry.common.AMComponents;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.utils.NbtType;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import com.github.mixinors.astromine.common.volume.energy.EnergyVolume;
import com.github.mixinors.astromine.registry.common.AMItems;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

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
	static EnergyComponentImpl of(double size) {
		return new EnergyComponentImpl(size);
	}

	/** Instantiates an {@link EnergyComponent}. */
	static EnergyComponentImpl of(EnergyVolume volume) {
		return new EnergyComponentImpl(volume);
	}

	/** Instantiates an {@link EnergyComponent} with automatic synchronization. */
	static EnergyComponentSyncedImpl ofSynced(double size) {
		return new EnergyComponentSyncedImpl(size);
	}

	/** Instantiates an {@link EnergyComponent} with automatic synchronization. */
	static EnergyComponentSyncedImpl ofSynced(EnergyVolume volume) {
		return new EnergyComponentSyncedImpl(volume);
	}
	
	/** Returns this component's {@link Item} symbol. */
	default Item getSymbol() {
		return AMItems.ENERGY.get().asItem();
	}

	/** Returns this component's {@link Text} name. */
	default Text getText() {
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

	/** Transfers the specified amount of energy from this component to another. */
	default void into(EnergyComponent component, double amount) {
		var ourVolume = getVolume();
		var theirVolume = component.getVolume();

		var transferable = min(theirVolume.getSize() - theirVolume.getAmount(), amount);

		ourVolume.give(theirVolume, transferable);
	}
	
	/** Asserts whether this component's volume has the specified amount of energy stored. */
	default boolean hasStored(double amount) {
		return getVolume().hasStored(amount);
	}
	
	/** Asserts whether this component's volume has the specified amount of energy available. */
	default boolean hasAvailable(double amount) {
		return getVolume().hasAvailable(amount);
	}
	
	/** Takes the specified amount of energy from this component's volume. */
	default void take(double amount) {
		getVolume().take(amount);
	}
	
	/** Gives the specified amount of energy to this component's volume. */
	default void give(double amount) {
		getVolume().give(amount);
	}

	/** Serializes this {@link EnergyComponent} to a {@link CompoundTag}. */
	@Override
	default void toTag(CompoundTag tag) {
		var dataTag = new CompoundTag();

		dataTag.putDouble("Energy", getVolume().getAmount());

		tag.put("EnergyComponent", dataTag);
	}

	/** Deserializes this {@link EnergyComponent} from a {@link CompoundTag}. */
	@Override
	default void fromTag(CompoundTag tag) {
		var dataTag = tag.getCompound("EnergyComponent");

		var volume = getVolume();

		if (dataTag.contains("Energy", NbtType.COMPOUND)) {
			var energy = EnergyVolume.fromTag(dataTag.getCompound("Energy"));
			volume.setAmount(energy.getAmount());
		} else if (dataTag.contains("Energy", NbtType.DOUBLE)) {
			double energy = dataTag.getDouble("Energy");
			volume.setAmount(energy);
		}
	}

	/** Returns the {@link EnergyComponent} of the given {@link V}. */
	@Nullable
	static <V> EnergyComponent from(V v) {
		if (v instanceof EnergyComponentProvider) {
			return ((EnergyComponentProvider) v).getEnergyComponent();
		}
		
		return fromPost(v);
	}
	
	@ExpectPlatform
	static <V> EnergyComponent fromPost(V v) {
		throw new AssertionError();
	}
	
	/** Returns this component's {@link Identifier}. */
	@Override
	default Identifier getId() {
		return AMComponents.ENERGY;
	}
}
