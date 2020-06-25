package com.github.chainmailstudios.astromine.common.block.entity;

import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedItemBlockEntity;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;

public class CreativeBufferBlockEntity extends DefaultedItemBlockEntity implements Tickable {
	public CreativeBufferBlockEntity() {
		super(AstromineBlockEntityTypes.CREATIVE_BUFFER);

		itemComponent.getStack(0).setCount(itemComponent.getStack(0).getMaxCount());
	}

	@Override
	public void tick() {
		itemComponent.getStack(0).setCount(itemComponent.getStack(0).getMaxCount());
	}
}
