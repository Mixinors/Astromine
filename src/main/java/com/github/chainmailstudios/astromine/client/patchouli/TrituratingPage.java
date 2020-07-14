package com.github.chainmailstudios.astromine.client.patchouli;

import com.github.chainmailstudios.astromine.common.recipe.TrituratingRecipe;
import vazkii.patchouli.client.book.page.abstr.PageSimpleProcessingRecipe;

public class TrituratingPage extends PageSimpleProcessingRecipe<TrituratingRecipe> {
    public TrituratingPage() {
        super(TrituratingRecipe.Type.INSTANCE);
    }
}
