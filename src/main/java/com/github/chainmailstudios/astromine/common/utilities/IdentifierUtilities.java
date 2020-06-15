package com.github.chainmailstudios.astromine.common.utilities;

/**
 * Utilities for dealing
 * with Identifiers.
 */
public class IdentifierUtilities {
    /**
     * Assets whether an identifier is valid or not.
     *
     * @param identifier the specified identifier.
     * @return true if yes; false if no.
     */
    public static boolean isValid(String identifier) {
        try {
            return isNamespaceValid(identifier.substring(0, identifier.indexOf(":"))) && isPathValid(identifier.substring(identifier.indexOf(":") + 1));
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * Assets whether an identifier's namespace is valid or not.
     *
     * @param namespace the specified namespace.
     * @return true if yes; false if no.
     */
    private static boolean isNamespaceValid(String namespace) {
        return namespace.chars().allMatch((c) -> {
            return c == 95 || c == 45 || c >= 97 && c <= 122 || c >= 48 && c <= 57 || c == 46;
        });
    }

    /**
     * Assets whether an identifier's path is valid or not.
     *
     * @param path the specified path.
     * @return true if yes; false if no.
     */
    private static boolean isPathValid(String path) {
        return path.chars().allMatch((c) -> {
            return c == 95 || c == 45 || c >= 97 && c <= 122 || c >= 48 && c <= 57 || c == 47 || c == 46;
        });
    }
}
