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
import com.github.mixinors.astromine.common.util.ItemDataUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class RocketItem extends Item {
	public static final String ENTITY_TAG_KEY = "EntityTag";
	
	public static final String ID_KEY = "Id";
	
	private final EntityType<?> type;
	
	public RocketItem(EntityType<?> type, Item.Properties settings) {
		super(settings);
		
		this.type = type;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		var itemStack = user.getItemInHand(hand);
		var hitResult = SpawnEggItem.getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY);
		
		if (((HitResult) hitResult).getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(itemStack);
		}
		
		if (!(world instanceof ServerLevel serverLevel)) {
			return InteractionResultHolder.success(itemStack);
		}
		
		var blockPos = hitResult.getBlockPos();
		
		if (world.getBlockState(blockPos).getBlock() instanceof LiquidBlock) {
			return InteractionResultHolder.pass(itemStack);
		}
		
		if (!world.mayInteract(user, blockPos) || !user.mayUseItemAt(blockPos, hitResult.getDirection(), itemStack)) {
			return InteractionResultHolder.fail(itemStack);
		}
		
		var entityType = this.getEntityType(ItemDataUtils.get(itemStack));
		
		var result = entityType.spawn(serverLevel, itemStack, user, blockPos.above(), MobSpawnType.SPAWN_EGG, false, false);
		if (result == null) {
			return InteractionResultHolder.pass(itemStack);
		}
		
		if (!user.getAbilities().instabuild) {
			itemStack.shrink(1);
		}
		
		if (result instanceof RocketEntity rocket) {
			rocket.setRocket(RocketManager.create(serverLevel.getServer(), user.getUUID(), rocket.getUUID()));
		}
		
		user.awardStat(Stats.ITEM_USED.get(this));
		world.gameEvent(user, GameEvent.ENTITY_PLACE, user.position());
		
		return InteractionResultHolder.consume(itemStack);
	}
	
	public EntityType<?> getEntityType(@Nullable CompoundTag nbt) {
		var nbtCompound = (CompoundTag) null;
		
		if (nbt != null && nbt.contains(ENTITY_TAG_KEY, Tag.TAG_COMPOUND) && (nbtCompound = nbt.getCompound(ENTITY_TAG_KEY)).contains(ID_KEY, Tag.TAG_STRING)) {
			return EntityType.byString(nbtCompound.getString(ID_KEY)).orElse(this.type);
		}
		
		return this.type;
	}
}
