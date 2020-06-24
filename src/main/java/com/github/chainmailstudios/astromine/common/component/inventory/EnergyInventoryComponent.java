package com.github.chainmailstudios.astromine.common.component.inventory;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface EnergyInventoryComponent extends Component {
	Map<Integer, EnergyVolume> getContents();

	default Collection<EnergyVolume> getContentsMatching(Predicate<EnergyVolume> predicate) {
		return this.getContents().values().stream().filter(predicate).collect(Collectors.toList());
	}

	default Collection<EnergyVolume> getContentsMatchingSimulated(Predicate<EnergyVolume> predicate) {
		return this.getContentsSimulated().stream().map(EnergyVolume::copy).filter(predicate).collect(Collectors.toList());
	}

	default Collection<EnergyVolume> getContentsSimulated() {
		return this.getContents().values().stream().map(EnergyVolume::copy).collect(Collectors.toList());
	}

	default boolean canInsert() {
		return true;
	}

	default boolean canInsert(int slot) {
		return true;
	}

	default boolean canInsert(EnergyVolume volume) {
		return true;
	}

	default boolean canInsert(EnergyVolume volume, int slot) {
		return true;
	}

	default boolean canExtract() {
		return true;
	}

	default boolean canExtract(int slot) {
		return true;
	}

	default boolean canExtract(EnergyVolume volume) {
		return true;
	}

	default boolean canExtract(EnergyVolume volume, int slot) {
		return true;
	}

	default TypedActionResult<EnergyVolume> insert(EnergyVolume volume) {
		if (this.canInsert(volume)) {
			return this.insert(volume.getFraction());
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, volume);
		}
	}

	default TypedActionResult<EnergyVolume> insert(Fraction fraction) {
		Optional<Map.Entry<Integer, EnergyVolume>> matchingVolumeOptional = this.getContents().entrySet().stream().filter(entry -> {
			return entry.getValue().hasAvailable(fraction);
		}).findFirst();

		if (matchingVolumeOptional.isPresent()) {
			matchingVolumeOptional.get().getValue().insertVolume(fraction);
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

	default TypedActionResult<Collection<EnergyVolume>> extractMatching(Predicate<EnergyVolume> predicate) {
		HashSet<EnergyVolume> extractedVolumes = new HashSet<>();
		this.getContents().forEach((slot, volume) -> {
			if (predicate.test(volume)) {
				TypedActionResult<EnergyVolume> extractionResult = this.extract(slot);

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

	default TypedActionResult<EnergyVolume> extract(int slot) {
		EnergyVolume volume = this.getVolume(slot);

		if (!volume.isEmpty() && this.canExtract(volume, slot)) {
			return this.extract(slot, volume.getFraction());
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, EnergyVolume.empty());
		}
	}

	default EnergyVolume getVolume(int slot) {
		return this.getContents().get(slot);
	}


	default TypedActionResult<EnergyVolume> extract(int slot, Fraction fraction) {
		Optional<EnergyVolume> matchingVolumeOptional = Optional.ofNullable(this.getVolume(slot));

		if (matchingVolumeOptional.isPresent()) {
			return new TypedActionResult<>(ActionResult.SUCCESS, matchingVolumeOptional.get().extractVolume(fraction));
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
			if (source.getVolume(position) != null && !source.getVolume(position).isEmpty()) {
				EnergyVolume volume = source.getVolume(position);

				if (volume != null && !volume.isEmpty()) {
					CompoundTag volumeTag = source.getVolume(position).toTag(new CompoundTag());

					if (volumeTag.isEmpty()) {
						volumesTag.put(String.valueOf(position), volumeTag);
					}
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
			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + CompoundTag.class.getName() + " does not contain 'size' value!");
			return;
		}

		int size = compoundTag.getInt("size");

		if (size == 0) {
			AstromineCommon.LOGGER.log(Level.WARN, "Inventory contents size successfully read, but with size of zero. This may indicate a non-integer 'size' value!");
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
						target.getVolume(position).setFraction(volume.getFraction());
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

	default void removeListener(Runnable listener) {
		this.getListeners().remove(listener);
	}

	default Fraction getMaximumFradction(Fraction slot) {
		return new Fraction(16, 1);
	}

	default void clear() {
		this.getContents().clear();
	}

	default boolean isEmpty() {
		return this.getContents().values().stream().allMatch(EnergyVolume::isEmpty);
	}
}