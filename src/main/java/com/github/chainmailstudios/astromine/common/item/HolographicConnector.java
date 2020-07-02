package com.github.chainmailstudios.astromine.common.item;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.block.HolographicBridgeProjectorBlock;
import com.github.chainmailstudios.astromine.common.block.entity.HolographicBridgeProjectorBlockEntity;
import com.github.chainmailstudios.astromine.registry.AstromineSounds;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

public class HolographicConnector extends Item {
	public static final Object2ObjectArrayMap<World, Object> CACHE = new Object2ObjectArrayMap<>();

	public HolographicConnector(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.shouldCancelInteraction()) return super.useOnBlock(context);

		World world = context.getWorld();

		BlockPos position = context.getBlockPos();

		if (world.getBlockState(position).getBlock() instanceof HolographicBridgeProjectorBlock) {
			HolographicBridgeProjectorBlockEntity entity = (HolographicBridgeProjectorBlockEntity) world.getBlockEntity(position);

			if (CACHE.getOrDefault(world, null) == null) {
				CACHE.put(world, entity);
				context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connector_select", toShortString(entity.getPos())).formatted(Formatting.BLUE), true);
				world.playSound(context.getPlayer(), context.getBlockPos(), AstromineSounds.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
			} else {
				HolographicBridgeProjectorBlockEntity parent = (HolographicBridgeProjectorBlockEntity) CACHE.get(world);

				BlockPos nP = entity.getPos();
				BlockPos oP = parent.getPos();

				if (parent.getPos().getZ() < entity.getPos().getZ() || parent.getPos().getX() < entity.getPos().getX()) {
					HolographicBridgeProjectorBlockEntity temporary = parent;
					parent = entity;
					entity = temporary;
				}

				if ((parent.getPos().getX() != entity.getPos().getX() && parent.getPos().getZ() != entity.getPos().getZ()) || oP.getSquaredDistance(nP) > 65536) {
					CACHE.put(world, null);
					context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connection_failed", parent.getPos().toShortString(), entity.getPos().toShortString()).formatted(Formatting.RED), true);
					world.playSound(context.getPlayer(), context.getBlockPos(), AstromineSounds.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
					return ActionResult.FAIL;
				} else if (parent.getCachedState().get(HorizontalFacingBlock.FACING).getOpposite() != entity.getCachedState().get(HorizontalFacingBlock.FACING)) {
					CACHE.put(world, null);
					context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connection_failed", parent.getPos().toShortString(), entity.getPos().toShortString()).formatted(Formatting.RED), true);
					world.playSound(context.getPlayer(), context.getBlockPos(), AstromineSounds.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
					return ActionResult.FAIL;
				}

				parent.setChild(entity);

				if (entity.hasChild()) {
					entity.setChild(null);
				}

				parent.buildBridge();

				if (world.isClient) {
					CACHE.put(world, null);
					context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connection_successful", parent.getPos().toShortString(), entity.getPos().toShortString()).formatted(Formatting.GREEN), true);
					world.playSound(context.getPlayer(), context.getBlockPos(), AstromineSounds.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
				}
			}
		} else {
			if (world.isClient) {
				context.getPlayer().sendMessage(new TranslatableText("text.astromine.message.holographic_connection_clear").formatted(Formatting.YELLOW), true);
				world.playSound(context.getPlayer(), context.getBlockPos(), AstromineSounds.HOLOGRAPHIC_CONNECTOR_CLICK, SoundCategory.PLAYERS, 0.5f, 0.33f);
			}

			CACHE.put(world, null);
		}

		return super.useOnBlock(context);
	}

	public String toShortString(BlockPos pos) {
		return "" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
	}
}
