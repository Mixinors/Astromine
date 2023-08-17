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

package dev.vini2003.hammer.gui.energy.api.common.util;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.util.NumberUtil;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class EnergyTextUtil {
	public static Color COLOR = new Color(0.67F, 0.89F, 0.47F, 1.0F);
	
	public static MutableText getEnergy() {
		return Text.translatable("text.hammer.energy").styled((style) -> style.withColor(COLOR.toRgb()));
	}
	
	public static List<Text> getDetailedTooltips(EnergyStorage storage) {
		var tooltips = new ArrayList<Text>();
		
		tooltips.add(getEnergy());
		tooltips.add(Text.literal(NumberUtil.getPrettyString(storage.getAmount(), "E") + " / " + NumberUtil.getPrettyString(storage.getCapacity(), "E")).formatted(Formatting.GRAY));
		
		return tooltips;
	}
	
	public static List<Text> getShortenedTooltips(EnergyStorage storage) {
		var tooltips = new ArrayList<Text>();
		
		tooltips.add(getEnergy());
		tooltips.add(Text.literal(NumberUtil.getPrettyShortenedString(storage.getAmount(), "E") + " / " + NumberUtil.getPrettyShortenedString(storage.getCapacity(), "E")).formatted(Formatting.GRAY));
		
		return tooltips;
	}
}
