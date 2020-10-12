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

package com.github.chainmailstudios.astromine.common.component;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public interface SidedComponentProvider extends nerdhub.cardinal.components.api.component.ComponentProvider {
	static SidedComponentProvider fromBlockEntity(BlockEntity blockEntity) {
		return blockEntity instanceof SidedComponentProvider ? (SidedComponentProvider) blockEntity : null;
	}

	<T extends Component> Collection<T> getSidedComponents(@Nullable Direction direction);

	default <T extends Component> T getSidedComponent(@Nullable Direction direction, @NotNull ComponentType<T> type) {
		Optional<T> optional = (Optional<T>) getSidedComponents(direction).stream().filter(component -> type.getComponentClass().isInstance(component)).findFirst();
		return optional.orElse(null);
	}
}
