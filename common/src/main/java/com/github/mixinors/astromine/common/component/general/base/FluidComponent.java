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

package com.github.mixinors.astromine.common.component.general.base;

import com.github.mixinors.astromine.common.component.block.entity.TransferComponent;
import com.github.mixinors.astromine.common.component.general.miscellaneous.NamedComponent;
import com.github.mixinors.astromine.common.component.general.provider.FluidComponentProvider;
import com.github.mixinors.astromine.common.util.VolumeUtils;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.mixin.common.BucketItemAccessor;
import com.github.mixinors.astromine.registry.common.AMItems;
import me.shedaniel.architectury.utils.NbtType;
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
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Math.min;

/**
 * A {@link NamedComponent} representing a fluid reserve.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag(CompoundTag)} and {@link #fromTag(CompoundTag)}.
 */
public interface FluidComponent extends Iterable<FluidVolume>, NamedComponent {
	/** Instantiates a {@link FluidComponent}. */
	static FluidComponent of(int size) {
		return new SimpleFluidComponent(size);
	}
	
	/** Instantiates a {@link FluidComponent}. */
	static FluidComponent of(FluidVolume... volumes) {
		return new SimpleFluidComponent(volumes);
	}
	
	/** Instantiates a {@link FluidComponent} associated with a {@link TransferComponent} provider. */
	static <V> FluidComponent of(V v, int size) {
		return new SimpleDirectionalFluidComponent(v, size);
	}
	
	/** Instantiates a {@link FluidComponent} associated with a {@link TransferComponent} provider. */
	public static <V> FluidComponent of(V v, FluidVolume... volumes) {
		return new SimpleDirectionalFluidComponent(v, volumes);
	}
	
	/** Instantiates a {@link FluidComponent} with automatic synchronization. */
	static FluidComponent ofSynced(int size) {
		return new SimpleAutoSyncedFluidComponent(size);
	}
	
	/** Instantiates a {@link FluidComponent} with automatic synchronization. */
	static FluidComponent ofSynced(FluidVolume... volumes) {
		return new SimpleAutoSyncedFluidComponent(volumes);
	}
	
	/** Instantiates a {@link FluidComponent} associated with a {@link TransferComponent} provider,
	 *  with automatic synchronization. */
	static <V> FluidComponent ofSynced(V v, int size) {
		return new SimpleAutoSyncedDirectionalFluidComponent(v, size);
	}
	
	/** Instantiates a {@link FluidComponent} associated with a {@link TransferComponent} provider,
	 *  with automatic synchronization. */
	static <V> FluidComponent ofSynced(V v, FluidVolume... volumes) {
		return new SimpleAutoSyncedDirectionalFluidComponent(v, volumes);
	}

	/** Returns this component's {@link Item} symbol. */
	default Item getSymbol() {
		return AMItems.FLUID.get().asItem();
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
		var volumes = getExtractableVolumes(direction);
		
		volumes.removeIf(FluidVolume::isEmpty);
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	/** Returns the first volume matching the given predicate
	 * extractable through the specified direction. */
	@Nullable
	default FluidVolume getFirstExtractableVolume(@Nullable Direction direction, Predicate<FluidVolume> predicate) {
		var volumes = getExtractableVolumes(direction, predicate);
		
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	/** Returns the first volume insertable through the given direction
	 * which accepts the specified volume. */
	@Nullable
	default FluidVolume getFirstInsertableVolume(@Nullable Direction direction, FluidVolume volume) {
		var volumes = getInsertableVolumes(direction, volume);
		
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	/** Returns the first volume matching the given predicate
	 * insertable through the specified direction which accepts the supplied volume. */
	@Nullable
	default FluidVolume getFirstInsertableVolume(Direction direction, FluidVolume volume, Predicate<FluidVolume> predicate) {
		var volumes = getInsertableVolumes(direction, volume, predicate);
		
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	/** Transfers all transferable content from this component
	 * to the target component. */
	default void into(FluidComponent target, long count, Direction extractionDirection, Direction insertionDirection) {
		for (var sourceSlot = 0; sourceSlot < getSize(); ++sourceSlot) {
			var sourceVolume = getVolume(sourceSlot);

			if (canExtract(extractionDirection, sourceVolume, sourceSlot)) {
				for (var targetSlot = 0; targetSlot < target.getSize(); ++targetSlot) {
					var targetVolume = target.getVolume(targetSlot);

					if (!sourceVolume.isEmpty() && count > 0) {
						var insertionVolume = (FluidVolume) sourceVolume.copy();
						insertionVolume.setAmount(min(min(count, insertionVolume.getAmount()), targetVolume.getSize() - targetVolume.getAmount()));

						var insertionCount = insertionVolume.getAmount();

						if (target.canInsert(insertionDirection, insertionVolume, targetSlot)) {
							var merge = VolumeUtils.merge(insertionVolume, targetVolume);

							sourceVolume.take(insertionCount - merge.getLeft().getAmount());
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

	/** Transfers all transferable content from this component
	 * to the target component. */
	default void into(FluidComponent target, long count, Direction direction) {
		into(target, count, direction, direction.getOpposite());
	}

	/** Transfers all transferable content from this component
	 * to the target component. */
	default void into(FluidComponent target, long count) {
		into(target, count, null, null);
	}

	/** Transfers all transferable content from this component
	 * to the target component. */
	default void into(FluidComponent target) {
		into(target, FluidVolume.BUCKET, null, null);
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
		var volume = getVolume(slot);

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
			volume.setAmount(0L);
			volume.setFluid(Fluids.EMPTY);
		});
	}

	/** Serializes this {@link FluidComponent} to a {@link CompoundTag}. */
	@Override
	default void toTag(CompoundTag tag) {
		var listTag = new ListTag();

		for (var i = 0; i < getSize(); ++i) {
			FluidVolume volume = getVolume(i);

			listTag.add(i, volume.toTag());
		}

		var dataTag = new CompoundTag();

		dataTag.putInt("Size", getSize());
		dataTag.put("Volumes", listTag);

		tag.put("FluidComponent", dataTag);
	}

	/** Deserializes this {@link FluidComponent} from a {@link CompoundTag}. */
	@Override
	default void fromTag(CompoundTag tag) {
		var dataTag = tag.getCompound("FluidComponent");

		var size = dataTag.getInt("Size");

		var volumesTag = dataTag.getList("Volumes", NbtType.COMPOUND);

		for (var i = 0; i < size; ++i) {
			var volumeTag = volumesTag.getCompound(i);

			setVolume(i, FluidVolume.fromTag(volumeTag));
		}
	}

	/** Returns the {@link FluidComponent} of the given {@link V}. */
	@Nullable
	static <V> FluidComponent get(V v) {
		if (v instanceof FluidComponentProvider) {
			return ((FluidComponentProvider) v).getFluidComponent();
		} else if (v instanceof ItemStack stack) {
			var item = stack.getItem();

			if (item instanceof BucketItem bucket) {
				return of(FluidVolume.of(FluidVolume.BUCKET, ((BucketItemAccessor) bucket).getFluid()));
			} else if (item instanceof PotionItem) {
				if (PotionUtil.getPotion(stack).equals(Potions.WATER))
					return of(FluidVolume.of(FluidVolume.BOTTLE, Fluids.WATER));
			}
		}
		
		return null;
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
