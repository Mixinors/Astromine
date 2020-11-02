/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.technologies.client.rei;

import com.github.chainmailstudios.astromine.technologies.client.rei.fluidmixing.FluidMixingDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.rei.AstromineRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.technologies.client.rei.alloysmelting.AlloySmeltingCategory;
import com.github.chainmailstudios.astromine.technologies.client.rei.alloysmelting.AlloySmeltingDisplay;
import com.github.chainmailstudios.astromine.technologies.client.rei.electricsmelting.ElectricSmeltingCategory;
import com.github.chainmailstudios.astromine.technologies.client.rei.electricsmelting.ElectricSmeltingDisplay;
import com.github.chainmailstudios.astromine.technologies.client.rei.electrolyzing.ElectrolyzingCategory;
import com.github.chainmailstudios.astromine.technologies.client.rei.electrolyzing.ElectrolyzingDisplay;
import com.github.chainmailstudios.astromine.technologies.client.rei.fluidmixing.FluidMixingCategory;
import com.github.chainmailstudios.astromine.technologies.client.rei.liquidgenerating.LiquidGeneratingCategory;
import com.github.chainmailstudios.astromine.technologies.client.rei.liquidgenerating.LiquidGeneratingDisplay;
import com.github.chainmailstudios.astromine.technologies.client.rei.solidgenerating.SolidGeneratingCategory;
import com.github.chainmailstudios.astromine.technologies.client.rei.solidgenerating.SolidGeneratingDisplay;
import com.github.chainmailstudios.astromine.technologies.client.rei.pressing.PressingCategory;
import com.github.chainmailstudios.astromine.technologies.client.rei.pressing.PressingDisplay;
import com.github.chainmailstudios.astromine.technologies.client.rei.refining.RefiningCategory;
import com.github.chainmailstudios.astromine.technologies.client.rei.refining.RefiningDisplay;
import com.github.chainmailstudios.astromine.technologies.client.rei.triturating.TrituratingCategory;
import com.github.chainmailstudios.astromine.technologies.client.rei.triturating.TrituratingDisplay;
import com.github.chainmailstudios.astromine.technologies.client.rei.wire_milling.WireMillingCategory;
import com.github.chainmailstudios.astromine.technologies.client.rei.wire_milling.WireMillingDisplay;
import com.github.chainmailstudios.astromine.technologies.common.recipe.AlloySmeltingRecipe;
import com.github.chainmailstudios.astromine.technologies.common.recipe.ElectrolyzingRecipe;
import com.github.chainmailstudios.astromine.technologies.common.recipe.FluidMixingRecipe;
import com.github.chainmailstudios.astromine.technologies.common.recipe.LiquidGeneratingRecipe;
import com.github.chainmailstudios.astromine.technologies.common.recipe.PressingRecipe;
import com.github.chainmailstudios.astromine.technologies.common.recipe.RefiningRecipe;
import com.github.chainmailstudios.astromine.technologies.common.recipe.TrituratingRecipe;
import com.github.chainmailstudios.astromine.technologies.common.recipe.WireMillingRecipe;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;

