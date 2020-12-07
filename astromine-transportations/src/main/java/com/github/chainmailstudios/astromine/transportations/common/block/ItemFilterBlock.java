package com.github.chainmailstudios.astromine.transportations.common.block;

import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.ItemFilterBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemFilterBlock extends ConveyorBlock {
	public ItemFilterBlock(Settings settings) {
		super(settings, AstromineConfig.get().eliteConveyorSpeed);
	}

	@Override
	public ConveyorTypes getType() {
		return ConveyorTypes.NORMAL;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockView world) {
		return new ItemFilterBlockEntity();
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView blockView, BlockPos blockPos, ShapeContext entityContext) {
		return VoxelShapes.fullCube();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			ItemFilterBlockEntity blockEntity = (ItemFilterBlockEntity) world.getBlockEntity(pos);

			ItemStack stack = player.getStackInHand(hand);

			if (stack.isEmpty()) {
				blockEntity.setFilterTag(false);
				blockEntity.setFilterStack(ItemStack.EMPTY);
			} else {
				if (StackUtilities.areItemsAndTagsEqual(stack, blockEntity.getFilterStack())) {
					blockEntity.setFilterTag(!blockEntity.isFilterTag());
				} else {
					blockEntity.setFilterTag(false);
					blockEntity.setFilterStack(stack.copy());
				}
			}

			player.sendMessage(new TranslatableText("text.astromine.item_filter_use", new TranslatableText(stack.getItem().getTranslationKey()).getString(), blockEntity.isFilterTag() ? "Yes" : "No"), false);
		}

		return ActionResult.SUCCESS;
	}
}
