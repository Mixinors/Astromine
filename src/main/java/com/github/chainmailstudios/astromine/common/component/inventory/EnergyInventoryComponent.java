package com.github.chainmailstudios.astromine.common.component.inventory;

public interface EnergyInventoryComponent {}
//public interface EnergyInventoryComponent extends NameableComponent {
//	Map<Integer, EnergyVolume> getContents();
//
//	default Item getSymbol() {
//		return AstromineItems.ENERGY;
//	}
//
//	default TranslatableText getName() {
//		return new TranslatableText("text.astromine.energy");
//	}
//
//	default Collection<EnergyVolume> getContentsMatching(Predicate<EnergyVolume> predicate) {
//		return this.getContents().values().stream().filter(predicate).collect(Collectors.toList());
//	}
//
//	default Collection<EnergyVolume> getContentsMatchingSimulated(Predicate<EnergyVolume> predicate) {
//		return this.getContentsSimulated().stream().map(EnergyVolume::copy).filter(predicate).collect(Collectors.toList());
//	}
//
//	default Collection<EnergyVolume> getContentsSimulated() {
//		return this.getContents().values().stream().map(EnergyVolume::copy).collect(Collectors.toList());
//	}
//
//	default boolean canInsert() {
//		return true;
//	}
//
//	default boolean canInsert(Direction direction, EnergyVolume volume, int slot) {
//		return true;
//	}
//
//	default boolean canExtract() {
//		return true;
//	}
//
//	default boolean canExtract(Direction direction, EnergyVolume volume, int slot) {
//		return true;
//	}
//
////	default TypedActionResult<EnergyVolume> insert(Direction direction, EnergyVolume volume) {
////		if (this.canInsert()) {
////			return this.insert(direction, volume.getAmount());
////		} else {
////			return new TypedActionResult<>(ActionResult.FAIL, volume);
////		}
////	}
////
////	default TypedActionResult<EnergyVolume> insert(Direction direction, double amount) {
////		Optional<Map.Entry<Integer, EnergyVolume>> matchingVolumeOptional = this.getContents().entrySet().stream().filter(entry -> {
////			return canInsert(direction, entry.getValue(), entry.getKey());
////		}).findFirst();
////
////		if (matchingVolumeOptional.isPresent()) {
////			matchingVolumeOptional.get().getValue().insertVolume(amount);
////			return new TypedActionResult<>(ActionResult.SUCCESS, matchingVolumeOptional.get().getValue());
////		} else {
////			return new TypedActionResult<>(ActionResult.FAIL, null);
////		}
////	}
//
//	default void setVolume(int slot, EnergyVolume volume) {
//		if (slot <= this.getSize()) {
//			this.getContents().put(slot, volume);
//			this.dispatchConsumers();
//		}
//	}
//
//	int getSize();
//
//	default void dispatchConsumers() {
//		this.getListeners().forEach(Runnable::run);
//	}
//
//	List<Runnable> getListeners();
//
////	default TypedActionResult<Collection<EnergyVolume>> extractMatching(Direction direction, Predicate<EnergyVolume> predicate) {
////		HashSet<EnergyVolume> extractedVolumes = new HashSet<>();
////		this.getContents().forEach((slot, volume) -> {
////			if (predicate.test(volume)) {
////				TypedActionResult<EnergyVolume> extractionResult = this.extract(direction, slot);
////
////				if (extractionResult.getResult().isAccepted()) {
////					extractedVolumes.add(extractionResult.getValue());
////				}
////			}
////		});
////
////		if (!extractedVolumes.isEmpty()) {
////			return new TypedActionResult<>(ActionResult.SUCCESS, extractedVolumes);
////		} else {
////			return new TypedActionResult<>(ActionResult.FAIL, extractedVolumes);
////		}
////	}
//
////	default TypedActionResult<EnergyVolume> extract(Direction direction, int slot) {
////		EnergyVolume volume = this.getVolume(slot);
////
////		if (this.canExtract(direction, volume, slot)) {
////			return this.extract(slot, volume.getAmount());
////		} else {
////			return new TypedActionResult<>(ActionResult.FAIL, EnergyVolume.empty());
////		}
////	}
//
//	default EnergyVolume getVolume(int slot) {
//		return this.getContents().get(slot);
//	}
//
//	default EnergyVolume getFirstExtractableVolume(Direction direction) {
//		return getContents().entrySet().stream().filter((entry) -> canExtract(direction, entry.getValue(), entry.getKey())).map(Map.Entry::getValue).findFirst().orElse(null);
//	}
//
//	default EnergyVolume getFirstInsertableVolume(Direction direction) {
//		return getContents().entrySet().stream().filter((entry) -> canInsert(direction, entry.getValue(), entry.getKey())).map(Map.Entry::getValue).findFirst().orElse(null);
//	}
//
////	default TypedActionResult<EnergyVolume> extract(int slot, double amount) {
////		Optional<EnergyVolume> matchingVolumeOptional = Optional.ofNullable(this.getVolume(slot));
////
////		if (matchingVolumeOptional.isPresent()) {
////			return new TypedActionResult<>(ActionResult.SUCCESS, matchingVolumeOptional.get().extractVolume(amount));
////		} else {
////			return new TypedActionResult<>(ActionResult.FAIL, EnergyVolume.empty());
////		}
////	}
//
//	default CompoundTag write(EnergyInventoryComponent source, Optional<String> subtag, Optional<Range<Integer>> range) {
//		CompoundTag tag = new CompoundTag();
//		this.write(source, tag, subtag, range);
//		return tag;
//	}
//
//	default void write(EnergyInventoryComponent source, CompoundTag tag, Optional<String> subtag, Optional<Range<Integer>> range) {
//		if (source == null || source.getSize() <= 0) {
//			return;
//		}
//
//		if (tag == null) {
//			return;
//		}
//
//		CompoundTag volumesTag = new CompoundTag();
//
//		int minimum = range.isPresent() ? range.get().getMinimum() : 0;
//		int maximum = range.isPresent() ? range.get().getMaximum() : source.getSize();
//
//		for (int position = minimum; position < maximum; ++position) {
//			if (source.getVolume(position) != null) {
//				EnergyVolume volume = source.getVolume(position);
//
//				if (volume != null) {
//					CompoundTag volumeTag = source.getVolume(position).toTag(new CompoundTag());
//
//					volumesTag.put(String.valueOf(position), volumeTag);
//				}
//			}
//		}
//
//		if (subtag.isPresent()) {
//			CompoundTag inventoryTag = new CompoundTag();
//
//			inventoryTag.putInt("size", source.getSize());
//			inventoryTag.put("volumes", volumesTag);
//
//			tag.put(subtag.get(), inventoryTag);
//		} else {
//			tag.putInt("size", source.getSize());
//			tag.put("volumes", volumesTag);
//		}
//	}
//
//	default void read(EnergyInventoryComponent target, CompoundTag tag, Optional<String> subtag, Optional<Range<Integer>> range) {
//		if (tag == null) {
//			return;
//		}
//
//		Tag rawTag;
//
//		if (subtag.isPresent()) {
//			rawTag = tag.get(subtag.get());
//		} else {
//			rawTag = tag;
//		}
//
//		if (!(rawTag instanceof CompoundTag)) {
//			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + rawTag.getClass().getName() + " is not instance of " + CompoundTag.class.getName() + "!");
//			return;
//		}
//
//		CompoundTag compoundTag = (CompoundTag) rawTag;
//
//		if (!compoundTag.contains("size")) {
//			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + CompoundTag.class.getName() + " does not contain 'size' value! (" + getClass().getName() + ")");
//			return;
//		}
//
//		int size = compoundTag.getInt("size");
//
//		if (size == 0) {
//			AstromineCommon.LOGGER.log(Level.WARN, "Inventory contents size successfully read, but with size of zero. This may indicate a non-integer 'size' value! (" + getClass().getName() + ")");
//		}
//
//		if (!compoundTag.contains("volumes")) {
//			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + CompoundTag.class.getName() + " does not contain 'volumes' subtag!");
//			return;
//		}
//
//		Tag rawVolumesTag = compoundTag.get("volumes");
//
//		if (!(rawVolumesTag instanceof CompoundTag)) {
//			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + rawVolumesTag.getClass().getName() + " is not instance of " + CompoundTag.class.getName() + "!");
//			return;
//		}
//
//		CompoundTag volumesTag = (CompoundTag) rawVolumesTag;
//
//		int minimum = range.isPresent() ? range.get().getMinimum() : 0;
//		int maximum = range.isPresent() ? range.get().getMaximum() : target.getSize();
//
//		if (size < maximum) {
//			AstromineCommon.LOGGER.log(Level.WARN, "Inventory size from tag smaller than specified maximum: will continue reading!");
//			maximum = size;
//		}
//
//		if (target.getSize() < maximum) {
//			AstromineCommon.LOGGER.log(Level.WARN, "Inventory size from target smaller than specified maximum: will continue reading!");
//			maximum = target.getSize();
//		}
//
//		for (int position = minimum; position < maximum; ++position) {
//			if (volumesTag.contains(String.valueOf(position))) {
//				Tag rawVolumeTag = volumesTag.get(String.valueOf(position));
//
//				if (!(rawVolumeTag instanceof CompoundTag)) {
//					AstromineCommon.LOGGER.log(Level.ERROR, "Inventory volume skipped: stored tag not instance of " + CompoundTag.class.getName() + "!");
//					return;
//				}
//
//				CompoundTag volumeTag = (CompoundTag) rawVolumeTag;
//
//				EnergyVolume volume = EnergyVolume.fromTag(volumeTag);
//
//				if (target.getSize() >= position) {
//					if (target.getVolume(position) != null) {
//						target.getVolume(position).setAmount(volume.getAmount());
//						target.getVolume(position).setMaxAmount(volume.getMaxAmount());
//					} else {
//						target.setVolume(position, volume);
//					}
//				}
//			}
//		}
//	}
//
//	default void addListener(Runnable listener) {
//		this.getListeners().add(listener);
//	}
//
//	default void removeListener(Runnable listener) {
//		this.getListeners().remove(listener);
//	}
//
//	default Fraction getMaximumFradction(Fraction slot) {
//		return new Fraction(16, 1);
//	}
//
//	default void clear() {
//		this.getContents().clear();
//	}
//
//	default boolean isEmpty() {
//		return this.getContents().values().stream().allMatch(EnergyVolume::isEmpty);
//	}
//
//	@Override
//	default @NotNull ComponentType<?> getComponentType() {
//		return AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT;
//	}
//}