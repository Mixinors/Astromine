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

package com.github.mixinors.astromine.common.item.entity;

import com.github.mixinors.astromine.common.entity.rocket.RocketEntity;
import com.github.mixinors.astromine.common.manager.RocketManager;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class RocketItem extends Item {
	public static final String ENTITY_TAG_KEY = "EntityTag";
	
	public static final String ID_KEY = "Id";
	
	private final EntityType<?> type;
	
	public RocketItem(EntityType<?> type, Item.Settings settings) {
		super(settings);
		
		this.type = type;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		var itemStack = user.getStackInHand(hand);
		var hitResult = SpawnEggItem.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
		
		if (((HitResult) hitResult).getType() != HitResult.Type.BLOCK) {
			return TypedActionResult.pass(itemStack);
		}
		
		if (!(world instanceof ServerWorld)) {
			return TypedActionResult.success(itemStack);
		}
		
		var blockPos = hitResult.getBlockPos();
		
		if (world.getBlockState(blockPos).getBlock() instanceof FluidBlock) {
			return TypedActionResult.pass(itemStack);
		}
		
		if (!world.canPlayerModifyAt(user, blockPos) || !user.canPlaceOn(blockPos, hitResult.getSide(), itemStack)) {
			return TypedActionResult.fail(itemStack);
		}
		
		var entityType = this.getEntityType(itemStack.getNbt());
		
		var result = entityType.spawnFromItemStack((ServerWorld) world, itemStack, user, blockPos.up(), SpawnReason.SPAWN_EGG, false, false);
		if (result == null) {
			return TypedActionResult.pass(itemStack);
		}
		
		if (!user.getAbilities().creativeMode) {
			itemStack.decrement(1);
		}
		
		if (result instanceof RocketEntity rocket) {
			rocket.setRocket(RocketManager.create(user.getUuid(), rocket.getUuid()));
		}
		
		user.incrementStat(Stats.USED.getOrCreateStat(this));
		world.emitGameEvent(user, GameEvent.ENTITY_PLACE, user.getPos());
		
		return TypedActionResult.consume(itemStack);
	}
	
	public EntityType<?> getEntityType(@Nullable NbtCompound nbt) {
		var nbtCompound = (NbtCompound) null;
		
		if (nbt != null && nbt.contains(ENTITY_TAG_KEY, NbtElement.COMPOUND_TYPE) && (nbtCompound = nbt.getCompound(ENTITY_TAG_KEY)).contains(ID_KEY, NbtElement.STRING_TYPE)) {
			return EntityType.get(nbtCompound.getString(ID_KEY)).orElse(this.type);
		}
		
		return this.type;
	}
}
