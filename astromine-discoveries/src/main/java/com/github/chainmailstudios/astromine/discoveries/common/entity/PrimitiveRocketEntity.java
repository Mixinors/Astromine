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

package com.github.chainmailstudios.astromine.discoveries.common.entity;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.handler.FluidHandler;
import com.github.chainmailstudios.astromine.discoveries.common.entity.base.RocketEntity;
import com.github.chainmailstudios.astromine.discoveries.common.screenhandler.RocketScreenHandler;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsFluids;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3d;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PrimitiveRocketEntity extends RocketEntity implements ExtendedScreenHandlerFactory {
	public static final Identifier PRIMITIVE_ROCKET_SPAWN = AstromineCommon.identifier("primitive_rocket_spawn");

	public PrimitiveRocketEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	protected Predicate<FluidVolume> createFuelPredicate() {
		return volume -> volume.getFluid() == AstromineFoundationsFluids.ROCKET_FUEL;
	}

	@Override
	protected Function<RocketEntity, Fraction> createConsumptionFunction() {
		return entity -> Fraction.of((long) (1024 - entity.getY()), 1024 * 256);
	}

	@Override
	protected Collection<ItemStack> createExplosionRemains() {
		return Collections.emptyList();
	}

	@Override
	protected Function<RocketEntity, Vector3d> createAccelerationFunction() {
		return entity -> new Vector3d(0D, getY() / 1024D / 4D / 3D, 0D);
	}

	@Override
	protected Supplier<Vector3f> createPassengerPosition() {
		return () -> new Vector3f(0.0F, 7.75F, 0.0F);
	}

	@Override
	public FluidInventoryComponent createFluidComponent() {
		FluidInventoryComponent fluidComponent = new SimpleFluidInventoryComponent(1);
		FluidHandler.of(fluidComponent).getFirst().setSize(Fraction.of(128));
		return fluidComponent;
	}

	@Override
	public ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(2);
	}

	@Override
	public boolean collides() {
		return !this.removed;
	}

	@Override
	public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
		if (player.world.isClient) {
			return ActionResult.CONSUME;
		}

		if (player.isSneaking() || !player.getStackInHand(hand).isEmpty()) {
			player.openHandledScreen(this);
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
		packet.writeInt(this.getEntityId());

		return ServerSidePacketRegistry.INSTANCE.toPacket(PRIMITIVE_ROCKET_SPAWN, packet);
	}

	@Override
	public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf buffer) {
		buffer.writeInt(this.getEntityId());
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new RocketScreenHandler(syncId, player, getEntityId());
	}
}
