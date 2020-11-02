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

package com.github.chainmailstudios.astromine.discoveries.common.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Lazy;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.InventoryFromItemComponent;
import com.github.chainmailstudios.astromine.discoveries.common.recipe.AltarRecipe;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlockEntityTypes;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesSoundEvents;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AltarBlockEntity extends BlockEntity implements InventoryFromItemComponent, Tickable, BlockEntityClientSerializable {
	public static final int CRAFTING_TIME = 100;
	public static final int CRAFTING_TIME_SPIN = 80;
	public static final int CRAFTING_TIME_FALL = 60;
	public static final float HEIGHT_OFFSET = 0.3f;
	public static final float HOVER_HEIGHT = 0f;
	public int spinAge;
	public int yAge;
	public int lastAgeAddition;
	public int craftingTicks = 0;
	public float craftingTicksDelta = 0;
	public AltarRecipe recipe;
	public List<Supplier<AltarPedestalBlockEntity>> children = Lists.newArrayList();
	private ItemComponent inventory = SimpleItemComponent.of(1).withListener(inventory -> {
		if (hasWorld() && !world.isClient)
			sync();
	});

	public AltarBlockEntity() {
		super(AstromineDiscoveriesBlockEntityTypes.ALTAR);
	}

	@Override
	public ItemComponent getItemComponent() {
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
		return InventoryFromItemComponent.super.getStack(slot);
	}

	@Override
	public void tick() {
		yAge++;
		lastAgeAddition = spinAge;
		spinAge++;
		spinAge += Math.min(CRAFTING_TIME, craftingTicks) / 5 * (craftingTicks >= CRAFTING_TIME + CRAFTING_TIME_SPIN ? 1 - (craftingTicks - CRAFTING_TIME_SPIN - CRAFTING_TIME) / (float) CRAFTING_TIME_FALL : 1);
		lastAgeAddition = spinAge - lastAgeAddition;

		if (craftingTicks == 0)
			craftingTicksDelta = 0;

		if (craftingTicks > 0) {
			craftingTicks++;
			if (!world.isClient) {
				sync();

				if (craftingTicks == CRAFTING_TIME + CRAFTING_TIME_SPIN / 2 && recipe != null) {
					inventory.setStack(0, recipe.getOutput().copy());

					for (Supplier<AltarPedestalBlockEntity> child : children) {
						child.get().setStack(0, ItemStack.EMPTY);
						child.get().parentPos = null;
						child.get().sync();
						spinAge = child.get().getSpinAge();
					}

					recipe = null;
					LightningEntity entity = EntityType.LIGHTNING_BOLT.create(world);
					entity.refreshPositionAfterTeleport(pos.getX() + 0.5, pos.getY() + 1 + HEIGHT_OFFSET, pos.getZ() + 0.5);
					entity.setCosmetic(true);
					world.spawnEntity(entity);
					world.playSound(null, getPos(), AstromineDiscoveriesSoundEvents.ALTAR_FINISH, SoundCategory.BLOCKS, 1.5F, 1);
				}
				if (craftingTicks >= CRAFTING_TIME + CRAFTING_TIME_SPIN + CRAFTING_TIME_FALL) {
					onRemove();
				}
			}
		}

		if (!world.isClient && !inventory.getStack(0).isEmpty() && craftingTicks > 0 && craftingTicks <= CRAFTING_TIME + CRAFTING_TIME_SPIN + CRAFTING_TIME_FALL) {
			spawnParticles();
		}
	}

	public void spawnParticles() {
		double yProgress = getYProgress(craftingTicks);
		float l = AltarBlockEntity.HOVER_HEIGHT + 0.1F;
		DustParticleEffect effect = new DustParticleEffect(1, 1, 1, 1);
		((ServerWorld) world).spawnParticles(effect, getPos().getX() + 0.5D, getPos().getY() + l + 1.0D - 0.1D + AltarBlockEntity.HEIGHT_OFFSET * yProgress, getPos().getZ() + 0.5D, 2, 0.1D, 0D, 0.1D, 0);
	}

	public double getYProgress(double craftingTicksDelta) {
		double progress = 0;

		if (craftingTicks > 0) {
			progress = Math.min(1, craftingTicksDelta / (double) AltarBlockEntity.CRAFTING_TIME);
			if (craftingTicksDelta >= AltarBlockEntity.CRAFTING_TIME + AltarBlockEntity.CRAFTING_TIME_SPIN) {
				progress *= 1 - Math.min(1, (craftingTicksDelta - AltarBlockEntity.CRAFTING_TIME - AltarBlockEntity.CRAFTING_TIME_SPIN) / (double) AltarBlockEntity.CRAFTING_TIME_FALL);
			}
			BlockPos pos = getPos();
			BlockPos parentPos = getPos();
		}

		return progress;
	}

	public int getCraftingTicks() {
		return craftingTicks;
	}

	public boolean initializeCrafting() {
		if (isCrafting())
			return false;

		children = scanDisplayers();
		Iterator<Supplier<AltarPedestalBlockEntity>> iterator = children.iterator();

		while (iterator.hasNext()) {
			AltarPedestalBlockEntity child = iterator.next().get();

			if (child.getStack(0).isEmpty()) {
				child.parentPos = null;
				iterator.remove();
			} else {
				child.parentPos = pos;
			}
		}

		Optional<AltarRecipe> match = world.getRecipeManager().getFirstMatch(AltarRecipe.Type.INSTANCE, this, world);
		if (match.isPresent()) {
			recipe = match.get();
			craftingTicks = 1;
			craftingTicksDelta = 1;
			for (Supplier<AltarPedestalBlockEntity> child : children) {
				child.get().sync();
			}

			world.playSound(null, getPos(), AstromineDiscoveriesSoundEvents.ALTAR_START, SoundCategory.BLOCKS, 1, 1);

			return true;
		} else {
			onRemove();
			return false;
		}
	}

	private List<Supplier<AltarPedestalBlockEntity>> scanDisplayers() {
		return IntStream.range(-4, 5).boxed().flatMap(xOffset -> IntStream.range(-4, 5).boxed().map(zOffset -> world.getBlockEntity(pos.add(xOffset, 0, zOffset)))).filter(blockEntity -> blockEntity instanceof AltarPedestalBlockEntity).map(
			blockEntity -> (AltarPedestalBlockEntity) blockEntity).filter(blockEntity -> blockEntity.parentPos == null || pos.equals(blockEntity.parentPos)).map(blockEntity -> (Supplier<AltarPedestalBlockEntity>) () -> blockEntity).collect(Collectors.toList());
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
		inventory.readFromNbt(tag);
		craftingTicks = tag.getInt("craftingTicks");
		if (craftingTicksDelta == 0 || craftingTicks == 0 || craftingTicks == 1)
			craftingTicksDelta = craftingTicks;
		children.clear();
		ListTag children = tag.getList("children", NbtType.LONG);
		for (Tag child : children) {
			long pos = ((LongTag) child).getLong();
			Lazy<AltarPedestalBlockEntity> lazy = new Lazy<>(() -> (AltarPedestalBlockEntity) world.getBlockEntity(BlockPos.fromLong(pos)));
			this.children.add(lazy::get);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		inventory.writeToNbt(tag);
		tag.putInt("craftingTicks", craftingTicks);
		ListTag childrenTag = new ListTag();
		for (Supplier<AltarPedestalBlockEntity> child : children) {
			childrenTag.add(LongTag.of(child.get().getPos().asLong()));
		}
		tag.put("children", childrenTag);
		return super.toTag(tag);
	}

	public void onRemove() {
		recipe = null;
		craftingTicks = 0;
		craftingTicksDelta = 0;

		for (Supplier<AltarPedestalBlockEntity> child : children) {
			child.get().parentPos = null;
			if (!world.isClient)
				child.get().sync();
		}

		children.clear();
	}

	public boolean isCrafting() {
		return getCraftingTicks() > 0;
	}
}
