package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.registry.base.BiRegistry;

import java.util.Optional;

public class IdentifierFixRegistry extends BiRegistry<String, String> {
	public static final IdentifierFixRegistry INSTANCE = new IdentifierFixRegistry();

	@Override
	public String get(String oldPath) {
		String newPath = Optional.ofNullable(super.get(oldPath)).orElse(oldPath);
		if (this.containsKey(newPath)) return this.get(newPath);
		return newPath;
	}

	@Override
	public String register(String oldPath, String newPath) {
		if (this.contains(newPath, oldPath)) {
			throw new IllegalArgumentException("Recursive Identifier fix attempted with paths " + oldPath + " and " + newPath);
		}
		if (this.containsKey(newPath)) {
			return this.register(oldPath, this.get(newPath));
		} else return super.register(oldPath, newPath);
	}
}
