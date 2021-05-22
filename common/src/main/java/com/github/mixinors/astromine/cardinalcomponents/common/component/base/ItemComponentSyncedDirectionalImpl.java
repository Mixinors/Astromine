package com.github.mixinors.astromine.cardinalcomponents.common.component.base;

import com.github.mixinors.astromine.cardinalcomponents.common.component.Component;
import net.minecraft.item.ItemStack;

public class ItemComponentSyncedDirectionalImpl extends ItemComponentDirectionalImpl implements Component.Synced {
	<V> ItemComponentSyncedDirectionalImpl(V v, int size) {
		super(v, size);
	}
	
	<V> ItemComponentSyncedDirectionalImpl(V v, ItemStack... stacks) {
		super(v, stacks);
	}
}
