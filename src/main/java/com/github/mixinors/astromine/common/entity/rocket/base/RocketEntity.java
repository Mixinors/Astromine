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

package com.github.mixinors.astromine.common.entity.rocket.base;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.entity.base.ExtendedEntity;
import com.github.mixinors.astromine.common.entity.rocket.part.*;
import com.github.mixinors.astromine.common.item.rocket.*;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.screen.handler.entity.RocketScreenHandler;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMCriteria;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.github.mixinors.astromine.registry.common.AMParticles;
import com.google.common.collect.ImmutableList;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import dev.vini2003.hammer.gravity.api.common.manager.GravityManager;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;

import static java.lang.Math.max;
import static java.lang.Math.min;

// TODO: Add Tracked Data Manager to Hammer!
public abstract class RocketEntity extends ExtendedEntity implements ExtendedMenuProvider {
	private static final String FUEL_TANK_ITEM_ID_KEY = "FuelTankItemId";
	private static final String HULL_ITEM_ID_KEY = "HullItemId";
	private static final String LANDING_MECHANISM_ID_KEY = "LandingMechanismId";
	private static final String LIFE_SUPPORT_ID_KEY = "LifeSupportId";
	private static final String SHIELDING_ID_KEY = "ShieldingId";
	private static final String THRUSTER_ID_KEY = "ThrusterId";
	
