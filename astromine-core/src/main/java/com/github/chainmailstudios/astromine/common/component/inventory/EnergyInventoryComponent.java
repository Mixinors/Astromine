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

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import nerdhub.cardinal.components.api.ComponentType;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface EnergyInventoryComponent extends NameableComponent {
	EnergyVolume getVolume();

	default Item getSymbol() {
		return AstromineItems.ENERGY.asItem();
	}

	default TranslatableText getName() {
		return new TranslatableText("text.astromine.energy");
	}

	default EnergyVolume getSimulated() {
		return getVolume().copy();
	}

	default boolean canInsert(@Nullable Direction direction) {
		return true;
	}

	default boolean canInsert(@Nullable Direction direction, double volume) {
		return true;
	}

	default boolean canExtract(@Nullable Direction direction) {
		return true;
	}

	default boolean canExtract(@Nullable Direction direction, double volume) {
		return true;
	}

	default TypedActionResult<EnergyVolume> insert(Direction direction, EnergyVolume volume) {
		if (this.canInsert(direction)) {
			return this.insert(direction, volume);
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, volume);
		}
	}

	default TypedActionResult<EnergyVolume> insert(Direction direction, double amount) {
		EnergyVolume volume = getVolume();
		if (canInsert(direction, amount)) {
			volume.add(amount);
			return new TypedActionResult<>(ActionResult.SUCCESS, volume);
		}
		return new TypedActionResult<>(ActionResult.FAIL, null);
	}

	default void setVolume(EnergyVolume volume) {
		getVolume().setSize(volume.getSize());
		getVolume().setAmount(volume.getAmount());
	}

	default void dispatchConsumers() {
		this.getListeners().forEach(Runnable::run);
	}

	List<Runnable> getListeners();

	default TypedActionResult<Collection<EnergyVolume>> extractMatching(Direction direction, Predicate<EnergyVolume> predicate) {
		HashSet<EnergyVolume> extractedVolumes = new HashSet<>();
		EnergyVolume volume = getVolume();
		if (canExtract(direction) && predicate.test(volume)) {
			TypedActionResult<EnergyVolume> extractionResult = this.extract(direction);

			if (extractionResult.getResult().isAccepted()) {
				extractedVolumes.add(extractionResult.getValue());
			}
		}

		if (!extractedVolumes.isEmpty()) {
			return new TypedActionResult<>(ActionResult.SUCCESS, extractedVolumes);
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, extractedVolumes);
		}
	}

	default TypedActionResult<EnergyVolume> extract(Direction direction) {
		EnergyVolume volume = this.getVolume();

		if (!volume.isEmpty() && this.canExtract(direction)) {
			return this.extract(direction, volume.getAmount());
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, EnergyVolume.empty());
		}
	}

	@Nullable
	default EnergyVolume getFirstExtractableVolume(Direction direction) {
		EnergyVolume volume = getVolume();
		if (canExtract(direction) && !volume.isEmpty()) return volume;
		return null;
	}

	@Nullable
	default EnergyVolume getFirstInsertableVolume(double amount, Direction direction) {
		EnergyVolume volume = getVolume();
		if (canInsert(direction) && volume.hasAvailable(amount)) return volume;
		return null;
	}

	@Nullable
	default EnergyVolume getFirstInsertableVolume(Direction direction) {
		EnergyVolume volume = getVolume();
		if (canInsert(direction) && !volume.isFull()) return volume;
		return null;
	}

	default TypedActionResult<EnergyVolume> extract(Direction direction, double amount) {
		EnergyVolume volume = this.getVolume();

		if (canExtract(direction, amount)) {
			return new TypedActionResult<>(ActionResult.SUCCESS, volume.minus(amount));
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, EnergyVolume.empty());
		}
	}

	default CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		this.write(tag);
		return tag;
	}

	default void write(CompoundTag tag) {
		tag.putDouble("energy", getVolume().getAmount());
	}

	default void read(CompoundTag tag) {
		clear();
		EnergyVolume volume = getVolume();
		if (tag.contains("energy", NbtType.COMPOUND)) {
			EnergyVolume energy = EnergyVolume.fromTag(tag.getCompound("energy"));
			volume.setAmount(energy.getAmount());
		} else if (tag.contains("energy", NbtType.DOUBLE)) {
			double energy = tag.getDouble("energy");
			volume.setAmount(energy);
		}
	}

	default void addListener(Runnable listener) {
		this.getListeners().add(listener);
	}

	default EnergyInventoryComponent withListener(Consumer<EnergyInventoryComponent> listener) {
		addListener(() -> listener.accept(this));
		return this;
	}

	default void removeListener(Runnable listener) {
		this.getListeners().remove(listener);
	}

	default void clear() {
		this.getVolume().setAmount(0.0);
	}

	default boolean isEmpty() {
		return this.getVolume().isEmpty();
	}

	<T extends EnergyInventoryComponent> T copy();

	@Override
	default @NotNull ComponentType<?> getComponentType() {
		return AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT;
	}

	default void setAmount(double amount) {
		getVolume().setAmount(amount);
	}
	
	default double getAmount() {
		return getVolume().getAmount();
	}

	default void setSize(double amount) {
		getVolume().setSize(amount);
	}

	default double getSize() {
		return getVolume().getSize();
	}
}
