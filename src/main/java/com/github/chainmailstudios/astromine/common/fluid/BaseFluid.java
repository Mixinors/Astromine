package com.github.chainmailstudios.astromine.common.fluid;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import javax.imageio.ImageIO;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineFluids;
import com.github.chainmailstudios.astromine.registry.AstromineItemGroups;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstromineResources;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.util.ImageUtil;
import spinnery.widget.api.Color;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;

public abstract class BaseFluid extends FlowableFluid {
	private static final String BASE_BLOCKSTATE = "{\n" + "    \"variants\": {\n" + "        \"\": { \"model\": \"block/water\" }\n" + "    }\n" + "}\n";

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

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public Fluid getFlowing() {
		return this.flowing;
	}

	@Override
	public Fluid getStill() {
		return this.still;
	}

	@Override
	protected boolean isInfinite() {
		return this.isInfinite;
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
		return this.bucket;
	}

	@Override
	protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
		return direction == Direction.DOWN && fluid != this.flowing && fluid != this.still;
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
		return this.block.getDefaultState().with(FluidBlock.LEVEL, method_15741(state));
	}

	@Override
	public boolean isStill(FluidState state) {
		return false;
	}

	@Override
	public int getLevel(FluidState state) {
		return 0;
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == this.flowing || fluid == this.still;
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

		public Fluid build() {
			BaseFluid flowing = AstromineFluids.register(this.name + "_flowing", new Flowing(this.fogColor, this.isInfinite));
			BaseFluid still = AstromineFluids.register(this.name, new Still(this.fogColor, this.isInfinite));

			flowing.flowing = flowing;
			still.flowing = flowing;
			this.flowing = flowing;

			flowing.still = still;
			still.still = still;
			this.still = still;

			Block block = AstromineBlocks.register(this.name, new FluidBlock(still, AbstractBlock.Settings.of(Material.WATER).noCollision().strength(100.0F).dropsNothing()));

			Item bucket = AstromineItems.register(this.name + "_bucket", new BucketItem(still, (new Item.Settings()).recipeRemainder(Items.BUCKET).maxCount(1).group(AstromineItemGroups.ASTROMINE)));

			flowing.block = block;
			still.block = block;
			this.block = block;

			flowing.bucket = bucket;
			still.bucket = bucket;
			this.bucket = bucket;

			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
				this.buildClient();
			}

			return still;
		}

		private void buildClient() {
			final Identifier stillSpriteIdentifier = new Identifier("block/water_still"); // AstromineCommon.identifier("block/" + name + "_still");
			final Identifier flowingSpriteIdentifier = new Identifier("block/water_flow"); // AstromineCommon.identifier("block/" + name + "_flow");
			final Identifier listenerIdentifier = AstromineCommon.identifier(this.name + "_reload_listener");

			final Sprite[] fluidSprites = {null, null};

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

					RuntimeResourcePack pack = AstromineResources.RESOURCE_PACK;

					JState state = JState.state(JState.variant(JState.model("block/water")));

					pack.addBlockState(state, AstromineCommon.identifier(Builder.this.name));

					JModel itemModel = JModel.model("item/generated").textures(new JTextures().layer0(AstromineCommon.MOD_ID + ":textures/item/bucket/" + Builder.this.name));

					pack.addModel(itemModel, AstromineCommon.identifier("item/" + Builder.this.name + "_bucket"));

					try {
						BufferedImage bucketLayerA = ImageIO.read(resourceManager.getResource(AstromineCommon.identifier("textures/item/bucket_base.png")).getInputStream());
						BufferedImage bucketLayerB = ImageIO.read(resourceManager.getResource(AstromineCommon.identifier("textures/item/bucket_overlay.png")).getInputStream());

						int sXB = bucketLayerB.getWidth();
						int sYB = bucketLayerB.getHeight();

						for (int y = 0; y < sYB; ++sYB) {
							for (int x = 0; x < sXB; ++sXB) {
								if (bucketLayerB.getRGB(x, y) != 0x00000000) {
									bucketLayerB.setRGB(x, y, ImageUtil.recolor(bucketLayerB.getRGB(x, y), Builder.this.tint));
								}
							}
						}

						int sXA = bucketLayerA.getWidth();
						int sYA = bucketLayerA.getHeight();

						BufferedImage bucketLayered = new BufferedImage(sXA, sYA, BufferedImage.TYPE_INT_ARGB);

						Graphics graphics = bucketLayered.getGraphics();

						graphics.drawImage(bucketLayerA, 0, 0, null);
						graphics.drawImage(bucketLayerB, 0, 0, null);

						graphics.dispose();

						pack.addTexture(AstromineCommon.identifier("item/bucket/" + Builder.this.name), bucketLayered);
					} catch (Exception ignored) {
						// Unused.
					}


				}
			});

			final FluidRenderHandler handler = new FluidRenderHandler() {
				@Override
				public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
					return fluidSprites;
				}

				@Override
				public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
					return Builder.this.tint;
				}
			};

			FluidRenderHandlerRegistry.INSTANCE.register(this.still, handler);
			FluidRenderHandlerRegistry.INSTANCE.register(this.flowing, handler);
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
		public boolean isStill(FluidState state) {
			return false;
		}

		@Override
		public int getLevel(FluidState state) {
			return state.get(LEVEL);
		}
	}

	public static class Still extends BaseFluid {
		public Still(int fogColor, boolean isInfinite) {
			super(fogColor, isInfinite);
		}

		@Override
		public boolean isStill(FluidState state) {
			return true;
		}

		@Override
		public int getLevel(FluidState state) {
			return 8;
		}
	}
}
