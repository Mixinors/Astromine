package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.patchouli.AlloySmeltingPage;
import com.github.mixinors.astromine.client.patchouli.PressingPage;
import com.github.mixinors.astromine.client.patchouli.TrituratingPage;
import com.github.mixinors.astromine.registry.client.AMPatchouliPages;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.patchouli.client.book.ClientBookRegistry;

@Mixin(AMPatchouliPages.class)
public class FAMPatchouliPagesMixin {
	@Environment(EnvType.CLIENT)
	@Overwrite
	@SuppressWarnings("all")
	public static void init() {
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.id("triturating"), TrituratingPage.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.id("pressing"), PressingPage.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(AMCommon.id("alloy_smelting"), AlloySmeltingPage.class);
	}
}
