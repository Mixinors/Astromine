package com.github.chainmailstudios.astromine.common.fluid;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.*;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import spinnery.widget.api.Color;

import java.util.function.Function;

public abstract class BaseFluid extends FlowableFluid {
	private static final String BASE_BLOCKSTATE = "{\n" +
			"    \"variants\": {\n" +
			"        \"\": { \"model\": \"block/water\" }\n" +
			"    }\n" +
			"}\n";

	final int fogColor;

	final boolean isInfinite;

	Block block;

	Fluid flowing;
	Fluid still;

	Item bucket;

	public BaseFluid(int fogColor, boolean isInfinite) {
		this.fogColor = fogColor;
		this.isInfinite = isInfinite;
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
		Block.dropStacks(state, world.getWorld(), position, blockEntity);
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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		int fogColor = Color.DEFAULT.ARGB;

		int tint = Color.DEFAULT.ARGB;

		boolean isInfinite = false;

		String name = "";

		Block block;

		Fluid flowing;
		Fluid still;

		Item bucket;

		private Builder() {
			// Unused.
		}

		public Builder fog(int fogColor) {
			this.fogColor = fogColor;
			return this;
		}

		public Builder tint(int tint) {
			this.tint = tint;
			return this;
		}

		public Builder infinite(boolean isInfinite) {
			this.isInfinite = isInfinite;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public void build() {
			BaseFluid flowing = AstromineFluids.register(name + "_flowing", new Flowing(fogColor, isInfinite));
			BaseFluid still = AstromineFluids.register(name, new Still(fogColor, isInfinite));

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
				buildClient();
			}
		}

		private void buildClient() {
			final Identifier stillSpriteIdentifier = new Identifier("block/water_still"); // AstromineCommon.identifier("block/" + name + "_still");
			final Identifier flowingSpriteIdentifier = new Identifier("block/water_flow"); // AstromineCommon.identifier("block/" + name + "_flow");
			final Identifier listenerIdentifier = AstromineCommon.identifier(name + "_reload_listener");

			final Sprite[] fluidSprites = { null, null };

			ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((atlasTexture, registry) -> {
				registry.register(stillSpriteIdentifier);
				registry.register(flowingSpriteIdentifier);
			});

			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
				@Override
				public Identifier getFabricId() {
					return listenerIdentifier;
				}

				@Override
				public void apply(ResourceManager resourceManager) {
					final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
					fluidSprites[0] = atlas.apply(stillSpriteIdentifier);
					fluidSprites[1] = atlas.apply(flowingSpriteIdentifier);
				}
			});

			final FluidRenderHandler handler = new FluidRenderHandler() {
				@Override
				public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
					return fluidSprites;
				}

				@Override
				public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
					return tint;
				}
			};

			FluidRenderHandlerRegistry.INSTANCE.register(still, handler);
			FluidRenderHandlerRegistry.INSTANCE.register(flowing, handler);

			RuntimeResourcePack pack = AstromineResources.RESOURCE_PACK;

			JState state = JState.state(
					JState.variant(
							JState.model("block/water")
					)
			);

			pack.addBlockState(state, AstromineCommon.identifier("blockstates/" + name));
		}
	}

	public static class Flowing extends BaseFluid {
		public Flowing(int fogColor, boolean isInfinite) {
			super(fogColor, isInfinite);
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

	public static class Still extends BaseFluid {
		public Still(int fogColor, boolean isInfinite) {
			super(fogColor, isInfinite);
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
