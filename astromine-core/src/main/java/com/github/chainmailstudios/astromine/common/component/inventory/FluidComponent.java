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

import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.common.utilities.VolumeUtilities;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.chainmailstudios.astromine.common.volume.fraction.Fraction.minimum;
import static java.lang.Integer.min;

/**
 * A {@link IdentifiableComponent} representing a fluid reserve.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag(CompoundTag)} and {@link #fromTag(CompoundTag)}.
 */
public interface FluidComponent extends Iterable<FluidVolume>, IdentifiableComponent {
	/** Instantiates a {@link FluidComponent}. */
	static FluidComponent of(int size) {
		return SimpleFluidComponent.of(size);
	}

	/** Instantiates a {@link FluidComponent}. */
	static FluidComponent of(FluidVolume... volumes) {
		return SimpleFluidComponent.of(volumes);
	}

	/** Instantiates a {@link FluidComponent} and synchronization. */
	static FluidComponent ofSynced(int size) {
		return SimpleAutoSyncedFluidComponent.of(size);
	}

	/** Instantiates a {@link FluidComponent} and synchronization. */
	static FluidComponent ofSynced(FluidVolume... volumes) {
		return SimpleAutoSyncedFluidComponent.of(volumes);
	}

	/** Returns this component's {@link Item} symbol. */
	default Item getSymbol() {
		return AstromineItems.FLUID.asItem();
	}

	/** Returns this component's {@link Text} name. */
	default Text getName() {
		return new TranslatableText("text.astromine.fluid");
	}

	/** Returns this component's size. */
	int getSize();

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
	default FluidComponent withListener(Consumer<FluidComponent> listener) {
		addListener(() -> listener.accept(this));
		return this;
	}

	/** Returns this component's contents. */
	Map<Integer, FluidVolume> getContents();

	/** Returns this component's contents matching the given predicate. */
	default List<FluidVolume> getVolumes(Predicate<FluidVolume> predicate) {
		return getContents().values().stream().filter(predicate).collect(Collectors.toList());
	}

