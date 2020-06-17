package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.registry.AstromineItems;
import com.github.chainmailstudios.astromine.registry.AstrominePotions;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeRegistryMixin {
	@Shadow
	private static native void registerPotionRecipe(Potion input, Item item, Potion output);

	@Redirect(method = "registerDefaults",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/recipe/BrewingRecipeRegistry;registerPotionRecipe(Lnet/minecraft/potion/Potion;Lnet/minecraft/item/Item;Lnet/minecraft/potion/Potion;)V",
					ordinal = 0),
			slice = @Slice(from = @At(value = "FIELD",
					target = "Lnet/minecraft/item/Items;SUGAR:Lnet/minecraft/item/Item;")))
	private static void yoteMundane(Potion input, Item item, Potion output) {
		// corb
	}

	@Inject(method = "registerDefaults",
			at = @At("TAIL"))
	private static void addRocketFueling(CallbackInfo ci) {
		registerPotionRecipe(Potions.WATER, Items.SUGAR, AstrominePotions.SUGAR_WATER);
		registerPotionRecipe(AstrominePotions.SUGAR_WATER, AstromineItems.YEAST, AstrominePotions.ROCKET_FUEL);
	}
}
