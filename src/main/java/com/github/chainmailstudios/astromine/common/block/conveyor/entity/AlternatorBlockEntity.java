package com.github.chainmailstudios.astromine.common.block.conveyor.entity;

import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineSoundEvents;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;

public class AlternatorBlockEntity extends DoubleMachineBlockEntity {
	public boolean right = false;

	public AlternatorBlockEntity() {
        super(AstromineBlockEntityTypes.ALTERNATOR);
    }

    public AlternatorBlockEntity(BlockEntityType type) {
        super(type);
    }

	@Override
	public void give(ItemStack stack) {
		ItemStack copyStack = stack.copy();

		if (right) {
			setRightStack(copyStack);
		} else {
			setLeftStack(copyStack);
		}
		right = !right;

		getWorld().playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), AstromineSoundEvents.MACHINE_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}
