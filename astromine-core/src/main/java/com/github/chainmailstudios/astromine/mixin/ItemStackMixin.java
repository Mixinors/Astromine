package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine. registry.AstromineTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Inject(at = @At("RETURN"), method = "getDisplayName")
    void onGetName(CallbackInfoReturnable<Component> returnable) {
        MutableComponent text = (MutableComponent) returnable.getReturnValue();
        Style style = returnable.getReturnValue().getStyle();

        if (AstromineTooltips.isPrimitive(getItem())) {
            text.setStyle(style.withColor(AstromineTooltips.PRIMITIVE_COLOR));
        }

        if (AstromineTooltips.isBasic(getItem())) {
            text.setStyle(style.withColor(AstromineTooltips.BASIC_COLOR));
        }

        if (AstromineTooltips.isAdvanced(getItem())) {
            text.setStyle(style.withColor(AstromineTooltips.ADVANCED_COLOR));
        }

        if (AstromineTooltips.isElite(getItem())) {
            text.setStyle(style.withColor(AstromineTooltips.ELITE_COLOR));
        }

        if (AstromineTooltips.isCreative(getItem())) {
            text.setStyle(style.withColor(AstromineTooltips.CREATIVE_COLOR));
        }

        if (AstromineTooltips.isSpecial(getItem())) {
            text.setStyle(style.withColor(AstromineTooltips.SPECIAL_COLOR));
        }
    }
}
