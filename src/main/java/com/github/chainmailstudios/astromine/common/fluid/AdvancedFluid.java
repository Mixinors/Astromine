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

package com.github.chainmailstudios.astromine.common.fluid;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import com.github.chainmailstudios.astromine.common.gas.Breathable;
import com.github.chainmailstudios.astromine.common.utilities.ClientUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.github.chainmailstudios.astromine.registry.AstromineItemGroups;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.vini2003.blade.common.data.Color;
import org.jetbrains.annotations.Nullable;

public abstract class AdvancedFluid extends FlowableFluid implements Breathable {
	final int fogColor;
	final int damage;

	final boolean isInfinite;
	final boolean isToxic;

	Block block;

	Fluid flowing;
	Fluid still;

	Item bucket;

	DamageSource source;

	public AdvancedFluid(int fogColor, int damage, boolean isInfinite, boolean isToxic, @Nullable DamageSource source) {
		this.fogColor = fogColor;
		this.damage = damage;
		this.isInfinite = isInfinite;
		this.isToxic = isToxic;
		this.source = source == null ? DamageSource.GENERIC : source;
	}

	public DamageSource getSource() {
		return source;
	}

	@Override
	public boolean isToxic() {
		return isToxic;
	}

	public int getDamage() {
		return damage;
	}

	@Override
	public Fluid getStill() {
		return still;
	}

	@Override
	public Fluid getFlowing() {
		return flowing;
	}

	@Override
	protected boolean isInfinite() {
		return isInfinite;
	}

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos position, BlockState state) {
		BlockEntity blockEntity = state.getBlock().hasBlockEntity() ? world.getBlockEntity(position) : null;
		Block.dropStacks(state, world, position, blockEntity);
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == flowing || fluid == still;
	}

	@Override
	protected int getFlowSpeed(WorldView world) {
		return 4;
	}

	@Override
	protected int getLevelDecreasePerBlock(WorldView world) {
		return 1;
	}

	@Override
	public Item getBucketItem() {
		return bucket;
	}

	@Override
	protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
		return direction == Direction.DOWN && fluid != flowing && fluid != still;
	}

	@Override
	public int getTickRate(WorldView world) {
		return 5;
	}

	@Override
	protected float getBlastResistance() {
		return 100.0F;
	}

	@Override
	protected BlockState toBlockState(FluidState state) {
		return block.getDefaultState().with(FluidBlock.LEVEL, method_15741(state));
	}

	@Override
	public boolean isStill(FluidState state) {
		return false;
	}

	@Override
	public int getLevel(FluidState state) {
		return 0;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		int fog = Color.standard().toInt();
		int tint = Color.standard().toInt();
		int damage = 0;

		boolean isInfinite = false;
		boolean isToxic = false;

		String name = "";

		Block block;

		Fluid flowing;
		Fluid still;

		Item bucket;

		DamageSource source;

		private Builder() {
			// Unused.
		}

		public Builder fog(int fog) {
			this.fog = fog;
			return this;
		}

		public Builder tint(int tint) {
			this.tint = tint;
			return this;
		}

		public Builder damage(int damage) {
			this.damage = damage;
			return this;
		}

		public Builder infinite(boolean isInfinite) {
			this.isInfinite = isInfinite;
			return this;
		}

		public Builder toxic(boolean isToxic) {
			this.isToxic = isToxic;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder source(DamageSource source) {
			this.source = source;
			return this;
		}

		public Fluid build() {
			AdvancedFluid flowing = AstromineFluids.register(name + "_flowing", new Flowing(fog, damage, isInfinite, isToxic, source));
			AdvancedFluid still = AstromineFluids.register(name, new Still(fog, damage, isInfinite, isToxic, source));

			flowing.flowing = flowing;
			still.flowing = flowing;
			this.flowing = flowing;

			flowing.still = still;
			still.still = still;
			this.still = still;

			Block block = AstromineBlocks.register(name, new FluidBlock(still, AbstractBlock.Settings.of(Material.WATER).noCollision().strength(100.0F).dropsNothing()));

			Item bucket = AstromineItems.register(name + "_bucket", new BucketItem(still, (new Item.Settings()).recipeRemainder(Items.BUCKET).maxCount(1).group(AstromineItemGroups.ASTROMINE)));

			flowing.block = block;
			still.block = block;
			this.block = block;

			flowing.bucket = bucket;
			still.bucket = bucket;
			this.bucket = bucket;

			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
				ClientUtilities.buildClient(name, tint, still, flowing);
			}

			return still;
		}
	}

	public static class Flowing extends AdvancedFluid {
		public Flowing(int fogColor, int damage, boolean isInfinite, boolean isToxic, DamageSource source) {
			super(fogColor, damage, isInfinite, isToxic, source);
		}

		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState state) {
			return state.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState state) {
			return false;
		}
	}

	public static class Still extends AdvancedFluid {
		public Still(int fogColor, int damage, boolean isInfinite, boolean isToxic, DamageSource source) {
			super(fogColor, damage, isInfinite, isToxic, source);
		}

		@Override
		public int getLevel(FluidState state) {
			return 8;
		}

		@Override
		public boolean isStill(FluidState state) {
			return true;
		}
	}
}
