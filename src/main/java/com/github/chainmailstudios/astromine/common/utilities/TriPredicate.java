package com.github.chainmailstudios.astromine.common.utilities;

@FunctionalInterface
public interface TriPredicate<MAYBE, THIS, AMAZING> {
	boolean test(MAYBE first, THIS second, AMAZING third);
}
