package com.github.mixinors.astromine.registry.client.fabric;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.compat.patchouli.client.page.AlloySmeltingPage;
import com.github.mixinors.astromine.compat.patchouli.client.page.PressingPage;
import com.github.mixinors.astromine.compat.patchouli.client.page.TrituratingPage;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class AMPatchouliPagesImpl {
	public static void postInit() {
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.id("triturating"), TrituratingPage.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.id("pressing"), PressingPage.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.id("alloy_smelting"), AlloySmeltingPage.class);
	}
}
