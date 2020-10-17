package com.github.chainmailstudios.astromine.common.block.redstone;

import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public enum RedstoneType {
    WORK_WHEN_ON,
    WORK_WHEN_OFF,
    WORK_ALWAYS;

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

    public TranslatableText asText() {
        switch (this) {
            case WORK_WHEN_OFF: {
                return (TranslatableText) new TranslatableText("text.astromine.work_when_off").formatted(Formatting.RED);
            }

            case WORK_WHEN_ON: {
                return (TranslatableText) new TranslatableText("text.astromine.work_when_on").formatted(Formatting.GREEN);
            }

            case WORK_ALWAYS: {
                return (TranslatableText) new TranslatableText("text.astromine.work_always").formatted(Formatting.YELLOW);
            }

            default: {
                return null;
            }
        }
    }
}
