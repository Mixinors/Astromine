package com.github.chainmailstudios.astromine.transportations.registry;

import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.transportations.registry.client.AstromineTransportationsItemGroups;
import net.minecraft.item.Item;

public class AstromineTransportationsItems extends AstromineItems {
	public static Item.Settings getBasicSettings() {
		return new Item.Settings().group(AstromineTransportationsItemGroups.TRANSPORTATIONS);
	}

	public static void initialize() {

	}
}
