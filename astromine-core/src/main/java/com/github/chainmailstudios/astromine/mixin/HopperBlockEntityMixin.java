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

package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
	@Shadow
	public abstract double getHopperX();

	@Shadow
	public abstract double getHopperY();

	@Shadow
	public abstract double getHopperZ();

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

	@Inject(at = @At("HEAD"), method = "getOutputInventory()Lnet/minecraft/inventory/Inventory;", cancellable = true)
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
