/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.block.base;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Locale;
import java.util.Optional;
import java.util.Random;

// TODO: This is concerning code and does not allow upgrades to work on non-horizontal machinery.

/**
 * A {@link HorizontalFacingBlockWithEntity} with wrenching behavior.
 */
public abstract class HorizontalFacingTieredBlockWithEntity extends HorizontalFacingBlockWithEntity {
	public HorizontalFacingTieredBlockWithEntity(Settings settings) {
		super(settings);
	}

	/** Override behavior to implement tier upgrades.
	 * Yes, the ones from the Technologies module.
	 * Why is this here?
	 * Why am I here?
	 * Just to suffer?
	 * Is there a point anymore? */
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var blockId = Registry.BLOCK.getId(this);
		
		var blockTier = Tier.fromId(blockId);

		if (blockTier != null) {
			var stack = player.getStackInHand(hand);
			
			var itemId = Registry.ITEM.getId(stack.getItem());

			if (itemId.getNamespace().equals(AMCommon.MOD_ID) && itemId.getPath().endsWith("_machine_upgrade_kit")) {
				var itemTier = Tier.fromId(itemId);

				if (itemTier != null && itemTier.ordinal() != 0 && Tier.values()[itemTier.ordinal() - 1] == blockTier) {
					var newBlockId = new Identifier(blockId.toString().replace(blockTier.name().toLowerCase(Locale.ROOT) + "_", itemTier.name().toLowerCase(Locale.ROOT) + "_"));
					
					var newBlock = Registry.BLOCK.getOrEmpty(newBlockId);

					if (newBlock.isPresent()) {
						if (world.isClient) {
							var random = world.random;
							
							var x = pos.getX() - 0.3;
							var y = pos.getY() - 0.3;
							var z = pos.getZ() - 0.3;

							for (var i = 0; i < 20; i++) {
								world.addParticle(ParticleTypes.COMPOSTER, x + random.nextDouble() * 1.6, y + random.nextDouble() * 1.6, z + random.nextDouble() * 1.6, -0.2 + random.nextDouble() * 0.4, -0.2 + random.nextDouble() * 0.4, -0.2 + random.nextDouble() * 0.4);
							}

							world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1, 1, false);

							return ActionResult.CONSUME;
						}

						if (!player.isCreative()) {
							var copy = stack.copy();

							copy.decrement(1);

							player.setStackInHand(hand, copy);
						}
						
						var blockEntity = world.getBlockEntity(pos);
						
						var beTag = (NbtCompound) null;

						if (blockEntity != null) {
							beTag = blockEntity.createNbtWithId();
							beTag.putInt("x", pos.getX());
							beTag.putInt("y", pos.getY());
							beTag.putInt("z", pos.getZ());
						}

						world.removeBlockEntity(pos);
						
						var newState = newBlock.get().getDefaultState();

						for (var property : state.getProperties()) {
							if (newState.contains(property)) {
								newState = newState.with((Property) property, state.get(property));
							}
						}

						world.setBlockState(pos, newState, 3, 512);
						
						var newBlockEntity = world.getBlockEntity(pos);

						if (newBlockEntity != null && beTag != null) {
							newBlockEntity.readNbt(beTag);
						}

						return ActionResult.SUCCESS;
					}
				}
			}
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	/**
	 * An enum representing machine tiers, because yes?
	 */
	private enum Tier {
		PRIMITIVE,
		BASIC,
		ADVANCED,
		ELITE;

		/** Returns the {@link Tier} of the given {@link Identifier}'s path. */
		static Tier fromId(Identifier identifier) {
			var path = identifier.getPath();
			for (var tier : values()) {
				if (path.startsWith(tier.name().toLowerCase(Locale.ROOT) + "_")) {
					return tier;
				}
			}
			return null;
		}
	}
}
