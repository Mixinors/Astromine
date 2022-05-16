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

import com.github.mixinors.astromine.common.entity.base.RocketEntity;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.screenhandler.PrimitiveRocketScreenHandler;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import com.google.common.collect.Lists;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
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

import javax.annotation.Nullable;
import java.util.Collection;

public class PrimitiveRocketEntity extends RocketEntity implements ExtendedMenuProvider {
	private static final FluidIngredient FUEL_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.FUEL), FluidConstants.BUCKET / 9 / 20 / 2);
	
	private static final FluidIngredient OXYGEN_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.OXYGEN), FluidConstants.BUCKET / 27 / 20 / 2);
	
	public PrimitiveRocketEntity(EntityType<?> type, World world) {
		super(type, world);
		
		fluidStorage.getStorage(FLUID_INPUT_SLOT_1).setCapacity(FluidConstants.BUCKET * 16);
		fluidStorage.getStorage(FLUID_INPUT_SLOT_2).setCapacity(FluidConstants.BUCKET * 16);
	}
	
	@Override
	public Vec3d getLerpedPos(float delta) {
		if (hasPassengers()) {
			var passengers = getPassengerList();
			var passenger = passengers.get(0);
			
			var passengerPosition = getPassengerPosition();
			
			prevX = passenger.prevX;
			prevY = passenger.prevY;
			prevZ = passenger.prevZ;
			
			lastRenderX = passenger.lastRenderX;
			lastRenderY = passenger.lastRenderY;
			lastRenderZ = passenger.lastRenderZ;
			
			return passenger.getLerpedPos(delta).subtract(passengerPosition.getX(), passengerPosition.getY(), passengerPosition.getZ());
		}
		
		return super.getLerpedPos(delta);
	}
	
	@Override
	public FluidIngredient getFirstFuel() {
		return FUEL_INGREDIENT;
	}
	
	@Override
	public FluidIngredient getSecondFuel() {
		return OXYGEN_INGREDIENT;
	}
	
	@Override
	protected Vector3d getAcceleration() {
		return new Vector3d(0.0D, 0.000025 / (Math.abs(getY()) / 1024.0D), 0.0D);
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
		super.tick();
		
		if (!world.isClient) {
			if (world.getRegistryKey().equals(AMWorlds.EARTH_ORBIT_WORLD)) {
				setVelocity(0.0F, 0.0F, 0.0F);
				
				dataTracker.set(IS_RUNNING, false);
			}
			
			try (var transaction = Transaction.openOuter()) {
				var wildItemStorage = itemStorage.getWildProxy();
				var wildFluidStorage = fluidStorage.getWildProxy();
				
				var itemInputStorage1 = wildItemStorage.getStorage(ITEM_INPUT_SLOT_1);
				var itemInputStorage2 = wildItemStorage.getStorage(ITEM_INPUT_SLOT_2);
				
				var itemBufferStorage = wildItemStorage.getStorage(ITEM_BUFFER_SLOT_1);
				
				var itemOutputStorage1 = wildItemStorage.getStorage(ITEM_OUTPUT_SLOT_1);
				var itemOutputStorage2 = wildItemStorage.getStorage(ITEM_OUTPUT_SLOT_2);
				
				var fluidInputStorage1 = wildFluidStorage.getStorage(FLUID_INPUT_SLOT_1);
				var fluidInputStorage2 = wildFluidStorage.getStorage(FLUID_INPUT_SLOT_2);
				
				var itemInputFluidStorage1 = FluidStorage.ITEM.find(itemInputStorage1.getStack(), ContainerItemContext.ofSingleSlot(itemInputStorage1));
				var itemInputFluidStorage2 = FluidStorage.ITEM.find(itemInputStorage2.getStack(), ContainerItemContext.ofSingleSlot(itemInputStorage2));
				
				var itemOutputFluidStorage1 = FluidStorage.ITEM.find(itemOutputStorage1.getStack(), ContainerItemContext.ofSingleSlot(itemOutputStorage1));
				var itemOutputFluidStorage2 = FluidStorage.ITEM.find(itemOutputStorage2.getStack(), ContainerItemContext.ofSingleSlot(itemOutputStorage2));
				
				StorageUtil.move(itemInputFluidStorage1, fluidInputStorage1, fluidVariant -> {
					return getFirstFuel().testVariant(fluidVariant);
				}, FluidConstants.BUCKET, transaction);
				StorageUtil.move(itemInputFluidStorage2, fluidInputStorage2, fluidVariant -> {
					return getSecondFuel().testVariant(fluidVariant);
				}, FluidConstants.BUCKET, transaction);
				
				StorageUtil.move(fluidInputStorage1, itemOutputFluidStorage1, fluidVariant -> true, FluidConstants.BUCKET, transaction);
				StorageUtil.move(fluidInputStorage2, itemOutputFluidStorage2, fluidVariant -> true, FluidConstants.BUCKET, transaction);
				
				StorageUtil.move(itemInputStorage1, itemBufferStorage, (variant) -> {
					var stored = StorageUtil.findStoredResource(itemInputFluidStorage1, transaction);
					return stored == null || stored.isBlank();
				}, 1, transaction);
				
				StorageUtil.move(itemInputStorage2, itemBufferStorage, (variant) -> {
					var stored = StorageUtil.findStoredResource(itemInputFluidStorage2, transaction);
					return stored == null || stored.isBlank();
				}, 1, transaction);
				
				transaction.commit();
			}
		}
	}
}
