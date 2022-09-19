/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.client.rei;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.rei.alloysmelting.AlloySmeltingCategory;
import com.github.mixinors.astromine.client.rei.alloysmelting.AlloySmeltingDisplay;
import com.github.mixinors.astromine.client.rei.electricsmelting.ElectricSmeltingCategory;
import com.github.mixinors.astromine.client.rei.electricsmelting.ElectricSmeltingDisplay;
import com.github.mixinors.astromine.client.rei.electrolyzing.ElectrolyzingCategory;
import com.github.mixinors.astromine.client.rei.electrolyzing.ElectrolyzingDisplay;
import com.github.mixinors.astromine.client.rei.fluidgenerating.FluidGeneratingCategory;
import com.github.mixinors.astromine.client.rei.fluidgenerating.FluidGeneratingDisplay;
import com.github.mixinors.astromine.client.rei.fluidmixing.FluidMixingCategory;
import com.github.mixinors.astromine.client.rei.fluidmixing.FluidMixingDisplay;
import com.github.mixinors.astromine.client.rei.melting.MeltingCategory;
import com.github.mixinors.astromine.client.rei.melting.MeltingDisplay;
import com.github.mixinors.astromine.client.rei.pressing.PressingCategory;
import com.github.mixinors.astromine.client.rei.pressing.PressingDisplay;
import com.github.mixinors.astromine.client.rei.refining.RefiningCategory;
import com.github.mixinors.astromine.client.rei.refining.RefiningDisplay;
import com.github.mixinors.astromine.client.rei.solidgenerating.SolidGeneratingCategory;
import com.github.mixinors.astromine.client.rei.solidgenerating.SolidGeneratingDisplay;
import com.github.mixinors.astromine.client.rei.solidifying.SolidifyingCategory;
import com.github.mixinors.astromine.client.rei.solidifying.SolidifyingDisplay;
import com.github.mixinors.astromine.client.rei.triturating.TrituratingCategory;
import com.github.mixinors.astromine.client.rei.triturating.TrituratingDisplay;
import com.github.mixinors.astromine.client.rei.wiremilling.WireMillingCategory;
import com.github.mixinors.astromine.client.rei.wiremilling.WireMillingDisplay;
import com.github.mixinors.astromine.common.recipe.*;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.fluid.FluidStack;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.scissor.Scissors;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.client.util.LayerUtil;
import dev.vini2003.hammer.gui.api.client.screen.base.BaseHandledScreen;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.energy.api.common.util.EnergyTextUtil;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.AbstractRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.DisplayBoundsProvider;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.util.ClientEntryStacks;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BucketItem;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public class AMRoughlyEnoughItemsPlugin implements REIClientPlugin {
	private static final Identifier THIN_ENERGY_BACKGROUND_TEXTURE = AMCommon.id("textures/widget/thin_energy_bar_background.png");
	private static final Identifier THIN_ENERGY_FOREGROUND_TEXTURE = AMCommon.id("textures/widget/thin_energy_bar_foreground.png");
	
	public static final CategoryIdentifier<TrituratingDisplay> TRITURATING = CategoryIdentifier.of(AMCommon.id("triturating"));
	public static final CategoryIdentifier<ElectricSmeltingDisplay> ELECTRIC_SMELTING = CategoryIdentifier.of(AMCommon.id("electric_smelting"));
	public static final CategoryIdentifier<FluidGeneratingDisplay> FLUID_GENERATING = CategoryIdentifier.of(AMCommon.id("fluid_generating"));
	public static final CategoryIdentifier<SolidGeneratingDisplay> SOLID_GENERATING = CategoryIdentifier.of(AMCommon.id("solid_generating"));
	public static final CategoryIdentifier<FluidMixingDisplay> FLUID_MIXING = CategoryIdentifier.of(AMCommon.id("fluid_mixing"));
	public static final CategoryIdentifier<ElectrolyzingDisplay> ELECTROLYZING = CategoryIdentifier.of(AMCommon.id("electrolyzing"));
	public static final CategoryIdentifier<RefiningDisplay> REFINING = CategoryIdentifier.of(AMCommon.id("refining"));
	public static final CategoryIdentifier<PressingDisplay> PRESSING = CategoryIdentifier.of(AMCommon.id("pressing"));
	public static final CategoryIdentifier<MeltingDisplay> MELTING = CategoryIdentifier.of(AMCommon.id("melting"));
	public static final CategoryIdentifier<WireMillingDisplay> WIRE_MILLING = CategoryIdentifier.of(AMCommon.id("wire_milling"));
	public static final CategoryIdentifier<AlloySmeltingDisplay> ALLOY_SMELTING = CategoryIdentifier.of(AMCommon.id("alloy_smelting"));
	public static final CategoryIdentifier<SolidifyingDisplay> SOLIDIFYING = CategoryIdentifier.of(AMCommon.id("solidifying"));
	
	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(
				new SolidifyingCategory(),
				new TrituratingCategory(),
				new ElectricSmeltingCategory(),
				new FluidGeneratingCategory(),
				new SolidGeneratingCategory(),
				new PressingCategory(),
				new MeltingCategory(),
				new WireMillingCategory(),
				new AlloySmeltingCategory(),
				new FluidMixingCategory(),
				new ElectrolyzingCategory(),
				new RefiningCategory()
		);
		
		registry.addWorkstations(TRITURATING, EntryStacks.of(AMBlocks.PRIMITIVE_TRITURATOR.get()), EntryStacks.of(AMBlocks.BASIC_TRITURATOR.get()), EntryStacks.of(AMBlocks.ADVANCED_TRITURATOR.get()), EntryStacks.of(AMBlocks.ELITE_TRITURATOR.get()));
		registry.addWorkstations(ELECTRIC_SMELTING, EntryStacks.of(AMBlocks.PRIMITIVE_ELECTRIC_FURNACE.get()), EntryStacks.of(AMBlocks.BASIC_ELECTRIC_FURNACE.get()), EntryStacks.of(AMBlocks.ADVANCED_ELECTRIC_FURNACE.get()), EntryStacks.of(AMBlocks.ELITE_ELECTRIC_FURNACE.get()));
		registry.addWorkstations(FLUID_GENERATING, EntryStacks.of(AMBlocks.PRIMITIVE_FLUID_GENERATOR.get()), EntryStacks.of(AMBlocks.BASIC_FLUID_GENERATOR.get()), EntryStacks.of(AMBlocks.ADVANCED_FLUID_GENERATOR.get()), EntryStacks.of(AMBlocks.ELITE_FLUID_GENERATOR.get()));
		registry.addWorkstations(SOLID_GENERATING, EntryStacks.of(AMBlocks.PRIMITIVE_SOLID_GENERATOR.get()), EntryStacks.of(AMBlocks.BASIC_SOLID_GENERATOR.get()), EntryStacks.of(AMBlocks.ADVANCED_SOLID_GENERATOR.get()), EntryStacks.of(AMBlocks.ELITE_SOLID_GENERATOR.get()));
		registry.addWorkstations(FLUID_MIXING, EntryStacks.of(AMBlocks.PRIMITIVE_FLUID_MIXER.get()), EntryStacks.of(AMBlocks.BASIC_FLUID_MIXER.get()), EntryStacks.of(AMBlocks.ADVANCED_FLUID_MIXER.get()), EntryStacks.of(AMBlocks.ELITE_FLUID_MIXER.get()));
		registry.addWorkstations(ELECTROLYZING, EntryStacks.of(AMBlocks.PRIMITIVE_ELECTROLYZER.get()), EntryStacks.of(AMBlocks.BASIC_ELECTROLYZER.get()), EntryStacks.of(AMBlocks.ADVANCED_ELECTROLYZER.get()), EntryStacks.of(AMBlocks.ELITE_ELECTROLYZER.get()));
		registry.addWorkstations(REFINING, EntryStacks.of(AMBlocks.PRIMITIVE_REFINERY.get()), EntryStacks.of(AMBlocks.BASIC_REFINERY.get()), EntryStacks.of(AMBlocks.ADVANCED_REFINERY.get()), EntryStacks.of(AMBlocks.ELITE_REFINERY.get()));
		registry.addWorkstations(PRESSING, EntryStacks.of(AMBlocks.PRIMITIVE_PRESSER.get()), EntryStacks.of(AMBlocks.BASIC_PRESSER.get()), EntryStacks.of(AMBlocks.ADVANCED_PRESSER.get()), EntryStacks.of(AMBlocks.ELITE_PRESSER.get()));
		registry.addWorkstations(MELTING, EntryStacks.of(AMBlocks.PRIMITIVE_MELTER.get()), EntryStacks.of(AMBlocks.BASIC_MELTER.get()), EntryStacks.of(AMBlocks.ADVANCED_MELTER.get()), EntryStacks.of(AMBlocks.ELITE_MELTER.get()));
		registry.addWorkstations(WIRE_MILLING, EntryStacks.of(AMBlocks.PRIMITIVE_WIRE_MILL.get()), EntryStacks.of(AMBlocks.BASIC_WIRE_MILL.get()), EntryStacks.of(AMBlocks.ADVANCED_WIRE_MILL.get()), EntryStacks.of(AMBlocks.ELITE_WIRE_MILL.get()));
		registry.addWorkstations(ALLOY_SMELTING, EntryStacks.of(AMBlocks.PRIMITIVE_ALLOY_SMELTER.get()), EntryStacks.of(AMBlocks.BASIC_ALLOY_SMELTER.get()), EntryStacks.of(AMBlocks.ADVANCED_ALLOY_SMELTER.get()), EntryStacks.of(AMBlocks.ELITE_ALLOY_SMELTER.get()));
		registry.addWorkstations(SOLIDIFYING, EntryStacks.of(AMBlocks.PRIMITIVE_SOLIDIFIER.get()), EntryStacks.of(AMBlocks.BASIC_SOLIDIFIER.get()), EntryStacks.of(AMBlocks.ADVANCED_SOLIDIFIER.get()), EntryStacks.of(AMBlocks.ELITE_SOLIDIFIER.get()));
		
		// TODO: Remove!
		registry.removePlusButton(FLUID_GENERATING);
		registry.setPlusButtonArea(SOLID_GENERATING, bounds -> new Rectangle(bounds.getCenterX() - 55.0F + 110.0F - 16.0F, bounds.getMaxY() - 16.0F, 10.0F, 10.0F));
		registry.removePlusButton(FLUID_MIXING);
		registry.removePlusButton(ELECTROLYZING);
		registry.removePlusButton(REFINING);
	}
	
	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registry.registerFiller(TrituratingRecipe.class, TrituratingDisplay::new);
		registry.registerFiller(SmeltingRecipe.class, ElectricSmeltingDisplay::new);
		registry.registerFiller(FluidGeneratingRecipe.class, FluidGeneratingDisplay::new);
		registry.registerFiller(FluidMixingRecipe.class, FluidMixingDisplay::new);
		registry.registerFiller(ElectrolyzingRecipe.class, ElectrolyzingDisplay::new);
		registry.registerFiller(RefiningRecipe.class, RefiningDisplay::new);
		registry.registerFiller(PressingRecipe.class, PressingDisplay::new);
		registry.registerFiller(MeltingRecipe.class, MeltingDisplay::new);
		registry.registerFiller(WireMillingRecipe.class, WireMillingDisplay::new);
		registry.registerFiller(AlloySmeltingRecipe.class, AlloySmeltingDisplay::new);
		registry.registerFiller(SolidifyingRecipe.class, SolidifyingDisplay::new);
		
		registry.registerFiller(WireCuttingRecipe.class, recipe -> {
			return new DefaultCustomDisplay(recipe, EntryIngredients.ofIngredients(ImmutableList.of(recipe.getInput(), recipe.getTool())), ImmutableList.of(EntryIngredients.of(recipe.getOutput())));
		});
		
		for (var entry : AbstractFurnaceBlockEntity.createFuelTimeMap().entrySet()) {
			if (!(entry.getKey() instanceof BucketItem) && entry.getValue() > 0) {
				registry.add(new SolidGeneratingDisplay(ImmutableList.of(EntryIngredients.of(entry.getKey())), (int) ((entry.getValue() / 2.0F) / 6.0F), (long) ((entry.getValue() / 2.0F * 5.0F) / (entry.getValue() / 2.0F) * 6.0F), null));
			}
		}
	}
	
	public static EntryStack<FluidStack> convertToEntryStack(FluidVariant variant, long amount) {
		return EntryStacks.of(variant.getFluid(), amount);
	}
	
	public static ImmutableList<Widget> createEnergyDisplay(Rectangle bounds, long energy, boolean generating, int speed) {
		return ImmutableList.of(new EnergyEntryWidget(bounds, speed, generating).entry(ClientEntryStacks.of(new AbstractRenderer() {
			@Override
			public void render(MatrixStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {}
			
			@Override
			public @Nullable Tooltip getTooltip(TooltipContext context) {
				return Tooltip.create(context.getPoint(), EnergyTextUtil.getEnergy().styled(style -> style.withColor(EnergyTextUtil.COLOR.toRgb())), Text.literal("" + energy + "E").formatted(Formatting.GRAY));
			}
		})).notFavoritesInteractable());
	}
	
	public static ImmutableList<Widget> createFluidDisplay(Rectangle bounds, List<EntryStack<?>> fluidStacks, boolean generating, long speed) {
		var entry = new FluidEntryWidget(bounds, speed, generating).entries(fluidStacks);
		
		if (generating) {
			entry.markOutput();
		} else {
			entry.markInput();
		}
		
		return ImmutableList.of(entry);
	}
	
	@Override
	public void registerScreens(ScreenRegistry registry) {
		REIClientPlugin.super.registerScreens(registry);
		
		registry.registerDecider(new DisplayBoundsProvider<BaseHandledScreen>() {
			@Override
			public @Nullable Rectangle getScreenBounds(BaseHandledScreen screen) {
				var handler = (BaseScreenHandler) screen.getScreenHandler();
				
				var rectangle = handler.getScreenHandler().getShape();
				
				return new Rectangle(rectangle.getStartPos().getX(), rectangle.getStartPos().getY(), rectangle.getWidth(), rectangle.getHeight());
			}
			
			@Override
			public <R extends Screen> boolean isHandingScreen(Class<R> screen) {
				return BaseHandledScreen.class.isAssignableFrom(screen);
			}
		});
	}
	
	private static class EnergyEntryWidget extends EntryWidget {
		private final long speed;
		private final boolean generating;
		
		protected EnergyEntryWidget(Rectangle rectangle, long speed, boolean generating) {
			super(new Point(rectangle.x, rectangle.y));
			
			this.getBounds().setBounds(rectangle);
			
			this.speed = speed;
			this.generating = generating;
		}
		
		@Override
		protected void drawBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			if (background) {
				var bounds = getBounds();
				
				var provider = InstanceUtil.getClient().getBufferBuilders().getEntityVertexConsumers();
				
				var height = (float) Math.ceil((float) bounds.height - ((double) System.currentTimeMillis() / (double) (speed / bounds.height) % (double) bounds.height));
				
				if (generating) {
					height = (float) Math.ceil((float) ((double) System.currentTimeMillis() / (double) (speed / bounds.height) % (double) bounds.height));
				}
				
				DrawingUtil.drawTexturedQuad(
						matrices,
						provider,
						THIN_ENERGY_BACKGROUND_TEXTURE,
						bounds.x, bounds.y, 0.0F,
						bounds.width, bounds.height,
						0.0F, 0.0F,
						1.0F, 1.0F,
						0.0F, 0.0F, 0.0F,
						DrawingUtil.DEFAULT_OVERLAY,
						DrawingUtil.DEFAULT_LIGHT,
						Color.WHITE,
						LayerUtil.get(THIN_ENERGY_BACKGROUND_TEXTURE)
				);
				
				var scissors = new Scissors((float) bounds.x, (float) bounds.y + ((float) bounds.height - height), (float) bounds.width, height, provider);
				
				DrawingUtil.drawTexturedQuad(
						matrices,
						provider,
						THIN_ENERGY_FOREGROUND_TEXTURE,
						bounds.x, bounds.y, 0.0F,
						bounds.width, bounds.height,
						0.0F, 0.0F,
						1.0F, 1.0F,
						0.0F, 0.0F, 0.0F,
						DrawingUtil.DEFAULT_OVERLAY,
						DrawingUtil.DEFAULT_LIGHT,
						Color.WHITE,
						LayerUtil.get(THIN_ENERGY_FOREGROUND_TEXTURE)
				);
				
				
				scissors.destroy();
			}
		}
		
		@Override
		protected void drawCurrentEntry(MatrixStack matrices, int mouseX, int mouseY, float delta) {}
	}
	
	private static class FluidEntryWidget extends EntryWidget {
		private final long speed;
		private final boolean generating;
		
		protected FluidEntryWidget(Rectangle rectangle, long speed, boolean generating) {
			super(new Point(rectangle.x, rectangle.y));
			
			this.getBounds().setBounds(rectangle);
			
			this.speed = speed;
			this.generating = generating;
		}
		
		@Override
		protected void drawBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			if (background) {
				var bounds = getBounds();
				
				RenderSystem.setShaderTexture(0, THIN_ENERGY_BACKGROUND_TEXTURE);
				
				drawTexture(matrices, bounds.x, bounds.y, 0, 0, bounds.width, bounds.height, bounds.width, bounds.height);
			}
		}
		
		@Override
		protected void drawCurrentEntry(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			var entry = getCurrentEntry();
			
			if (entry.getType() == VanillaEntryTypes.FLUID && !entry.isEmpty()) {
				var bounds = getBounds();
				
				var height = (float) Math.ceil((float) ((double) System.currentTimeMillis() / (double) (speed / bounds.height) % (double) bounds.height));
				
				if (!generating) {
					height = (float) Math.ceil(((float) (bounds.height - ((double) System.currentTimeMillis() / (double) (speed / bounds.height) % (double) bounds.height))));
				}
				
				var provider = InstanceUtil.getClient().getBufferBuilders().getEntityVertexConsumers();
				
				var fluidStack = (FluidStack) entry.getValue();
				var fluid = fluidStack.getFluid();
				
				var fluidVariant = FluidVariant.of(fluid);
				
				var sprite = FluidVariantRendering.getSprite(fluidVariant);
				var spriteColor = FluidVariantRendering.getColor(fluidVariant);
				
				DrawingUtil.drawTiledTexturedQuad(
						matrices,
						provider,
						sprite.getId(),
						bounds.x + 1.0F, bounds.y + (bounds.height - height) + 1.0F, getZOffset() + 1.0F,
						bounds.width - 2.0F, height - 2.0F,
						sprite.getWidth(),
						sprite.getHeight(),
						Integer.MAX_VALUE, Integer.MAX_VALUE,
						0.0F, 0.0F,
						sprite.getMinU(),
						sprite.getMinV(),
						sprite.getMaxU(),
						sprite.getMaxV(),
						0.0F,
						0.0F,
						0.0F,
						DrawingUtil.DEFAULT_OVERLAY,
						DrawingUtil.DEFAULT_LIGHT,
						new Color(spriteColor),
						RenderLayer.getSolid()
				);
				
				provider.draw();
			}
		}
	}
}
