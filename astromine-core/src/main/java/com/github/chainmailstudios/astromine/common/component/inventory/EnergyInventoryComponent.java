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

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.TranslatableText;

import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergyStorage;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface EnergyInventoryComponent extends NameableComponent {
	Map<Integer, EnergyVolume> getContents();

	default Item getSymbol() {
		return AstromineItems.ENERGY.asItem();
	}

	default TranslatableText getName() {
		return new TranslatableText("text.astromine.energy");
	}

	default Collection<EnergyVolume> getContentsMatching(Predicate<EnergyVolume> predicate) {
		return this.getContents().values().stream().filter(predicate).collect(Collectors.toList());
	}

	default Collection<EnergyVolume> getExtractableContentsMatching(Direction direction, Predicate<EnergyVolume> predicate) {
		return this.getContents().entrySet().stream().filter((entry) -> canExtract(direction, entry.getValue(), entry.getKey()) && predicate.test(entry.getValue())).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	default Collection<EnergyVolume> getContentsMatchingSimulated(Predicate<EnergyVolume> predicate) {
		return this.getContentsSimulated().stream().filter(predicate).collect(Collectors.toList());
	}

	default Collection<EnergyVolume> getContentsSimulated() {
		return this.getContents().values().stream().map((volume) -> (EnergyVolume) volume.copy()).collect(Collectors.toList());
	}

	default boolean canInsert() {
		return true;
	}

	default boolean canInsert(@Nullable Direction direction, EnergyVolume fluid, int slot) {
		return true;
	}

	default boolean canExtract() {
		return true;
	}

	default boolean canExtract(@Nullable Direction direction, EnergyVolume fluid, int slot) {
		return true;
	}

	default TypedActionResult<EnergyVolume> insert(Direction direction, EnergyVolume volume) {
		if (this.canInsert()) {
			return this.insert(direction, volume);
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, volume);
		}
	}

	default TypedActionResult<EnergyVolume> insert(Direction direction, double amount) {
		Optional<Map.Entry<Integer, EnergyVolume>> matchingVolumeOptional = this.getContents().entrySet().stream().filter(entry -> {
			return canInsert(direction, entry.getValue(), entry.getKey());
		}).findFirst();

		if (matchingVolumeOptional.isPresent()) {
			matchingVolumeOptional.get().getValue().into(amount);
			return new TypedActionResult<>(ActionResult.SUCCESS, matchingVolumeOptional.get().getValue());
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, null);
		}
	}

	default void setVolume(int slot, EnergyVolume volume) {
		if (slot <= this.getSize()) {
			this.getContents().put(slot, volume);
			this.dispatchConsumers();
		}
	}

	int getSize();

	default void dispatchConsumers() {
		this.getListeners().forEach(Runnable::run);
	}

	List<Runnable> getListeners();

	default TypedActionResult<Collection<EnergyVolume>> extractMatching(Direction direction, Predicate<EnergyVolume> predicate) {
		HashSet<EnergyVolume> extractedVolumes = new HashSet<>();
		this.getContents().forEach((slot, volume) -> {
			if (canExtract(direction, volume, slot) && predicate.test(volume)) {
				TypedActionResult<EnergyVolume> extractionResult = this.extract(direction, slot);

				if (extractionResult.getResult().isAccepted()) {
					extractedVolumes.add(extractionResult.getValue());
				}
			}
		});

		if (!extractedVolumes.isEmpty()) {
			return new TypedActionResult<>(ActionResult.SUCCESS, extractedVolumes);
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, extractedVolumes);
		}
	}

	default TypedActionResult<EnergyVolume> extract(Direction direction, int slot) {
		EnergyVolume volume = this.getVolume(slot);

		if (!volume.isEmpty() && this.canExtract(direction, volume, slot)) {
			return this.extract(direction, slot, volume.getAmount());
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, EnergyVolume.empty());
		}
	}

	@Nullable
	default EnergyVolume getVolume(int slot) {
		return this.getContents().getOrDefault(slot, null);
	}

	@Nullable
	default EnergyVolume getFirstExtractableVolume(Direction direction) {
		return getContents().entrySet().stream().filter((entry) -> canExtract(direction, entry.getValue(), entry.getKey()) && (!entry.getValue().isEmpty())).map(Map.Entry::getValue).findFirst().orElse(null);
	}

	@Nullable
	default EnergyVolume getFirstInsertableVolume(EnergyVolume volume, Direction direction) {
		return getContents().entrySet().stream().filter((entry) -> canInsert(direction, entry.getValue(), entry.getKey()) && (entry.getValue().hasAvailable(volume.getAmount()))).map(
				Map.Entry::getValue).findFirst().orElse(null);
	}

	@Nullable
	default EnergyVolume getFirstInsertableVolume(Direction direction) {
		return getContents().entrySet().stream().filter((entry) -> canInsert(direction, entry.getValue(), entry.getKey()) && (!entry.getValue().isFull())).map(Map.Entry::getValue).findFirst().orElse(null);
	}

	default TypedActionResult<EnergyVolume> extract(Direction direction, int slot, double amount) {
		Optional<EnergyVolume> matchingVolumeOptional = Optional.ofNullable(this.getVolume(slot));

		if (matchingVolumeOptional.isPresent()) {
			EnergyVolume volume = matchingVolumeOptional.get();

			if (canExtract(direction, volume, slot)) {
				return new TypedActionResult<>(ActionResult.SUCCESS, matchingVolumeOptional.get().from(matchingVolumeOptional.get(), amount));
			} else {
				return new TypedActionResult<>(ActionResult.FAIL, EnergyVolume.empty());
			}
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, EnergyVolume.empty());
		}
	}

	default CompoundTag write(EnergyInventoryComponent source, Optional<String> subtag, Optional<Range<Integer>> range) {
		CompoundTag tag = new CompoundTag();
		this.write(source, tag, subtag, range);
		return tag;
	}

	default void write(EnergyInventoryComponent source, CompoundTag tag, Optional<String> subtag, Optional<Range<Integer>> range) {
		if (source == null || source.getSize() <= 0) {
			return;
		}

		if (tag == null) {
			return;
		}

		CompoundTag volumesTag = new CompoundTag();

		int minimum = range.isPresent() ? range.get().getMinimum() : 0;
		int maximum = range.isPresent() ? range.get().getMaximum() : source.getSize();

		for (int position = minimum; position < maximum; ++position) {
			if (source.getVolume(position) != null) {
				EnergyVolume volume = source.getVolume(position);

				if (volume != null) {
					CompoundTag volumeTag = source.getVolume(position).toTag();

					volumesTag.put(String.valueOf(position), volumeTag);
				}
			}
		}

		if (subtag.isPresent()) {
			CompoundTag inventoryTag = new CompoundTag();

			inventoryTag.putInt("size", source.getSize());
			inventoryTag.put("volumes", volumesTag);

			tag.put(subtag.get(), inventoryTag);
		} else {
			tag.putInt("size", source.getSize());
			tag.put("volumes", volumesTag);
		}
	}

	default void read(EnergyInventoryComponent target, CompoundTag tag, Optional<String> subtag, Optional<Range<Integer>> range) {
		if (tag == null) {
			return;
		}

		Tag rawTag;

		if (subtag.isPresent()) {
			rawTag = tag.get(subtag.get());
		} else {
			rawTag = tag;
		}

		if (!(rawTag instanceof CompoundTag)) {
			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + rawTag.getClass().getName() + " is not instance of " + CompoundTag.class.getName() + "!");
			return;
		}

		CompoundTag compoundTag = (CompoundTag) rawTag;

		if (!compoundTag.contains("size")) {
			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + CompoundTag.class.getName() + " does not contain 'size' value! (" + getClass().getName() + ")");
			return;
		}

		int size = compoundTag.getInt("size");

		if (size == 0) {
			AstromineCommon.LOGGER.log(Level.WARN, "Inventory contents size successfully read, but with size of zero. This may indicate a non-integer 'size' value! (" + getClass().getName() + ")");
		}

		if (!compoundTag.contains("volumes")) {
			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + CompoundTag.class.getName() + " does not contain 'volumes' subtag!");
			return;
		}

		Tag rawVolumesTag = compoundTag.get("volumes");

		if (!(rawVolumesTag instanceof CompoundTag)) {
			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + rawVolumesTag.getClass().getName() + " is not instance of " + CompoundTag.class.getName() + "!");
			return;
		}

		CompoundTag volumesTag = (CompoundTag) rawVolumesTag;

		int minimum = range.isPresent() ? range.get().getMinimum() : 0;
		int maximum = range.isPresent() ? range.get().getMaximum() : target.getSize();

		if (size < maximum) {
			AstromineCommon.LOGGER.log(Level.WARN, "Inventory size from tag smaller than specified maximum: will continue reading!");
			maximum = size;
		}

		if (target.getSize() < maximum) {
			AstromineCommon.LOGGER.log(Level.WARN, "Inventory size from target smaller than specified maximum: will continue reading!");
			maximum = target.getSize();
		}

		for (int position = minimum; position < maximum; ++position) {
			if (volumesTag.contains(String.valueOf(position))) {
				Tag rawVolumeTag = volumesTag.get(String.valueOf(position));

				if (!(rawVolumeTag instanceof CompoundTag)) {
					AstromineCommon.LOGGER.log(Level.ERROR, "Inventory volume skipped: stored tag not instance of " + CompoundTag.class.getName() + "!");
					return;
				}

				CompoundTag volumeTag = (CompoundTag) rawVolumeTag;

				EnergyVolume volume = EnergyVolume.fromTag(volumeTag);

				if (target.getSize() >= position) {
					if (target.getVolume(position) != null) {
						target.getVolume(position).setAmount(volume.getAmount());
						target.getVolume(position).setSize(volume.getSize());
					} else {
						target.setVolume(position, volume);
					}
				}
			}
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
		this.getContents().clear();
	}

	default boolean isEmpty() {
		return this.getContents().values().stream().allMatch(EnergyVolume::isEmpty);
	}

	<T extends EnergyInventoryComponent> T copy();

	@Override
	default @NotNull ComponentType<?> getComponentType() {
		return AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT;
	}
}
