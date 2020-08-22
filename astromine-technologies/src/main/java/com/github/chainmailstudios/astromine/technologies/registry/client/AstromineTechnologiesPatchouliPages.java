package com.github.chainmailstudios.astromine.technologies.registry.client;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.client.AstrominePatchouliPages;
import com.github.chainmailstudios.astromine.technologies.client.patchouli.AlloySmeltingPage;
import com.github.chainmailstudios.astromine.technologies.client.patchouli.PressingPage;
import com.github.chainmailstudios.astromine.technologies.client.patchouli.TrituratingPage;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class AstromineTechnologiesPatchouliPages extends AstrominePatchouliPages {
	public static void initialize() {
		ClientBookRegistry.INSTANCE.pageTypes.put(AstromineCommon.identifier("triturating"), TrituratingPage.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(AstromineCommon.identifier("pressing"), PressingPage.class);
		ClientBookRegistry.INSTANCE.pageTypes.put(AstromineCommon.identifier("alloy_smelting"), AlloySmeltingPage.class);
	}
}
