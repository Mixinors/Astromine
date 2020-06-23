package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.EpsilionBlockEntity;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import net.minecraft.util.Tickable;

public class CreativeBufferBlockEntity extends EpsilionBlockEntity implements Tickable {
	public CreativeBufferBlockEntity() {
		super(AstromineBlockEntityTypes.CREATIVE_BUFFER);

		itemComponent.getStack(0).setCount(itemComponent.getStack(0).getMaxCount());
	}

	@Override
	public void tick() {
		itemComponent.getStack(0).setCount(itemComponent.getStack(0).getMaxCount());
	}
}
