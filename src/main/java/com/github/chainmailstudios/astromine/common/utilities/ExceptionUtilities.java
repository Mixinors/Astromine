package com.github.chainmailstudios.astromine.common.utilities;

import java.util.function.Supplier;

public class ExceptionUtilities {
	public static final Supplier<UnsupportedOperationException> REGISTRY_KEY_NOT_FOUND = () -> new UnsupportedOperationException("Registry key did not exist in specified registry!");
}
