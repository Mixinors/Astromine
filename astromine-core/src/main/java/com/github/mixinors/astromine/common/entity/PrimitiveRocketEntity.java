/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.entity;

import java.util.Collection;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import com.github.mixinors.astromine.common.entity.base.RocketEntity;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.screenhandler.PrimitiveRocketScreenHandler;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMDimensions;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMNetworks;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;

import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public class PrimitiveRocketEntity extends RocketEntity implements ExtendedMenuProvider {
	private static final int ITEM_INPUT_SLOT_1 = 0;
	private static final int ITEM_INPUT_SLOT_2 = 2;
	
	private static final int ITEM_OUTPUT_SLOT_1 = 1;
	private static final int ITEM_OUTPUT_SLOT_2 = 3;
	
	private static final int[] ITEM_INSERT_SLOTS = new int[] { ITEM_INPUT_SLOT_1, ITEM_INPUT_SLOT_2 };
	
	private static final int[] ITEM_EXTRACT_SLOTS = new int[] { ITEM_OUTPUT_SLOT_1, ITEM_OUTPUT_SLOT_2 };
	
	private static final FluidIngredient FUEL_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.FUEL), FluidConstants.BUCKET / 9);

	private static final FluidIngredient OXYGEN_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.OXYGEN), FluidConstants.BUCKET / 27);

	public PrimitiveRocketEntity(EntityType<?> type, World world) {
		super(type, world);

		fluidStorage.getStorage(FLUID_INPUT_SLOT_1).setCapacity(FluidConstants.BUCKET * 16);
		fluidStorage.getStorage(FLUID_INPUT_SLOT_2).setCapacity(FluidConstants.BUCKET * 16);
		
		itemStorage = new SimpleItemStorage(4).extractPredicate((variant, slot) ->
			slot == ITEM_OUTPUT_SLOT_1 || slot == ITEM_OUTPUT_SLOT_2
		).insertPredicate((variant, slot) ->
			FluidStorage.ITEM.getProvider(variant.getItem()) != null && (slot == ITEM_INPUT_SLOT_1 || slot == ITEM_INPUT_SLOT_2)
		).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
	}

	@Override
	protected FluidIngredient getPrimaryFuelIngredient() {
		return FUEL_INGREDIENT;
	}

	@Override
	protected FluidIngredient getSecondaryFuelIngredient() {
		return OXYGEN_INGREDIENT;
	}

	@Override
	protected Vector3d getAcceleration() {
		return new Vector3d(0D, 0.000025 / (getY() / 1024D), 0D);
	}

	@Override
	protected Vec3f getPassengerPosition() {
		return new Vec3f(0.0F, 7.75F, 0.0F);
	}

	@Override
	protected Collection<ItemStack> getDroppedStacks() {
		return Lists.newArrayList(new ItemStack(AMItems.PRIMITIVE_ROCKET_BOOSTER.get()), new ItemStack(AMItems.PRIMITIVE_ROCKET_HULL.get()), new ItemStack(AMItems.PRIMITIVE_ROCKET_PLATING.get(), 2));
	}

	@Override
	public void openInventory(PlayerEntity player) {
		MenuRegistry.openExtendedMenu((ServerPlayerEntity) player, this);
	}

	@Override
	public boolean collides() {
		return !this.isRemoved();
	}

	@Override
	public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
		if (player.world.isClient) {
			return ActionResult.CONSUME;
		}

		if (player.isSneaking()) {
			this.openInventory(player);
		} else {
			player.startRiding(this);
		}

		return super.interactAt(player, hitPos, hand);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}
	
	@Override
	public void saveExtraData(PacketByteBuf buf) {
		buf.writeInt(this.getId());
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
		return new PrimitiveRocketScreenHandler(syncId, player, getId());
	}

	@Override
	public void tick() {
		if (world.getRegistryKey().equals(AMDimensions.EARTH_SPACE_WORLD)) {
			setVelocity(0, 0, 0);

			getDataTracker().set(IS_RUNNING, false);
		}
		
		try (var transaction = Transaction.openOuter()) {
			var firstItemInputStack = itemStorage.getStack(ITEM_INPUT_SLOT_1);
			var secondItemInputStack = itemStorage.getStack(ITEM_INPUT_SLOT_2);
			
			var firstItemInputStorage = itemStorage.getStorage(ITEM_INPUT_SLOT_1);
			var secondItemInputStorage = itemStorage.getStorage(ITEM_INPUT_SLOT_2);
			
			var firstItemOutputStorage = itemStorage.getStorage(ITEM_OUTPUT_SLOT_1);
			var secondItemOutputStorage = itemStorage.getStorage(ITEM_OUTPUT_SLOT_2);
			
			var firstFluidInputStorage = fluidStorage.getStorage(FLUID_INPUT_SLOT_1);
			var secondFluidInputStorage = fluidStorage.getStorage(FLUID_INPUT_SLOT_2);
			
			var firstFluidOutputStorage = FluidStorage.ITEM.find(firstItemInputStack, ContainerItemContext.ofSingleSlot(firstItemInputStorage));
			var secondFluidOutputStorage = FluidStorage.ITEM.find(secondItemInputStack, ContainerItemContext.ofSingleSlot(secondItemInputStorage));
			
			StorageUtil.move(firstFluidOutputStorage, firstFluidInputStorage, fluidVariant -> true, FluidConstants.BUCKET, transaction);
			StorageUtil.move(secondFluidOutputStorage, secondFluidInputStorage, fluidVariant -> true, FluidConstants.BUCKET, transaction);
			
			if (firstItemOutputStorage.getResource().isBlank()) {
				StorageUtil.move(firstItemInputStorage, firstItemOutputStorage, (variant) -> {
					if(firstItemOutputStorage.isResourceBlank()) return true;

					var storage = FluidStorage.ITEM.find(variant.toStack(), ContainerItemContext.ofSingleSlot(firstItemOutputStorage));
					
					return storage == null || storage.iterator(transaction).next().isResourceBlank();
				}, 1, transaction);
			}
			
			if (secondItemOutputStorage.getResource().isBlank()) {
				StorageUtil.move(secondItemInputStorage, secondItemOutputStorage, (variant) -> {
					if(secondItemOutputStorage.isResourceBlank()) return true;

					var storage = FluidStorage.ITEM.find(variant.toStack(), ContainerItemContext.ofSingleSlot(firstItemOutputStorage));
					
					return storage == null || storage.iterator(transaction).next().isResourceBlank();
				}, 1, transaction);
			}
			
			transaction.commit();
		}
		
		super.tick();
	}
}
