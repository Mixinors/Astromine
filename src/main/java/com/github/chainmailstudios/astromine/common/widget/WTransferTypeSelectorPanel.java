package com.github.chainmailstudios.astromine.common.widget;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.NameableComponent;
import com.google.common.collect.ImmutableSet;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WTabHolder;
import spinnery.widget.WTexturedButton;
import spinnery.widget.api.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class WTransferTypeSelectorPanel extends WAbstractWidget implements WCollection, WDelegatedEventListener {
	WTabHolder tabs;

	BlockEntityTransferComponent component;

	ComponentProvider provider;

	public WTransferTypeSelectorPanel() {
		tabs = new WTabHolder().setParent(getParent()).setInterface(getInterface());
		tabs.setMode(WTabHolder.Mode.OCCUPY_PARTIAL);
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return ImmutableSet.<WAbstractWidget>builder().add(tabs).build();
	}

	@Override
	public boolean contains(WAbstractWidget... widgets) {
		return Arrays.stream(widgets).allMatch(widget -> getWidgets().contains(widget));
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

	public <W extends WTransferTypeSelectorPanel> W setProvider(ComponentProvider provider) {
		this.provider = provider;
		return (W) this;
	}

	public <W extends WTransferTypeSelectorPanel> W setComponent(BlockEntityTransferComponent component) {
		this.component = component;

		component.get().forEach((type, entry) -> {
			NameableComponent nameable = (NameableComponent) provider.getComponent(type);

			createTab(nameable.getSymbol(), nameable.getName(), type);
		});

		return (W) this;
	}

	public BlockEntityTransferComponent getComponent() {
		return component;
	}

	public void createTab(Item symbol, Text name, ComponentType<?> type) {
		WTabHolder.WTab tab = tabs.addTab(symbol);

		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(this, 7 + 22, 31 + 22, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.NORTH));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(this, 7 + 22, 31 + 44, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.SOUTH));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(this, 7 + 22, 31 + 0, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.UP));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(this, 7 + 0,  31 + 44, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.DOWN));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(this, 7 + 0,  31 + 22, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.WEST));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(this, 7 + 44, 31 + 22, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.EAST));
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		tabs.draw(matrices, provider);
		super.draw(matrices, provider);
	}
}
