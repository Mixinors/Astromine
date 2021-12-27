/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

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
import com.github.mixinors.astromine.client.render.sprite.SpriteRenderer;
import com.github.mixinors.astromine.common.recipe.*;
import com.github.mixinors.astromine.common.util.ClientUtils;
import com.github.mixinors.astromine.common.util.FluidUtils;
import com.github.mixinors.astromine.common.util.TextUtils;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.fluid.FluidStack;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.AbstractRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.util.ClientEntryStacks;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;

@Environment(EnvType.CLIENT)
public class AMRoughlyEnoughItemsPlugin implements REIClientPlugin {
	private static final Identifier ENERGY_BACKGROUND = AMCommon.id("textures/widget/energy_volume_fractional_vertical_bar_background_thin.png");
	private static final Identifier ENERGY_FOREGROUND = AMCommon.id("textures/widget/energy_volume_fractional_vertical_bar_foreground_thin.png");

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
		registry.add(new SolidifyingCategory(),
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
			
		registry.removePlusButton(FLUID_GENERATING);
		registry.setPlusButtonArea(SOLID_GENERATING, bounds -> new Rectangle(bounds.getCenterX() - 55 + 110 - 16, bounds.getMaxY() - 16, 10, 10));
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
			return new DefaultCustomDisplay(recipe, EntryIngredients.ofIngredients(ImmutableList.of(recipe.getInput(), recipe.getTool())), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
		});

		for (var entry : AbstractFurnaceBlockEntity.createFuelTimeMap().entrySet()) {
			if (!(entry.getKey() instanceof BucketItem) && entry.getValue() > 0) {
				registry.add(new SolidGeneratingDisplay(Collections.singletonList(EntryIngredients.of(entry.getKey())), (entry.getValue() / 2) / 6, (long) ((entry.getValue() / 2F * 5) / (entry.getValue() / 2F) * 6), null));
			}
		}
	}

	public static EntryStack<FluidStack> convertToEntryStack(FluidVariant variant, long amount) {
		return EntryStacks.of(variant.getFluid(), amount);
	}

	public static List<Widget> createEnergyDisplay(Rectangle bounds, long energy, boolean generating, int speed) {
		return Collections.singletonList(new EnergyEntryWidget(bounds, speed, generating).entry(ClientEntryStacks.of(new AbstractRenderer() {
			@Override
			public void render(MatrixStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {}

			@Override
			public @Nullable Tooltip getTooltip(Point mouse) {
				return Tooltip.create(mouse, new TranslatableText("text.astromine.energy"), TextUtils.getEnergyAmount(energy), TextUtils.getAstromine());
			}
		})).notFavoritesInteractable());
	}

	public static List<Widget> createFluidDisplay(Rectangle bounds, List<EntryStack<?>> fluidStacks, boolean generating, long speed) {
		var entry = new FluidEntryWidget(bounds, speed, generating).entries(fluidStacks);
		if (generating)
			entry.markOutput();
		else entry.markInput();
		return Collections.singletonList(entry);
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
				RenderSystem.setShaderTexture(0, ENERGY_BACKGROUND);
				drawTexture(matrices, bounds.x, bounds.y, 0, 0, bounds.width, bounds.height, bounds.width, bounds.height);
				RenderSystem.setShaderTexture(0, ENERGY_FOREGROUND);
				var height = MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				if (generating) {
					height = bounds.height - MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				}
				
				drawTexture(matrices, bounds.x, bounds.y + height, 0, height, bounds.width - 1, bounds.height - height - 1, bounds.width, bounds.height);
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
				RenderSystem.setShaderTexture(0, ENERGY_BACKGROUND);
				drawTexture(matrices, bounds.x, bounds.y, 0, 0, bounds.width, bounds.height, bounds.width, bounds.height);
			}
		}

		@Override
		protected void drawCurrentEntry(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			var entry = getCurrentEntry();
			if (entry.getType() == VanillaEntryTypes.FLUID && !entry.isEmpty()) {
				var bounds = getBounds();
				var height = MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				if (!generating)
					height = bounds.height - MathHelper.ceil((System.currentTimeMillis() / (speed / bounds.height) % bounds.height) / 1f);
				var consumers = ClientUtils.getInstance().getBufferBuilders().getEntityVertexConsumers();
				var fluid = entry.<FluidStack>castValue().getFluid();
				SpriteRenderer.beginPass().setup(consumers, RenderLayer.getSolid()).sprite(FluidUtils.getSprite(fluid)).color(FluidUtils.getColor(ClientUtils.getPlayer(), fluid)).light(0x00f000f0).overlay(OverlayTexture.DEFAULT_UV).alpha(
					0xff).normal(matrices.peek().getNormalMatrix(), 0, 0, 0).position(matrices.peek().getPositionMatrix(), bounds.x + 1, bounds.y + bounds.height - height + 1, bounds.x + bounds.width - 1, bounds.y + bounds.height - 1, getZOffset() + 1).next(
					PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
				consumers.draw();
			}
		}
	}
}
