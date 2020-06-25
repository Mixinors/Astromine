package com.github.chainmailstudios.astromine.client.rei;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.rei.electricalsmelting.ElectricalSmeltingCategory;
import com.github.chainmailstudios.astromine.client.rei.electricalsmelting.ElectricalSmeltingDisplay;
import com.github.chainmailstudios.astromine.client.rei.generating.LiquidGeneratingCategory;
import com.github.chainmailstudios.astromine.client.rei.generating.LiquidGeneratingDisplay;
import com.github.chainmailstudios.astromine.client.rei.sorting.SortingCategory;
import com.github.chainmailstudios.astromine.client.rei.sorting.SortingDisplay;
import com.github.chainmailstudios.astromine.client.render.SpriteRenderer;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.github.chainmailstudios.astromine.recipe.LiquidGeneratingRecipe;
import com.github.chainmailstudios.astromine.recipe.SortingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class AstromineREIPlugin implements REIPluginV0 {
	private static final Identifier ENERGY_BACKGROUND = AstromineCommon.identifier("textures/widget/energy_volume_fractional_vertical_bar_background_thin.png");
	private static final Identifier ENERGY_FOREGROUND = AstromineCommon.identifier("textures/widget/energy_volume_fractional_vertical_bar_foreground_thin.png");

	public static final Identifier SORTING = AstromineCommon.identifier("sorting");
	public static final Identifier ELECTRICAL_SMELTING = AstromineCommon.identifier("electrical_smelting");
	public static final Identifier LIQUID_GENERATING = AstromineCommon.identifier("liquid_generating");

	@Override
	public Identifier getPluginIdentifier() {
		return AstromineCommon.identifier("rei_plugin");
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new SortingCategory());
		recipeHelper.registerCategory(new ElectricalSmeltingCategory());
		recipeHelper.registerCategory(new LiquidGeneratingCategory());
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(SORTING, SortingRecipe.class, SortingDisplay::new);
		recipeHelper.registerRecipes(ELECTRICAL_SMELTING, SmeltingRecipe.class, ElectricalSmeltingDisplay::new);
		recipeHelper.registerRecipes(LIQUID_GENERATING, LiquidGeneratingRecipe.class, LiquidGeneratingDisplay::new);
	}

	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(SORTING, EntryStack.create(AstromineBlocks.SORTER));
		recipeHelper.registerWorkingStations(ELECTRICAL_SMELTING, EntryStack.create(AstromineBlocks.ELECTRICAL_SMELTER));
		recipeHelper.registerWorkingStations(LIQUID_GENERATING, EntryStack.create(AstromineBlocks.LIQUID_GENERATOR));
		recipeHelper.registerAutoCraftButtonArea(LIQUID_GENERATING, bounds -> new Rectangle(bounds.getCenterX() - 55 + 5, bounds.y + 5, 10, 10));
	}

	public static List<Widget> createEnergyDisplay(Rectangle bounds, Fraction energy, long speed) {
		return Collections.singletonList(
				Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
					MinecraftClient.getInstance().getTextureManager().bindTexture(ENERGY_BACKGROUND);
					DrawableHelper.drawTexture(matrices, bounds.x, bounds.y, 0, 0, bounds.width, bounds.height, bounds.width, bounds.height);
					MinecraftClient.getInstance().getTextureManager().bindTexture(ENERGY_FOREGROUND);
					int height = bounds.height - MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
					DrawableHelper.drawTexture(matrices, bounds.x, bounds.y + height, 0, height, bounds.width - 1,
							bounds.height - height - 1, bounds.width, bounds.height);
				})
		);
	}

	public static List<Widget> createFluidDisplay(Rectangle bounds, EntryStack fluidStack, long speed) {
		// TODO get this working please
		if (fluidStack.getType() != EntryStack.Type.FLUID) return Collections.emptyList();
		return Collections.singletonList(
				Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
					MinecraftClient.getInstance().getTextureManager().bindTexture(ENERGY_BACKGROUND);
					DrawableHelper.drawTexture(matrices, bounds.x, bounds.y, 0, 0, bounds.width, bounds.height, bounds.width, bounds.height);
					int height = bounds.height - MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
					VertexConsumerProvider.Immediate consumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
					SpriteRenderer.beginPass()
							.setup(consumers, RenderLayer.getSolid())
							.sprite(FluidUtilities.texture(fluidStack.getFluid())[0])
							.color(FluidUtilities.color(MinecraftClient.getInstance().player, fluidStack.getFluid()))
							.light(0x00f000f0)
							.overlay(OverlayTexture.DEFAULT_UV)
							.alpha(0xff)
							.normal(matrices.peek().getNormal(), 0, 0, 0)
							.position(matrices.peek().getModel(),
									bounds.x + 1,
									bounds.y + bounds.height - height + 1,
									bounds.x + bounds.width - 1,
									bounds.y + bounds.height - 1, helper.getZOffset() + 1)
							.next(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
					consumers.draw();
				})
		);
	}
}
