package com.github.chainmailstudios.astromine.client.patchouli;

import com.github.chainmailstudios.astromine.common.recipe.SortingRecipe;
import vazkii.patchouli.client.book.page.abstr.PageSimpleProcessingRecipe;

public class SortingPage extends PageSimpleProcessingRecipe<SortingRecipe> {
    public SortingPage() {
        super(SortingRecipe.Type.INSTANCE);
    }
}
