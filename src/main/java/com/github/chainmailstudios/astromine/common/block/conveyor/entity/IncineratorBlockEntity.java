package com.github.chainmailstudios.astromine.common.block.conveyor.entity;

import com.github.chainmailstudios.astromine.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.common.conveyor.ConveyorType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Direction;

public class IncineratorBlockEntity extends BlockEntity implements Conveyable {
	public boolean hasBeenRemoved = false;

	public IncineratorBlockEntity() {
        super(AstromineBlockEntityTypes.INCINERATOR);
    }

    public IncineratorBlockEntity(BlockEntityType type) {
        super(type);
    }

	@Override
	public boolean hasBeenRemoved() {
		return hasBeenRemoved;
	}

	@Override
	public void setRemoved(boolean hasBeenRemoved) {
		this.hasBeenRemoved = hasBeenRemoved;
	}

	@Override
	public boolean accepts(ItemStack stack) {
		return true;
	}

	@Override
	public boolean validInputSide(Direction direction) {
		return direction == getCachedState().get(HorizontalFacingBlock.FACING).getOpposite();
	}

	@Override
	public boolean isOutputSide(Direction direction, ConveyorType type) {
		return false;
	}

	@Override
	public void give(ItemStack stack) {
		getWorld().playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.125F, 1.0F);
	}
}
