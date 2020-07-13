package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.DyeColor;

import com.github.chainmailstudios.astromine.access.DyeColorAccess;

@Mixin(DyeColor.class)
public class DyeColorMixin implements DyeColorAccess {
    @Shadow
    int color;

    public int astromine_getColor() {
        return color;
    }
}
