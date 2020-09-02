package com.github.chainmailstudios.astromine.common.volume.handler;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.base.Volume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.util.math.Direction;

import java.util.Optional;
import java.util.function.Consumer;

public class TransferHandler {
	private final BlockEntityTransferComponent transferComponent;

	public TransferHandler(BlockEntityTransferComponent transferComponent) {
		this.transferComponent = transferComponent;
	}

	public TransferHandler withDirection(ComponentType<?> type, Direction direction, Consumer<TransferType> consumer) {
		Optional.ofNullable(transferComponent.get(type).get(direction)).ifPresent(consumer);

		return this;
	}

	public static Optional<TransferHandler> of(Object object) {
		if (object instanceof ComponentProvider) {
			ComponentProvider provider = (ComponentProvider) object;

			if (provider.hasComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT)) {
				BlockEntityTransferComponent component = provider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

				if (component != null) {
					return Optional.of(new TransferHandler(component));
				}
			}
		}

		return Optional.empty();
	}
}
