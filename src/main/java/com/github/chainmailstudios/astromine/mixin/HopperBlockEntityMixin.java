package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
	@Shadow public abstract double getHopperX();

	@Shadow public abstract double getHopperY();

	@Shadow public abstract double getHopperZ();

	@Inject(at = @At("HEAD"), method = "getInputInventory(Lnet/minecraft/block/entity/Hopper;)Lnet/minecraft/inventory/Inventory;", cancellable = true)
	private static void astromine_onGetInputInventory(Hopper hopper, CallbackInfoReturnable<Inventory> callbackInformationReturnable) {
		BlockPos hopperPos = new BlockPos(hopper.getHopperX(), hopper.getHopperY(), hopper.getHopperZ());

		BlockEntity componentEntity = hopper.getWorld().getBlockEntity(hopperPos.offset(Direction.UP));

		if (componentEntity instanceof ComponentProvider) {
			ComponentProvider componentProvider = (ComponentProvider) componentEntity;

			if (componentProvider.hasComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)) {
				if (componentProvider.hasComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT)) {
					BlockEntityTransferComponent transferComponent = componentProvider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

					if (transferComponent.get(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).get(Direction.DOWN).canExtract()) {
						callbackInformationReturnable.setReturnValue(ItemInventoryFromInventoryComponent.of(componentProvider.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)));
						callbackInformationReturnable.cancel();
					}
				} else {
					callbackInformationReturnable.setReturnValue(ItemInventoryFromInventoryComponent.of(componentProvider.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)));
					callbackInformationReturnable.cancel();
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/block/entity/HopperBlockEntity;getOutputInventory()Lnet/minecraft/inventory/Inventory;", cancellable = true)
	void astromine_onGetOutputInventory(CallbackInfoReturnable<Inventory> callbackInformationReturnable) {
		BlockPos hopperPos = new BlockPos(getHopperX(), getHopperY(), getHopperZ());

		BlockEntity hopperEntity = (HopperBlockEntity) (Object) this;

		BlockEntity componentEntity = hopperEntity.getWorld().getBlockEntity(hopperPos.offset(Direction.DOWN));

		if (componentEntity instanceof ComponentProvider) {
			ComponentProvider componentProvider = (ComponentProvider) componentEntity;

			if (componentProvider.hasComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)) {
				if (componentProvider.hasComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT)) {
					BlockEntityTransferComponent transferComponent = componentProvider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

					if (transferComponent.get(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).get(Direction.UP).canInsert()) {
						callbackInformationReturnable.setReturnValue(ItemInventoryFromInventoryComponent.of(componentProvider.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)));
						callbackInformationReturnable.cancel();
					}
				} else {
					callbackInformationReturnable.setReturnValue(ItemInventoryFromInventoryComponent.of(componentProvider.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)));
					callbackInformationReturnable.cancel();
				}
			}
		}
	}
}
