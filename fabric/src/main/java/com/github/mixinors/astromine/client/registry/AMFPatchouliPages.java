package com.github.mixinors.astromine.client.registry;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.patchouli.client.page.AlloySmeltingPage;
import com.github.mixinors.astromine.patchouli.client.page.PressingPage;
import com.github.mixinors.astromine.patchouli.client.page.TrituratingPage;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class AMFPatchouliPages {
	public static void init() {
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.id("triturating"), TrituratingPage.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.id("pressing"), PressingPage.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.id("alloy_smelting"), AlloySmeltingPage.class);
	}
}
