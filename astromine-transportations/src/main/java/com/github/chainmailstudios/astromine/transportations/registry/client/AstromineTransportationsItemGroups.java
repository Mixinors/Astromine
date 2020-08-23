package com.github.chainmailstudios.astromine.transportations.registry.client;

import com.github.chainmailstudios.astromine.registry.AstromineItemGroups;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlocks;
import net.minecraft.item.ItemGroup;

public class AstromineTransportationsItemGroups extends AstromineItemGroups {
	public static final ItemGroup TRANSPORTATIONS = register("transportations", () -> AstromineTransportationsBlocks.BASIC_CONVEYOR);

	public static void initialize() {

	}
}
