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
import com.github.mixinors.astromine.registry.common.AMComponents;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.mixinors.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.mixinors.astromine.common.component.general.miscellaneous.IdentifiableComponent;
import com.github.mixinors.astromine.common.util.WidgetUtils;
import com.github.mixinors.astromine.common.widget.blade.RedstoneWidget;
import com.github.vini2003.blade.common.collection.TabWidgetCollection;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.utilities.Slots;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import com.github.vini2003.blade.common.widget.base.TabWidget;
import com.github.vini2003.blade.common.widget.base.TextWidget;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.BiConsumer;
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
		return Position.of(width / 2 - tabs.getWidth() / 2, height / 2 - tabs.getHeight() / 2);
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

		addWidget(tabs);

		mainTab = (TabWidgetCollection) tabs.addTab(blockEntity.getCachedState().getBlock().asItem(), () -> Collections.singletonList(new TranslatableText(blockEntity.getCachedState().getBlock().getTranslationKey())));
		mainTab.setPosition(Position.of(tabs, 0, 25F + 7F));
		mainTab.setSize(Size.of(176F, 184F));

		TextWidget title = new TextWidget();
		title.setPosition(Position.of(mainTab, 8, 0));
		title.setText(new TranslatableText(blockEntity.getCachedState().getBlock().asItem().getTranslationKey()));
		title.setColor(4210752);
		mainTab.addWidget(title);

		Position invPos = Position.of(tabs, 7F + (tabs.getWidth() / 2 - ((9 * 18F) / 2) - 7F), 25F + 7F + (184 - 18 - 18 - (18 * 4) - 3 + getTabWidgetExtendedHeight()));
		TextWidget invTitle = new TextWidget();
		invTitle.setPosition(Position.of(invPos, 0, -10));
		invTitle.setText(getPlayer().inventory.getName());
		invTitle.setColor(4210752);
		mainTab.addWidget(invTitle);
		playerSlots = Slots.addPlayerInventory(invPos, Size.of(18F, 18F), mainTab, getPlayer().inventory);

		Direction rotation = Direction.NORTH;
		Block block = blockEntity.getCachedState().getBlock();

		if (block instanceof HorizontalFacingBlockWithEntity) {
			DirectionProperty property = ((HorizontalFacingBlockWithEntity) block).getDirectionProperty();
			if (property != null)
				rotation = blockEntity.getCachedState().get(property);
		}

		final Direction finalRotation = rotation;

		RedstoneWidget redstoneWidget = new RedstoneWidget();
		redstoneWidget.setPosition(Position.of(tabs, tabs.getWidth() - 20, 0));
		redstoneWidget.setSize(Size.of(20, 19));
		redstoneWidget.setBlockEntity(blockEntity);

		addWidget(redstoneWidget);

		TransferComponent transferComponent = TransferComponent.get(blockEntity);

		BiConsumer<IdentifiableComponent, ComponentKey<? extends IdentifiableComponent>> tabAdder = (identifiableComponent, key) -> {
			TabWidgetCollection current = (TabWidgetCollection) tabs.addTab(identifiableComponent.getSymbol(), () -> Collections.singletonList(identifiableComponent.getName()));
			WidgetUtils.createTransferTab(current, Position.of(tabs, tabs.getWidth() / 2 - 38, getTabWidgetExtendedHeight() / 2), finalRotation, transferComponent, blockEntity.getPos(), key);
			TextWidget invTabTitle = new TextWidget();
			invTabTitle.setPosition(Position.of(invPos, 0, -10));
			invTabTitle.setText(getPlayer().inventory.getName());
			invTabTitle.setColor(4210752);
			current.addWidget(invTabTitle);
			playerSlots.addAll(Slots.addPlayerInventory(invPos, Size.of(18F, 18F), current, getPlayer().inventory));

			TextWidget tabTitle = new TextWidget();
			tabTitle.setPosition(Position.of(mainTab, 8, 0));
			tabTitle.setText(identifiableComponent.getName());
			tabTitle.setColor(4210752);
			current.addWidget(tabTitle);
		};

		if (blockEntity instanceof ItemComponentProvider) {
			tabAdder.accept(((ItemComponentProvider) blockEntity).getItemComponent(), AMComponents.ITEM_INVENTORY_COMPONENT);
		}

		if (blockEntity instanceof FluidComponentProvider) {
			tabAdder.accept(((FluidComponentProvider) blockEntity).getFluidComponent(), AMComponents.FLUID_INVENTORY_COMPONENT);
		}

		if (blockEntity instanceof EnergyComponentProvider) {
			tabAdder.accept(((EnergyComponentProvider) blockEntity).getEnergyComponent(), AMComponents.ENERGY_INVENTORY_COMPONENT);
		}
	}
}