	public static final FluidIngredient LIQUID_FUEL_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.FUEL), FluidConstants.BUCKET / 9 / 20 / 2);
	public static final FluidIngredient LIQUID_OXYGEN_INGREDIENT = new FluidIngredient(FluidVariant.of(AMFluids.OXYGEN), FluidConstants.BUCKET / 27 / 20 / 2);
	
	public static final int FLUID_INPUT_SLOT_1 = 0;
	public static final int FLUID_INPUT_SLOT_2 = 1;
	public static final int FLUID_OUTPUT_SLOT_1 = 0;
	public static final int FLUID_OUTPUT_SLOT_2 = 1;
	
	public static final int ITEM_INPUT_SLOT_1 = 0;
	public static final int ITEM_INPUT_SLOT_2 = 2;
	public static final int ITEM_BUFFER_SLOT_1 = 4;
	public static final int ITEM_OUTPUT_SLOT_1 = 1;
	public static final int ITEM_OUTPUT_SLOT_2 = 3;
	
	public static final int[] ITEM_INSERT_SLOTS = new int[] { ITEM_INPUT_SLOT_1, ITEM_INPUT_SLOT_2 };
	public static final int[] ITEM_EXTRACT_SLOTS = new int[] { ITEM_BUFFER_SLOT_1, ITEM_OUTPUT_SLOT_1, ITEM_OUTPUT_SLOT_2 };
	
	public static final int[] FLUID_INSERT_SLOTS = new int[] { FLUID_INPUT_SLOT_1, FLUID_INPUT_SLOT_2 };
	public static final int[] FLUID_EXTRACT_SLOTS = new int[] { };
	
	private RocketFuelTankPart fuelTank;
	private RocketHullPart hull;
	private RocketLandingMechanismPart landingMechanism;
	private RocketLifeSupportPart lifeSupport;
	private RocketShieldingPart shielding;
	private RocketThrusterPart thruster;
	
	private RegistryKey<World> source;
	private RegistryKey<World> target;
	
	public static final TrackedData<Boolean> RUNNING = DataTracker.registerData(RocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	public RocketEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(RUNNING, false);
	}
	
	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		
		NbtUtil.putIdentifier(nbt, FUEL_TANK_ITEM_ID_KEY, Registry.ITEM.getId(fuelTank.asItem()));
		NbtUtil.putIdentifier(nbt, HULL_ITEM_ID_KEY, Registry.ITEM.getId(hull.asItem()));
		NbtUtil.putIdentifier(nbt, LANDING_MECHANISM_ID_KEY, Registry.ITEM.getId(landingMechanism.asItem()));
		NbtUtil.putIdentifier(nbt, LIFE_SUPPORT_ID_KEY, Registry.ITEM.getId(lifeSupport.asItem()));
		NbtUtil.putIdentifier(nbt, SHIELDING_ID_KEY, Registry.ITEM.getId(shielding.asItem()));
		NbtUtil.putIdentifier(nbt, THRUSTER_ID_KEY, Registry.ITEM.getId(thruster.asItem()));
	}
	
	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		
		var fuelTankItem = (RocketFuelTankItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, FUEL_TANK_ITEM_ID_KEY));
		
		this.fuelTank = fuelTankItem.getPart();
		
		var hullItem = (RocketHullItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, HULL_ITEM_ID_KEY));
		
		this.hull = hullItem.getPart();
		
		var landingMechanismItem = (RocketLandingMechanismItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, LANDING_MECHANISM_ID_KEY));
		
		this.landingMechanism = landingMechanismItem.getPart();
		
		var lifeSupportItem = (RocketLifeSupportItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, LIFE_SUPPORT_ID_KEY));
		
		this.lifeSupport = lifeSupportItem.getPart();
		
		var shieldingItem = (RocketShieldingItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, SHIELDING_ID_KEY));
		
		this.shielding = shieldingItem.getPart();
		
		var thrusterItem = (RocketThrusterItem) Registry.ITEM.get(NbtUtil.getIdentifier(nbt, THRUSTER_ID_KEY));
		
		this.thruster = thrusterItem.getPart();
	}
	
	protected void onPartChanged() {
		itemStorage = new SimpleItemStorage(5).extractPredicate((variant, slot) ->
				slot == ITEM_BUFFER_SLOT_1 || slot == ITEM_OUTPUT_SLOT_1 || slot == ITEM_OUTPUT_SLOT_2
		).insertPredicate((variant, slot) ->
				slot == ITEM_INPUT_SLOT_1 || slot == ITEM_INPUT_SLOT_2
		).listener(() -> {
			syncData();
		}).insertSlots(ITEM_INSERT_SLOTS).extractSlots(ITEM_EXTRACT_SLOTS);
		
		fluidStorage = new SimpleFluidStorage(2, getFluidStorageSize()).extractPredicate((variant, slot) ->
				false
		).insertPredicate((variant, slot) ->
				(slot == FLUID_INPUT_SLOT_1 && LIQUID_OXYGEN_INGREDIENT.testVariant(variant)) || (slot == FLUID_INPUT_SLOT_2 && LIQUID_FUEL_INGREDIENT.testVariant(variant))
		).listener(() -> {
			syncData();
		}).insertSlots(FLUID_INSERT_SLOTS).extractSlots(FLUID_EXTRACT_SLOTS);
	}
	
	public boolean isFuelMatching() {
		return LIQUID_OXYGEN_INGREDIENT.test(fluidStorage.getStorage(FLUID_INPUT_SLOT_1)) && LIQUID_FUEL_INGREDIENT.test(fluidStorage.getStorage(FLUID_INPUT_SLOT_2));
	}
	
	public Vec3d getAcceleration() {
		return new Vec3d(0.0D, 0.000025 / (Math.abs(getY()) / 1024.0D), 0.0D);
	}

	public Vec3f getPassengerPosition() {
		return new Vec3f(0.0F, 7.75F, 0.0F);
	}
	
	public Collection<ItemStack> getDroppedStacks() {
		return ImmutableList.of(
				fuelTank.asItem().getDefaultStack(),
				hull.asItem().getDefaultStack(),
				landingMechanism.asItem().getDefaultStack(),
				lifeSupport.asItem().getDefaultStack(),
				shielding.asItem().getDefaultStack(),
				thruster.asItem().getDefaultStack()
		);
	}
	
	@Override
	public void updatePassengerPosition(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			var position = getPassengerPosition();
			
			passenger.setPosition(getX() + position.getX(), getY() + position.getY(), getZ() + position.getZ());
		}
	}
	
	@Override
	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		var offset = getPassengerDismountOffset(this.getWidth(), passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.RIGHT ? 90.0F : -90.0F));
		
		return new Vec3d(offset.getX() + this.getX(), offset.getY() + this.getY(), offset.getZ() + this.getZ());
	}
	
	public void openInventory(PlayerEntity player) {
		MenuRegistry.openExtendedMenu((ServerPlayerEntity) player, this);
	}
	
	
	public void travel(Body source, Body target, PlayerEntity launcher) {
		// TODO!
		if (fluidStorage.getStorage(FLUID_INPUT_SLOT_1).getAmount() > 0 && fluidStorage.getStorage(FLUID_INPUT_SLOT_2).getAmount() > 0) {
			this.getDataTracker().set(RocketEntity.RUNNING, true);
			
			if (launcher instanceof ServerPlayerEntity serverLauncher) {
				AMCriteria.LAUNCH_ROCKET.trigger(serverLauncher);
			}
		}
	}
	
	public void disassemble(boolean intentional) {
		for (var stack : getDroppedStacks()) {
			ItemScatterer.spawn(world, getX(), getY(), getZ(), stack.copy());
		}
		
		var passengers = this.getPassengersDeep();
		
		for (var passenger : passengers) {
			if (passenger instanceof ServerPlayerEntity) {
				AMCriteria.DESTROY_ROCKET.trigger((ServerPlayerEntity) passenger, intentional);
			}
			
			passenger.stopRiding();
		}
		
		this.remove(RemovalReason.KILLED);
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
		return new RocketScreenHandler(syncId, player, getId());
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (!world.isClient) {
			if (isRunning()) {
				if (isFuelMatching()) {
					var acceleration = getAcceleration();
					
					this.addVelocity(0, acceleration.y, 0);
					this.move(MovementType.SELF, this.getVelocity());
					
					var box = getBoundingBox();
					
					for (var x = box.minX; x < box.maxX; x += 0.0625) {
						for (var z = box.minZ; z < box.maxZ; z += 0.0625) {
							((ServerWorld) world).spawnParticles(AMParticles.ROCKET_FLAME.get(), x, getY(), z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
						}
					}
				} else if (!world.isClient) {
					this.addVelocity(0, -GravityManager.get(world.getRegistryKey()), 0);
					this.move(MovementType.SELF, this.getVelocity());
					
					if (getVelocity().y < -GravityManager.get(world.getRegistryKey())) {
						if (BlockPos.Mutable.stream(getBoundingBox().offset(0, -1, 0)).anyMatch(pos -> !world.getBlockState(pos).isAir()) && !world.isClient) {
							this.disassemble(false);
						}
					}
					
					velocityDirty = true;
				}
			} else {
				this.addVelocity(0, -GravityManager.get(world.getRegistryKey()), 0);
				this.move(MovementType.SELF, this.getVelocity());
				
				if (getVelocity().y < -GravityManager.get(world.getRegistryKey())) {
					if (BlockPos.Mutable.stream(getBoundingBox().offset(0, -1, 0)).anyMatch(pos -> !world.getBlockState(pos).isAir()) && !world.isClient) {
						this.disassemble(false);
					}
				}
				
				velocityDirty = true;
			}
		}
	}
	
	@Override
	public long getFluidStorageSize() {
		return AMConfig.get().entities.primitiveRocketFluid;
	}
	
	public boolean isRunning() {
		return dataTracker.get(RUNNING);
	}
	
	public void setRunning(boolean running) {
		dataTracker.set(RUNNING, running);
	}
	
	public RocketFuelTankPart getFuelTank() {
		return fuelTank;
	}
	
	public void setFuelTank(RocketFuelTankPart fuelTank) {
		this.fuelTank = fuelTank;
		
		onPartChanged();
	}
	
	public RocketHullPart getHull() {
		return hull;
	}
	
	public void setHull(RocketHullPart hull) {
		this.hull = hull;
		
		onPartChanged();
	}
	
	public RocketLandingMechanismPart getLandingMechanism() {
		return landingMechanism;
	}
	
	public void setLandingMechanism(RocketLandingMechanismPart landingMechanism) {
		this.landingMechanism = landingMechanism;
		
		onPartChanged();
	}
	
	public RocketLifeSupportPart getLifeSupport() {
		return lifeSupport;
	}
	
	public void setLifeSupport(RocketLifeSupportPart lifeSupport) {
		this.lifeSupport = lifeSupport;
		
		onPartChanged();
	}
	
	public RocketShieldingPart getShielding() {
		return shielding;
	}
	
	public void setShielding(RocketShieldingPart shielding) {
		this.shielding = shielding;
		
		onPartChanged();
	}
	
	public RocketThrusterPart getThruster() {
		return thruster;
	}
	
	public void setThruster(RocketThrusterPart thruster) {
		this.thruster = thruster;
		
		onPartChanged();
	}
	
	public RegistryKey<World> getSource() {
		return source;
	}
	
	public void setSource(RegistryKey<World> source) {
		this.source = source;
	}
	
	public RegistryKey<World> getTarget() {
		return target;
	}
	
	public void setTarget(RegistryKey<World> target) {
		this.target = target;
	}
}
