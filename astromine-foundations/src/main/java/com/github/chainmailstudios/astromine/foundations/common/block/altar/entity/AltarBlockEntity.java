package com.github.chainmailstudios.astromine.foundations.common.block.altar.entity;

import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.foundations.common.recipe.AltarRecipe;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlockEntityTypes;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Lazy;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AltarBlockEntity extends BlockEntity implements ItemInventoryFromInventoryComponent, Tickable, BlockEntityClientSerializable {
	public static final int CRAFTING_TIME = 100;
	public static final int CRAFTING_TIME_SPIN = 80;
	public static final int CRAFTING_TIME_FALL = 60;
	public int spinAge;
	public int yAge;
	public int lastAgeAddition;
	public int craftingTicks = 0;
	public float craftingTicksDelta = 0;
	public AltarRecipe recipe;
	public List<Supplier<ItemDisplayerBlockEntity>> children = Lists.newArrayList();
	private ItemInventoryComponent inventory = new SimpleItemInventoryComponent(1).withListener(component -> {
		if (!world.isClient)
			sync();
	});

	public AltarBlockEntity() {
		super(AstromineFoundationsBlockEntityTypes.ALTAR);
	}

	@Override
	public ItemInventoryComponent getItemComponent() {
		return inventory;
	}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	@Override
	public ItemStack getStack(int slot) {
		if (craftingTicks > 0 && craftingTicks <= CRAFTING_TIME + CRAFTING_TIME_SPIN + CRAFTING_TIME_FALL) {
			return ItemStack.EMPTY;
		}
		return ItemInventoryFromInventoryComponent.super.getStack(slot);
	}

	@Override
	public void tick() {
		yAge++;
		lastAgeAddition = spinAge;
		spinAge++;
		spinAge += Math.min(CRAFTING_TIME, craftingTicks) / 5 * (craftingTicks >= CRAFTING_TIME + CRAFTING_TIME_SPIN ?
			1 - (craftingTicks - CRAFTING_TIME_SPIN - CRAFTING_TIME) / (float) CRAFTING_TIME_FALL : 1);
		lastAgeAddition = spinAge - lastAgeAddition;

		if (craftingTicks == 0) craftingTicksDelta = 0;

		if (craftingTicks > 0) {
			craftingTicks++;
			if (!world.isClient) {
				sync();

				if (craftingTicks == CRAFTING_TIME + CRAFTING_TIME_SPIN / 2 && recipe != null) {
					inventory.setStack(0, recipe.getOutput().copy());

					for (Supplier<ItemDisplayerBlockEntity> child : children) {
						child.get().setStack(0, ItemStack.EMPTY);
						child.get().parent = null;
						child.get().sync();
						spinAge = child.get().getSpinAge();
					}

					recipe = null;
				}
				if (craftingTicks >= CRAFTING_TIME + CRAFTING_TIME_SPIN + CRAFTING_TIME_FALL) {
					onRemove();
				}
			}
		}
	}

	public int getCraftingTicks() {
		return craftingTicks;
	}

	public boolean initializeCrafting() {
		if (craftingTicks > 0) return false;

		children = scanDisplayers();
		Iterator<Supplier<ItemDisplayerBlockEntity>> iterator = children.iterator();

		while (iterator.hasNext()) {
			ItemDisplayerBlockEntity child = iterator.next().get();

			if (child.getStack(0).isEmpty()) {
				child.parent = null;
				iterator.remove();
			} else {
				child.parent = pos;
			}
		}

		Optional<AltarRecipe> match = world.getRecipeManager().getFirstMatch(AltarRecipe.Type.INSTANCE, this, world);
		if (match.isPresent()) {
			recipe = match.get();
			craftingTicks = 1;
			craftingTicksDelta = 1;
			for (Supplier<ItemDisplayerBlockEntity> child : children) {
				child.get().sync();
			}

			return true;
		} else {
			onRemove();
			return false;
		}
	}

	private List<Supplier<ItemDisplayerBlockEntity>> scanDisplayers() {
		return IntStream.range(-4, 5).boxed().flatMap(xOffset ->
			IntStream.range(-4, 5).boxed()
				.map(zOffset -> world.getBlockEntity(pos.add(xOffset, 0, zOffset))))
			.filter(blockEntity -> blockEntity instanceof ItemDisplayerBlockEntity)
			.map(blockEntity -> (ItemDisplayerBlockEntity) blockEntity)
			.filter(blockEntity -> blockEntity.parent == null || pos.equals(blockEntity.parent))
			.map(blockEntity -> (Supplier<ItemDisplayerBlockEntity>) () -> blockEntity)
			.collect(Collectors.toList());
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(null, compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		inventory.fromTag(tag);
		craftingTicks = tag.getInt("craftingTicks");
		if (craftingTicksDelta == 0 || craftingTicks == 0 || craftingTicks == 1)
			craftingTicksDelta = craftingTicks;
		children.clear();
		ListTag children = tag.getList("children", NbtType.LONG);
		for (Tag child : children) {
			long pos = ((LongTag) child).getLong();
			Lazy<ItemDisplayerBlockEntity> lazy = new Lazy<>(() -> (ItemDisplayerBlockEntity) world.getBlockEntity(BlockPos.fromLong(pos)));
			this.children.add(lazy::get);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		inventory.toTag(tag);
		tag.putInt("craftingTicks", craftingTicks);
		ListTag childrenTag = new ListTag();
		for (Supplier<ItemDisplayerBlockEntity> child : children) {
			childrenTag.add(LongTag.of(child.get().getPos().asLong()));
		}
		tag.put("children", childrenTag);
		return super.toTag(tag);
	}

	public void onRemove() {
		recipe = null;
		craftingTicks = 0;
		craftingTicksDelta = 0;

		for (Supplier<ItemDisplayerBlockEntity> child : children) {
			child.get().parent = null;
			child.get().sync();
		}

		children.clear();
	}
}
