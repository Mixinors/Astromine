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

package com.github.mixinors.astromine.common.item.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public final class DrillMiningHandler {
	private static final int TARGET_MAX_AGE = 1200;
	private static final Map<UUID, Target> TARGETS = new HashMap<>();
	private static final ThreadLocal<Boolean> BREAKING_EXTRA_BLOCK = ThreadLocal.withInitial(() -> false);

	private DrillMiningHandler() {
	}

	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}

		if (!(player.getMainHandItem().getItem() instanceof DrillItem)) {
			TARGETS.remove(player.getUUID());
			return;
		}

		if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.START && event.getFace() != null) {
			TARGETS.put(player.getUUID(), new Target(player.level().dimension(), event.getPos().immutable(), event.getFace(), player.serverLevel().getGameTime()));
		} else if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.ABORT) {
			TARGETS.remove(player.getUUID());
		}
	}

	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		if (event.isCanceled() || BREAKING_EXTRA_BLOCK.get()) {
			return;
		}

		if (!(event.getPlayer() instanceof ServerPlayer player)) {
			return;
		}

		if (!(event.getLevel() instanceof ServerLevel level)) {
			return;
		}

		var stack = player.getMainHandItem();

		if (!(stack.getItem() instanceof DrillItem drill) || drill.getMiningDiameter() <= 1) {
			return;
		}

		var target = getTarget(player, event.getPos());

		if (target == null) {
			return;
		}

		var energyPerBlock = drill.getEnergyConsumedOnBlockBreak();

		BREAKING_EXTRA_BLOCK.set(true);

		try {
			for (var pos : DrillMiningArea.positions(event.getPos(), target.face(), drill.getMiningDiameter())) {
				if (pos.equals(event.getPos())) {
					continue;
				}

				if (!canBreakExtraBlock(level, player, pos)) {
					continue;
				}

				if (!player.isCreative() && energyPerBlock > 0 && drill.getStoredEnergy(stack) < energyPerBlock * 2L) {
					break;
				}

				player.gameMode.destroyBlock(pos);
			}
		} finally {
			TARGETS.remove(player.getUUID());
			BREAKING_EXTRA_BLOCK.set(false);
		}
	}

	private static Target getTarget(ServerPlayer player, BlockPos pos) {
		var target = TARGETS.get(player.getUUID());

		if (target == null || !target.dimension().equals(player.level().dimension()) || !target.pos().equals(pos)) {
			return null;
		}

		if (player.serverLevel().getGameTime() - target.gameTime() > TARGET_MAX_AGE) {
			TARGETS.remove(player.getUUID());
			return null;
		}

		return target;
	}

	private static boolean canBreakExtraBlock(ServerLevel level, ServerPlayer player, BlockPos pos) {
		if (!level.isLoaded(pos) || !level.getWorldBorder().isWithinBounds(pos)) {
			return false;
		}

		var state = level.getBlockState(pos);

		if (state.isAir() || state.getDestroySpeed(level, pos) < 0.0F || !state.getBlock().isEnabled(level.enabledFeatures())) {
			return false;
		}

		if (player.blockActionRestricted(level, pos, player.gameMode.getGameModeForPlayer())) {
			return false;
		}

		var stack = player.getMainHandItem();

		if (!(stack.getItem() instanceof DrillItem)) {
			return false;
		}

		return canHarvest(level, player, state, pos);
	}

	private static boolean canHarvest(ServerLevel level, ServerPlayer player, BlockState state, BlockPos pos) {
		return !state.requiresCorrectToolForDrops() || state.canHarvestBlock(level, pos, player);
	}

	private record Target(ResourceKey<Level> dimension, BlockPos pos, Direction face, long gameTime) {}
}
