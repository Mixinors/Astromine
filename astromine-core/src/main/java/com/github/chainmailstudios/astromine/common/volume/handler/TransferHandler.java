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

package com.github.chainmailstudios.astromine.common.volume.handler;

import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.ComponentProvider;

import java.util.Optional;
import java.util.function.Consumer;

public class TransferHandler {
	private final BlockEntityTransferComponent transferComponent;

	public TransferHandler(BlockEntityTransferComponent transferComponent) {
		this.transferComponent = transferComponent;
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

	public TransferHandler withDirection(ComponentType<?> type, Direction direction, Consumer<TransferType> consumer) {
		Optional.ofNullable(transferComponent.get(type).get(direction)).ifPresent(consumer);

		return this;
	}
}
