package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.recipe.AstromineRecipeType;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
	/**
	 * @reason We are doing this to tell minecraft to shut up
	 */
	@Inject(method = "getGroupForRecipe", cancellable = true, at = @At("HEAD"))
	private static void getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
		if (recipe.getType() instanceof AstromineRecipeType)
			cir.setReturnValue(RecipeBookGroup.UNKNOWN);
	}
}
