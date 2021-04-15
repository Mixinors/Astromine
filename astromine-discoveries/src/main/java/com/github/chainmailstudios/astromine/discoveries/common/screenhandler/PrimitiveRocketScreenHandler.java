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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.chainmailstudios.astromine.common.screenhandler.base.entity.ComponentEntityFluidItemScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.blade.FluidVerticalBarWidget;
import com.github.chainmailstudios.astromine.discoveries.common.entity.base.RocketEntity;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesItems;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesScreenHandlers;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import com.github.vini2003.blade.common.widget.base.SlotWidget;

public class PrimitiveRocketScreenHandler extends ComponentEntityFluidItemScreenHandler {
	public PrimitiveRocketScreenHandler(int syncId, PlayerEntity player, int entityId) {
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
		launchButtonWidget.setDisabled(() -> entity.getDataTracker().get(RocketEntity.IS_RUNNING) || (entity.getFluidComponent().getFirst().smallerOrEqualThan(0L) && entity.getFluidComponent().getSecond().smallerOrEqualThan(0L)));

		ButtonWidget abortButtonWidget = new ButtonWidget(() -> {
			((RocketEntity) entity).tryDisassemble(true);

			return null;
		});

		abortButtonWidget.setPosition(Position.of(mainTab, 3 + 4, 11 + fluidBar.getHeight() - 18));
		abortButtonWidget.setSize(Size.of(48, 18));
		abortButtonWidget.setLabel(new TranslatableText("text.astromine.rocket.destroy").formatted(Formatting.RED));

		fluidBar.setPosition(Position.of(width / 2F - fluidBar.getWidth() / 2F + 2, fluidBar.getY()));

		SlotWidget firstInput = new SlotWidget(0, entity);
		firstInput.setPosition(Position.of(fluidBar, -18 - 3, 0));
		firstInput.setSize(Size.of(18, 18));

		SlotWidget firstOutput = new SlotWidget(1, entity);
		firstOutput.setPosition(Position.of(fluidBar, -18 - 3, fluidBar.getHeight() - 18));
		firstOutput.setSize(Size.of(18, 18));

		FluidVerticalBarWidget secondFluidBar = new FluidVerticalBarWidget();
		secondFluidBar.setPosition(Position.of(fluidBar, 24 + 18 + 3 + 3, 0));
		secondFluidBar.setSize(Size.of(24F, 48F));
		secondFluidBar.setVolumeSupplier(() -> entity.getFluidComponent().getSecond());

		SlotWidget secondInput = new SlotWidget(2, entity);
		secondInput.setPosition(Position.of(secondFluidBar, -18 - 3, 0));
		secondInput.setSize(Size.of(18, 18));

		SlotWidget secondOutput = new SlotWidget(3, entity);
		secondOutput.setPosition(Position.of(secondFluidBar, -18 - 3, secondFluidBar.getHeight() - 18));
		secondOutput.setSize(Size.of(18, 18));

		mainTab.addWidget(launchButtonWidget);
		mainTab.addWidget(abortButtonWidget);

		mainTab.addWidget(secondFluidBar);

		mainTab.addWidget(firstInput);
		mainTab.addWidget(firstOutput);

		mainTab.addWidget(secondInput);
		mainTab.addWidget(secondOutput);
	}
}
