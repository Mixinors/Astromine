package com.github.chainmailstudios.astromine.common.component.inventory;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface FluidInventoryComponent extends Component {
	Map<Integer, FluidVolume> getContents();

	default Collection<FluidVolume> getContentsMatching(Predicate<FluidVolume> predicate) {
		return this.getContents().values().stream().filter(predicate).collect(Collectors.toList());
	}

	default Collection<FluidVolume> getContentsMatchingSimulated(Predicate<FluidVolume> predicate) {
		return this.getContentsSimulated().stream().map(FluidVolume::copy).filter(predicate).collect(Collectors.toList());
	}

	default Collection<FluidVolume> getContentsSimulated() {
		return this.getContents().values().stream().map(FluidVolume::copy).collect(Collectors.toList());
	}

	default boolean canInsert() {
		return true;
	}

	default boolean canInsert(int slot) {
		return true;
	}

	default boolean canInsert(FluidVolume volume) {
		return true;
	}

	default boolean canInsert(FluidVolume volume, int slot) {
		return true;
	}

	default boolean canExtract() {
		return true;
	}

	default boolean canExtract(int slot) {
		return true;
	}

	default boolean canExtract(FluidVolume volume) {
		return true;
	}

	default boolean canExtract(FluidVolume volume, int slot) {
		return true;
	}

	default TypedActionResult<FluidVolume> insert(FluidVolume volume) {
		if (this.canInsert(volume)) {
			return this.insert(volume.getFluid(), volume.getFraction());
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, volume);
		}
	}

	default TypedActionResult<FluidVolume> insert(Fluid fluid, Fraction fraction) {
		Optional<Map.Entry<Integer, FluidVolume>> matchingVolumeOptional = this.getContents().entrySet().stream().filter(entry -> {
			return entry.getValue().canInsert(fluid, fraction);
		}).findFirst();

		if (matchingVolumeOptional.isPresent()) {
			matchingVolumeOptional.get().getValue().insertVolume(fluid, fraction);
			return new TypedActionResult<>(ActionResult.SUCCESS, matchingVolumeOptional.get().getValue());
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, null);
		}
	}

	default void setVolume(int slot, FluidVolume volume) {
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

	default TypedActionResult<Collection<FluidVolume>> extractMatching(Predicate<FluidVolume> predicate) {
		HashSet<FluidVolume> extractedVolumes = new HashSet<>();
		this.getContents().forEach((slot, volume) -> {
			if (predicate.test(volume)) {
				TypedActionResult<FluidVolume> extractionResult = this.extract(slot);

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

	default TypedActionResult<FluidVolume> extract(int slot) {
		FluidVolume volume = this.getVolume(slot);

		if (!volume.isEmpty() && this.canExtract(volume, slot)) {
			return this.extract(slot, volume.getFraction());
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, FluidVolume.empty());
		}
	}

	default FluidVolume getVolume(int slot) {
		return this.getContents().get(slot);
	}


	default TypedActionResult<FluidVolume> extract(int slot, Fraction fraction) {
		Optional<FluidVolume> matchingVolumeOptional = Optional.ofNullable(this.getVolume(slot));

		if (matchingVolumeOptional.isPresent()) {
			if (matchingVolumeOptional.get().canExtract(matchingVolumeOptional.get().getFluid(), fraction)) {
				return new TypedActionResult<>(ActionResult.SUCCESS, matchingVolumeOptional.get().extractVolume(matchingVolumeOptional.get().getFluid(), fraction));
			} else {
				return new TypedActionResult<>(ActionResult.FAIL, FluidVolume.empty());
			}
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, FluidVolume.empty());
		}
	}

	default CompoundTag write(FluidInventoryComponent source, Optional<String> subtag, Optional<Range<Integer>> range) {
		CompoundTag tag = new CompoundTag();
		this.write(source, tag, subtag, range);
		return tag;
	}

	default void write(FluidInventoryComponent source, CompoundTag tag, Optional<String> subtag, Optional<Range<Integer>> range) {
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
				FluidVolume volume = source.getVolume(position);

				if (volume != null) {
					CompoundTag volumeTag = source.getVolume(position).toTag(new CompoundTag());

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

	default void read(FluidInventoryComponent target, CompoundTag tag, Optional<String> subtag, Optional<Range<Integer>> range) {
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

				FluidVolume volume = FluidVolume.fromTag(volumeTag);

				if (target.getSize() >= position) {
					if (target.getVolume(position) != null) {
						target.getVolume(position).setFraction(volume.getFraction());
						target.getVolume(position).setSize(volume.getSize());
						target.getVolume(position).setFluid(volume.getFluid());
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
		return this.getContents().values().stream().allMatch(FluidVolume::isEmpty);
	}
	
	<T extends FluidInventoryComponent> T copy();
}