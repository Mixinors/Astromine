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

import com.github.mixinors.astromine.common.util.FluidUtils;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMFluids;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.shnupbups.cauldronlib.CauldronLib;
import com.shnupbups.cauldronlib.block.FullCauldronBlock;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.vini2003.hammer.core.api.client.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.transfer.v1.fluid.CauldronFluidContent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public abstract class ExtendedFluid extends FlowableFluid {
	public static final Supplier<AbstractBlock.Settings> INDUSTRIAL_FLUID_MATERIAL = () -> AbstractBlock.Settings.create()
																										   .mapColor(MapColor.WATER_BLUE)
																										   .noCollision()
																										   .pistonBehavior(PistonBehavior.DESTROY)
																										   .replaceable()
																										   .liquid()
																										   .notSolid()
																										   .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY);
	
	final int fogColor;
	final int tintColor;
	
	final boolean infinite;
	
	RegistrySupplier<Block> block;
	
	Fluid flowing;
	Fluid still;
	
	RegistrySupplier<Item> bucket;
	
	Map<Item, CauldronBehavior> cauldronBehaviorMap;
	RegistrySupplier<CauldronBlock> cauldron;
	
	@Nullable
	final DamageSource source;
	
	public ExtendedFluid(int fogColor, int tintColor, boolean infinite, @Nullable DamageSource source) {
		this.fogColor = fogColor;
		this.tintColor = tintColor;
		this.infinite = infinite;
		this.source = source;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public DamageSource getSource(DamageSources sources) {
		return source == null ? sources.generic() : source;
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
	protected boolean isInfinite(World world) {
		return infinite;
	}
	
	public int getFogColor() {
		return fogColor;
	}
	
	public int getTintColor() {
		return tintColor;
	}
	
	public Block getBlock() {
		return block.get();
	}
	
	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos position, BlockState state) {
		var blockEntity = world.getBlockEntity(position);
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
		return bucket.get();
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
		return block.get().getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
	}
	
	public Map<Item, CauldronBehavior> getCauldronBehaviorMap() {
		return cauldronBehaviorMap;
	}
	
	public Block getCauldron() {
		return cauldron.get();
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
		
		private ItemGroup group;
		
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
		
		public Builder group(ItemGroup group) {
			this.group = group;
			return this;
		}
		
		
		public ExtendedFluid build() {
			var flowing = AMFluids.register(name + "_flowing", new Flowing(fogColor, tintColor, infinite, source));
			var still = AMFluids.register(name, new Still(fogColor, tintColor, infinite, source));
			
			flowing.flowing = flowing;
			still.flowing = flowing;
			
			flowing.still = still;
			still.still = still;
			
			var block = AMBlocks.register(name, () -> new FluidBlock(still, INDUSTRIAL_FLUID_MATERIAL.get().noCollision().strength(100.0F).dropsNothing()));
			
			var bucket = AMItems.register(name + "_bucket", () -> new BucketItem(still, (new Item.Settings()).recipeRemainder(Items.BUCKET).maxCount(1).group(group)));
			
			flowing.block = (RegistrySupplier) block;
			still.block = (RegistrySupplier) block;
			
			flowing.bucket = (RegistrySupplier) bucket;
			still.bucket = (RegistrySupplier) bucket;
			
			var cauldronBehaviorMap = CauldronBehavior.createMap();
			
			var cauldron = AMBlocks.register(name + "_cauldron", () -> new FullCauldronBlock(AbstractBlock.Settings.copy(Blocks.CAULDRON), cauldronBehaviorMap));
			
			flowing.cauldronBehaviorMap = cauldronBehaviorMap;
			still.cauldronBehaviorMap = cauldronBehaviorMap;
			
			flowing.cauldron = (RegistrySupplier) cauldron;
			still.cauldron = (RegistrySupplier) cauldron;
			
			CauldronLib.registerBehaviorMap(cauldronBehaviorMap);
			CauldronLib.registerFillFromBucketBehavior(bucket.get(), ((RegistrySupplier<? extends Block>) cauldron).get());
			
			cauldronBehaviorMap.put(Items.BUCKET, CauldronLib.createEmptyIntoBucketBehavior(bucket.get()));
			
			CauldronFluidContent.registerCauldron(((RegistrySupplier<? extends Block>) cauldron).get(), still, FluidConstants.BUCKET, null); // Fabric only! If we're going to do a Forge version, make sure this only runs on Fabric!
			
			if (Platform.getEnv() == EnvType.CLIENT) {
				FluidUtils.registerSimpleFluid(name, tintColor, still, flowing, customSprite, customHandler);
			}
			
			return still;
		}
	}
	
	public static class Flowing extends ExtendedFluid {
		public Flowing(int fogColor, int tintColor, boolean isInfinite, @Nullable DamageSource source) {
			super(fogColor, tintColor, isInfinite, source);
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
	
	public static class Still extends ExtendedFluid {
		public Still(int fogColor, int tintColor, boolean isInfinite, @Nullable DamageSource source) {
			super(fogColor, tintColor, isInfinite, source);
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
