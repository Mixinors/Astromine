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

package com.github.mixinors.astromine.common.screenhandler;

import com.github.mixinors.astromine.common.entity.base.RocketEntity;
import com.github.mixinors.astromine.common.screenhandler.base.entity.ExtendedEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.common.geometry.position.Position;
import dev.vini2003.hammer.common.geometry.size.Size;
import dev.vini2003.hammer.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.common.widget.button.ButtonWidget;
import dev.vini2003.hammer.common.widget.slot.SlotWidget;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class PrimitiveRocketScreenHandler extends ExtendedEntityScreenHandler {
	public PrimitiveRocketScreenHandler(int syncId, PlayerEntity player, int entityId) {
		super(AMScreenHandlers.ROCKET, syncId, player, entityId);
	}

	@Override
	public ItemStack getSymbol() {
		return new ItemStack(AMItems.PRIMITIVE_ROCKET.get());
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		var launchButtonWidget = new ButtonWidget(() -> {
			((RocketEntity) entity).tryLaunch(this.getPlayer());

			return null;
		});

		launchButtonWidget.setPosition(Position.of(mainTab, 3.0F + 4.0F, 11.0F, 0.0F));
		launchButtonWidget.setSize(Size.of(48.0F, 18.0F, 0.0F));
		launchButtonWidget.setLabel(new TranslatableText("text.astromine.rocket.launch"));
		launchButtonWidget.setDisabled(() -> entity.getDataTracker().get(RocketEntity.IS_RUNNING) || (entity.getFluidStorage().getStorage(0).isResourceBlank() && entity.getFluidStorage().getStorage(1).isResourceBlank()));
		
		var abortButtonWidget = new ButtonWidget(() -> {
			((RocketEntity) entity).tryDisassemble(true);

			return null;
		});

		abortButtonWidget.setPosition(Position.of(mainTab, 3.0F + 4.0F, 11.0F + fluidBar.getHeight() - 18.0F, 0.0F));
		abortButtonWidget.setSize(Size.of(48.0F, 18.0F, 0.0F));
		abortButtonWidget.setLabel(new TranslatableText("text.astromine.rocket.destroy").formatted(Formatting.RED));

		fluidBar.setPosition( Position.of(width / 2.0F - fluidBar.getWidth() / 2.0F + 2.0F, fluidBar.getY(), 0.0F));
		
		var firstInput = new SlotWidget(0, entity.getItemStorage());
		firstInput.setPosition(Position.of(fluidBar, -18.0F - 3.0F, 0.0F, 0.0F));
		firstInput.setSize(Size.of(18.0F, 18.0F, 0.0F));
		
		var firstOutput = new SlotWidget(1, entity.getItemStorage());
		firstOutput.setPosition(Position.of(fluidBar, -18.0F - 3.0F, fluidBar.getHeight() - 18.0F, 0.0F));
		firstOutput.setSize(Size.of(18.0F, 18.0F, 0.0F));
		
		var secondFluidBar = new FluidBarWidget();
		secondFluidBar.setPosition(Position.of(fluidBar, 24.0F + 18.0F + 3.0F + 3.0F, 0.0F, 0.0F));
		secondFluidBar.setSize(Size.of(24.0F, 48.0F, 0.0F));
		secondFluidBar.setStorage(entity.getFluidStorage().getStorage(1));
		
		var secondInput = new SlotWidget(2, entity.getItemStorage());
		secondInput.setPosition(Position.of(secondFluidBar, -18.0F - .0F, 0.0F, 0.0F));
		secondInput.setSize(Size.of(18.0F, 18.0F, 0.0F));
		
		var secondOutput = new SlotWidget(3, entity.getItemStorage());
		secondOutput.setPosition(Position.of(secondFluidBar, -18.0F - 3.0F, secondFluidBar.getHeight() - 18.0F, 0.0F));
		secondOutput.setSize(Size.of(18.0F, 18.0F, 0.0F));

		mainTab.add(launchButtonWidget);
		mainTab.add(abortButtonWidget);

		mainTab.add(secondFluidBar);

		mainTab.add(firstInput);
		mainTab.add(firstOutput);

		mainTab.add(secondInput);
		mainTab.add(secondOutput);
	}
}
