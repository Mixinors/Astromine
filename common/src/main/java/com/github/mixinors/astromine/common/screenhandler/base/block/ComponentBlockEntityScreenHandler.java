/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.screenhandler.base.block;

import com.github.mixinors.astromine.common.component.block.entity.TransferComponent;
import com.github.mixinors.astromine.common.component.general.provider.EnergyComponentProvider;
import com.github.mixinors.astromine.common.component.general.provider.FluidComponentProvider;
import com.github.mixinors.astromine.common.component.general.provider.ItemComponentProvider;
import com.github.mixinors.astromine.common.util.MirrorUtils;
import com.github.mixinors.astromine.common.widget.blade.TransferTypeSelectorButtonWidget;
import com.github.mixinors.astromine.registry.common.AMComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.mixinors.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.mixinors.astromine.common.component.general.miscellaneous.NamedComponent;
import com.github.mixinors.astromine.common.widget.blade.RedstoneWidget;
import com.github.vini2003.blade.common.collection.TabWidgetCollection;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.utilities.Slots;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import com.github.vini2003.blade.common.widget.base.TabWidget;
import com.github.vini2003.blade.common.widget.base.TextWidget;

import java.util.*;
import java.util.function.Supplier;

/**
 * A {@link BlockStateScreenHandler}with an attached
 * {@link ComponentBlockEntity}.
 */
public abstract class ComponentBlockEntityScreenHandler extends BlockStateScreenHandler {
	protected ComponentBlockEntity blockEntity;

	protected Collection<SlotWidget> playerSlots = new HashSet<>();

	protected TabWidget tabs;

	protected TabWidgetCollection mainTab;

	/** Instantiates a {@link ComponentBlockEntityScreenHandler},
	 * synchronizing its attached {@link ComponentBlockEntity}. */
	public ComponentBlockEntityScreenHandler(Supplier<? extends ScreenHandlerType<?>> type, int syncId, PlayerEntity player, BlockPos position) {
		super(type, syncId, player, position);

		this.blockEntity = (ComponentBlockEntity) player.world.getBlockEntity(position);

		if (!player.world.isClient) {
			blockEntity.doNotSkipInventory();
			blockEntity.syncData();
		}
	}

	/** Returns this {@link ComponentBlockEntityScreenHandler}'s
	 * {@link ComponentBlockEntity}. */
	public ComponentBlockEntity getBlockEntity() {
		return blockEntity;
	}

	/** Returns the {@link Position} at which the {@link TabWidget}
	 * should be located. */
	public Position getTabsPosition(int width, int height) {
		return Position.of(width / 2 - tabs.getWidth()/ 2.0F, height / 2 - tabs.getHeight() / 2);
	}

	/** Returns the {@link Size} which the {@link TabWidget}
	 * should use. */
	public Size getTabsSize(int width, int height) {
		return Size.of(176F, 188F + getTabWidgetExtendedHeight());
	}

	/** Returns the additional height that the {@link TabWidget} should have.
	 * At that, I don't know why this method is a thing. */
	public int getTabWidgetExtendedHeight() {
		return 0;
	}

