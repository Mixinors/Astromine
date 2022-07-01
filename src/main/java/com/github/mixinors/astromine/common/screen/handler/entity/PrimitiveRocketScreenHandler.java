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

package com.github.mixinors.astromine.common.screen.handler.entity;

import com.github.mixinors.astromine.common.entity.rocket.RocketEntity;
import com.github.mixinors.astromine.common.screen.handler.base.entity.ExtendedEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.ExtractionSlot;
import com.github.mixinors.astromine.common.slot.FilterSlot;
import com.github.mixinors.astromine.common.util.StorageUtils;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class PrimitiveRocketScreenHandler extends ExtendedEntityScreenHandler {
	private final RocketEntity rocket;
	
	public PrimitiveRocketScreenHandler(int syncId, PlayerEntity player, int entityId) {
		super(AMScreenHandlers.ROCKET, syncId, player, entityId);
		
		rocket = (RocketEntity) entity;
	}
	
	@Override
	public ItemStack getSymbol() {
		return new ItemStack(AMItems.PRIMITIVE_ROCKET.get());
	}
	
	@Override
	public void init(int width, int height) {
		super.init(width, height);
		
		
		var launchButton = new ButtonWidget();
		
		launchButton.onEvent(EventType.MOUSE_CLICKED, (MouseClickedEvent event) -> {
			rocket.tryLaunch(this.getPlayer());
		});
		
		launchButton.setPosition(new Position(tab, PAD_7, PAD_11 + (BAR_HEIGHT - LAUNCH_BUTTON_HEIGHT) / 2.0F));
		launchButton.setSize(new Size(LAUNCH_BUTTON_WIDTH, LAUNCH_BUTTON_HEIGHT));
		launchButton.setLabel(new TranslatableText("text.astromine.rocket.go").formatted(Formatting.BOLD));
		launchButton.setDisabled(() -> entity.getDataTracker().get(RocketEntity.IS_RUNNING) || !rocket.isFuelMatching());
		
		fluidBar.setPosition(new Position(tab, TABS_WIDTH - PAD_7 - (BAR_WIDTH + PAD_3 + SLOT_WIDTH + PAD_3 + SLOT_WIDTH + PAD_3 + SLOT_WIDTH + PAD_3 + BAR_WIDTH), PAD_11));
		
		var firstInput = new SlotWidget(RocketEntity.ITEM_INPUT_SLOT_1, entity.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				try (var transaction = Transaction.openOuter()) {
					var itemFluidStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
					
					if (itemFluidStorage == null) {
						return false;
					}
					
					var itemFluidStorageView = StorageUtils.first(itemFluidStorage, transaction, (view) -> rocket.getFirstFuel().testVariant(view.getResource()));
					
					return itemFluidStorageView != null;
				}
			});
			
			return slot;
		});
		firstInput.setPosition(new Position(fluidBar, BAR_WIDTH + PAD_3, 0.0F));
		firstInput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var firstOutput = new SlotWidget(entity.getItemStorage(), RocketEntity.ITEM_OUTPUT_SLOT_1, (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				try (var transaction = Transaction.openOuter()) {
					var rocketFluidStorage = rocket.getFluidStorage().getStorage(RocketEntity.FLUID_OUTPUT_SLOT_1);
					
					var itemFluidStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
					
					if (itemFluidStorage == null) {
						return false;
					}
					
					var itemFluidStorageView = StorageUtils.first(itemFluidStorage, transaction, (view) -> view.isResourceBlank() || view.getResource() == rocketFluidStorage.getResource());
					
					return itemFluidStorageView != null;
				}
			});
			
			return slot;
		});
		firstOutput.setPosition(new Position(fluidBar, BAR_WIDTH + PAD_3, BAR_HEIGHT - SLOT_HEIGHT));
		firstOutput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var secondFluidBar = new FluidBarWidget();
		secondFluidBar.setPosition(new Position(tab, TABS_WIDTH - PAD_7 - (BAR_WIDTH), PAD_11));
		secondFluidBar.setSize(new Size(BAR_WIDTH, BAR_HEIGHT));
		secondFluidBar.setStorageView(() -> entity.getFluidStorage().getStorage(RocketEntity.FLUID_INPUT_SLOT_2));
		secondFluidBar.setSmooth(false);
		
		var secondInput = new SlotWidget(RocketEntity.ITEM_INPUT_SLOT_2, entity.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				try (var transaction = Transaction.openOuter()) {
					var itemFluidStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
					
					if (itemFluidStorage == null) {
						return false;
					}
					
					var itemFluidStorageView = StorageUtils.first(itemFluidStorage, transaction, (view) -> rocket.getSecondFuel().testVariant(view.getResource()));
					
					return itemFluidStorageView != null;
				}
			});
			
			return slot;
		});
		secondInput.setPosition(new Position(secondFluidBar, -SLOT_WIDTH - PAD_3, 0.0F));
		secondInput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var secondOutput = new SlotWidget(RocketEntity.ITEM_OUTPUT_SLOT_2, entity.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				try (var transaction = Transaction.openOuter()) {
					var rocketFluidStorage = rocket.getFluidStorage().getStorage(RocketEntity.FLUID_OUTPUT_SLOT_2);
					
					var itemFluidStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
					
					if (itemFluidStorage == null) {
						return false;
					}
					
					var itemFluidStorageView = StorageUtils.first(itemFluidStorage, transaction, (view) -> view.isResourceBlank() || view.getResource() == rocketFluidStorage.getResource());
					
					return itemFluidStorageView != null;
				}
			});
			
			return slot;
		});
		secondOutput.setPosition(new Position(secondFluidBar, -SLOT_WIDTH - PAD_3, BAR_HEIGHT - SLOT_HEIGHT));
		secondOutput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var buffer = new SlotWidget(RocketEntity.ITEM_BUFFER_SLOT_1, entity.getItemStorage(), ExtractionSlot::new);
		buffer.setPosition(new Position(firstInput, PAD_3 + SLOT_WIDTH, SLOT_HEIGHT - 4.0F));
		buffer.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		tab.add(buffer);
		
		tab.add(launchButton);
		
		tab.add(secondFluidBar);
		
		tab.add(firstInput);
		tab.add(firstOutput);
		
		tab.add(secondInput);
		tab.add(secondOutput);
	}
}
