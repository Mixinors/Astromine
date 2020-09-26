package com.github.chainmailstudios.astromine.common.utilities;

public class NumberUtilities {
    public static String shorten(double value, String unit) {
        if (value < 1000) {
            return value + "";
        }
        int exponent = (int) (Math.log(value) / Math.log(1000));
        String[] units = new String[]{ "k" + unit, "M" + unit, "G" + unit, "T" + unit, "P" + unit, "E" + unit, "Z" + unit, "Y" + unit };
        return String.format("%.1f%s", value / Math.pow(1000, exponent), units[exponent - 1]);
    }
}
