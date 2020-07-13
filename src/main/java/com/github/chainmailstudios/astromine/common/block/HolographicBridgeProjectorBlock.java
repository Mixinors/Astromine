package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.access.DyeColorAccess;
import com.github.chainmailstudios.astromine.common.block.base.DefaultedHorizontalFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.HolographicBridgeProjectorBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import spinnery.widget.api.Color;

public class HolographicBridgeProjectorBlock extends DefaultedHorizontalFacingBlockWithEntity {
	public HolographicBridgeProjectorBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos position, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);

		if (stack.getItem() instanceof DyeItem) {
			DyeItem dye = (DyeItem) stack.getItem();

			HolographicBridgeProjectorBlockEntity entity = (HolographicBridgeProjectorBlockEntity) world.getBlockEntity(position);

			if (!world.isClient() && entity != null) {
				setColor(entity, Color.of(0x7e000000 | ((DyeColorAccess) (Object) dye.getColor()).astromine_getColor()));

				if (!player.isCreative()) {
					stack.decrement(1);
				}
				return ActionResult.SUCCESS;
			}
		} else if (stack.getItem() == Items.POTION && PotionUtil.getPotion(stack) == Potions.WATER) {
			HolographicBridgeProjectorBlockEntity entity = (HolographicBridgeProjectorBlockEntity) world.getBlockEntity(position);

			if (!world.isClient() && entity != null) {
				setColor(entity, HolographicBridgeProjectorBlockEntity.DEFAULT_COLOR);

				if (!player.isCreative()) {
					player.setStackInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
				}
				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
	}

	public void setColor(HolographicBridgeProjectorBlockEntity entity, Color color) {
		entity.color = color;
		entity.sync();

		if (entity.hasChild()) {
			entity.getChild().color = color;
			entity.getChild().sync();
		}
		if (entity.hasParent()) {
			entity.getParent().color = color;
			entity.getParent().sync();
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new HolographicBridgeProjectorBlockEntity();
	}
}
