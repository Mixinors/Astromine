package com.github.chainmailstudios.astromine.transportations.common.block;

import com.github.chainmailstudios.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.utilities.TextUtilities;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.FluidFilterBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.ItemFilterBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidFilterBlock extends HorizontalFacingBlockWithEntity {
	public FluidFilterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			FluidFilterBlockEntity blockEntity = (FluidFilterBlockEntity) world.getBlockEntity(pos);

			ItemStack stack = player.getStackInHand(hand);

			Fluid fluid = FluidComponent.get(stack).getFirst().getFluid();

			if (stack.isEmpty()) {
				blockEntity.setFilterFluid(Fluids.EMPTY);
			} else {
				blockEntity.setFilterFluid(fluid);
			}

			player.sendMessage(new TranslatableText("text.astromine.fluid_filter_use", TextUtilities.getFluid(fluid)), false);
		}

		return ActionResult.CONSUME;
	}

	@Override
	public boolean hasScreenHandler() {
		return false;
	}

	@Override
	public BlockEntity createBlockEntity() {
		return new ItemFilterBlockEntity();
	}

	@Override
	public ScreenHandler createScreenHandler(BlockState state, World world, BlockPos pos, int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return null;
	}

	@Override
	public void populateScreenHandlerBuffer(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, PacketByteBuf buffer) {}
}
