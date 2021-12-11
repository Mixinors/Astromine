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

package com.github.mixinors.astromine.common.block.entity;

import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMSoundEvents;
import dev.architectury.hooks.block.BlockEntityHooks;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import me.shedaniel.architectury.utils.NbtType;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Lazy;
import net.minecraft.util.math.BlockPos;

import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.common.component.general.SimpleItemComponent;
import com.github.mixinors.astromine.common.component.general.compatibility.InventoryFromItemComponent;
import com.github.mixinors.astromine.common.recipe.AltarRecipe;

import com.google.common.collect.Lists;
import net.minecraft.util.math.Vec3f;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AltarBlockEntity extends BlockEntity implements InventoryFromItemComponent, BlockEntityExtension {
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
			syncData();
	});

	public AltarBlockEntity(BlockPos pos, BlockState state) {
		super(AMBlockEntityTypes.ALTAR.get(), pos, state);
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

	// TODO tick from block class
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
				syncData();

				if (craftingTicks == CRAFTING_TIME + CRAFTING_TIME_SPIN / 2 && recipe != null) {
					inventory.setStack(0, recipe.getOutput().copy());

					for (Supplier<AltarPedestalBlockEntity> child : children) {
						child.get().setStack(0, ItemStack.EMPTY);
						child.get().parentPos = null;
						BlockEntityHooks.syncData(child.get());
						spinAge = child.get().getSpinAge();
					}

					recipe = null;
					LightningEntity entity = EntityType.LIGHTNING_BOLT.create(world);
					entity.refreshPositionAfterTeleport(pos.getX() + 0.5, pos.getY() + 1 + HEIGHT_OFFSET, pos.getZ() + 0.5);
					entity.setCosmetic(true);
					world.spawnEntity(entity);
					world.playSound(null, getPos(), AMSoundEvents.ALTAR_FINISH.get(), SoundCategory.BLOCKS, 1.5F, 1);
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
		DustParticleEffect effect = new DustParticleEffect(new Vec3f(1, 1, 1), 1);
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
				BlockEntityHooks.syncData(child.get());
			}

			world.playSound(null, getPos(), AMSoundEvents.ALTAR_START.get(), SoundCategory.BLOCKS, 1, 1);

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
	public void loadClientData(BlockState state, NbtCompound compoundTag) {
		readNbt(compoundTag);
	}

	@Override
	public NbtCompound saveClientData(NbtCompound compoundTag) {
		writeNbt(compoundTag);
		return compoundTag;
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		inventory.readFromNbt(tag);
		craftingTicks = tag.getInt("craftingTicks");
		if (craftingTicksDelta == 0 || craftingTicks == 0 || craftingTicks == 1)
			craftingTicksDelta = craftingTicks;
		children.clear();
		NbtList children = tag.getList("children", NbtType.LONG);
		for (NbtElement child : children) {
			long pos = ((NbtLong) child).longValue();
			Lazy<AltarPedestalBlockEntity> lazy = new Lazy<>(() -> (AltarPedestalBlockEntity) world.getBlockEntity(BlockPos.fromLong(pos)));
			this.children.add(lazy::get);
		}
	}

	@Override
	protected void writeNbt(NbtCompound tag) {
		inventory.writeToNbt(tag);
		tag.putInt("craftingTicks", craftingTicks);
		NbtList childrenTag = new NbtList();
		for (Supplier<AltarPedestalBlockEntity> child : children) {
			childrenTag.add(NbtLong.of(child.get().getPos().asLong()));
		}
		tag.put("children", childrenTag);
		super.writeNbt(tag);
	}

	public void onRemove() {
		recipe = null;
		craftingTicks = 0;
		craftingTicksDelta = 0;

		for (Supplier<AltarPedestalBlockEntity> child : children) {
			child.get().parentPos = null;
			if (!world.isClient)
				BlockEntityHooks.syncData(child.get());
		}

		children.clear();
	}

	public boolean isCrafting() {
		return getCraftingTicks() > 0;
	}
}
