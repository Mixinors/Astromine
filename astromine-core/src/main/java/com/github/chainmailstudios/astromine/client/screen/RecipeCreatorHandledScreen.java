package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.common.screenhandler.RecipeCreatorScreenHandler;
import com.github.vini2003.blade.client.handler.BaseHandledScreen;
import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link BaseHandledScreen} used by {@link RecipeCreatorScreenHandler}.
 */
public class RecipeCreatorHandledScreen extends BaseHandledScreen<RecipeCreatorScreenHandler> {
    /** Instantiates a {@link RecipeCreatorHandledScreen}. */
    public RecipeCreatorHandledScreen(@NotNull BaseScreenHandler handler, @NotNull PlayerInventory inventory, @NotNull Text title) {
        super(handler, inventory, title);

        playerInventoryTitleX = -Integer.MAX_VALUE;
        playerInventoryTitleY = -Integer.MAX_VALUE;
    }
}
