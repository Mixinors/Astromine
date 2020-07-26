package com.github.chainmailstudios.astromine.common.block.conveyor.entity;

import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineSoundEvents;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;

public class SplitterBlockEntity extends DoubleMachineBlockEntity {
	public SplitterBlockEntity() {
        super(AstromineBlockEntityTypes.SPLITTER);
    }

    public SplitterBlockEntity(BlockEntityType type) {
        super(type);
    }

	@Override
	public void give(ItemStack stack) {
		int size = stack.getCount();
		int smallHalf = size / 2;
		int largeHalf = size - smallHalf;

		ItemStack smallStack = stack.copy();
		ItemStack largeStack = stack.copy();

		smallStack.setCount(smallHalf);
		largeStack.setCount(largeHalf);

		if (smallStack.getCount() > 0)
			setLeftStack(smallStack);

		if (largeStack.getCount() > 0)
			setRightStack(largeStack);

		world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), AstromineSoundEvents.MACHINE_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}
