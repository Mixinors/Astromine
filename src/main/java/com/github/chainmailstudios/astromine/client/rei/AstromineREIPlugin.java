package com.github.chainmailstudios.astromine.client.rei;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

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
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.ClientHelper;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import me.shedaniel.rei.api.widgets.Tooltip;
import me.shedaniel.rei.gui.widget.EntryWidget;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.impl.RenderingEntry;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

	public static List<Widget> createEnergyDisplay(Rectangle bounds, Fraction energy, boolean generating, long speed) {
		return Collections.singletonList(
				new EnergyEntryWidget(bounds, speed, generating).entry(
						new RenderingEntry() {
							@Override
							public void render(MatrixStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {}

							@Override
							public @Nullable Tooltip getTooltip(Point mouse) {
								if (generating) return Tooltip.create(
										mouse,
										new TranslatableText("text.astromine.energy"),
										ClientHelper.getInstance().getFormattedModFromIdentifier(AstromineCommon.identifier("a")),
										new LiteralText(""),
										new TranslatableText("category.astromine.generating.energy", FluidUtilities.rawFraction(energy)),
										new TranslatableText("category.astromine.generating.energy", energy.toPrettyString())
								);
								else return Tooltip.create(
										mouse,
										new TranslatableText("text.astromine.energy"),
										ClientHelper.getInstance().getFormattedModFromIdentifier(AstromineCommon.identifier("a")),
										new LiteralText(""),
										new TranslatableText("category.astromine.consuming.energy", FluidUtilities.rawFraction(energy)),
										new TranslatableText("category.astromine.consuming.energy", energy.toPrettyString())
								);
							}
						}
				).notFavoritesInteractable()
		);
	}

	public static List<Widget> createFluidDisplay(Rectangle bounds, EntryStack fluidStack, Fraction consumedPerTick, long speed) {
		return Collections.singletonList(new FluidEntryWidget(bounds, speed).setConsumedPerTick(consumedPerTick).markInput().entry(fluidStack.copy()));
	}

	private static class EnergyEntryWidget extends EntryWidget {
		private long speed;
		private boolean generating;

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
				if (generating) height = bounds.height - MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				else height = MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				DrawableHelper.drawTexture(matrices, bounds.x, bounds.y + height, 0, height, bounds.width - 1,
						bounds.height - height - 1, bounds.width, bounds.height);
			}
		}

		@Override
		protected void drawCurrentEntry(MatrixStack matrices, int mouseX, int mouseY, float delta) {}
	}

	private static class FluidEntryWidget extends EntryWidget {
		private long speed;
		@Nullable
		private Fraction consumedPerTick;

		protected FluidEntryWidget(Rectangle rectangle, long speed) {
			super(rectangle.x, rectangle.y);
			this.getBounds().setBounds(rectangle);
			this.speed = speed;
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
					tooltip.getText().add(new TranslatableText("category.astromine.fluid.generating.consumed", FluidUtilities.rawFraction(consumedPerTick), consumedPerTick.toPrettyString()));
				}
				return tooltip;
			}).orElse(null);
		}

		@Override
		protected void drawCurrentEntry(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			EntryStack entry = getCurrentEntry();
			if (entry.getType() == EntryStack.Type.FLUID) {
				Rectangle bounds = getBounds();
				int height = bounds.height - MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				VertexConsumerProvider.Immediate consumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
				SpriteRenderer.beginPass()
						.setup(consumers, RenderLayer.getSolid())
						.sprite(FluidUtilities.texture(entry.getFluid())[0])
						.color(FluidUtilities.color(MinecraftClient.getInstance().player, entry.getFluid()))
						.light(0x00f000f0)
						.overlay(OverlayTexture.DEFAULT_UV)
						.alpha(0xff)
						.normal(matrices.peek().getNormal(), 0, 0, 0)
						.position(matrices.peek().getModel(),
								bounds.x + 1,
								bounds.y + bounds.height - height + 1,
								bounds.x + bounds.width - 1,
								bounds.y + bounds.height - 1, getZOffset() + 1)
						.next(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
				consumers.draw();
			}
		}
	}
}
