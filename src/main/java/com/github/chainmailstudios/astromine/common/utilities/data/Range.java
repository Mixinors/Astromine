package com.github.chainmailstudios.astromine.common.utilities.data;

public class Range<T extends Number> {
    private final T minimum;
    private final T maximum;

    public Range(T minimum, T maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public static <T extends Number> Range<T> of(T minimum, T maximum) {
        return new Range<>(minimum, maximum);
    }

    public T getMinimum() {
        return this.minimum;
    }

    public T getMaximum() {
        return this.maximum;
    }
}
