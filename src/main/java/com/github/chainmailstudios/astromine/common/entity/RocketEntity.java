package com.github.chainmailstudios.astromine.common.entity;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.utilities.LineUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
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
        System.out.println("FUCK!");
        player.startRiding(this);
        return super.interact(player, hand);
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        System.out.println("FUUUUUUUUCK!");
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

    @Override
    public void tick() {
        super.tick();

        if (ROUTE.isEmpty()) {
            ROUTE.addAll(LineUtilities.getBezierSegments(new Vector3f((float) getX(), (float) getY(), (float) getZ()), new Vector3f((float) getX() + 128, 1024, (float) getZ()), null, 4096));
        }

        Vector3f cP = ROUTE.get(0);

        ROUTE.remove(0);

        addVelocity((cP.getX() - getX()) * 0.000001d, (cP.getY() - getY()) * 0.000001, (cP.getZ() - getZ()) * 0.000001);

        //setVelocity(0, 0, 0);

        double opp = 1024 - getY();

        double adj = 128;

        double hip = Math.sqrt(Math.pow(opp, 2) + Math.pow(adj, 2));

        double sin = opp / hip;

        double yaw = Math.toDegrees(Math.asin(Math.abs(sin)));

       //for (PlayerEntity player : world.getPlayers()) {
       //    player.setVelocity((cP.getX() - getX()) * 0.001d, (cP.getY() - getY()) * 0.001, (cP.getZ() - getZ()) * 0.001);
       //}

        setRotation(90 - (float) yaw, 0);

        System.out.print("Altitude: " + getY() + " | Yaw: " + (90 - yaw) + "\n");

       /// setRotation((float) (yaw * (cP.getX() - getX())), (float) (pitch - (cP.getZ() - getZ())));

        this.move(MovementType.SELF, this.getVelocity());

        Vec3d thrustVec = new Vec3d(0.035, 0, 0.035);


        thrustVec = thrustVec.multiply(getRotationVector());

        Vector3f speed = new Vector3f((float) thrustVec.getX(), (float) thrustVec.getY(), (float) thrustVec.getZ());

        for (int i = 0; i < 360; ++i) {
            speed.rotate(Vector3f.POSITIVE_Y.getDegreesQuaternion(1));

            world.addParticle(AstromineParticles.ROCKET_FLAME, getX() + thrustVec.x,  getY() + thrustVec.y, getZ() + thrustVec.z - 0.05f, speed.getX(), 0, speed.getZ());
        }
    }
}
