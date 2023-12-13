package com.github.mixinors.astromine.common.screen.handler.rocket;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.entity.rocket.RocketControllerBlockEntity;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.ExtractionSlot;
import com.github.mixinors.astromine.common.slot.FilterSlot;
import com.github.mixinors.astromine.common.util.StorageUtils;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.image.ImageWidget;
import dev.vini2003.hammer.gui.api.common.widget.item.ItemWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class RocketControllerScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final RocketControllerBlockEntity rocketController;
	
	public RocketControllerScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.ROCKET_CONTROLLER, syncId, player, position);
		
		rocketController = (RocketControllerBlockEntity) blockEntity;
	}
	
	@Override
	public Size getTabsSizeExtension() {
		return new Size(0.0F, 44.0F);
	}
	
	@Override
	public void init(int width, int height) {
		super.init(width, height);

		if (rocketController.getItemStorage() != null && rocketController.getFluidStorage() != null) {
			var payloadSlot = new SlotWidget(0, rocketController.getItemStorage(), (inventory, id, x, y) -> {
				var slot = new FilterSlot(inventory, id, x, y);

				slot.setInsertPredicate((stack) -> {
					return false;
				});

				return slot;
			});

			payloadSlot.setPosition(new Position(tabs, TABS_WIDTH / 2.0F - SLOT_WIDTH / 2.0F, 24.0F));
			payloadSlot.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));

			tab.add(payloadSlot);

			var leftFluidBar = fluidBar;
			leftFluidBar.setPosition(new Position(payloadSlot, -BAR_WIDTH  / 2.0F - PAD_3, SLOT_HEIGHT + PAD_3));
			leftFluidBar.setSize(new Size(BAR_WIDTH  / 2.0F, BAR_HEIGHT));
			leftFluidBar.setStorageView(() -> rocketController.getFluidStorage().getStorage(Rocket.OXYGEN_TANK_FLUID_IN));
			
			var leftFluidInput = new SlotWidget(Rocket.OXYGEN_TANK_UNLOAD_SLOT, rocketController.getItemStorage(), (inventory, id, x, y) -> {
				var slot = new FilterSlot(inventory, id, x, y);
				
				slot.setInsertPredicate((stack) -> {
					try (var transaction = Transaction.openOuter()) {
						var tankFluidStorage = rocketController.getFluidStorage().getStorage(Rocket.OXYGEN_TANK_FLUID_IN);
						
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
			
			var leftFluidBuffer = new SlotWidget(Rocket.OXYGEN_TANK_BUFFER_SLOT, rocketController.getItemStorage(), ExtractionSlot::new);
			leftFluidBuffer.setPosition(new Position(leftFluidInput, -SLOT_WIDTH - PAD_3, SLOT_HEIGHT - 4.0F)); // 4.0F centers the buffer slot against the two other slots.
			leftFluidBuffer.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			var leftFluidOutput = new SlotWidget(Rocket.OXYGEN_TANK_OUTPUT_SLOT, rocketController.getItemStorage(), (inventory, id, x, y) -> {
				var slot = new FilterSlot(inventory, id, x, y);
				
				slot.setInsertPredicate((stack) -> {
					try (var transaction = Transaction.openOuter()) {
						var tankFluidStorage = rocketController.getFluidStorage().getStorage(Rocket.OXYGEN_TANK_FLUID_OUT);
						
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
			
			leftFluidOutput.setPosition(new Position(leftFluidInput, 0.0F, SLOT_HEIGHT + PAD_3 + FILTER_HEIGHT + 1.0F));
			leftFluidOutput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			tab.add(leftFluidBar);
			
			tab.add(leftFluidInput);
			tab.add(leftFluidBuffer);
			
			tab.add(leftFluidOutput);
			
			var rightFluidBar = new FluidBarWidget();
			rightFluidBar.setPosition(new Position(payloadSlot, SLOT_WIDTH + PAD_3, SLOT_HEIGHT + PAD_3));
			rightFluidBar.setSize(new Size(BAR_WIDTH  / 2.0F, BAR_HEIGHT));
			rightFluidBar.setStorageView(() -> rocketController.getFluidStorage().getStorage(0));
			
			var rightFluidInput = new SlotWidget(Rocket.FUEL_TANK_UNLOAD_SLOT, rocketController.getItemStorage(), (inventory, id, x, y) -> {
				var slot = new FilterSlot(inventory, id, x, y);
				
				slot.setInsertPredicate((stack) -> {
					try (var transaction = Transaction.openOuter()) {
						var tankFluidStorage = rocketController.getFluidStorage().getStorage(Rocket.FUEL_TANK_FLUID_IN);
						
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
			rightFluidInput.setPosition(new Position(rightFluidBar, BAR_WIDTH  / 2.0F + PAD_3, 0.0F));
			
			var rightFluidBuffer = new SlotWidget(Rocket.FUEL_TANK_BUFFER_SLOT, rocketController.getItemStorage(), ExtractionSlot::new);
			rightFluidBuffer.setPosition(new Position(rightFluidInput, SLOT_WIDTH + PAD_3, SLOT_HEIGHT - 4.0F)); // 4.0F centers the buffer slot against the two other slots.
			rightFluidBuffer.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			var rightFluidOutput = new SlotWidget(Rocket.FUEL_TANK_OUTPUT_SLOT, rocketController.getItemStorage(), (inventory, id, x, y) -> {
				var slot = new FilterSlot(inventory, id, x, y);
				
				slot.setInsertPredicate((stack) -> {
					try (var transaction = Transaction.openOuter()) {
						var tankFluidStorage = rocketController.getFluidStorage().getStorage(Rocket.FUEL_TANK_FLUID_OUT);
						
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
			
			rightFluidOutput.setPosition(new Position(rightFluidInput, 0.0F, SLOT_HEIGHT + PAD_3 + FILTER_HEIGHT + 1.0F));
			rightFluidOutput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			tab.add(rightFluidBar);
			tab.add(rightFluidInput);
			tab.add(rightFluidBuffer);
			tab.add(rightFluidOutput);

			var leftFluidInputArrow = new ImageWidget();
			leftFluidInputArrow.setPosition(new Position(leftFluidInput, -18.0F, 1.0F));
			leftFluidInputArrow.setSize(16.0F, 16.0F);
			leftFluidInputArrow.setTexture(new ImageTexture(AMCommon.id("textures/widget/arrow_pointing_right_to_down.png")));

			tab.add(leftFluidInputArrow);

			var leftFluidOutputArrow = new ImageWidget();
			leftFluidOutputArrow.setPosition(new Position(leftFluidOutput, -18.0F, 3.0F));
			leftFluidOutputArrow.setSize(16.0F, 16.0F);
			leftFluidOutputArrow.setTexture(new ImageTexture(AMCommon.id("textures/widget/arrow_pointing_up_to_right.png")));

			tab.add(leftFluidOutputArrow);

			var rightFluidInputArrow = new ImageWidget();
			rightFluidInputArrow.setPosition(new Position(rightFluidInput, +18.0F + 1.0F, 1.0F));
			rightFluidInputArrow.setSize(16.0F, 16.0F);
			rightFluidInputArrow.setTexture(new ImageTexture(AMCommon.id("textures/widget/arrow_pointing_left_to_down.png")));

			tab.add(rightFluidInputArrow);

			var rightFluidOutputArrow = new ImageWidget();
			rightFluidOutputArrow.setPosition(new Position(rightFluidOutput, +18.0F + 1.0F, 3.0F));
			rightFluidOutputArrow.setSize(16.0F, 16.0F);
			rightFluidOutputArrow.setTexture(new ImageTexture(AMCommon.id("textures/widget/arrow_pointing_up_to_left.png")));

			tab.add(rightFluidOutputArrow);
			
			var fuelTankSlot = new SlotWidget(Rocket.FUEL_TANK_SLOT, rocketController.getItemStorage(), Slot::new);
			var hullItemSlot = new SlotWidget(Rocket.HULL_SLOT, rocketController.getItemStorage(), Slot::new);
			var landingMechanismSlot = new SlotWidget(Rocket.LANDING_MECHANISM_SLOT, rocketController.getItemStorage(), Slot::new);
			var lifeSupportSlot = new SlotWidget(Rocket.LIFE_SUPPORT_SLOT, rocketController.getItemStorage(), Slot::new);
			var shieldingSlot = new SlotWidget(Rocket.SHIELDING_SLOT, rocketController.getItemStorage(), Slot::new);
			var thrusterSlot = new SlotWidget(Rocket.THRUSTER_SLOT, rocketController.getItemStorage(), Slot::new);
			
			fuelTankSlot.setPosition(new Position(leftFluidBar, BAR_WIDTH  / 2.0F - SLOT_WIDTH, BAR_HEIGHT + PAD_25));
			fuelTankSlot.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			hullItemSlot.setPosition(new Position(fuelTankSlot, -SLOT_WIDTH - PAD_3, 0.0F));
			hullItemSlot.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			landingMechanismSlot.setPosition(new Position(hullItemSlot, -SLOT_WIDTH - PAD_3, 0.0F));
			landingMechanismSlot.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			lifeSupportSlot.setPosition(new Position(rightFluidBar, 0.0F, BAR_HEIGHT + PAD_25));
			lifeSupportSlot.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			shieldingSlot.setPosition(new Position(lifeSupportSlot, SLOT_WIDTH + PAD_3, 0.0F));
			shieldingSlot.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			thrusterSlot.setPosition(new Position(shieldingSlot, SLOT_WIDTH + PAD_3, 0.0F));
			thrusterSlot.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			var fuelTankItem = new ItemWidget();
			fuelTankItem.setItem(AMItems.LOW_CAPACITY_ROCKET_FUEL_TANK);
			fuelTankItem.setTooltip(() -> List.of(Text.translatable("text.astromine.fuel_tank").asOrderedText()));
			
			var hullItem = new ItemWidget();
			hullItem.setItem(AMItems.LOW_DURABILITY_ROCKET_HULL);
			hullItem.setTooltip(() -> List.of(Text.translatable("text.astromine.hull").asOrderedText()));
			
			var landingMechanismItem = new ItemWidget();
			landingMechanismItem.setItem(AMItems.STANDING_ROCKET_LANDING_MECHANISM);
			landingMechanismItem.setTooltip(() -> List.of(Text.translatable("text.astromine.landing_mechanism").asOrderedText()));
			
			var lifeSupportItem = new ItemWidget();
			lifeSupportItem.setItem(AMItems.ROCKET_LIFE_SUPPORT);
			fuelTankItem.setTooltip(() -> List.of(Text.translatable("text.astromine.life_support").asOrderedText()));
			
			var shieldingItem = new ItemWidget();
			shieldingItem.setItem(AMItems.LOW_TEMPERATURE_ROCKET_SHIELDING);
			shieldingItem.setTooltip(() -> List.of(Text.translatable("text.astromine.shielding").asOrderedText()));
			
			var thrusterItem = new ItemWidget();
			thrusterItem.setItem(AMItems.LOW_EFFICIENCY_ROCKET_THRUSTER);
			thrusterItem.setTooltip(() -> List.of(Text.translatable("text.astromine.thruster").asOrderedText()));
			
			fuelTankItem.setPosition(new Position(fuelTankSlot, SLOT_WIDTH - 16.0F, -SLOT_HEIGHT - PAD_3));
			fuelTankItem.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			hullItem.setPosition(new Position(hullItemSlot, SLOT_WIDTH - 16.0F, -SLOT_HEIGHT - PAD_3));
			hullItem.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			landingMechanismItem.setPosition(new Position(landingMechanismSlot, SLOT_WIDTH - 16.0F, -SLOT_HEIGHT - PAD_3));
			landingMechanismItem.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			lifeSupportItem.setPosition(new Position(lifeSupportSlot, 0.0F, -SLOT_HEIGHT - PAD_3));
			lifeSupportItem.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			shieldingItem.setPosition(new Position(shieldingSlot, 0.0F, -SLOT_HEIGHT - PAD_3));
			shieldingItem.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			thrusterItem.setPosition(new Position(thrusterSlot, 0.0F, -SLOT_HEIGHT - PAD_3));
			thrusterItem.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
			
			tab.add(fuelTankSlot);
			tab.add(hullItemSlot);
			tab.add(landingMechanismSlot);
			tab.add(lifeSupportSlot);
			tab.add(shieldingSlot);
			tab.add(thrusterSlot);
			
			tab.add(fuelTankItem);
			tab.add(hullItem);
			tab.add(landingMechanismItem);
			tab.add(lifeSupportItem);
			tab.add(shieldingItem);
			tab.add(thrusterItem);

			payloadSlot.setPosition(new Position(fuelTankItem, 16.0F + PAD_3, -1.0F));
		}
	}
}