	/** Override behavior to build the machine interface,
	 * instantiating and configuring the {@link TabWidget},
	 * its tabs, the inventory, and other miscellaneous things. */
	@Override
	public void initialize(int width, int height) {
		tabs = new TabWidget();
		tabs.setSize(getTabsSize(width, height));
		tabs.setPosition(getTabsPosition(width, height));

		this.addWidget(tabs);

		mainTab = (TabWidgetCollection) tabs.addTab(blockEntity.getCachedState().getBlock().asItem(), () -> Collections.singletonList(new TranslatableText(blockEntity.getCachedState().getBlock().getTranslationKey())));
		mainTab.setPosition(Position.of(tabs, 0, 25F + 7F));
		mainTab.setSize(Size.of(176F, 184F));

		var mainTabTitle = new TextWidget();
		mainTabTitle.setPosition(Position.of(mainTab, 8, 0));
		mainTabTitle.setText(new TranslatableText(blockEntity.getCachedState().getBlock().asItem().getTranslationKey()));
		mainTabTitle.setColor(4210752);
		
		mainTab.addWidget(mainTabTitle);

		var inventoryTitlePos = Position.of(tabs, 7F + (tabs.getWidth() / 2 - ((9 * 18F) / 2) - 7F), 25F + 7F + (184 - 18 - 18 - (18 * 4) - 3 + getTabWidgetExtendedHeight()));
		var inventoryTitle = new TextWidget();
		inventoryTitle.setPosition(Position.of(inventoryTitlePos, 0, -10));
		inventoryTitle.setText(getPlayer().inventory.getName());
		inventoryTitle.setColor(4210752);
		
		mainTab.addWidget(inventoryTitle);
		
		playerSlots = Slots.addPlayerInventory(inventoryTitlePos, Size.of(18F, 18F), mainTab, getPlayer().inventory);
		
		var ref = new Object() {
			Direction rotation = Direction.NORTH;
		};

		if (blockEntity.getCachedState().getBlock() instanceof HorizontalFacingBlockWithEntity facingBlock) {
			var property = facingBlock.getDirectionProperty();
			
			if (property != null)
				ref.rotation = blockEntity.getCachedState().get(property);
		}

		var redstoneWidget = new RedstoneWidget();
		redstoneWidget.setPosition(Position.of(tabs, tabs.getWidth() - 20, 0));
		redstoneWidget.setSize(Size.of(20, 19));
		redstoneWidget.setBlockEntity(blockEntity);

		this.addWidget(redstoneWidget);

		var transferComponent = TransferComponent.get(blockEntity);
		
		var tabComponents = new HashMap<NamedComponent, Identifier>();
		
		if (blockEntity instanceof ItemComponentProvider provider) {
			transferComponent.addItem();
			tabComponents.put(provider.getItemComponent(), AMComponents.ITEM);
		}
		
		if (blockEntity instanceof FluidComponentProvider provider) {
			transferComponent.addFluid();
			tabComponents.put(provider.getFluidComponent(), AMComponents.FLUID);
		}
		
		if (blockEntity instanceof EnergyComponentProvider provider) {
			transferComponent.addEnergy();
			tabComponents.put(provider.getEnergyComponent(), AMComponents.ENERGY);
		}
		
		tabComponents.forEach((tabComponent, id) -> {
			var symbol = tabComponent.getSymbol();
			var name = tabComponent.getText();
			
			var tab = (TabWidgetCollection) tabs.addTab(symbol, () -> List.of(name));
			var anchor = Position.of(tabs, tabs.getWidth() / 2.0F - 38.0F, getTabWidgetExtendedHeight() / 2.0F);

			var positions = Map.of(
					Direction.NORTH, Position.of(anchor, 7.0F + 22.0F, 31.0F + 22.0F),
					Direction.SOUTH, Position.of(anchor, 7.0F + 0.0F, 31.0F + 44.0F),
					Direction.WEST, Position.of(anchor, 7.0F + 44.0F, 31.0F + 22.0F),
					Direction.EAST, Position.of(anchor, 7.0F + 0.0F, 31.0F + 22.0F),
					Direction.UP, Position.of(anchor, 7.0F + 22.0F, 31.0F + 0.0F),
					Direction.DOWN, Position.of(anchor, 7.0F + 22.0F, 31.0F + 44.0F)
			);
			
			for (var direction : Direction.values()) {
				var button = new TransferTypeSelectorButtonWidget();
				
				button.setPosition(positions.get(MirrorUtils.rotate(direction, ref.rotation)));
				button.setSize(Size.of(18.0F, 18.0F));
				button.setComponent(transferComponent);
				button.setId(id);
				button.setRotation(ref.rotation);
				button.setDirection(direction);
				button.setBlockPos(blockEntity.getPos());
				
				tab.addWidget(button);
			}
			
			var tabInventoryTitle = new TextWidget();
			tabInventoryTitle.setPosition(Position.of(inventoryTitlePos, 0, -10));
			tabInventoryTitle.setText(getPlayer().inventory.getName());
			tabInventoryTitle.setColor(4210752);
			
			tab.addWidget(tabInventoryTitle);
			
			var tabTitle = new TextWidget();
			tabTitle.setPosition(Position.of(mainTab, 8, 0));
			tabTitle.setText(tabComponent.getText());
			tabTitle.setColor(4210752);
			
			tab.addWidget(tabTitle);
			
			playerSlots.addAll(Slots.addPlayerInventory(inventoryTitlePos, Size.of(18, 18), tab, getPlayer().inventory));
		});
	}
}
