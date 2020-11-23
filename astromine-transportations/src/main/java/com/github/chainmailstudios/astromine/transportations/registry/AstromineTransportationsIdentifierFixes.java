package com.github.chainmailstudios.astromine.transportations.registry;

import com.github.chainmailstudios.astromine.registry.AstromineIdentifierFixes;
import org.jetbrains.annotations.ApiStatus;

public class AstromineTransportationsIdentifierFixes extends AstromineIdentifierFixes {
	public static void initialize() {
		initializeFixesOnePointTwelve();
	}

	@ApiStatus.AvailableSince("1.12.0")
	public static void initializeFixesOnePointTwelve() {
		register("incinerator", "shredder");
	}
}
