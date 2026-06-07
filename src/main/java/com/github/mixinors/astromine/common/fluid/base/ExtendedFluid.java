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

package com.github.mixinors.astromine.common.fluid.base;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.common.util.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class ExtendedFluid extends FlowingFluid {
	final int fogColor;
	final int tintColor;
	
	final boolean infinite;
	
	private final Links links;
	
	final DamageSource damageSource;
	
	private ExtendedFluid(int fogColor, int tintColor, boolean infinite, @Nullable DamageSource source, Links links) {
		this.fogColor = fogColor;
		this.tintColor = tintColor;
		this.infinite = infinite;
		this.damageSource = source;
		this.links = links;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public DamageSource getDamageSource() {
		return damageSource;
	}
	
	@Override
	public Fluid getSource() {
		return links.still.get();
	}
	
	@Override
	public Fluid getFlowing() {
		return links.flowing.get();
	}
	
	@Override
	public FluidType getFluidType() {
		return links.type.get();
	}
	
	@Override
	protected boolean canConvertToSource(Level level) {
		return infinite;
	}
	
	public int getFogColor() {
		return fogColor;
	}
	
	public int getTintColor() {
		return tintColor;
	}
	
	public Block getBlock() {
		return links.block.get();
	}
	
	@Override
	protected void beforeDestroyingBlock(LevelAccessor world, BlockPos position, BlockState state) {
		var blockEntity = world.getBlockEntity(position);
		Block.dropResources(state, world, position, blockEntity);
	}
	
	@Override
	public boolean isSame(Fluid fluid) {
		return fluid == getFlowing() || fluid == getSource();
	}
	
	@Override
	protected int getSlopeFindDistance(LevelReader world) {
		return 4;
	}
	
	@Override
	protected int getDropOff(LevelReader world) {
		return 1;
	}
	
	@Override
	public Item getBucket() {
		return links.bucket.get();
	}
	
	@Override
	protected boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid fluid, Direction direction) {
		return direction == Direction.DOWN && fluid != getFlowing() && fluid != getSource();
	}
	
	@Override
	public int getTickDelay(LevelReader world) {
		return 5;
	}
	
	@Override
	protected float getExplosionResistance() {
		return 100.0F;
	}
	
	@Override
	protected BlockState createLegacyBlock(FluidState state) {
		return links.block.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
	}
	
	private static class Links {
		private Supplier<? extends FluidType> type;
		private Supplier<? extends Fluid> flowing;
		private Supplier<? extends Fluid> still;
		private Supplier<? extends Block> block;
		private Supplier<? extends Item> bucket;
	}
	
	public record Entry(
			DeferredHolder<Fluid, Still> still,
			DeferredHolder<Fluid, Flowing> flowing,
			DeferredHolder<Block, ? extends Block> block,
			DeferredHolder<Item, ? extends Item> bucket,
			DeferredHolder<FluidType, FluidType> type
	) {
		public Still getSource() {
			return still.get();
		}
		
		public Flowing getFlowing() {
			return flowing.get();
		}
		
		public Block getBlock() {
			return block.get();
		}
		
		public Item getBucket() {
			return bucket.get();
		}
		
		public FluidType getType() {
			return type.get();
		}
	}
	
	public static class Builder {
		private int fogColor = Color.WHITE.toRgb();
		private int tintColor = Color.WHITE.toRgb();
		
		private float damage = 0.0F;
		
		private boolean infinite = false;
		private boolean toxic = false;
		
		private boolean customSprite = false;
		private boolean customHandler = false;
		
		private String name = "";
		
		private DamageSource source;
		
		private Builder() {
		}
		
		public Builder fogColor(int fogColor) {
			this.fogColor = fogColor;
			return this;
		}
		
		public Builder tintColor(int tintColor) {
			this.tintColor = tintColor;
			return this;
		}
		
		public Builder damage(float damage) {
			this.damage = damage;
			return this;
		}
		
		public Builder infinite(boolean infinite) {
			this.infinite = infinite;
			return this;
		}
		
		public Builder toxic(boolean toxic) {
			this.toxic = toxic;
			return this;
		}
		
		public Builder customSprite(boolean customSprite) {
			this.customSprite = customSprite;
			return this;
		}
		
		public Builder customHandler(boolean customHandler) {
			this.customHandler = customHandler;
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
		
		public Builder group(Supplier<?> group) {
			return this;
		}
		
		
		public Entry build() {
			var links = new Links();
			var type = AMFluids.registerType(name, () -> new FluidType(FluidType.Properties.create()
					.descriptionId("block." + AMCommon.MOD_ID + "." + name)
					.canConvertToSource(infinite)
					.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
					.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			));
			var flowing = AMFluids.register(name + "_flowing", () -> new Flowing(fogColor, tintColor, infinite, source, links));
			var still = AMFluids.register(name, () -> new Still(fogColor, tintColor, infinite, source, links));
			var block = AMBlocks.register(name, () -> new LiquidBlock(still.get(), BlockBehaviour.Properties.of().noCollission().liquid().strength(100.0F).noLootTable()));
			var bucket = AMItems.register(name + "_bucket", () -> new BucketItem(still.get(), (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1)));
			
			links.type = type;
			links.flowing = flowing;
			links.still = still;
			links.block = block;
			links.bucket = bucket;
			
			return new Entry(still, flowing, block, bucket, type);
		}
	}
	
	public static class Flowing extends ExtendedFluid {
		private Flowing(int fogColor, int tintColor, boolean isInfinite, @Nullable DamageSource source, Links links) {
			super(fogColor, tintColor, isInfinite, source, links);
		}
		
		@Override
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}
		
		@Override
		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}
		
		@Override
		public boolean isSource(FluidState state) {
			return false;
		}
	}
	
	public static class Still extends ExtendedFluid {
		private Still(int fogColor, int tintColor, boolean isInfinite, @Nullable DamageSource source, Links links) {
			super(fogColor, tintColor, isInfinite, source, links);
		}
		
		@Override
		public int getAmount(FluidState state) {
			return 8;
		}
		
		@Override
		public boolean isSource(FluidState state) {
			return true;
		}
	}
}
