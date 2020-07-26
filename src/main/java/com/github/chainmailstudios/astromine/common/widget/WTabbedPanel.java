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

package com.github.chainmailstudios.astromine.common.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WTabHolder;
import spinnery.widget.api.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class WTabbedPanel extends WAbstractWidget implements WCollection, WDelegatedEventListener {
	public WTabHolder tabs;

	public WTabbedPanel() {
		tabs = new WTabHolder().setParent(getParent()).setInterface(getInterface());
		tabs.setMode(WTabHolder.Mode.OCCUPY_PARTIAL);
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return Collections.singleton(tabs);
	}

	@Override
	public boolean contains(WAbstractWidget... widgets) {
		return Stream.of(widgets).anyMatch(widget -> getWidgets().contains(widget));
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		return getWidgets();
	}

	@Override
	public void onLayoutChange() {
		tabs.setPosition(Position.of(this)).setSize(Size.of(this));
		super.onLayoutChange();
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		tabs.draw(matrices, provider);

		super.draw(matrices, provider);
	}

	public WTabHolder.WTab addTab(Item item) {
		return tabs.addTab(item);
	}

	public WTabHolder.WTab addTab(Item item, Text text) {
		return tabs.addTab(item);
	}

	@Override
	public <W extends WAbstractWidget> W setInterface(WInterface linkedInterface) {
		tabs.setInterface(linkedInterface);
		return super.setInterface(linkedInterface);
	}

	@Override
	public <W extends WAbstractWidget> W setParent(WLayoutElement parent) {
		tabs.setParent(this);
		return super.setParent(parent);
	}
}
