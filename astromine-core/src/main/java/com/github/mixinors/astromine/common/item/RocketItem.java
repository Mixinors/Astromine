package com.github.mixinors.astromine.common.item;

import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.FluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * Based on SpawnEggItem
 */
public class RocketItem extends Item {
	private final EntityType<?> type;

	public RocketItem(EntityType<?> type, Item.Settings settings) {
		super(settings);
		this.type = type;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		BlockHitResult hitResult = SpawnEggItem.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
		if (((HitResult)hitResult).getType() != HitResult.Type.BLOCK) {
			return TypedActionResult.pass(itemStack);
		}
		if (!(world instanceof ServerWorld)) {
			return TypedActionResult.success(itemStack);
		}
		BlockHitResult blockHitResult = hitResult;
		BlockPos blockPos = blockHitResult.getBlockPos();
		if ( world.getBlockState(blockPos).getBlock() instanceof FluidBlock ) {
			return TypedActionResult.pass(itemStack);
		}
		if (!world.canPlayerModifyAt(user, blockPos) || !user.canPlaceOn(blockPos, blockHitResult.getSide(), itemStack)) {
			return TypedActionResult.fail(itemStack);
		}
		EntityType<?> entityType = this.getEntityType(itemStack.getNbt());
		if (entityType.spawnFromItemStack((ServerWorld)world, itemStack, user, blockPos.offset(Direction.UP), SpawnReason.SPAWN_EGG, false, false) == null) {
			return TypedActionResult.pass(itemStack);
		}
		if (!user.getAbilities().creativeMode) {
			itemStack.decrement(1);
		}
		user.incrementStat(Stats.USED.getOrCreateStat(this));
		world.emitGameEvent(GameEvent.ENTITY_PLACE, user);
		return TypedActionResult.consume(itemStack);
	}

	public EntityType<?> getEntityType(@Nullable NbtCompound nbt) {
		NbtCompound nbtCompound;
		if (nbt != null && nbt.contains("EntityTag", 10) && (nbtCompound = nbt.getCompound("EntityTag")).contains("id", 8)) {
			return EntityType.get(nbtCompound.getString("id")).orElse(this.type);
		}
		return this.type;
	}
}
