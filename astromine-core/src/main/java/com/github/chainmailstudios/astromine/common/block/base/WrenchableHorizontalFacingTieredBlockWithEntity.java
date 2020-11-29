/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import com.github.chainmailstudios.astromine.AstromineCommon;

import java.util.Locale;
import java.util.Optional;
import java.util.Random;

// TODO: This is concerning code and does not allow upgrades to work on non-horizontal machinery.

/**
 * A {@link HorizontalFacingBlockWithEntity} with wrenching behavior.
 */
public abstract class WrenchableHorizontalFacingTieredBlockWithEntity extends WrenchableHorizontalFacingBlockWithEntity {
	public WrenchableHorizontalFacingTieredBlockWithEntity(Properties settings) {
		super(settings);
	}

	/** Override behavior to implement tier upgrades.
	 * Yes, the ones from the Technologies module.
	 * Why is this here?
	 * Why am I here?
	 * Just to suffer?
	 * Is there a point anymore? */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);

		Tier blockTier = Tier.fromId(blockId);

		if (blockTier != null) {
			ItemStack stack = player.getItemInHand(hand);

			ResourceLocation itemId = Registry.ITEM.getKey(stack.getItem());

			if (itemId.getNamespace().equals(AstromineCommon.MOD_ID) && itemId.getPath().endsWith("_machine_upgrade_kit")) {
				Tier itemTier = Tier.fromId(itemId);

				if (itemTier != null && itemTier.ordinal() != 0 && Tier.values()[itemTier.ordinal() - 1] == blockTier) {
					ResourceLocation newBlockId = new ResourceLocation(blockId.toString().replace(blockTier.name().toLowerCase(Locale.ROOT) + "_", itemTier.name().toLowerCase(Locale.ROOT) + "_"));

					Optional<Block> newBlock = Registry.BLOCK.getOptional(newBlockId);

					if (newBlock.isPresent()) {
						if (world.isClientSide) {
							Random random = world.random;

							double x = pos.getX() - 0.3;
							double y = pos.getY() - 0.3;
							double z = pos.getZ() - 0.3;

							for (int i = 0; i < 20; i++) {
								world.addParticle(ParticleTypes.COMPOSTER, x + random.nextDouble() * 1.6, y + random.nextDouble() * 1.6, z + random.nextDouble() * 1.6, -0.2 + random.nextDouble() * 0.4, -0.2 + random.nextDouble() * 0.4, -0.2 + random.nextDouble() * 0.4);
							}

							world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1, 1, false);

							return InteractionResult.CONSUME;
						}

						if (!player.isCreative()) {
							ItemStack copy = stack.copy();

							copy.shrink(1);

							player.setItemInHand(hand, copy);
						}

						BlockEntity blockEntity = world.getBlockEntity(pos);

						CompoundTag beTag = null;

						if (blockEntity != null) {
							beTag = blockEntity.save(new CompoundTag());
							beTag.putInt("x", pos.getX());
							beTag.putInt("y", pos.getY());
							beTag.putInt("z", pos.getZ());
						}

						world.removeBlockEntity(pos);

						BlockState newState = newBlock.get().defaultBlockState();

						for (Property property : state.getProperties()) {
							if (newState.hasProperty(property)) {
								newState = newState.setValue(property, state.getValue(property));
							}
						}

						world.setBlock(pos, newState, 3, 512);

						BlockEntity newBlockEntity = world.getBlockEntity(pos);

						if (newBlockEntity != null && beTag != null) {
							newBlockEntity.load(newState, beTag);
						}

						return InteractionResult.SUCCESS;
					}
				}
			}
		}

		return super.use(state, world, pos, player, hand, hit);
	}

	/**
	 * An enum representing machine tiers, because yes?
	 */
	private enum Tier {
		PRIMITIVE,
		BASIC,
		ADVANCED,
		ELITE;

		/** Returns the {@link Tier} of the given {@link ResourceLocation}'s path. */
		static Tier fromId(ResourceLocation identifier) {
			String path = identifier.getPath();
			for (Tier tier : values()) {
				if (path.startsWith(tier.name().toLowerCase(Locale.ROOT) + "_")) {
					return tier;
				}
			}
			return null;
		}
	}
}
