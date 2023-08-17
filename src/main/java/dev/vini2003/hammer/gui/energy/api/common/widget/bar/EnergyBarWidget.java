/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
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

package dev.vini2003.hammer.gui.energy.api.common.widget.bar;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.widget.bar.ImageBarWidget;
import dev.vini2003.hammer.gui.energy.api.common.util.EnergyTextUtil;
import net.minecraft.client.gui.screen.Screen;
import team.reborn.energy.api.EnergyStorage;

import java.util.function.Supplier;

public class EnergyBarWidget extends ImageBarWidget {
	public static final Texture STANDARD_FOREGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/energy_bar_filled.png"));
	public static final Texture STANDARD_BACKGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/energy_bar_background.png"));
	
	protected Supplier<EnergyStorage> storage = () -> null;
	
	public EnergyBarWidget() {
		foregroundTexture = () -> STANDARD_FOREGROUND_TEXTURE;
		backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
		
		setTooltipSupplier(() -> {
			if (storage.get() == null) {
				return ImmutableList.of(TextUtil.getEmpty());
			}
			
			if (Screen.hasShiftDown()) {
				return EnergyTextUtil.getDetailedTooltips(storage.get());
			} else {
				return EnergyTextUtil.getShortenedTooltips(storage.get());
			}
		});
	}
	
	public EnergyBarWidget(EnergyStorage storage) {
		this();
		
		current = () -> storage.getAmount();
		maximum = () -> storage.getCapacity();
	}
	
	public void setStorage(Supplier<EnergyStorage> storage) {
		this.storage = storage;
	}
}
