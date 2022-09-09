package com.github.mixinors.astromine.common.screen.handler.rocket;

import com.github.mixinors.astromine.common.block.entity.rocket.RocketControllerBlockEntity;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.ExtractionSlot;
import com.github.mixinors.astromine.common.slot.FilterSlot;
import com.github.mixinors.astromine.common.util.StorageUtils;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class RocketControllerScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final RocketControllerBlockEntity rocketController;
	
	public RocketControllerScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.ROCKET_CONTROLLER, syncId, player, position);
		
		rocketController = (RocketControllerBlockEntity) blockEntity;
		((RocketControllerBlockEntity) blockEntity).setRocket(RocketManager.getOrCreate(UUID.randomUUID()));
		
		if (!player.world.isClient) {
			rocketController.getRocket().setSyncItemStorage(true);
			rocketController.getRocket().setSyncFluidStorage(true);
		}
	}
	
	@Override
	public Size getTabsSizeExtension() {
		return new Size(0.0F, 81.0F);
	}
	
	@Override
	public void init(int width, int height) {
		super.init(width, height);
		
		var payloadSlot = new SlotWidget(0, rocketController.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				return false;
			});
			
			return slot;
		});
		
		payloadSlot.setPosition(new Position(width / 2.0F - SLOT_WIDTH / 2.0F, 54.0F));
		payloadSlot.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		tab.add(payloadSlot);
		
		var leftFluidBar = fluidBar;
		leftFluidBar.setPosition(new Position(payloadSlot, -BAR_WIDTH - PAD_3, SLOT_HEIGHT + PAD_3));
		leftFluidBar.setSize(new Size(BAR_WIDTH, BAR_HEIGHT));
		leftFluidBar.setStorageView(() -> rocketController.getFluidStorage().getStorage(Rocket.FLUID_INPUT_SLOT_1));
		
		var leftFluidInput = new SlotWidget(Rocket.ITEM_INPUT_SLOT_1, rocketController.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				try (var transaction = Transaction.openOuter()) {
					var tankFluidStorage = rocketController.getFluidStorage().getStorage(Rocket.FLUID_INPUT_SLOT_1);
					
					var itemFluidStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
					
					if (itemFluidStorage == null) {
						return false;
					}
					
					var itemFluidStorageView = StorageUtils.first(itemFluidStorage, transaction, (view) -> !view.isResourceBlank() && (tankFluidStorage.isResourceBlank() || view.getResource() == tankFluidStorage.getResource()));
					
					return itemFluidStorageView != null;
				}
			});
			
			return slot;
		});
		
		leftFluidInput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		leftFluidInput.setPosition(new Position(leftFluidBar, -SLOT_WIDTH - PAD_3, 0.0F));
		
		var leftFluidBuffer = new SlotWidget(Rocket.ITEM_BUFFER_SLOT_1, rocketController.getItemStorage(), ExtractionSlot::new);
		leftFluidBuffer.setPosition(new Position(leftFluidInput, -SLOT_WIDTH - PAD_3, SLOT_HEIGHT - 4.0F)); // 4.0F centers the buffer slot against the two other slots.
		leftFluidBuffer.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var leftFluidOutput = new SlotWidget(Rocket.ITEM_OUTPUT_SLOT_1, rocketController.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				try (var transaction = Transaction.openOuter()) {
					var tankFluidStorage = rocketController.getFluidStorage().getStorage(Rocket.FLUID_OUTPUT_SLOT_1);
					
					var itemFluidStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
					
					if (itemFluidStorage == null) {
						return false;
					}
					
					var itemFluidStorageView = StorageUtils.first(itemFluidStorage, transaction, (view) -> view.isResourceBlank() || view.getResource() == tankFluidStorage.getResource());
					
					return itemFluidStorageView != null;
				}
			});
			
			return slot;
		});
		
		leftFluidOutput.setPosition(new Position(leftFluidInput, 0.0F, SLOT_HEIGHT + PAD_3 + FILTER_HEIGHT + PAD_3));
		leftFluidOutput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		tab.add(leftFluidBar);
		
		tab.add(leftFluidInput);
		tab.add(leftFluidBuffer);
		
		tab.add(leftFluidOutput);
		
		var rightFluidBar = new FluidBarWidget();
		rightFluidBar.setPosition(new Position(payloadSlot, SLOT_WIDTH + PAD_3, SLOT_HEIGHT + PAD_3));
		rightFluidBar.setSize(new Size(BAR_WIDTH, BAR_HEIGHT));
		rightFluidBar.setStorageView(() -> rocketController.getFluidStorage().getStorage(0));
		
		var rightFluidInput = new SlotWidget(Rocket.ITEM_INPUT_SLOT_2, rocketController.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				try (var transaction = Transaction.openOuter()) {
					var tankFluidStorage = rocketController.getFluidStorage().getStorage(Rocket.FLUID_INPUT_SLOT_2);
					
					var itemFluidStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
					
					if (itemFluidStorage == null) {
						return false;
					}
					
					var itemFluidStorageView = StorageUtils.first(itemFluidStorage, transaction, (view) -> !view.isResourceBlank() && (tankFluidStorage.isResourceBlank() || view.getResource() == tankFluidStorage.getResource()));
					
					return itemFluidStorageView != null;
				}
			});
			
			return slot;
		});
		
		rightFluidInput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		rightFluidInput.setPosition(new Position(rightFluidBar, BAR_WIDTH + PAD_3, 0.0F));
		
		var rightFluidBuffer = new SlotWidget(Rocket.ITEM_BUFFER_SLOT_2, rocketController.getItemStorage(), ExtractionSlot::new);
		rightFluidBuffer.setPosition(new Position(rightFluidInput, SLOT_WIDTH + PAD_3, SLOT_HEIGHT - 4.0F)); // 4.0F centers the buffer slot against the two other slots.
		rightFluidBuffer.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var rightFluidOutput = new SlotWidget(Rocket.ITEM_OUTPUT_SLOT_2, rocketController.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				try (var transaction = Transaction.openOuter()) {
					var tankFluidStorage = rocketController.getFluidStorage().getStorage(Rocket.FLUID_OUTPUT_SLOT_2);
					
					var itemFluidStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
					
					if (itemFluidStorage == null) {
						return false;
					}
					
					var itemFluidStorageView = StorageUtils.first(itemFluidStorage, transaction, (view) -> view.isResourceBlank() || view.getResource() == tankFluidStorage.getResource());
					
					return itemFluidStorageView != null;
				}
			});
			
			return slot;
		});
		
		rightFluidOutput.setPosition(new Position(rightFluidInput, 0.0F, SLOT_HEIGHT + PAD_3 + FILTER_HEIGHT + PAD_3));
		rightFluidOutput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		tab.add(rightFluidBar);
		
		tab.add(rightFluidInput);
		tab.add(rightFluidBuffer);
		
		tab.add(rightFluidOutput);
	}
}
