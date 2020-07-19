package com.github.chainmailstudios.astromine.common.entity;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.AstromineOreBlock;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.Optional;

public class RocketEntity extends Entity {
	private static final TrackedData<Boolean> IS_GO = DataTracker.registerData(RocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private final FluidInventoryComponent fluidInventory = new SimpleFluidInventoryComponent(1);

	public static final Identifier ROCKET_SPAWN = AstromineCommon.identifier("rocket_spawn");

	public RocketEntity(EntityType<?> type, World world) {
		super(type, world);
		fluidInventory.getContents().forEach((k, v) -> v.setSize(new Fraction(100, 1)));
		this.getDataTracker().set(IS_GO, false);
	}

	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(IS_GO, false);
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		fluidInventory.read(fluidInventory, tag.getCompound("fluid"), Optional.empty(), Optional.empty());
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		tag.put("fluid", fluidInventory.write(fluidInventory, Optional.empty(), Optional.empty()));
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
	public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
		if (player.world.isClient) {
			return ActionResult.CONSUME;
		}

		ItemStack stack = player.getStackInHand(hand);

		if (stack.getItem() == Items.FLINT_AND_STEEL) {
			this.getDataTracker().set(IS_GO, true);
			return ActionResult.SUCCESS;
		} else if (stack.getItem() == Items.STICK) {
			this.kill();
		}

		if (!stack.isEmpty()) return ActionResult.CONSUME;

		player.startRiding(this);

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
	public void updatePassengerPosition(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			Vector3f position = new Vector3f(0, 7.75f, 0);
			passenger.updatePosition(getX() + position.getZ(), getY() + position.getY(), getZ());
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getDataTracker().get(IS_GO)) {
			addVelocity(0, 0.0015f, 0);
			this.move(MovementType.SELF, this.getVelocity());

			Vec3d thrustVec = new Vec3d(0.035, -2.5f, 0.035);
			Vec3d speed = new Vec3d(0.02, -0.2f, 0.02);

			for (int i = 0; i < 90; ++i) {
				speed = speed.rotateY(1);
				spawnParticles(thrustVec, speed);
			}
		} else {
			setVelocity(Vec3d.ZERO);
			this.velocityDirty = true;
		}

		if (!world.isClient) {
			if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
				int j = MathHelper.floor(this.getY());
				int n = MathHelper.floor(this.getX());
				int o = MathHelper.floor(this.getZ());
				boolean bl = false;

				for (int p = -2; p <= 2; ++p) {
					for (int q = -2; q <= 2; ++q) {
						for (int r = 0; r <= 22; ++r) {
							int s = n + p;
							int t = j + r;
							int u = o + q;
							BlockPos blockPos = new BlockPos(s, t, u);
							BlockState blockState = this.world.getBlockState(blockPos);
							float power = 0;
							if (blockState.getBlock() instanceof AstromineOreBlock || blockState.isIn(TagRegistry.block(AstromineCommon.identifier("rocket_explode")))) {
								bl = true;
								power = 3;
							} else if (WitherEntity.canDestroy(blockState)) {
								power = 2.1f;
								bl = true;
							}
							if (power > 0) {
								this.world.createExplosion(null, DamageSource.explosion((LivingEntity) null), new ExplosionBehavior() {
									@Override
									public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
										return Optional.empty();
									}

									@Override
									public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
										return true;
									}
								}, blockPos.getX() + .5, blockPos.getY() + .5, blockPos.getZ() + .5, power, false, Explosion.DestructionType.DESTROY);
							}
						}
					}
				}

				if (bl) {
					this.world.syncWorldEvent(null, 1022, this.getBlockPos(), 0);
				}
			}
		}
	}

	public void spawnParticles(Vec3d thrustVec, Vec3d speed) {
		world.addParticle(AstromineParticles.ROCKET_FLAME,
				getX() + ((thrustVec.getX() - (Math.min(0.6f, random.nextFloat())) * (random.nextBoolean() ? 1 : -1))),
				getY() + thrustVec.getY(),
				getZ() + ((thrustVec.getZ() - (Math.min(0.6f, random.nextFloat())) * (random.nextBoolean() ? 1 : -1))),
				speed.getX(),
				speed.getY(),
				speed.getZ());
	}
}
