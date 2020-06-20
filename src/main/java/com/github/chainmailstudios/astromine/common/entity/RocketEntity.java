package com.github.chainmailstudios.astromine.common.entity;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.utilities.LineUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.buffer.Unpooled;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public class RocketEntity extends Entity {
    public static final Identifier ROCKET_SPAWN = AstromineCommon.identifier("rocket_spawn");

    private static final ArrayList<Vector3f> ROUTE = new ArrayList<>();

    private final BiMap<Integer, Entity> passengers = HashBiMap.create();

    public RocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {

    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {

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
        return super.interact(player, hand);
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
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
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity removed) {
        super.removePassenger(removed);
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
            position.rotate(Vector3f.POSITIVE_X.getDegreesQuaternion(getYaw(0)));
            passenger.updatePosition(getX() + position.getZ(), getY() + position.getY(), getZ());
        }
    }

    @Override
    public void tick() {
        super.tick();

        getPassengerList().forEach(Entity::tickRiding);

        if (ROUTE.isEmpty()) {
            ROUTE.addAll(LineUtilities.getBezierSegments(new Vector3f((float) getX(), (float) getY(), (float) getZ()), new Vector3f((float) getX() + 128, 1024, (float) getZ()), null, 4096));
        }

        Vector3f cP = ROUTE.get(0);

        ROUTE.remove(0);

        addVelocity((cP.getX() - getX()) * 0.000001d, (cP.getY() - getY()) * 0.000001, (cP.getZ() - getZ()) * 0.000001);

        double opp = 1256 - getY();

        double adj = 128;

        double hip = Math.sqrt(Math.pow(opp, 2) + Math.pow(adj, 2));

        double sin = opp / hip;

        double yaw = Math.toDegrees(Math.asin(Math.abs(sin)));

        setRotation(90 - (float) yaw, 0);

        this.move(MovementType.SELF, this.getVelocity());

        Vec3d thrustVec = new Vec3d(0.035, -4, 0.035).multiply(getRotationVector());

        Vec3d vel = thrustVec.multiply(getRotationVector());

        Vector3f speed = new Vector3f(0.3f, -1, 0);
        speed.rotate(Vector3f.NEGATIVE_Z.getDegreesQuaternion(90 - (float) yaw));

        for (int i = 0; i < 120; ++i) {
           world.addParticle(AstromineParticles.ROCKET_FLAME,
                   getX() + ((thrustVec.getX() - (Math.min(0.6f, random.nextFloat())) * (random.nextBoolean() ? 1 : -1))),
                   getY() + ((thrustVec.getY() - 1.5f - random.nextFloat() * 0.5f)),
                   getZ() + ((thrustVec.getZ() - (Math.min(0.6f, random.nextFloat())) * (random.nextBoolean() ? 1 : -1))),
                   speed.getX() * -1    ,
                   speed.getY(),
                   speed.getZ());
        }

        //if (getY() < 6) {
        //    for (int i = 0; i < 360; ++i) {
        //        speed.rotate(Vector3f.POSITIVE_Y.getDegreesQuaternion(1));
//
        //        world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX() + thrustVec.x,  getY() + thrustVec.y, getZ() + thrustVec.z - 0.05f, speed.getX() * 4, 0, speed.getZ() * 4);
        //    }
        //}
    }
}
