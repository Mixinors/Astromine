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

package com.github.chainmailstudios.astromine.client.rei;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.rei.alloysmelting.AlloySmeltingCategory;
import com.github.chainmailstudios.astromine.client.rei.alloysmelting.AlloySmeltingDisplay;
import com.github.chainmailstudios.astromine.client.rei.electricsmelting.ElectricSmeltingCategory;
import com.github.chainmailstudios.astromine.client.rei.electricsmelting.ElectricSmeltingDisplay;
import com.github.chainmailstudios.astromine.client.rei.fluidmixing.ElectrolyzingCategory;
import com.github.chainmailstudios.astromine.client.rei.fluidmixing.ElectrolyzingDisplay;
import com.github.chainmailstudios.astromine.client.rei.fluidmixing.FluidMixingCategory;
import com.github.chainmailstudios.astromine.client.rei.fluidmixing.FluidMixingDisplay;
import com.github.chainmailstudios.astromine.client.rei.generating.LiquidGeneratingCategory;
import com.github.chainmailstudios.astromine.client.rei.generating.LiquidGeneratingDisplay;
import com.github.chainmailstudios.astromine.client.rei.generating.SolidGeneratingCategory;
import com.github.chainmailstudios.astromine.client.rei.generating.SolidGeneratingDisplay;
import com.github.chainmailstudios.astromine.client.rei.pressing.PressingCategory;
import com.github.chainmailstudios.astromine.client.rei.pressing.PressingDisplay;
import com.github.chainmailstudios.astromine.client.rei.triturating.TrituratingCategory;
import com.github.chainmailstudios.astromine.client.rei.triturating.TrituratingDisplay;
import com.github.chainmailstudios.astromine.client.render.SpriteRenderer;
import com.github.chainmailstudios.astromine.client.screen.base.DefaultedHandledScreen;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.recipe.*;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.ClientHelper;
import me.shedaniel.rei.api.EntryRegistry;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import me.shedaniel.rei.api.widgets.Tooltip;
import me.shedaniel.rei.gui.widget.EntryWidget;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.impl.RenderingEntry;
import me.shedaniel.rei.plugin.DefaultPlugin;
import me.shedaniel.rei.plugin.information.DefaultInformationDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;



