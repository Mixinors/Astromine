package com.github.chainmailstudios.astromine.common.component;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import org.apache.logging.log4j.Level;

import java.util.Map;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface ItemInventoryComponent extends Component {
	/**
	 * Retrieves contents of this inventory that match a specific predicate as a collection as ItemStack copies.
	 *
	 * @param predicate the specified predicate.
	 * @return the retrieved collection.
	 */
	default Collection<ItemStack> getContentsMatching(Predicate<ItemStack> predicate) {
		return this.getItemContents().values().stream().filter(predicate).collect(Collectors.toList());
	}

	/**
	 * Retrieves contents of this inventory as a collection of the held ItemStack copies.
	 *
	 * @return the retrieved collection.
	 */
	Map<Integer, ItemStack> getItemContents();

	/**
	 * Retrieves contents of this inventory that match a specific predicate as a collection as ItemStack copies.
	 *
	 * @param predicate the specified predicate.
	 * @return the retrieved collection.
	 */
	default Collection<ItemStack> getContentsMatchingSimulated(Predicate<ItemStack> predicate) {
		return this.getContentsSimulated().stream().map(ItemStack::copy).filter(predicate).collect(Collectors.toList());
	}

	/**
	 * Retrieves contents of this inventory as a collection of the held ItemStack copies.
	 *
	 * @return the retrieved collection.
	 */
	default Collection<ItemStack> getContentsSimulated() {
		return this.getItemContents().values().stream().map(ItemStack::copy).collect(Collectors.toList());
	}

	default ActionResult canInsert()  {
		return ActionResult.SUCCESS;
	}

	default ActionResult canInsert(int slot) {
		return ActionResult.SUCCESS;
	}

	default ActionResult canInsert(ItemStack stack) {
		return ActionResult.SUCCESS;
	}

	default ActionResult canInsert(ItemStack stack, int slot) {
		return ActionResult.SUCCESS;
	}

	default ActionResult canExtract() {
		return ActionResult.SUCCESS;
	}

	default ActionResult canExtract(int slot) {
		return ActionResult.SUCCESS;
	}

	default ActionResult canExtract(ItemStack stack) {
		return ActionResult.SUCCESS;
	}

	default ActionResult canExtract(ItemStack stack, int slot) {
		return ActionResult.SUCCESS;
	}

	/**
	 * Inserts a specific ItemStack into this inventory if possible, from a generic, non-existent position.
	 *
	 * @param stack the specified stack.
	 * @return SUCCESS w. empty if inserted; FAIL w. stack if not.
	 */
	default TypedActionResult<ItemStack> insert(ItemStack stack) {
		if (this.canInsert(stack).isAccepted()) {
			return this.insert(stack, stack.getCount());
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, stack);
		}
	}

	/**
	 * Inserts a specific ItemStack into this inventory if possible, from a generic, non-existent position, the count inserted depending on the specified count.
	 *
	 * @param stack the specified stack.
	 * @param count the specified count.
	 * @return SUCCESS w. modified stack if inserted; FAIL w. unmodified stack if not.
	 */
	default TypedActionResult<ItemStack> insert(ItemStack stack, int count) {
		ItemStack finalStack = stack;
		Optional<Map.Entry<Integer, ItemStack>> matchingStackOptional = this.getItemContents().entrySet().stream().filter(entry -> {
			ItemStack storedStack = entry.getValue();

			return ((storedStack.getItem() == finalStack.getItem() && storedStack.getMaxCount() - storedStack.getCount() >= count && (!storedStack.hasTag() && !finalStack.hasTag()) || (storedStack.hasTag() && finalStack.hasTag() && storedStack.getTag()
			                                                                                                                                                                                                                                       .equals(finalStack
					                                                                                                                                                                                                                                               .getTag()))) || storedStack
							                                                                                                                                                                                                                                                               .isEmpty()) && this.canInsert(
					finalStack).isAccepted();
		}).findFirst();

		if (matchingStackOptional.isPresent() && matchingStackOptional.get().getValue().getMaxCount() - stack.getCount() >= count) {
			ItemStack matchingStack = matchingStackOptional.get().getValue();
			if (matchingStack.isEmpty()) {
				matchingStack = stack.copy();
				stack = ItemStack.EMPTY;
				this.setStack(matchingStackOptional.get().getKey(), matchingStack);
			} else {
				matchingStack.increment(stack.getCount());
				stack.decrement(stack.getCount());
			}
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, stack);
		}
	}

	/**
	 * Sets an ItemStack in a given slot.
	 */
	default void setStack(int slot, ItemStack stack) {
		if (slot <= this.getItemSize()) {
			this.getItemContents().put(slot, stack);
			this.dispatchConsumers();
		}
	}

	/**
	 * Retrieves the inventory's size.
	 */
	int getItemSize();

	/**
	 * Dispatches updates to all listeners.
	 */
	default void dispatchConsumers() {
		this.getItemListeners().forEach(Runnable::run);
	}

	/**
	 * Retrieves all listeners listening to this inventory.
	 *
	 * @return
	 */
	List<Runnable> getItemListeners();

	/**
	 * Extracts the contents of this inventory that match a given predicate as a collection, from a generic non-existent position.
	 *
	 * @param predicate the specified predicate.
	 * @return SUCCESS w. the retrieved collection if extracted anything; FAIL w. empty if not.
	 */
	default TypedActionResult<Collection<ItemStack>> extractMatching(Predicate<ItemStack> predicate) {
		HashSet<ItemStack> extractedStacks = new HashSet<>();
		this.getItemContents().forEach((slot, stack) -> {
			if (predicate.test(stack)) {
				TypedActionResult<ItemStack> extractionResult = this.extract(slot);

				if (extractionResult.getResult().isAccepted()) {
					extractedStacks.add(extractionResult.getValue());
				}
			}
		});

		if (!extractedStacks.isEmpty()) {
			return new TypedActionResult<>(ActionResult.SUCCESS, extractedStacks);
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, extractedStacks);
		}
	}

	/**
	 * Extracts a specific ItemStack from this inventory if possible, from a generic, non-existent position.
	 *
	 * @param slot the slot of the specified stack.
	 * @return SUCCESS w. stack if extracted; FAIL w. empty if not.
	 */
	default TypedActionResult<ItemStack> extract(int slot) {
		ItemStack matchingStack = this.getStack(slot);

		if (!matchingStack.isEmpty() && this.canExtract(matchingStack).isAccepted()) {
			return this.extract(slot, matchingStack.getCount());
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, ItemStack.EMPTY);
		}
	}

	/**
	 * Retrieves an ItemStack from a given slot.
	 */
	default ItemStack getStack(int slot) {
		return this.getItemContents().get(slot);
	}

	/**
	 * Extracts a specific ItemStack from this inventory if possible, from a generic non-existent position, the count extracted depending on the specified count.
	 *
	 * @param slot the slot of the specified stack.
	 * @param count the specified count.
	 * @return SUCCESS w. stack if extracted; FAIL w. empty if not.
	 */
	default TypedActionResult<ItemStack> extract(int slot, int count) {
		Optional<ItemStack> matchingStackOptional = Optional.ofNullable(this.getStack(slot));

		if (matchingStackOptional.isPresent()) {
			if (matchingStackOptional.get().getCount() >= count) {
				ItemStack matchingStack = matchingStackOptional.get();
				ItemStack remainingStack = matchingStack.copy();
				remainingStack.decrement(count);
				matchingStack.setCount(count);
				this.setStack(slot, remainingStack);
				return new TypedActionResult<>(ActionResult.SUCCESS, matchingStack);
			} else {
				return new TypedActionResult<>(ActionResult.FAIL, ItemStack.EMPTY);
			}
		} else {
			return new TypedActionResult<>(ActionResult.FAIL, ItemStack.EMPTY);
		}
	}

	/**
	 * Serializes this inventory to a new CompoundTag. If a subtag is passed, contents are serialized to it. If a range is passed, only contents in slots within the range are serialized. If a subtag is passed, the inventory is written to the subtag.
	 *
	 * @param source the specified source.
	 * @param subtag the optional subtag.
	 * @param range the optional range.
	 */
	default CompoundTag write(ItemInventoryComponent source, Optional<String> subtag, Optional<Range<Integer>> range) {
		CompoundTag tag = new CompoundTag();
		this.write(source, tag, subtag, range);
		return tag;
	}

	/**
	 * Serializes this inventory to an existing CompoundTag. If a subtag is passed, contents are serialized to it. If a range is passed, only contents in slots within the range are serialized. If a subtag is specified, the inventory is written to the
	 * subtag.
	 *
	 * @param tag the specified tag.
	 * @param subtag the optional subtag.
	 * @param range the optional range.
	 */
	default void write(ItemInventoryComponent source, CompoundTag tag, Optional<String> subtag, Optional<Range<Integer>> range) {
		if (source == null || source.getItemSize() <= 0) {
			return;
		}

		if (tag == null) {
			return;
		}

		CompoundTag stacksTag = new CompoundTag();

		int minimum = range.isPresent() ? range.get().getMinimum() : 0;
		int maximum = range.isPresent() ? range.get().getMaximum() : source.getItemSize();

		for (int position = minimum; position < maximum; ++position) {
			if (source.getStack(position) != null && source.getStack(position) != ItemStack.EMPTY) {
				ItemStack stack = source.getStack(position);

				if (stack != null && !stack.isEmpty()) {
					CompoundTag stackTag = source.getStack(position).toTag(new CompoundTag());

					if (stackTag.isEmpty()) {
						stacksTag.put(String.valueOf(position), stackTag);
					}
				}
			}
		}

		if (subtag.isPresent()) {
			CompoundTag inventoryTag = new CompoundTag();

			inventoryTag.putInt("size", source.getItemSize());
			inventoryTag.put("stacks", stacksTag);

			tag.put(subtag.get(), inventoryTag);
		} else {
			tag.putInt("size", source.getItemSize());
			tag.put("stacks", stacksTag);
		}
	}

	/**
	 * Deserializes a CompoundTag to an existing inventory. If a range is passed, contents are only deserialized for the given range. If a subtag is passed, the inventory is read form the subtag.
	 *
	 * @param target the specified target.
	 * @param tag the specified tag.
	 * @param subtag the optional subtag.
	 * @param range the optional range.
	 */
	default void read(ItemInventoryComponent target, CompoundTag tag, Optional<String> subtag, Optional<Range<Integer>> range) {
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

		if (!compoundTag.contains("stacks")) {
			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + CompoundTag.class.getName() + " does not contain 'stacks' subtag!");
			return;
		}

		Tag rawStacksTag = compoundTag.get("stacks");

		if (!(rawStacksTag instanceof CompoundTag)) {
			AstromineCommon.LOGGER.log(Level.ERROR, "Inventory contents failed to be read: " + rawStacksTag.getClass().getName() + " is not instance of " + CompoundTag.class.getName() + "!");
			return;
		}

		CompoundTag stacksTag = (CompoundTag) rawStacksTag;

		int minimum = range.isPresent() ? range.get().getMinimum() : 0;
		int maximum = range.isPresent() ? range.get().getMaximum() : target.getItemSize();

		if (size < maximum) {
			AstromineCommon.LOGGER.log(Level.WARN, "Inventory size from tag smaller than specified maximum: will continue reading!");
			maximum = size;
		}

		if (target.getItemSize() < maximum) {
			AstromineCommon.LOGGER.log(Level.WARN, "Inventory size from target smaller than specified maximum: will continue reading!");
			maximum = target.getItemSize();
		}

		for (int position = minimum; position < maximum; ++position) {
			if (stacksTag.contains(String.valueOf(position))) {
				Tag rawStackTag = stacksTag.get(String.valueOf(position));

				if (!(rawStackTag instanceof CompoundTag)) {
					AstromineCommon.LOGGER.log(Level.ERROR, "Inventory stack skipped: stored tag not instance of " + CompoundTag.class.getName() + "!");
					return;
				}

				CompoundTag stackTag = (CompoundTag) rawStackTag;

				ItemStack stack = ItemStack.fromTag(stackTag);

				if (target.getItemSize() >= position) {
					target.setStack(position, stack);
				}
			}
		}
	}

	/**
	 * Adds a listener to this inventory.
	 *
	 * @param listener the specified listener.
	 */
	default void addListener(Runnable listener) {
		this.getItemListeners().add(listener);
	}

	/**
	 * Removes a listener from this inventory.
	 *
	 * @param listener the specified listener.
	 */
	default void removeListener(Runnable listener) {
		this.getItemListeners().remove(listener);
	}

	/**
	 * Retrieves the maximum stack size for a given slot.
	 */
	default int getMaximumCount(int slot) {
		return 64;
	}

	/**
	 * Clears this inventory.
	 */
	default void clear() {
		this.getItemContents().clear();
	}

	/**
	 * Asserts whether this inventory is empty or not.
	 */
	default boolean isEmpty() {
		return this.getItemContents().values().stream().allMatch(ItemStack::isEmpty);
	}
}
