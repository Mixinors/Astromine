package com.github.chainmailstudios.astromine.client.rei;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.utilities.ToolUtilities;
import me.shedaniel.rei.api.BuiltinPlugin;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class AstromineCoreRoughlyEnoughItemsPlugin extends AstromineRoughlyEnoughItemsPlugin {
	@Override
	public Identifier getPluginIdentifier() {
		return AstromineCommon.identifier("core_rei_plugin");
	}

	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		BuiltinPlugin.getInstance().registerInformation(EntryStack.create(ToolUtilities.getAstromineBook()), new TranslatableText("item.astromine.manual"), texts -> {
			texts.add(new TranslatableText("text.astromine.manual.obtain.info"));
			return texts;
		});
	}
}