	/** Returns this component's contents extractable through the given direction. */
	default List<FluidVolume> getExtractableVolumes(@Nullable Direction direction) {
		return getContents().entrySet().stream().filter((entry) -> canExtract(direction, entry.getValue(), entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	/** Returns this component's contents matching the given predicate
	 * extractable through the specified direction. */
	default List<FluidVolume> getExtractableVolumes(@Nullable Direction direction, Predicate<FluidVolume> predicate) {
		return getExtractableVolumes(direction).stream().filter(predicate).collect(Collectors.toList());
	}

	/** Returns this component's contents insertable through the given direction. */
	default List<FluidVolume> getInsertableVolumes(@Nullable Direction direction) {
		return getContents().entrySet().stream().filter((entry) -> canInsert(direction, entry.getValue(), entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	/** Returns this component's contents insertable through the given direction
	 * which accept the specified volume. */
	default List<FluidVolume> getInsertableVolumes(@Nullable Direction direction, FluidVolume volume) {
		return getContents().entrySet().stream().filter((entry) -> canInsert(direction, volume, entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	/** Returns this component's contents matching the given predicate
	 * insertable through the specified direction which accept the supplied volume. */
	default List<FluidVolume> getInsertableVolumes(@Nullable Direction direction, FluidVolume volume, Predicate<FluidVolume> predicate) {
		return getInsertableVolumes(direction, volume).stream().filter(predicate).collect(Collectors.toList());
	}

	/** Returns the first volume extractable through the given direction. */
	@Nullable
	default FluidVolume getFirstExtractableVolume(@Nullable Direction direction) {
		List<FluidVolume> volumes = getExtractableVolumes(direction);
		volumes.removeIf(FluidVolume::isEmpty);
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	/** Returns the first volume matching the given predicate
	 * extractable through the specified direction. */
	@Nullable
	default FluidVolume getFirstExtractableVolume(@Nullable Direction direction, Predicate<FluidVolume> predicate) {
		List<FluidVolume> volumes = getExtractableVolumes(direction, predicate);
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	/** Returns the first volume insertable through the given direction
	 * which accepts the specified volume. */
	@Nullable
	default FluidVolume getFirstInsertableVolume(@Nullable Direction direction, FluidVolume volume) {
		List<FluidVolume> volumes = getInsertableVolumes(direction, volume);
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	/** Returns the first volume matching the given predicate
	 * insertable through the specified direction which accepts the supplied volume. */
	@Nullable
	default FluidVolume getFirstInsertableVolume(Direction direction, FluidVolume volume, Predicate<FluidVolume> predicate) {
		List<FluidVolume> volumes = getInsertableVolumes(direction, volume, predicate);
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	/** Transfers all transferable content from this component
	 * to the target component. */
	default void into(FluidComponent target, Fraction count, Direction direction) {
		for (int sourceSlot = 0; sourceSlot < getSize(); ++sourceSlot) {
			FluidVolume sourceVolume = getVolume(sourceSlot);

			if (canExtract(direction.getOpposite(), sourceVolume, sourceSlot)) {
				for (int targetSlot = 0; targetSlot < target.getSize(); ++targetSlot) {
					FluidVolume targetVolume = target.getVolume(targetSlot);

					if (!sourceVolume.isEmpty() && count.biggerThan(Fraction.EMPTY)) {
						FluidVolume insertionVolume = sourceVolume.copy();
						insertionVolume.setAmount(minimum(count, insertionVolume.getAmount()));

						Fraction insertionCount = insertionVolume.getAmount();

						if (target.canInsert(direction, insertionVolume, targetSlot)) {
							Pair<FluidVolume, FluidVolume> merge = VolumeUtilities.merge(insertionVolume, targetVolume);

							sourceVolume.take(insertionCount.subtract(merge.getLeft().getAmount()));
							setVolume(sourceSlot, sourceVolume);
							target.setVolume(targetSlot, merge.getRight());
						}
					} else {
						break;
					}
				}
			}
		}
	}

	/** Asserts whether the given volume can be inserted through the specified
	 * direction into the supplied slot. */
	default boolean canInsert(@Nullable Direction direction, FluidVolume volume, int slot) {
		if (getVolume(slot) == null) {
			return false;
		} else {
			return volume.test(getVolume(slot));
		}
	}

	/** Asserts whether the given volume can be extracted through the specified
	 * direction from the supplied slot. */
	default boolean canExtract(@Nullable Direction direction, FluidVolume volume, int slot) {
		return true;
	}

	/* Returns the volume at the given slot. */
	default FluidVolume getVolume(int slot) {
		if (!getContents().containsKey(slot)) throw new ArrayIndexOutOfBoundsException("Slot " + slot + " not found in FluidComponent!");
		return getContents().get(slot);
	}

	/** Sets the volume at the given slot to the specified value,
	 * attaching a listener to it. */
	default void setVolume(int slot, FluidVolume volume) {
		getContents().put(slot, volume.withRunnable(this::updateListeners));

		updateListeners();
	}

	/** Removes the volume at the given slot, returning it. */
	default FluidVolume removeVolume(int slot) {
		FluidVolume volume = getVolume(slot);

		setVolume(slot, FluidVolume.ofEmpty());

		return volume;
	}

	/** Asserts whether this component's contents are all empty or not. */
	default boolean isEmpty() {
		return getContents().values().stream().allMatch(FluidVolume::isEmpty);
	}

	/** Asserts whether this component's contents are not all empty or not. */
	default boolean isNotEmpty() {
		return !isEmpty();
	}

	 /** Clears this component's contents. */
	default void clear() {
		this.getContents().forEach((slot, volume) -> {
			volume.setAmount(Fraction.EMPTY);
			volume.setFluid(Fluids.EMPTY);
		});
	}

	/** Serializes this {@link FluidComponent} to a {@link CompoundTag}. */
	@Override
	default void writeToNbt(CompoundTag tag) {
		ListTag listTag = new ListTag();

		for (int i = 0; i < getSize(); ++i) {
			FluidVolume volume = getVolume(i);

			listTag.add(i, volume.toTag());
		}

		CompoundTag dataTag = new CompoundTag();

		dataTag.putInt("size", getSize());
		dataTag.put("volumes", listTag);

		tag.put(AstromineComponents.FLUID_INVENTORY_COMPONENT.getId().toString(), dataTag);
	}

	/** Deserializes this {@link FluidComponent} from a {@link CompoundTag}. */
	@Override
	default void readFromNbt(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound(AstromineComponents.FLUID_INVENTORY_COMPONENT.getId().toString());

		int size = dataTag.getInt("size");

		ListTag volumesTag = dataTag.getList("volumes", 10);

		for (int i = 0; i < size; ++i) {
			CompoundTag volumeTag = volumesTag.getCompound(i);

			setVolume(i, FluidVolume.fromTag(volumeTag));
		}
	}

	/** Returns the {@link FluidComponent} of the given {@link V}. */
	@Nullable
	static <V> FluidComponent get(V v) {
		if (v instanceof ItemStack) {
			ItemStack stack = (ItemStack) v;
			Item item = stack.getItem();

			if (item instanceof BucketItem) {
				BucketItem bucket = (BucketItem) item;

				return SimpleFluidComponent.of(FluidVolume.of(Fraction.BUCKET, bucket.fluid));
			} else if (item instanceof PotionItem) {
				if(PotionUtil.getPotion(stack).equals(Potions.WATER))
					return SimpleFluidComponent.of(FluidVolume.of(Fraction.BOTTLE, Fluids.WATER));
			}
		}

		try {
			return AstromineComponents.FLUID_INVENTORY_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}

	/** Returns an iterator of this component's contents. */
	@Override
	default  Iterator<FluidVolume> iterator() {
		return getContents().values().iterator();
	}

	/** Applies the given action to all of this component's contents. */
	@Override
	default void forEach(Consumer<? super FluidVolume> action) {
		getContents().values().forEach(action);
	}

	/** Applies the given action to all of this component's contents. */
	default void forEachIndexed(BiConsumer<Integer, ? super FluidVolume> action) {
		getContents().forEach(action);
	}

	/** Returns the first volume in this component. */
	default FluidVolume getFirst() {
		return getVolume(0);
	}

	/** Sets the first volume in this component to the specified value. */
	default void setFirst(FluidVolume volume) {
		setVolume(0, volume);
	}

	/** Returns the second volume in this component. */
	default FluidVolume getSecond() {
		return getVolume(1);
	}

	/** Sets the second volume in this component to the specified value. */
	default void setSecond(FluidVolume volume) {
		setVolume(1, volume);
	}

	/** Returns the third volume in this component. */
	default FluidVolume getThird() {
		return getVolume(2);
	}

	/** Sets the third volume in this component to the specified value. */
	default void setThird(FluidVolume volume) {
		setVolume(2, volume);
	}

	/** Returns the fourth volume in this component. */
	default FluidVolume getFourth() {
		return getVolume(3);
	}

	/** Sets the fourth volume in this component to the specified value. */
	default void setFourth(FluidVolume volume) {
		setVolume(3, volume);
	}

	/** Returns the fifth volume in this component. */
	default FluidVolume getFifth() {
		return getVolume(4);
	}

	/** Sets the fifth volume in this component to the specified value. */
	default void setFifth(FluidVolume volume) {
		setVolume(4, volume);
	}

	/** Returns the sixth volume in this component. */
	default FluidVolume getSixth() {
		return getVolume(5);
	}

	/** Sets the sixth volume in this component to the specified value. */
	default void setSixth(FluidVolume volume) {
		setVolume(5, volume);
	}

	/** Returns the seventh volume in this component. */
	default FluidVolume getSeventh() {
		return getVolume(6);
	}

	/** Sets the seventh volume in this component to the specified value. */
	default void setSeventh(FluidVolume volume) {
		setVolume(6, volume);
	}

	/** Returns the eighth volume in this component. */
	default FluidVolume getEighth() {
		return getVolume(7);
	}

	/** Sets the eighth volume in this component to the specified value. */
	default void setEight(FluidVolume volume) {
		setVolume(7, volume);
	}

	/** Returns the ninth volume in this component. */
	default FluidVolume getNinth() {
		return getVolume(8);
	}

	/** Sets the ninth volume in this component to the specified value. */
	default void setNinth(FluidVolume volume) {
		setVolume(8, volume);
	}
}
