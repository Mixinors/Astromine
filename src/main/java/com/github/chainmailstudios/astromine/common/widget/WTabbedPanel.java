package com.github.chainmailstudios.astromine.common.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WTabHolder;
import spinnery.widget.api.*;

import java.net.CacheRequest;
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
}
