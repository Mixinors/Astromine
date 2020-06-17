package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;

public class AstromineResources {
	public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(AstromineCommon.MOD_ID + ":runtime");

	public static void initialize() {
		RRPCallback.EVENT.register(packs -> packs.add(1, RESOURCE_PACK));
	}
}
