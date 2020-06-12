package com.github.chainmailstudios.astromine.common.entity;

import com.github.chainmailstudios.astromine.common.entity.ai.superspaceslime.*;
import com.github.chainmailstudios.astromine.registry.AstromineEntities;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SuperSpaceSlimeEntity extends MobEntity implements Monster {

    // data for slime explosion mechanic
    private static final TrackedData<Integer> EXPLOSION_PROGRESS = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IS_EXPLODING = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_EXPLODED = DataTracker.registerData(SpaceSlimeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private final ServerBossBar bossBar;
    public float targetStretch;
    public float stretch;
    public float lastStretch;
    private boolean onGroundLastTick;

    public SuperSpaceSlimeEntity(EntityType<? extends SuperSpaceSlimeEntity> entityType, World world) {
        super(entityType, world);
        this.bossBar = (ServerBossBar) (new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true);
        this.moveControl = new SuperSpaceSlimeMoveControl(this);
    }

    @Override
    public void initGoals() {
        this.goalSelector.add(0, new SuperSpaceSlimeExplosionGoal(this));
        this.goalSelector.add(1, new SuperSpaceSlimeSwimmingGoal(this));
        this.goalSelector.add(2, new SuperSpaceSlimeFaceTowardTargetGoal(this));
        this.goalSelector.add(3, new SuperSpaceSlimeRandomLookGoal(this));
        this.goalSelector.add(5, new SuperSpaceSlimeMoveGoal(this));

        this.targetSelector.add(1, new FollowTargetGoal<>(this, PlayerEntity.class, 10, true, false, (livingEntity) -> true));
    }

    /**
     * To prevent the Super Space Slime from dying through entity cramming during its explosion attack,
     * its cramming functionality is disabled.
     */
    @Override
    protected void tickCramming() {

    }

    /**
     * Called at the end of {@link SuperSpaceSlimeExplosionGoal}.
     *
     * <p>Spawns a large number of Space Slime around the Super Space Slime.
     * Note that entity hitbox mechanics remove the need to set random velocities on the new slimes.
     */
    public void explode() {
        for (int i = 0; i < 50; i++) {
            SpaceSlimeEntity spaceSlime = AstromineEntities.SPACE_SLIME.create(world);
            spaceSlime.initialize(world, world.getLocalDifficulty(getBlockPos()), SpawnReason.NATURAL, null, null);
            world.spawnEntity(spaceSlime);
            spaceSlime.requestTeleport(this.getX(), this.getY(), this.getZ());
        }
    }

    /**
     * Creates a {@link DefaultAttributeContainer.Builder} instance used for registering this entities' default attributes.
     *
     * @return  a {@link DefaultAttributeContainer.Builder} with default attribute information
     */
    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 300);
    }

    @Override
    public void tick() {
        this.stretch += (this.targetStretch - this.stretch) * 0.5F;
        this.lastStretch = this.stretch;

        super.tick();

        if (this.onGround && !this.onGroundLastTick) {
            int size = 10;

            // spawn random landing particles around this entity's hitbox base
            for(int j = 0; j < size * 8; ++j) {
                float f = this.random.nextFloat() * 6.2831855F;
                float g = this.random.nextFloat() * 0.5F + 0.5F;
                float particleX = MathHelper.sin(f) * (float) size * 0.5F * g;
                float particleZ = MathHelper.cos(f) * (float) size * 0.5F * g;
                this.world.addParticle(this.getParticles(), this.getX() + (double) particleX, this.getY(), this.getZ() + (double) particleZ, 0.0D, 0.0D, 0.0D);
            }

            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.targetStretch = -0.5F;
        } else if (!this.onGround && this.onGroundLastTick) {
            this.targetStretch = 1.0F;
        }

        this.onGroundLastTick = this.onGround;
        this.updateStretch();
    }

    @Override
    public void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(HAS_EXPLODED, false);
        this.dataTracker.startTracking(IS_EXPLODING, false);
        this.dataTracker.startTracking(EXPLOSION_PROGRESS, 0);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putBoolean("hasExploded", this.hasExploded());
        tag.putBoolean("wasOnGround", this.onGroundLastTick);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.setHasExploded(tag.getBoolean("hasExploded"));
        this.onGroundLastTick = tag.getBoolean("wasOnGround");
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        super.pushAwayFrom(entity);

        if (entity instanceof IronGolemEntity) {
            this.damage((LivingEntity) entity);
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        this.damage(player);
    }

    protected void damage(LivingEntity target) {
        if (this.isAlive()) {
            int size = 10;

            if (this.squaredDistanceTo(target) < 0.6D * (double) size * 0.6D * (double) size && this.canSee(target) && target.damage(DamageSource.mob(this), this.getDamageAmount())) {
                this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.dealDamage(this, target);
            }
        }
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.625F * dimensions.height;
    }

    @Override
    public float getSoundVolume() {
        return 1;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SLIME_DEATH;
    }

    @Override
    public void jump() {
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x, this.getJumpVelocity(), vec3d.z);
        this.velocityDirty = true;
    }

    public float getJumpSoundPitch() {
        float f = 0.8F;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
    }

    public SoundEvent getJumpSound() {
        return SoundEvents.ENTITY_SLIME_JUMP;
    }

    protected SoundEvent getSquishSound() {
        return SoundEvents.ENTITY_SLIME_SQUISH;
    }

    protected ParticleEffect getParticles() {
        return AstromineParticles.SPACE_SLIME;
    }

    protected void updateStretch() {
        this.targetStretch *= 0.6F;
    }

    public int getTicksUntilNextJump() {
        return this.random.nextInt(20) + 10;
    }

    protected float getDamageAmount() {
        return (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    public boolean isExploding() {
        return dataTracker.get(IS_EXPLODING);
    }

    public void setExploding(boolean exploding) {
        dataTracker.set(IS_EXPLODING, exploding);
    }

    public int getExplodingProgress() {
        return dataTracker.get(EXPLOSION_PROGRESS);
    }

    public void setExplodingProgress(int progress) {
        dataTracker.set(EXPLOSION_PROGRESS, progress);
    }

    public boolean hasExploded() {
        return dataTracker.get(HAS_EXPLODED);
    }

    public void setHasExploded(boolean exploded) {
        dataTracker.set(HAS_EXPLODED, exploded);
    }
}
