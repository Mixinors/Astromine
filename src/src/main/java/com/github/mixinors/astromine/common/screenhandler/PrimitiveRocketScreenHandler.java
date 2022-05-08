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

package com.github.mixinors.astromine.common.screenhandler;

import com.github.mixinors.astromine.common.entity.base.RocketEntity;
import com.github.mixinors.astromine.common.screenhandler.base.entity.ExtendedEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
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
		
		launchButtonWidget.setPosition(new Position(tab, 3.0F + 4.0F, 11.0F, 0.0F));
		launchButtonWidget.setSize(new Size(48.0F, 18.0F, 0.0F));
		launchButtonWidget.setLabel(new TranslatableText("text.astromine.rocket.launch"));
		launchButtonWidget.setDisabled(() -> entity.getDataTracker().get(RocketEntity.IS_RUNNING) || (entity.getFluidStorage().getStorage(0).isResourceBlank() && entity.getFluidStorage().getStorage(1).isResourceBlank()));
		
		var abortButtonWidget = new ButtonWidget(() -> {
			((RocketEntity) entity).tryDisassemble(true);
			
			return null;
		});
		
		abortButtonWidget.setPosition(new Position(tab, 3.0F + 4.0F, 11.0F + fluidBar.getHeight() - 18.0F, 0.0F));
		abortButtonWidget.setSize(new Size(48.0F, 18.0F, 0.0F));
		abortButtonWidget.setLabel(new TranslatableText("text.astromine.rocket.destroy").formatted(Formatting.RED));
		
		fluidBar.setPosition(new Position(width / 2.0F - fluidBar.getWidth() / 2.0F + 2.0F, fluidBar.getY(), 0.0F));
		
		var firstInput = new SlotWidget(0, entity.getItemStorage());
		firstInput.setPosition(new Position(fluidBar, -18.0F - 3.0F, 0.0F, 0.0F));
		firstInput.setSize(new Size(18.0F, 18.0F, 0.0F));
		
		var firstOutput = new SlotWidget(1, entity.getItemStorage());
		firstOutput.setPosition(new Position(fluidBar, -18.0F - 3.0F, fluidBar.getHeight() - 18.0F, 0.0F));
		firstOutput.setSize(new Size(18.0F, 18.0F, 0.0F));
		
		var secondFluidBar = new FluidBarWidget();
		secondFluidBar.setPosition(new Position(fluidBar, 24.0F + 18.0F + 3.0F + 3.0F, 0.0F, 0.0F));
		secondFluidBar.setSize(new Size(24.0F, 48.0F, 0.0F));
		secondFluidBar.setStorage(entity.getFluidStorage().getStorage(1));
		secondFluidBar.setSmooth(false);
		
		var secondInput = new SlotWidget(2, entity.getItemStorage());
		secondInput.setPosition(new Position(secondFluidBar, -18.0F - 3.0F, 0.0F, 0.0F));
		secondInput.setSize(new Size(18.0F, 18.0F, 0.0F));
		
		var secondOutput = new SlotWidget(3, entity.getItemStorage());
		secondOutput.setPosition(new Position(secondFluidBar, -18.0F - 3.0F, secondFluidBar.getHeight() - 18.0F, 0.0F));
		secondOutput.setSize(new Size(18.0F, 18.0F, 0.0F));
		
		tab.add(launchButtonWidget);
		tab.add(abortButtonWidget);
		
		tab.add(secondFluidBar);
		
		tab.add(firstInput);
		tab.add(firstOutput);
		
		tab.add(secondInput);
		tab.add(secondOutput);
	}
}
