package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedItemBlockEntity;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;

public class CreativeBufferBlockEntity extends DefaultedItemBlockEntity implements Tickable {
	public CreativeBufferBlockEntity() {
		super(AstromineBlockEntityTypes.CREATIVE_BUFFER);

		itemComponent.getStack(0).setCount(itemComponent.getStack(0).getMaxCount());
	}

	@Override
	protected ItemInventoryComponent createItemComponent() {
		return new SimpleItemInventoryComponent(1);
	}

	@Override
	public void tick() {
		itemComponent.getStack(0).setCount(itemComponent.getStack(0).getMaxCount());
	}
}