import vazkii.patchouli.api.PatchouliAPI;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class AstromineREIPlugin implements REIPluginV0 {
	private static final Identifier ENERGY_BACKGROUND = AstromineCommon.identifier("textures/widget/energy_volume_fractional_vertical_bar_background_thin.png");
	private static final Identifier ENERGY_FOREGROUND = AstromineCommon.identifier("textures/widget/energy_volume_fractional_vertical_bar_foreground_thin.png");

	public static final Identifier TRITURATING = AstromineCommon.identifier("triturating");
	public static final Identifier ELECTRIC_SMELTING = AstromineCommon.identifier("electric_smelting");
	public static final Identifier LIQUID_GENERATING = AstromineCommon.identifier("liquid_generating");
	public static final Identifier SOLID_GENERATING = AstromineCommon.identifier("solid_generating");
	public static final Identifier FLUID_MIXING = AstromineCommon.identifier("fluid_mixing");
	public static final Identifier ELECTROLYZING = AstromineCommon.identifier("electrolyzing");
	public static final Identifier PRESSING = AstromineCommon.identifier("pressing");
	public static final Identifier ALLOY_SMELTING = AstromineCommon.identifier("alloy_smelting");

	@Override
	public Identifier getPluginIdentifier() {
		return AstromineCommon.identifier("rei_plugin");
	}

	@Override
	public void registerEntries(EntryRegistry entryRegistry) {
		entryRegistry.registerEntry(EntryStack.create(PatchouliAPI.instance.getBookStack(AstromineCommon.identifier("manual"))).setting(EntryStack.Settings.CHECK_TAGS, EntryStack.Settings.TRUE));
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategories(new TrituratingCategory(), new ElectricSmeltingCategory(), new LiquidGeneratingCategory(), new SolidGeneratingCategory(), new FluidMixingCategory(FLUID_MIXING, "category.astromine.fluid_mixing", EntryStack.create(
			AstromineBlocks.ADVANCED_FLUID_MIXER)), new ElectrolyzingCategory(ELECTROLYZING, "category.astromine.electrolyzing", EntryStack.create(AstromineBlocks.ADVANCED_ELECTROLYZER)), new PressingCategory(), new AlloySmeltingCategory());
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(TRITURATING, TrituratingRecipe.class, TrituratingDisplay::new);
		recipeHelper.registerRecipes(ELECTRIC_SMELTING, SmeltingRecipe.class, ElectricSmeltingDisplay::new);
		recipeHelper.registerRecipes(LIQUID_GENERATING, LiquidGeneratingRecipe.class, LiquidGeneratingDisplay::new);
		recipeHelper.registerRecipes(SOLID_GENERATING, SolidGeneratingRecipe.class, SolidGeneratingDisplay::new);
		recipeHelper.registerRecipes(FLUID_MIXING, FluidMixingRecipe.class, FluidMixingDisplay::new);
		recipeHelper.registerRecipes(ELECTROLYZING, ElectrolyzingRecipe.class, ElectrolyzingDisplay::new);
		recipeHelper.registerRecipes(PRESSING, PressingRecipe.class, PressingDisplay::new);
		recipeHelper.registerRecipes(ALLOY_SMELTING, AlloySmeltingRecipe.class, AlloySmeltingDisplay::new);

		for (Map.Entry<Item, Integer> entry : AbstractFurnaceBlockEntity.createFuelTimeMap().entrySet()) {
			if (!(entry.getKey() instanceof BucketItem) && entry != null && entry.getValue() > 0) {
				recipeHelper.registerDisplay(new SolidGeneratingDisplay((entry.getValue() / 2 * 5) / (entry.getValue() / 2) * 6, Collections.singletonList(EntryStack.create(entry.getKey())), null, (entry.getValue() / 2) / 6.0));
			}
		}
	}

	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(TRITURATING, EntryStack.create(AstromineBlocks.PRIMITIVE_TRITURATOR), EntryStack.create(AstromineBlocks.BASIC_TRITURATOR), EntryStack.create(AstromineBlocks.ADVANCED_TRITURATOR), EntryStack.create(AstromineBlocks.ELITE_TRITURATOR));
		recipeHelper.registerWorkingStations(ELECTRIC_SMELTING, EntryStack.create(AstromineBlocks.PRIMITIVE_ELECTRIC_SMELTER), EntryStack.create(AstromineBlocks.BASIC_ELECTRIC_SMELTER), EntryStack.create(AstromineBlocks.ADVANCED_ELECTRIC_SMELTER), EntryStack.create(
			AstromineBlocks.ELITE_ELECTRIC_SMELTER));
		recipeHelper.registerWorkingStations(LIQUID_GENERATING, EntryStack.create(AstromineBlocks.PRIMITIVE_LIQUID_GENERATOR), EntryStack.create(AstromineBlocks.BASIC_LIQUID_GENERATOR), EntryStack.create(AstromineBlocks.ADVANCED_LIQUID_GENERATOR), EntryStack.create(
			AstromineBlocks.ELITE_LIQUID_GENERATOR));
		recipeHelper.registerWorkingStations(SOLID_GENERATING, EntryStack.create(AstromineBlocks.PRIMITIVE_SOLID_GENERATOR), EntryStack.create(AstromineBlocks.BASIC_SOLID_GENERATOR), EntryStack.create(AstromineBlocks.ADVANCED_SOLID_GENERATOR), EntryStack.create(
			AstromineBlocks.ELITE_SOLID_GENERATOR));
		recipeHelper.registerWorkingStations(FLUID_MIXING, EntryStack.create(AstromineBlocks.PRIMITIVE_FLUID_MIXER), EntryStack.create(AstromineBlocks.BASIC_FLUID_MIXER), EntryStack.create(AstromineBlocks.ADVANCED_FLUID_MIXER), EntryStack.create(
			AstromineBlocks.ELITE_FLUID_MIXER));
		recipeHelper.registerWorkingStations(ELECTROLYZING, EntryStack.create(AstromineBlocks.PRIMITIVE_ELECTROLYZER), EntryStack.create(AstromineBlocks.BASIC_ELECTROLYZER), EntryStack.create(AstromineBlocks.ADVANCED_ELECTROLYZER), EntryStack.create(
			AstromineBlocks.ELITE_ELECTROLYZER));
		recipeHelper.registerWorkingStations(PRESSING, EntryStack.create(AstromineBlocks.PRIMITIVE_PRESSER), EntryStack.create(AstromineBlocks.BASIC_PRESSER), EntryStack.create(AstromineBlocks.ADVANCED_PRESSER), EntryStack.create(AstromineBlocks.ELITE_PRESSER));
		recipeHelper.registerWorkingStations(ALLOY_SMELTING, EntryStack.create(AstromineBlocks.PRIMITIVE_ALLOY_SMELTER), EntryStack.create(AstromineBlocks.BASIC_ALLOY_SMELTER), EntryStack.create(AstromineBlocks.ADVANCED_ALLOY_SMELTER), EntryStack.create(
			AstromineBlocks.ELITE_ALLOY_SMELTER));
		recipeHelper.registerAutoCraftButtonArea(LIQUID_GENERATING, bounds -> new Rectangle(bounds.getCenterX() - 55 + 110 - 16, bounds.getMaxY() - 16, 10, 10));
		recipeHelper.registerAutoCraftButtonArea(SOLID_GENERATING, bounds -> new Rectangle(bounds.getCenterX() - 55 + 110 - 16, bounds.getMaxY() - 16, 10, 10));
		recipeHelper.registerAutoCraftButtonArea(FLUID_MIXING, bounds -> new Rectangle(bounds.getCenterX() - 65 + 130 - 16, bounds.getMaxY() - 16, 10, 10));
		recipeHelper.registerAutoCraftButtonArea(ELECTROLYZING, bounds -> new Rectangle(bounds.getCenterX() - 55 + 110 - 16 - 29, bounds.getMaxY() - 16, 10, 10));

		DefaultPlugin.registerInfoDisplay(DefaultInformationDisplay.createFromEntry(EntryStack.create(PatchouliAPI.instance.getBookStack(AstromineCommon.identifier("manual"))).setting(EntryStack.Settings.CHECK_TAGS, EntryStack.Settings.TRUE), new TranslatableText(
			"item.astromine.manual")).line(new TranslatableText("text.astromine.manual.obtain.info")));
	}

	public static List<Widget> createEnergyDisplay(Rectangle bounds, double energy, boolean generating, long speed) {
		return Collections.singletonList(new EnergyEntryWidget(bounds, speed, generating).entry(new RenderingEntry() {
			@Override
			public void render(MatrixStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {}

			@Override
			public @Nullable Tooltip getTooltip(Point mouse) {
				if (generating)
					return Tooltip.create(mouse, new TranslatableText("text.astromine.energy"), ClientHelper.getInstance().getFormattedModFromIdentifier(AstromineCommon.identifier("a")), new LiteralText(""), new TranslatableText("category.astromine.generating.energy",
						EnergyUtilities.simpleDisplay(energy)));
				else return Tooltip.create(mouse, new TranslatableText("text.astromine.energy"), ClientHelper.getInstance().getFormattedModFromIdentifier(AstromineCommon.identifier("a")), new LiteralText(""), new TranslatableText("category.astromine.consuming.energy",
					EnergyUtilities.simpleDisplay(energy)));
			}
		}).notFavoritesInteractable());
	}

	public static List<Widget> createFluidDisplay(Rectangle bounds, EntryStack fluidStack, Fraction consumedPerTick, boolean generating, long speed) {
		EntryWidget entry = new FluidEntryWidget(bounds, speed, generating).setConsumedPerTick(consumedPerTick).entry(fluidStack.copy());
		if (generating)
			entry.markOutput();
		else entry.markInput();
		return Collections.singletonList(entry);
	}

	private static class EnergyEntryWidget extends EntryWidget {
		private final long speed;
		private final boolean generating;

		protected EnergyEntryWidget(Rectangle rectangle, long speed, boolean generating) {
			super(rectangle.x, rectangle.y);
			this.getBounds().setBounds(rectangle);
			this.speed = speed;
			this.generating = generating;
		}

		@Override
		protected void drawBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			if (background) {
				Rectangle bounds = getBounds();
				MinecraftClient.getInstance().getTextureManager().bindTexture(ENERGY_BACKGROUND);
				DrawableHelper.drawTexture(matrices, bounds.x, bounds.y, 0, 0, bounds.width, bounds.height, bounds.width, bounds.height);
				MinecraftClient.getInstance().getTextureManager().bindTexture(ENERGY_FOREGROUND);
				int height;
				if (generating)
					height = bounds.height - MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				else height = MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				DrawableHelper.drawTexture(matrices, bounds.x, bounds.y + height, 0, height, bounds.width - 1, bounds.height - height - 1, bounds.width, bounds.height);
			}
		}

		@Override
		protected void drawCurrentEntry(MatrixStack matrices, int mouseX, int mouseY, float delta) {}
	}

	private static class FluidEntryWidget extends EntryWidget {
		private final long speed;
		@Nullable
		private Fraction consumedPerTick;
		private final boolean generating;

		protected FluidEntryWidget(Rectangle rectangle, long speed, boolean generating) {
			super(rectangle.x, rectangle.y);
			this.getBounds().setBounds(rectangle);
			this.speed = speed;
			this.generating = generating;
		}

		public FluidEntryWidget setConsumedPerTick(Fraction consumedPerTick) {
			this.consumedPerTick = consumedPerTick;
			return this;
		}

		@Override
		protected void drawBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			if (background) {
				Rectangle bounds = getBounds();
				MinecraftClient.getInstance().getTextureManager().bindTexture(ENERGY_BACKGROUND);
				DrawableHelper.drawTexture(matrices, bounds.x, bounds.y, 0, 0, bounds.width, bounds.height, bounds.width, bounds.height);
			}
		}

		@Override
		public @Nullable Tooltip getCurrentTooltip(Point point) {
			return Optional.ofNullable(super.getCurrentTooltip(point)).map(tooltip -> {
				if (consumedPerTick != null) {
					tooltip.getText().add(new LiteralText(""));
					if (generating)
						tooltip.getText().add(new TranslatableText("category.astromine.fluid.generating.generated", FluidUtilities.rawFraction(consumedPerTick), consumedPerTick.toDecimalString()));
					else tooltip.getText().add(new TranslatableText("category.astromine.fluid.generating.consumed", FluidUtilities.rawFraction(consumedPerTick), consumedPerTick.toDecimalString()));
				}
				return tooltip;
			}).orElse(null);
		}

		@Override
		protected void drawCurrentEntry(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			EntryStack entry = getCurrentEntry();
			if (entry.getType() == EntryStack.Type.FLUID) {
				Rectangle bounds = getBounds();
				int height;
				if (!generating)
					height = bounds.height - MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				else height = MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				VertexConsumerProvider.Immediate consumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
				SpriteRenderer.beginPass().setup(consumers, RenderLayer.getSolid()).sprite(FluidUtilities.texture(entry.getFluid())[0]).color(FluidUtilities.color(MinecraftClient.getInstance().player, entry.getFluid())).light(0x00f000f0).overlay(OverlayTexture.DEFAULT_UV).alpha(
					0xff).normal(matrices.peek().getNormal(), 0, 0, 0).position(matrices.peek().getModel(), bounds.x + 1, bounds.y + bounds.height - height + 1, bounds.x + bounds.width - 1, bounds.y + bounds.height - 1, getZOffset() + 1).next(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
				consumers.draw();
			}
		}
	}
}
