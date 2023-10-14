package com.github.mixinors.astromine.common.entity;

import com.github.mixinors.astromine.client.util.IsometricCameraHandler;
import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IsometricCameraEntity extends Entity {

    public float sidewaysSpeed = 0;
    public float upwardSpeed = 0;
    public float forwardSpeed = 0;

    public IsometricCameraEntity(EntityType<IsometricCameraEntity> type, World world) {
        super(type, world);
    }

    public IsometricCameraEntity(World world, Entity baseEntity) {
        this(AMEntityTypes.CAMERA_ENTITY.get(), world);
        noClip = true;

        setPosition(baseEntity.getX(), baseEntity.getY(), baseEntity.getZ());
        prevX = baseEntity.getX();
        prevY = baseEntity.getY();
        prevZ = baseEntity.getZ();

        setYaw(45);
        setPitch(33);
        prevYaw = 45;
        prevPitch = 33;
    }
	
	@Override
    public void tick() {
        this.attemptTickInVoid();
        this.updatePositionAndAngles(getX(), getY(), getZ(), getYaw(), getPitch());

        tickMovement();
    }
	
	@Override
	public boolean isCollidable() {
		return false;
	}
	
	@Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    protected void initDataTracker() {
    }

    public void tickMovement() {
        Vec3d vec3d = this.getVelocity();
        double h = vec3d.x;
        double i = vec3d.y;
        double j = vec3d.z;
        if (Math.abs(vec3d.x) < 0.003D) {
            h = 0.0D;
        }

        if (Math.abs(vec3d.y) < 0.003D) {
            i = 0.0D;
        }

        if (Math.abs(vec3d.z) < 0.003D) {
            j = 0.0D;
        }

        this.setVelocity(h, i, j);

        this.getEntityWorld().getProfiler().push("travel");
        this.sidewaysSpeed *= 0.5F;
        this.forwardSpeed *= 0.5F;
        this.travel(new Vec3d(this.sidewaysSpeed, this.upwardSpeed, this.forwardSpeed));
        this.getEntityWorld().getProfiler().pop();
    }

    public void travel(Vec3d movementInput) {
        Vec3d vec3d7 = this.method_26318(movementInput);

        this.setVelocity(vec3d7.x * (double)0.51F, vec3d7.y * 0.9800000190734863D, vec3d7.z * (double)0.51F);
    }

    public Vec3d method_26318(Vec3d vec3d) {
        this.updateVelocity(IsometricCameraHandler.speed, vec3d);
        this.move(MovementType.SELF, this.getVelocity());

        return this.getVelocity();
    }
}