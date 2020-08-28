package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.registry.AstromineItemGroups;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import net.minecraft.item.ItemGroup;

public class AstromineDiscoveriesItemGroups extends AstromineItemGroups {
	public static final ItemGroup DISCOVERIES = register("discoveries", () -> AstromineDiscoveriesItems.SPACE_SLIME_BALL);

	public static void initialize() {

	}
}
