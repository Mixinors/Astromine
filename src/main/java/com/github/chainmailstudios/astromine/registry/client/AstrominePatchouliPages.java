package com.github.chainmailstudios.astromine.registry.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.patchouli.SortingPage;
import vazkii.patchouli.client.book.ClientBookRegistry;

@Environment(EnvType.CLIENT)
public class AstrominePatchouliPages {
    @Environment(EnvType.CLIENT)
    public static void initialize() {
        ClientBookRegistry.INSTANCE.pageTypes.put(AstromineCommon.identifier("sorting"), SortingPage.class);
    }
}
