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

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.InventoryFromItemComponent;
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

public interface ItemComponent extends Iterable<Map.Entry<Integer, ItemStack>>, AutoSyncedComponent, NameableComponent {
	static ItemComponent of(int size) {
		return SimpleItemComponent.of(size);
	}

	static ItemComponent of(ItemStack... stacks) {
		return SimpleItemComponent.of(stacks);
	}

	static <V> ItemComponent get(V v) {
		try {
			return AstromineComponents.ITEM_INVENTORY_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}

	default Item getSymbol() {
		return AstromineItems.ITEM;
	}

	default TranslatableText getName() {
		return new TranslatableText("text.astromine.item");
	}

	List<Runnable> getListeners();

	default void addListener(Runnable listener) {
		this.getListeners().add(listener);
	}

	default void removeListener(Runnable listener) {
		this.getListeners().remove(listener);
	}

	default void updateListeners() {
		getListeners().forEach(Runnable::run);
	}

	default ItemComponent withListener(Consumer<ItemComponent> listener) {
		addListener(() -> listener.accept(this));
		return this;
	}

	Map<Integer, ItemStack> getContents();

	default List<ItemStack> getStacks(Predicate<ItemStack> predicate) {
		return getContents().values().stream().filter(predicate).collect(Collectors.toList());
	}

	default List<ItemStack> getExtractableStacks(Direction direction) {
		return getContents().entrySet().stream().filter((entry) -> canExtract(direction, entry.getValue(), entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	default List<ItemStack> getExtractableStacks(Direction direction, Predicate<ItemStack> predicate) {
		return getExtractableStacks(direction).stream().filter(predicate).collect(Collectors.toList());
	}

	default List<ItemStack> getInsertableStacks(Direction direction) {
		return getContents().entrySet().stream().filter((entry) -> canInsert(direction, entry.getValue(), entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	default List<ItemStack> getInsertableStacks(Direction direction, ItemStack Stack) {
		return getContents().entrySet().stream().filter((entry) -> canInsert(direction, Stack, entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	default List<ItemStack> getInsertableStacks(Direction direction, ItemStack Stack, Predicate<ItemStack> predicate) {
		return getInsertableStacks(direction, Stack).stream().filter(predicate).collect(Collectors.toList());
	}

	@Nullable
	default ItemStack getFirstExtractableStack(Direction direction) {
		List<ItemStack> Stacks = getExtractableStacks(direction);
		if (!Stacks.isEmpty())
			return Stacks.get(0);
		else return null;
	}

	@Nullable
	default ItemStack getFirstExtractableStack(Direction direction, Predicate<ItemStack> predicate) {
		List<ItemStack> Stacks = getExtractableStacks(direction, predicate);
		if (!Stacks.isEmpty())
			return Stacks.get(0);
		else return null;
	}

	@Nullable
	default ItemStack getFirstInsertableStack(Direction direction, ItemStack Stack) {
		List<ItemStack> Stacks = getInsertableStacks(direction, Stack);
		if (!Stacks.isEmpty())
			return Stacks.get(0);
		else return null;
	}

	@Nullable
	default ItemStack getFirstInsertableStack(Direction direction, ItemStack Stack, Predicate<ItemStack> predicate) {
		List<ItemStack> Stacks = getInsertableStacks(direction, Stack, predicate);
		if (!Stacks.isEmpty())
			return Stacks.get(0);
		else return null;
	}

	default boolean canInsert(@Nullable Direction direction, ItemStack stack, int slot) {
		return getStack(slot).isEmpty() || (ItemStack.areItemsEqual(stack, getStack(slot)) && ItemStack.areTagsEqual(stack, getStack(slot)) && getStack(slot).getMaxCount() - getStack(slot).getCount() >= stack.getCount());
	}

	default boolean canExtract(Direction direction, ItemStack stack, int slot) {
		return true;
	}

	default ItemStack getStack(int slot) {
		return getContents().getOrDefault(slot, ItemStack.EMPTY);
	}

	default void setStack(int slot, ItemStack stack) {
		getContents().put(slot, stack);

		updateListeners();
	}

	default ItemStack removeStack(int slot) {
		ItemStack stack = getContents().remove(slot);

		updateListeners();

		return stack;
	}

	int getSize();

	@Override
	default void writeToNbt(CompoundTag tag) {
		ListTag listTag = new ListTag();

		for (int i = 0; i < getSize(); ++i) {
			ItemStack stack = getStack(i);

			listTag.add(i, stack.toTag(new CompoundTag()));
		}

		CompoundTag dataTag = new CompoundTag();

		dataTag.putInt("size", getSize());
		dataTag.put("stacks", listTag);

		tag.put(AstromineComponents.ITEM_INVENTORY_COMPONENT.getId().toString(), dataTag);
	}

	default void readFromNbt(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound(AstromineComponents.ITEM_INVENTORY_COMPONENT.getId().toString());

		int size = dataTag.getInt("size");

		ListTag stacksTag = dataTag.getList("stacks", 10);

		for (int i = 0; i < size; ++i) {
			CompoundTag stackTag = stacksTag.getCompound(i);

			setStack(i, ItemStack.fromTag(stackTag));
		}
	}

	default void clear() {
		getContents().forEach((slot, stack) -> setStack(slot, ItemStack.EMPTY));
	}

	default boolean isEmpty() {
		return getContents().values().stream().allMatch(ItemStack::isEmpty);
	}

	default boolean isNotEmpty() {
		return !isEmpty();
	}

	default ItemStack getFirst() {
		return getStack(0);
	}

	default void setFirst(ItemStack Stack) {
		setStack(0, Stack);
	}

	default ItemStack getSecond() {
		return getStack(1);
	}

	default void setSecond(ItemStack Stack) {
		setStack(1, Stack);
	}

	default ItemStack getThird() {
		return getStack(2);
	}

	default void setThird(ItemStack Stack) {
		setStack(2, Stack);
	}

	default ItemStack getFourth() {
		return getStack(3);
	}

	default void setFourth(ItemStack Stack) {
		setStack(3, Stack);
	}

	default ItemStack getFifth() {
		return getStack(4);
	}

	default void setFifth(ItemStack Stack) {
		setStack(4, Stack);
	}

	default ItemStack getSixth() {
		return getStack(5);
	}

	default void setSixth(ItemStack Stack) {
		setStack(5, Stack);
	}

	default ItemStack getSeventh() {
		return getStack(6);
	}

	default void setSeventh(ItemStack Stack) {
		setStack(6, Stack);
	}

	default ItemStack getEighth() {
		return getStack(7);
	}

	default void setEight(ItemStack Stack) {
		setStack(7, Stack);
	}

	default ItemStack getNinth() {
		return getStack(8);
	}

	default void setNinth(ItemStack Stack) {
		setStack(8, Stack);
	}

	default Inventory asInventory() {
		return InventoryFromItemComponent.of(this);
	}

	@Override
	default void forEach(Consumer<? super Map.Entry<Integer, ItemStack>> action) {
		getContents().entrySet().forEach(action);
	}

	@Override
	default @NotNull Iterator<Map.Entry<Integer, ItemStack>> iterator() {
		return getContents().entrySet().iterator();
	}
}
