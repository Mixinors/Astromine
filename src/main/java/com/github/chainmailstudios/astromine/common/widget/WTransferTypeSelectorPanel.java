package com.github.chainmailstudios.astromine.common.widget;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WTabHolder;
import spinnery.widget.api.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class WTransferTypeSelectorPanel extends WAbstractWidget implements WCollection, WDelegatedEventListener {
	WTabHolder tabs;

	BlockEntityTransferComponent component;

	ComponentProvider provider;

	BlockPos blockPos;

	public WTransferTypeSelectorPanel() {
		tabs = new WTabHolder().setParent(getParent()).setInterface(getInterface());
		tabs.setMode(WTabHolder.Mode.OCCUPY_PARTIAL);
	}

	@Override
	public Set<WAbstractWidget> getWidgets() {
		return ImmutableSet.of(tabs);
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

//		component.get().forEach((type, entry) -> {
//			ComponentType<?> componentType = ComponentRegistry.INSTANCE.get(type);
//			if (componentType != null) {
//				NameableComponent nameableComponent = (NameableComponent) provider.getComponent(componentType);
//				createTab(nameableComponent.getSymbol(), nameableComponent.getName(), type);
//			} else {
//				BlockEntityTransferComponent.TransferComponentInfo info = BlockEntityTransferComponent.INFOS.get(type);
//				if (info != null) {
//					createTab(info.getSymbol(), info.getName(), type);
//				}
//			}
//		});

		return (W) this;
	}

	public BlockEntityTransferComponent getComponent() {
		return component;
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public <W extends WTransferTypeSelectorPanel> W setBlockPos(BlockPos blockPos) {
		this.blockPos = blockPos;
		return (W) this;
	}

	public static void createTab(WTabHolder.WTab tab, WPositioned anchor, BlockEntityTransferComponent component, BlockPos blockPos, Identifier type, WInterface wInterface) {
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(anchor, 7 + 22, 31 + 22, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.NORTH).setBlockPos(blockPos).setInterface(wInterface));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(anchor, 7 + 0, 31 + 44, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.SOUTH).setBlockPos(blockPos).setInterface(wInterface));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(anchor, 7 + 22, 31 + 0, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.UP).setBlockPos(blockPos).setInterface(wInterface));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(anchor, 7 + 22, 31 + 44, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.DOWN).setBlockPos(blockPos).setInterface(wInterface));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(anchor, 7 + 44, 31 + 22, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.WEST).setBlockPos(blockPos).setInterface(wInterface));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, Position.of(anchor, 7 + 0, 31 + 22, 0), Size.of(18, 18)).setComponent(component).setType(type).setDirection(Direction.EAST).setBlockPos(blockPos).setInterface(wInterface));
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		tabs.draw(matrices, provider);

		super.draw(matrices, provider);
	}
}