import java.util.Collections;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class AstromineTechnologiesRoughlyEnoughItemsPlugin extends AstromineRoughlyEnoughItemsPlugin {
	public static final Identifier TRITURATING = AstromineCommon.identifier("triturating");
	public static final Identifier ELECTRIC_SMELTING = AstromineCommon.identifier("electric_smelting");
	public static final Identifier LIQUID_GENERATING = AstromineCommon.identifier("liquid_generating");
	public static final Identifier SOLID_GENERATING = AstromineCommon.identifier("solid_generating");
	public static final Identifier FLUID_MIXING = AstromineCommon.identifier("fluid_mixing");
	public static final Identifier ELECTROLYZING = AstromineCommon.identifier("electrolyzing");
	public static final Identifier REFINING = AstromineCommon.identifier("refining");
	public static final Identifier PRESSING = AstromineCommon.identifier("pressing");
	public static final Identifier WIREMILLING = AstromineCommon.identifier("wire_milling");
	public static final Identifier ALLOY_SMELTING = AstromineCommon.identifier("alloy_smelting");

	@Override
	public Identifier getPluginIdentifier() {
		return AstromineCommon.identifier("technologies_rei_plugin");
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategories(new TrituratingCategory(), new ElectricSmeltingCategory(), new LiquidGeneratingCategory(), new SolidGeneratingCategory(), new PressingCategory(), new WireMillingCategory(), new AlloySmeltingCategory(), new FluidMixingCategory(FLUID_MIXING,
			"category.astromine.fluid_mixing", EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER)), new ElectrolyzingCategory(ELECTROLYZING, "category.astromine.electrolyzing", EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER)),
			new RefiningCategory(REFINING, "category.astromine.refining", EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_REFINERY)));
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(TRITURATING, TrituratingRecipe.class, TrituratingDisplay::new);
		recipeHelper.registerRecipes(ELECTRIC_SMELTING, SmeltingRecipe.class, ElectricSmeltingDisplay::new);
		recipeHelper.registerRecipes(LIQUID_GENERATING, LiquidGeneratingRecipe.class, LiquidGeneratingDisplay::new);
		recipeHelper.registerRecipes(FLUID_MIXING, FluidMixingRecipe.class, FluidMixingDisplay::new);
		recipeHelper.registerRecipes(ELECTROLYZING, ElectrolyzingRecipe.class, ElectrolyzingDisplay::new);
		recipeHelper.registerRecipes(REFINING, RefiningRecipe.class, RefiningDisplay::new);
		recipeHelper.registerRecipes(PRESSING, PressingRecipe.class, PressingDisplay::new);
		recipeHelper.registerRecipes(WIREMILLING, WireMillingRecipe.class, WireMillingDisplay::new);
		recipeHelper.registerRecipes(ALLOY_SMELTING, AlloySmeltingRecipe.class, AlloySmeltingDisplay::new);

		for (Map.Entry<Item, Integer> entry : AbstractFurnaceBlockEntity.createFuelTimeMap().entrySet()) {
			if (!(entry.getKey() instanceof BucketItem) && entry != null && entry.getValue() > 0) {
				recipeHelper.registerDisplay(new SolidGeneratingDisplay((entry.getValue() / 2F * 5) / (entry.getValue() / 2F) * 6, Collections.singletonList(EntryStack.create(entry.getKey())), null, (entry.getValue() / 2) / 6.0));
			}
		}
	}

	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(TRITURATING, EntryStack.create(AstromineTechnologiesBlocks.PRIMITIVE_TRITURATOR), EntryStack.create(AstromineTechnologiesBlocks.BASIC_TRITURATOR), EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_TRITURATOR), EntryStack.create(
			AstromineTechnologiesBlocks.ELITE_TRITURATOR));
		recipeHelper.registerWorkingStations(ELECTRIC_SMELTING, EntryStack.create(AstromineTechnologiesBlocks.PRIMITIVE_ELECTRIC_SMELTER), EntryStack.create(AstromineTechnologiesBlocks.BASIC_ELECTRIC_SMELTER), EntryStack.create(
			AstromineTechnologiesBlocks.ADVANCED_ELECTRIC_SMELTER), EntryStack.create(AstromineTechnologiesBlocks.ELITE_ELECTRIC_SMELTER));
		recipeHelper.registerWorkingStations(LIQUID_GENERATING, EntryStack.create(AstromineTechnologiesBlocks.PRIMITIVE_LIQUID_GENERATOR), EntryStack.create(AstromineTechnologiesBlocks.BASIC_LIQUID_GENERATOR), EntryStack.create(
			AstromineTechnologiesBlocks.ADVANCED_LIQUID_GENERATOR), EntryStack.create(AstromineTechnologiesBlocks.ELITE_LIQUID_GENERATOR));
		recipeHelper.registerWorkingStations(SOLID_GENERATING, EntryStack.create(AstromineTechnologiesBlocks.PRIMITIVE_SOLID_GENERATOR), EntryStack.create(AstromineTechnologiesBlocks.BASIC_SOLID_GENERATOR), EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_SOLID_GENERATOR),
			EntryStack.create(AstromineTechnologiesBlocks.ELITE_SOLID_GENERATOR));
		recipeHelper.registerWorkingStations(FLUID_MIXING, EntryStack.create(AstromineTechnologiesBlocks.PRIMITIVE_FLUID_MIXER), EntryStack.create(AstromineTechnologiesBlocks.BASIC_FLUID_MIXER), EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_FLUID_MIXER), EntryStack
			.create(AstromineTechnologiesBlocks.ELITE_FLUID_MIXER));
		recipeHelper.registerWorkingStations(ELECTROLYZING, EntryStack.create(AstromineTechnologiesBlocks.PRIMITIVE_ELECTROLYZER), EntryStack.create(AstromineTechnologiesBlocks.BASIC_ELECTROLYZER), EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_ELECTROLYZER), EntryStack
			.create(AstromineTechnologiesBlocks.ELITE_ELECTROLYZER));
		recipeHelper.registerWorkingStations(REFINING, EntryStack.create(AstromineTechnologiesBlocks.PRIMITIVE_REFINERY), EntryStack.create(AstromineTechnologiesBlocks.BASIC_REFINERY), EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_REFINERY), EntryStack.create(
			AstromineTechnologiesBlocks.ELITE_REFINERY));
		recipeHelper.registerWorkingStations(PRESSING, EntryStack.create(AstromineTechnologiesBlocks.PRIMITIVE_PRESSER), EntryStack.create(AstromineTechnologiesBlocks.BASIC_PRESSER), EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_PRESSER), EntryStack.create(
			AstromineTechnologiesBlocks.ELITE_PRESSER));
		recipeHelper.registerWorkingStations(WIREMILLING, EntryStack.create(AstromineTechnologiesBlocks.PRIMITIVE_WIREMILL), EntryStack.create(AstromineTechnologiesBlocks.BASIC_WIREMILL), EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_WIREMILL), EntryStack.create(
			AstromineTechnologiesBlocks.ELITE_WIREMILL));
		recipeHelper.registerWorkingStations(ALLOY_SMELTING, EntryStack.create(AstromineTechnologiesBlocks.PRIMITIVE_ALLOY_SMELTER), EntryStack.create(AstromineTechnologiesBlocks.BASIC_ALLOY_SMELTER), EntryStack.create(AstromineTechnologiesBlocks.ADVANCED_ALLOY_SMELTER),
			EntryStack.create(AstromineTechnologiesBlocks.ELITE_ALLOY_SMELTER));

		recipeHelper.registerAutoCraftButtonArea(LIQUID_GENERATING, bounds -> new Rectangle(0, 0, 0, 0));
		recipeHelper.registerAutoCraftButtonArea(SOLID_GENERATING, bounds -> new Rectangle(bounds.getCenterX() - 55 + 110 - 16, bounds.getMaxY() - 16, 10, 10));
		recipeHelper.registerAutoCraftButtonArea(FLUID_MIXING, bounds -> new Rectangle(0, 0, 0, 0));
		recipeHelper.registerAutoCraftButtonArea(ELECTROLYZING, bounds -> new Rectangle(0, 0, 0, 0));
		recipeHelper.registerAutoCraftButtonArea(REFINING, bounds -> new Rectangle(0, 0, 0, 0));
	}
}
