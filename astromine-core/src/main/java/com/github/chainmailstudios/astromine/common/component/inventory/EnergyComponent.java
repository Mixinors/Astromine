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

import net.fabricmc.fabric.api.util.NbtType;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public interface EnergyComponent extends NameableComponent, AutoSyncedComponent {
	static EnergyComponent of(double size) {
		return SimpleEnergyComponent.of(size);
	}

	static EnergyComponent of(EnergyVolume volume) {
		return SimpleEnergyComponent.of(volume);
	}

	@Nullable
	static <V> EnergyComponent get(V v) {
		try {
			return AstromineComponents.ENERGY_INVENTORY_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}

	default Item getSymbol() {
		return AstromineItems.ENERGY.asItem();
	}

	default TranslatableText getName() {
		return new TranslatableText("text.astromine.energy");
	}

	List<Runnable> getListeners();

	default void addListener(Runnable listener) {
		this.getListeners().add(listener);
	}

	default void removeListener(Runnable listener) {
		this.getListeners().remove(listener);
	}

	default void updateListeners() {
		this.getListeners().forEach(Runnable::run);
	}

	default EnergyComponent withListener(Consumer<EnergyComponent> listener) {
		addListener(() -> listener.accept(this));
		return this;
	}

	EnergyVolume getVolume();

	default void setVolume(EnergyVolume volume) {
		getVolume().setSize(volume.getSize());
		getVolume().setAmount(volume.getAmount());
	}

	default double getAmount() {
		return getVolume().getAmount();
	}

	default void setAmount(double amount) {
		getVolume().setAmount(amount);
	}

	default double getSize() {
		return getVolume().getSize();
	}

	default void setSize(double amount) {
		getVolume().setSize(amount);
	}

	@Override
	default void writeToNbt(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		dataTag.putDouble("energy", getVolume().getAmount());

		tag.put(AstromineComponents.ENERGY_INVENTORY_COMPONENT.getId().toString(), dataTag);
	}

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

	default void clear() {
		this.getVolume().setAmount(0.0);
	}

	default boolean isEmpty() {
		return this.getVolume().isEmpty();
	}

	default boolean isNotEmpty() {
		return !isEmpty();
	}
}
