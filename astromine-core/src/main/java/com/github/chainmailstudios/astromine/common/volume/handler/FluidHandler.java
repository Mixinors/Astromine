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

import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FluidHandler {
	private final FluidInventoryComponent component;

	private FluidHandler(FluidInventoryComponent component) {
		this.component = component;
	}

	public FluidVolume getVolume(int slot) {
		return component.getVolume(slot);
	}

	public void setVolume(int slot, FluidVolume volume) {
		component.setVolume(slot, volume);
	}

	public FluidHandler forEach(BiConsumer<Integer, FluidVolume> consumer) {
		component.getContents().forEach(consumer);

		return this;
	}

	public FluidHandler withVolume(int slot, Consumer<Optional<FluidVolume>> consumer) {
		consumer.accept(Optional.ofNullable(component.getVolume(slot)));

		return this;
	}

	public FluidHandler withFirstExtractable(@Nullable Direction diretion, Consumer<Optional<FluidVolume>> consumer) {
		consumer.accept(Optional.ofNullable(component.getFirstExtractableVolume(diretion)));

		return this;
	}

	public FluidHandler withFirstInsertable(@Nullable Direction direction, Fluid fluid, Consumer<Optional<FluidVolume>> consumer) {
		consumer.accept(Optional.ofNullable(component.getFirstInsertableVolume(FluidVolume.of(Fraction.bucket(), fluid), direction)));

		return this;
	}

	public FluidVolume getFirst() {
		return getVolume(0);
	}

	public FluidVolume getSecond() {
		return getVolume(1);
	}

	public FluidVolume getThird() {
		return getVolume(2);
	}

	public FluidVolume getFourth() {
		return getVolume(3);
	}

	public FluidVolume getFifth() {
		return getVolume(4);
	}

	public FluidVolume getSixth() {
		return getVolume(5);
	}

	public FluidVolume getSeventh() {
		return getVolume(6);
	}

	public FluidVolume getEighth() {
		return getVolume(7);
	}

	public void setFirst(FluidVolume volume) {
		setVolume(0, volume);
	}

	public void setSecond(FluidVolume volume) {
		setVolume(1, volume);
	}

	public void setThird(FluidVolume volume) {
		setVolume(2, volume);
	}

	public void setFourth(FluidVolume volume) {
		setVolume(3, volume);
	}

	public void setFifth(FluidVolume volume) {
		setVolume(4, volume);
	}

	public void setSixth(FluidVolume volume) {
		setVolume(5, volume);
	}

	public void setSeventh(FluidVolume volume) {
		setVolume(6, volume);
	}

	public void setEight(FluidVolume volume) {
		setVolume(7, volume);
	}

	public void setNinth(FluidVolume volume) {
		setVolume(8, volume);
	}

	public FluidVolume getNinth() {
		return getVolume(8);
	}

	public static FluidHandler of(Object object) {
		return ofOptional(object).get();
	}

	public static Optional<FluidHandler> ofOptional(Object object) {
		if (object instanceof ComponentProvider) {
			ComponentProvider provider = (ComponentProvider) object;

			if (provider.hasComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT)) {
				FluidInventoryComponent component = provider.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

				if (component != null) {
					return Optional.of(new FluidHandler(component));
				}
			} else {
				if (object instanceof ItemStack) {
					ItemStack stack = (ItemStack) object;
					Item item = stack.getItem();

					if (item instanceof BucketItem) {
						BucketItem bucket = (BucketItem) item;

						FluidInventoryComponent bucketComponent = new SimpleFluidInventoryComponent(1);

						Optional<FluidVolume> bucketVolume = Optional.of(FluidVolume.of(Fraction.bucket(), bucket.fluid));

						if (bucketVolume.isPresent()) {
							bucketComponent.setVolume(0, bucketVolume.get());
							return Optional.of(new FluidHandler(bucketComponent));
						}
					}
				}
			}
		} else if (object instanceof FluidInventoryComponent) {
			return Optional.of(new FluidHandler((FluidInventoryComponent) object));
		}

		return Optional.empty();
	}
}
