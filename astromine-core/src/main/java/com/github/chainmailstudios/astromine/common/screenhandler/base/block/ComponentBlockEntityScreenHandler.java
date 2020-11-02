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

package com.github.chainmailstudios.astromine.common.screenhandler.base.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.chainmailstudios.astromine.common.block.redstone.RedstoneType;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityRedstoneComponent;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.NameableComponent;
import com.github.chainmailstudios.astromine.common.utilities.WidgetUtilities;
import com.github.vini2003.blade.common.collection.TabWidgetCollection;
import com.github.vini2003.blade.common.collection.base.WidgetCollection;
import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.utilities.Slots;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import com.github.vini2003.blade.common.widget.base.TabWidget;
import com.github.vini2003.blade.common.widget.base.TextWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public abstract class ComponentBlockEntityScreenHandler extends BaseScreenHandler {
	public ComponentBlockEntity syncBlockEntity;
	public BlockPos position;
	public Block originalBlock;
	public Collection<SlotWidget> playerSlots = new HashSet<>();
	public TabWidgetCollection mainTab;
	protected TabWidget tabs;

	public ComponentBlockEntityScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerEntity player, BlockPos position) {
		super(type, syncId, player);

		this.position = position;
		this.syncBlockEntity = (ComponentBlockEntity) player.world.getBlockEntity(position);
		this.originalBlock = player.world.getBlockState(position).getBlock();

		if (!player.world.isClient) {
			syncBlockEntity.doNotSkipInventory();
			syncBlockEntity.sync();
		}
	}

	public Position getTabsPosition(int width, int height) {
		return Position.of(width / 2 - tabs.getWidth() / 2, height / 2 - tabs.getHeight() / 2);
	}

	public Size getTabsSize(int width, int height) {
		return Size.of(176F, 188F + getTabWidgetExtendedHeight());
	}

	public int getTabWidgetExtendedHeight() {
		return 0;
	}

	@Override
	public boolean canUse(@Nullable PlayerEntity player) {
		return canUse(ScreenHandlerContext.create(player.world, position), player, originalBlock);
	}

	@Override
	public void initialize(int width, int height) {
		tabs = new TabWidget();
		tabs.setSize(getTabsSize(width, height));
		tabs.setPosition(getTabsPosition(width, height));

		addWidget(tabs);

		mainTab = (TabWidgetCollection) tabs.addTab(syncBlockEntity.getCachedState().getBlock().asItem());
		mainTab.setPosition(Position.of(tabs, 0, 25F + 7F));
		mainTab.setSize(Size.of(176F, 184F));

		TextWidget title = new TextWidget();
		title.setPosition(Position.of(mainTab, 8, 0));
		title.setText(new TranslatableText(syncBlockEntity.getCachedState().getBlock().asItem().getTranslationKey()));
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
		Block block = syncBlockEntity.getCachedState().getBlock();

		if (block instanceof HorizontalFacingBlockWithEntity) {
			DirectionProperty property = ((HorizontalFacingBlockWithEntity) block).getDirectionProperty();
			if (property != null)
				rotation = syncBlockEntity.getCachedState().get(property);
		}

		final Direction finalRotation = rotation;

		BlockEntityTransferComponent transferComponent = BlockEntityTransferComponent.get(syncBlockEntity);

		transferComponent.get().forEach((key, entry) -> {
			if (key.get(syncBlockEntity) instanceof NameableComponent) {
				NameableComponent nameableComponent = (NameableComponent) key.get(syncBlockEntity);
				TabWidgetCollection current = (TabWidgetCollection) tabs.addTab(nameableComponent.getSymbol(), () -> Collections.singletonList(nameableComponent.getName()));
				WidgetUtilities.createTransferTab(current, Position.of(tabs, tabs.getWidth() / 2 - 38, getTabWidgetExtendedHeight() / 2), finalRotation, transferComponent, syncBlockEntity.getPos(), key);
				TextWidget invTabTitle = new TextWidget();
				invTabTitle.setPosition(Position.of(invPos, 0, -10));
				invTabTitle.setText(getPlayer().inventory.getName());
				invTabTitle.setColor(4210752);
				current.addWidget(invTabTitle);
				playerSlots.addAll(Slots.addPlayerInventory(invPos, Size.of(18F, 18F), current, getPlayer().inventory));

				TextWidget tabTitle = new TextWidget();
				tabTitle.setPosition(Position.of(mainTab, 8, 0));
				tabTitle.setText(nameableComponent.getName());
				tabTitle.setColor(4210752);
				current.addWidget(tabTitle);
			}
		});

		BlockEntityRedstoneComponent redstoneComponent = BlockEntityRedstoneComponent.get(syncBlockEntity);

		WidgetCollection redstoneTab = tabs.addTab(Items.REDSTONE, () -> Collections.singletonList(new TranslatableText("text.astromine.redstone")));

		ButtonWidget[] redstoneButtons = new ButtonWidget[]{ new ButtonWidget() {
			@NotNull
			@Override
			public List<Text> getTooltip() {
				return Collections.singletonList(new TranslatableText("tooltip.astromine.work_when_off").formatted(Formatting.RED));
			}
		}, new ButtonWidget() {
			@NotNull
			@Override
			public List<Text> getTooltip() {
				return Collections.singletonList(new TranslatableText("tooltip.astromine.work_when_on").formatted(Formatting.GREEN));
			}
		}, new ButtonWidget() {
			@NotNull
			@Override
			public List<Text> getTooltip() {
				return Collections.singletonList(new TranslatableText("tooltip.astromine.work_always").formatted(Formatting.YELLOW));
			}
		} };

		for (int i : new int[]{ 0, 1, 2 }) {
			ButtonWidget redstoneButton = redstoneButtons[i];

			int buttonOffset = (i * 18) + i * 9 + 9;

			redstoneButton.setPosition(Position.of(mainTab.getX() + mainTab.getWidth() / 2 - 64 / 2, mainTab.getY() + mainTab.getHeight() / 2 - buttonOffset + 9));
			redstoneButton.setSize(Size.of(64, 18));
			redstoneButton.setLabel(RedstoneType.byNumber(i).asText());
			redstoneButton.setClickAction(() -> {
				if (!redstoneButton.getHidden()) {
					RedstoneType type = RedstoneType.byNumber(i);

					redstoneButton.setLabel(type.asText());

					redstoneComponent.setType(type);

					for (int k : new int[]{ 0, 1, 2 }) {
						if (k != i) {
							redstoneButtons[k].setDisabled(() -> false);
						} else {
							redstoneButtons[k].setDisabled(() -> true);
						}
					}
				}

				return null;
			});

			if (RedstoneType.byNumber(i) == redstoneComponent.getType()) {
				redstoneButton.setDisabled(() -> true);
			}

			redstoneTab.addWidget(redstoneButton);
		}
	}
}
