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

package com.github.chainmailstudios.astromine.discoveries.common.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.chainmailstudios.astromine.common.screenhandler.base.entity.ComponentEntityFluidItemScreenHandler;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.discoveries.common.entity.base.RocketEntity;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesItems;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesScreenHandlers;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import com.github.vini2003.blade.common.widget.base.TextWidget;

public class RocketScreenHandler extends ComponentEntityFluidItemScreenHandler {
	private TextWidget fuelTextWidget;

	public RocketScreenHandler(int syncId, PlayerEntity player, int entityId) {
		super(AstromineDiscoveriesScreenHandlers.ROCKET, syncId, player, entityId);
	}

	@Override
	public ItemStack getSymbol() {
		return new ItemStack(AstromineDiscoveriesItems.PRIMITIVE_ROCKET);
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		ButtonWidget launchButtonWidget = new ButtonWidget(() -> {
			((RocketEntity) entity).tryLaunch(this.getPlayer());

			return null;
		});

		launchButtonWidget.setPosition(Position.of(mainTab, 3 + 4, 11));
		launchButtonWidget.setSize(Size.of(48, 18));
		launchButtonWidget.setLabel(new TranslatableText("text.astromine.rocket.launch"));
		launchButtonWidget.setDisabled(() -> entity.getDataTracker().get(RocketEntity.IS_RUNNING) || entity.getFluidComponent().getFirst().smallerOrEqualThan(Fraction.EMPTY));

		ButtonWidget abortButtonWidget = new ButtonWidget(() -> {
			((RocketEntity) entity).tryDisassemble(true);

			return null;
		});

		abortButtonWidget.setPosition(Position.of(mainTab, 3 + 4, 11 + fluidBar.getHeight() - 18));
		abortButtonWidget.setSize(Size.of(48, 18));
		abortButtonWidget.setLabel(new TranslatableText("text.astromine.rocket.destroy").formatted(Formatting.RED));

		fluidBar.setPosition(Position.of(width / 2F - fluidBar.getWidth() / 2F + 2, fluidBar.getY()));

		SlotWidget input = new SlotWidget(0, entity);
		input.setPosition(Position.of(fluidBar, -18 - 3, 0));
		input.setSize(Size.of(18, 18));

		SlotWidget output = new SlotWidget(1, entity);
		output.setPosition(Position.of(fluidBar, -18 - 3, fluidBar.getHeight() - 18));
		output.setSize(Size.of(18, 18));

		fuelTextWidget = new TextWidget();

		mainTab.addWidget(launchButtonWidget);
		mainTab.addWidget(abortButtonWidget);

		mainTab.addWidget(input);
		mainTab.addWidget(output);
	}

	public void setFuelText(Text text) {
		this.fuelTextWidget.setText(text);
	}
}
