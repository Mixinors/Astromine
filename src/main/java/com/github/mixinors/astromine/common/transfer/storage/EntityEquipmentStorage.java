package com.github.mixinors.astromine.common.transfer.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.MapMaker;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.fabricmc.fabric.impl.transfer.TransferApiImpl;

public class EntityEquipmentStorage extends CombinedStorage<ItemVariant, SingleSlotStorage<ItemVariant>> {
	private static final int ARMOR_INVENTORY_SIZE = 4;
	private static final int HELD_ITEMS_INVENTORY_SIZE = 2;
	private static final int INVENTORY_SIZE = ARMOR_INVENTORY_SIZE + HELD_ITEMS_INVENTORY_SIZE;
	private static final Map<MobEntity, EntityEquipmentStorage> WRAPPERS = new MapMaker().weakValues().makeMap();
	private final DroppedStacks droppedStacks;

	final MobEntity mobEntity;
	/**
	 * This {@code backingList} is the real list of wrappers.
	 * The {@code parts} in the superclass is the public-facing unmodifiable sublist with exactly the right amount of slots.
	 */
	final List<SimpleStackWrapper> backingList;

	EntityEquipmentStorage(MobEntity entity) {
		super(Collections.emptyList());
		this.mobEntity = entity;
		this.backingList = new ArrayList<>();
		this.droppedStacks = new DroppedStacks();
	}

	public static EntityEquipmentStorage of(MobEntity entity) {
		EntityEquipmentStorage storage = WRAPPERS.computeIfAbsent(entity, EntityEquipmentStorage::new);
		storage.updateSlotList();
		return storage;
	}

	public List<SingleSlotStorage<ItemVariant>> getSlots() {
		return parts;
	}

	/**
	 * Resize slot list to match the current size of the inventory.
	 */
	private void updateSlotList() {
		if (INVENTORY_SIZE != parts.size()) {
			backingList.clear();

			backingList.add(new SimpleStackWrapper(this, EquipmentSlot.MAINHAND));
			backingList.add(new SimpleStackWrapper(this, EquipmentSlot.FEET));
			backingList.add(new SimpleStackWrapper(this, EquipmentSlot.LEGS));
			backingList.add(new SimpleStackWrapper(this, EquipmentSlot.CHEST));
			backingList.add(new SimpleStackWrapper(this, EquipmentSlot.HEAD));
			backingList.add(new SimpleStackWrapper(this, EquipmentSlot.OFFHAND));

			parts = Collections.unmodifiableList(backingList);
		}
	}

	public SingleSlotStorage<ItemVariant> getHandSlot(Hand hand) {
		return switch(hand) {
			case MAIN_HAND -> getSlot(EquipmentSlot.MAINHAND);
			case OFF_HAND -> getSlot(EquipmentSlot.OFFHAND);
		};
	}

	public SingleSlotStorage<ItemVariant> getSlot(int slot) {
		return getSlots().get(slot);
	}

	public SingleSlotStorage<ItemVariant> getSlot(EquipmentSlot slot) {
		return getSlots().get(slot.getArmorStandSlotId());
	}

	public void offerOrDrop(ItemVariant variant, long amount, TransactionContext transaction) {
		long offered = offer(variant, amount, transaction);
		drop(variant, amount - offered, transaction);
	}

	@Override
	public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		return offer(resource, maxAmount, transaction);
	}

	public long offer(ItemVariant variant, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(variant, maxAmount);
		long initialAmount = maxAmount;

		// Stack into the main stack first and the offhand stack second.
		for (Hand hand : Hand.values()) {
			SingleSlotStorage<ItemVariant> handSlot = getHandSlot(hand);

			if (handSlot.getResource().equals(variant)) {
				maxAmount -= handSlot.insert(variant, maxAmount, transaction);

				if (maxAmount == 0) return initialAmount;
			}
		}

		return initialAmount - maxAmount;
	}

	public void drop(ItemVariant variant, long amount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(variant, amount);

		// Drop in the world on the server side (will be synced by the game with the client).
		// Dropping items is server-side only because it involves randomness.
		if (amount > 0 && !mobEntity.world.isClient()) {
			droppedStacks.addDrop(variant, amount, transaction);
		}
	}

	static class SimpleStackWrapper extends SingleStackStorage {
		private final EntityEquipmentStorage storage;
		final EquipmentSlot slot;
		private ItemStack lastReleasedSnapshot = null;

		SimpleStackWrapper(EntityEquipmentStorage storage, EquipmentSlot slot) {
			this.storage = storage;
			this.slot = slot;
		}

		@Override
		protected ItemStack getStack() {
			return storage.mobEntity.getEquippedStack(slot);
		}

		@Override
		protected void setStack(ItemStack stack) {
			TransferApiImpl.SUPPRESS_SPECIAL_LOGIC.set(Boolean.TRUE);

			try {
				storage.mobEntity.equipStack(slot, stack);
			} finally {
				TransferApiImpl.SUPPRESS_SPECIAL_LOGIC.remove();
			}
		}

		@Override
		protected boolean canInsert(ItemVariant itemVariant) {
			ItemStack stack = itemVariant.toStack();
			if(slot.getType() == EquipmentSlot.Type.ARMOR && !slot.equals(MobEntity.getPreferredEquipmentSlot(stack))) return false;
			return storage.mobEntity.canEquip(itemVariant.toStack());
		}

		@Override
		protected void releaseSnapshot(ItemStack snapshot) {
			lastReleasedSnapshot = snapshot;
		}

		@Override
		protected void onFinalCommit() {
			// Try to apply the change to the original stack
			ItemStack original = lastReleasedSnapshot;
			ItemStack currentStack = getStack();

			if (!original.isEmpty() && original.getItem() == currentStack.getItem()) {
				// None is empty and the items match: just update the amount and NBT, and reuse the original stack.
				original.setCount(currentStack.getCount());
				original.setNbt(currentStack.hasNbt() ? currentStack.getNbt().copy() : null);
				setStack(original);
			} else {
				// Otherwise assume everything was taken from original so empty it.
				original.setCount(0);
			}
		}
	}

	private class DroppedStacks extends SnapshotParticipant<Integer> {
		final List<DroppedStacks.Entry> entries = new ArrayList<>();

		void addDrop(ItemVariant key, long amount, TransactionContext transaction) {
			updateSnapshots(transaction);
			entries.add(new DroppedStacks.Entry(key, amount));
		}

		@Override
		protected Integer createSnapshot() {
			return entries.size();
		}

		@Override
		protected void readSnapshot(Integer snapshot) {
			// effectively cancel dropping the stacks
			int previousSize = snapshot;

			while (entries.size() > previousSize) {
				entries.remove(entries.size() - 1);
			}
		}

		@Override
		protected void onFinalCommit() {
			// actually drop the stacks
			for (DroppedStacks.Entry entry : entries) {
				long remainder = entry.amount;

				while (remainder > 0) {
					int dropped = (int) Math.min(entry.key.getItem().getMaxCount(), remainder);
					mobEntity.dropStack(entry.key.toStack(dropped));
					remainder -= dropped;
				}
			}

			entries.clear();
		}

		private record Entry(ItemVariant key, long amount) {
		}
	}
}
