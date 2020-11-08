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

import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface FluidComponent extends Iterable<Map.Entry<Integer, FluidVolume>>, NameableComponent, AutoSyncedComponent {
	static FluidComponent of(int size) {
		return SimpleFluidComponent.of(size);
	}

	static FluidComponent of(FluidVolume... volumes) {
		return SimpleFluidComponent.of(volumes);
	}

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

	default Item getSymbol() {
		return AstromineItems.FLUID.asItem();
	}

	default TranslatableText getName() {
		return new TranslatableText("text.astromine.fluid");
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

	default FluidComponent withListener(Consumer<FluidComponent> listener) {
		addListener(() -> listener.accept(this));
		return this;
	}

	Map<Integer, FluidVolume> getContents();

	default List<FluidVolume> getVolumes(Predicate<FluidVolume> predicate) {
		return getContents().values().stream().filter(predicate).collect(Collectors.toList());
	}

	default List<FluidVolume> getExtractableVolumes(Direction direction) {
		return getContents().entrySet().stream().filter((entry) -> canExtract(direction, entry.getValue(), entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	default List<FluidVolume> getExtractableVolumes(Direction direction, Predicate<FluidVolume> predicate) {
		return getExtractableVolumes(direction).stream().filter(predicate).collect(Collectors.toList());
	}

	default List<FluidVolume> getInsertableVolumes(Direction direction) {
		return getContents().entrySet().stream().filter((entry) -> canInsert(direction, entry.getValue(), entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	default List<FluidVolume> getInsertableVolumes(Direction direction, FluidVolume volume) {
		return getContents().entrySet().stream().filter((entry) -> canInsert(direction, volume, entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	default List<FluidVolume> getInsertableVolumes(Direction direction, FluidVolume volume, Predicate<FluidVolume> predicate) {
		return getInsertableVolumes(direction, volume).stream().filter(predicate).collect(Collectors.toList());
	}

	@Nullable
	default FluidVolume getFirstExtractableVolume(Direction direction) {
		List<FluidVolume> volumes = getExtractableVolumes(direction);
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	@Nullable
	default FluidVolume getFirstExtractableVolume(Direction direction, Predicate<FluidVolume> predicate) {
		List<FluidVolume> volumes = getExtractableVolumes(direction, predicate);
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	@Nullable
	default FluidVolume getFirstInsertableVolume(Direction direction, FluidVolume volume) {
		List<FluidVolume> volumes = getInsertableVolumes(direction, volume);
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	@Nullable
	default FluidVolume getFirstInsertableVolume(Direction direction, FluidVolume volume, Predicate<FluidVolume> predicate) {
		List<FluidVolume> volumes = getInsertableVolumes(direction, volume, predicate);
		if (!volumes.isEmpty())
			return volumes.get(0);
		else return null;
	}

	default boolean canInsert(@Nullable Direction direction, FluidVolume volume, int slot) {
		return volume.test(getVolume(slot));
	}

	default boolean canExtract(@Nullable Direction direction, FluidVolume volume, int slot) {
		return true;
	}

	@Nullable
	default FluidVolume getVolume(int slot) {
		return getContents().getOrDefault(slot, null);
	}

	default void setVolume(int slot, FluidVolume volume) {
		getContents().put(slot, volume.withRunnable(this::updateListeners));

		updateListeners();
	}

	int getSize();

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

	default void clear() {
		this.getContents().clear();
	}

	default boolean isEmpty() {
		return getContents().values().stream().allMatch(FluidVolume::isEmpty);
	}

	default boolean isNotEmpty() {
		return !isEmpty();
	}

	default FluidVolume getFirst() {
		return getVolume(0);
	}

	default void setFirst(FluidVolume volume) {
		setVolume(0, volume);
	}

	default FluidVolume getSecond() {
		return getVolume(1);
	}

	default void setSecond(FluidVolume volume) {
		setVolume(1, volume);
	}

	default FluidVolume getThird() {
		return getVolume(2);
	}

	default void setThird(FluidVolume volume) {
		setVolume(2, volume);
	}

	default FluidVolume getFourth() {
		return getVolume(3);
	}

	default void setFourth(FluidVolume volume) {
		setVolume(3, volume);
	}

	default FluidVolume getFifth() {
		return getVolume(4);
	}

	default void setFifth(FluidVolume volume) {
		setVolume(4, volume);
	}

	default FluidVolume getSixth() {
		return getVolume(5);
	}

	default void setSixth(FluidVolume volume) {
		setVolume(5, volume);
	}

	default FluidVolume getSeventh() {
		return getVolume(6);
	}

	default void setSeventh(FluidVolume volume) {
		setVolume(6, volume);
	}

	default FluidVolume getEighth() {
		return getVolume(7);
	}

	default void setEight(FluidVolume volume) {
		setVolume(7, volume);
	}

	default FluidVolume getNinth() {
		return getVolume(8);
	}

	default void setNinth(FluidVolume volume) {
		setVolume(8, volume);
	}

	@Override
	default void forEach(Consumer<? super Map.Entry<Integer, FluidVolume>> action) {
		getContents().entrySet().forEach(action);
	}

	@Override
	default @NotNull Iterator<Map.Entry<Integer, FluidVolume>> iterator() {
		return getContents().entrySet().iterator();
	}
}
