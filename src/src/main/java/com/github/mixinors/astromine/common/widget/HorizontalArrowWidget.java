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

package com.github.mixinors.astromine.common.widget;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.util.TextUtils;
import dev.vini2003.hammer.core.api.client.texture.BaseTexture;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.gui.api.common.widget.bar.TextureBarWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.IntSupplier;

public class HorizontalArrowWidget extends TextureBarWidget {
	private static final BaseTexture STANDARD_BACKGROUND_TEXTURE = new ImageTexture(AMCommon.id("textures/widget/horizontal_arrow_background.png"));
	private static final BaseTexture STANDARD_FOREGROUND_TEXTURE = new ImageTexture(AMCommon.id("textures/widget/horizontal_arrow_foreground.png"));
	
	public HorizontalArrowWidget() {
		this.setHorizontal(true);
		this.setSmooth(false);
		
		this.setBackgroundTexture(STANDARD_BACKGROUND_TEXTURE);
		this.setForegroundTexture(STANDARD_FOREGROUND_TEXTURE);
	}
	
	private IntSupplier progressSupplier = () -> 0;
	private IntSupplier limitSupplier = () -> 100;
	
	@NotNull
	@Override
	public List<Text> getTooltip() {
		return Collections.singletonList(TextUtils.getRatio(progressSupplier.getAsInt(), limitSupplier.getAsInt()));
	}
	
	public IntSupplier getProgressSupplier() {
		return progressSupplier;
	}
	
	public void setProgressSupplier(IntSupplier progressSupplier) {
		this.progressSupplier = progressSupplier;
	}
	
	public IntSupplier getLimitSupplier() {
		return limitSupplier;
	}
	
	public void setLimitSupplier(IntSupplier limitSupplier) {
		this.limitSupplier = limitSupplier;
	}
}
