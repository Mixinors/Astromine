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

package com.github.mixinors.astromine.common.entity;

import com.github.mixinors.astromine.common.entity.base.RocketEntity;
import com.github.mixinors.astromine.registry.common.AMDimensions;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMNetworks;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;

import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.util.VolumeUtils;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.common.screenhandler.PrimitiveRocketScreenHandler;
import io.netty.buffer.Unpooled;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.Collection;

public class PrimitiveRocketEntity extends RocketEntity implements ExtendedMenuProvider
{
	private static final FluidIngredient KEROSENE_INGREDIENT = FluidIngredient.ofFluidVolumes(FluidVolume.of(FluidVolume.BUCKET / 9L, AMFluids.FUEL));

	private static final FluidIngredient OXYGEN_INGREDIENT = FluidIngredient.ofFluidVolumes(FluidVolume.of(FluidVolume.BUCKET / 27L, AMFluids.OXYGEN));

	public PrimitiveRocketEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public SimpleFluidStorage createFluidComponent() {
		SimpleFluidStorage fluidStorage = SimpleAutoSyncedFluidComponent.of(2);
		fluidStorage.getFirst().setSize(FluidVolume.BUCKET * 16L);
		fluidStorage.getSecond().setSize(FluidVolume.BUCKET * 16L);
		return fluidStorage;
	}

	@Override
	public SimpleItemStorage createItemComponent() {
		return SimpleAutoSyncedItemComponent.of(4);
	}

	@Override
	protected boolean isFuelMatching() {
		return KEROSENE_INGREDIENT.test(getFluidComponent().getFirst()) && OXYGEN_INGREDIENT.test(getFluidComponent().getSecond());
	}

	@Override
	protected void consumeFuel() {
		getFluidComponent().getFirst().take(KEROSENE_INGREDIENT.testMatching(getFluidComponent().getFirst()).getAmount());
		getFluidComponent().getSecond().take(OXYGEN_INGREDIENT.testMatching(getFluidComponent().getSecond()).getAmount());
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
		PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());

		packet.writeDouble(this.getX());
		packet.writeDouble(this.getY());
		packet.writeDouble(this.getZ());
		packet.writeUuid(this.getUuid());
		packet.writeInt(this.getId());

		return NetworkManager.toPacket(NetworkManager.s2c(), AMNetworks.PRIMITIVE_ROCKET_SPAWN, packet);
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

		VolumeUtils.transferBetween(getItemComponent(), getFluidComponent(), 0, 1, 0);
		VolumeUtils.transferBetween(getItemComponent(), getFluidComponent(), 2, 3, 1);

		super.tick();
	}
}
