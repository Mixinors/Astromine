package com.github.chainmailstudios.astromine.common.entity;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.access.BucketAccessor;
import com.github.chainmailstudios.astromine.common.component.Component;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.utilities.LineUtilities;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.mixin.BucketItemMixin;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public class RocketEntity extends Entity {
    private final FluidInventoryComponent fluidInventory = new SimpleFluidInventoryComponent(1);

    public static final Identifier ROCKET_SPAWN = AstromineCommon.identifier("rocket_spawn");

    private final BiMap<Integer, Entity> passengers = HashBiMap.create();

    private boolean isGo = false;

    private double goY = 0;

    public RocketEntity(EntityType<?> type, World world) {
        super(type, world);
        fluidInventory.getContents().forEach((k, v) -> v.setSize(new Fraction(16, 1)));
    }
    
    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {
        tag.put("fluid", fluidInventory.write(fluidInventory, Optional.empty(), Optional.empty()));
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {
        fluidInventory.read(fluidInventory, tag, Optional.empty(), Optional.empty());
    }

    public Box getHardCollisionBox(Entity collidingEntity) {
        return collidingEntity.isPushable() ? collidingEntity.getBoundingBox() : null;
    }

    public Box getCollisionBox() {
        return this.getBoundingBox();
    }

    public boolean collides() {
        return !this.removed;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem() == Items.FLINT_AND_STEEL) {
            isGo = true;
            goY = getY();
        }

        return super.interact(player, hand);
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (player.getStackInHand(hand) != ItemStack.EMPTY) return ActionResult.PASS;

        passengers.inverse().remove(player);

        if (hitPos.getY() >= 2.4 && hitPos.getY() <= 4) {
            if (passengers.get(0) == null) {
                passengers.put(0, player);
                player.startRiding(this);
            }
        } else if (hitPos.getY() >= 4.25 && hitPos.getY() <= 5.6) {
            if (passengers.get(1) == null) {
                passengers.put(1, player);
                player.startRiding(this);
            }
        } else if (hitPos.getY() >= 6 && hitPos.getY() <= 7.3) {
            if (passengers.get(2) == null) {
                passengers.put(2, player);
                player.startRiding(this);
            }
        } else {
            if (passengers.get(3) == null) {
                passengers.put(3, player);
                player.startRiding(this);
            }
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

        return ServerSidePacketRegistry.INSTANCE.toPacket(ROCKET_SPAWN, packet);
    }

    public double getMountedHeightOffset(Entity passenger) {
        if (passengers.containsValue(passenger)) {
            if (passengers.get(0) == passenger) {
                return 1.85d;
            } else if (passengers.get(1) == passenger) {
                return 3.6d;
            } else if (passengers.get(2) == passenger) {
                return 5.35d;
            } else {
                return 11.75;
            }
        } else {
            return 11.75;
        }
    }


    @Override
    public void updatePassengerPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            Vector3f position = new Vector3f(0, (float) getMountedHeightOffset(passenger), 0);
            passenger.updatePosition(getX() + position.getZ(), getY() + position.getY(), getZ());
        }
    }

    @Override
    public void tick() {
        super.tick();

        getPassengerList().forEach(Entity::tickRiding);

        if (isGo) {
            addVelocity(0, 0.001f, 0);

            this.move(MovementType.SELF, this.getVelocity());

            Vec3d thrustVec = new Vec3d(0.035, -1, 0.035);
            Vec3d speed = new Vec3d(0.02, -0.2f, 0.02);

            for (int i = 0; i < 90; ++i) {
                speed = speed.rotateY(1);
                world.addParticle(AstromineParticles.ROCKET_FLAME,
                        getX() + ((thrustVec.getX() - (Math.min(0.6f, random.nextFloat())) * (random.nextBoolean() ? 1 : -1))),
                        getY() + thrustVec.getY(),
                        getZ() + ((thrustVec.getZ() - (Math.min(0.6f, random.nextFloat())) * (random.nextBoolean() ? 1 : -1))),
                        speed.getX(),
                        speed.getY(),
                        speed.getZ());
            }
        }
    }
}
