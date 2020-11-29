package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.registry.AstromineTooltips;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Inject(at = @At("RETURN"), method = "getName()Lnet/minecraft/text/Text;")
    void onGetName(CallbackInfoReturnable<Text> returnable) {
        MutableText text = (MutableText) returnable.getReturnValue();
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
