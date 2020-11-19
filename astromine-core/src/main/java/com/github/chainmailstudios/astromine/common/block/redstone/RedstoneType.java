package com.github.chainmailstudios.astromine.common.block.redstone;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

/**
 * An enum representing redstone behavior.
 */
public enum RedstoneType {
    WORK_WHEN_ON,
    WORK_WHEN_OFF,
    WORK_ALWAYS;

    /** Returns this type as a {@link Text}. */
    public Text asText() {
        switch (this) {
            case WORK_WHEN_OFF: {
                return new TranslatableText("text.astromine.work_when_off").formatted(Formatting.RED);
            }

            case WORK_WHEN_ON: {
                return new TranslatableText("text.astromine.work_when_on").formatted(Formatting.GREEN);
            }

            case WORK_ALWAYS: {
                return new TranslatableText("text.astromine.work_always").formatted(Formatting.YELLOW);
            }

            default: {
                return null;
            }
        }
    }

    /** Returns this type as a number. */
    public int asNumber() {
        switch (this) {

            case WORK_WHEN_ON: {
                return 1;
            }

            case WORK_ALWAYS: {
                return 2;
            }

            default: {
                return 0;
            }
        }
    }

    /** Returns the type corresponding to the given number. */
    public static RedstoneType byNumber(int number) {
        switch (number) {
            case 1: {
                return WORK_WHEN_ON;
            }

            case 2: {
                return WORK_ALWAYS;
            }

            default: {
                return WORK_WHEN_OFF;
            }
        }
    }
}
