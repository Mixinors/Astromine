package com.github.chainmailstudios.astromine.common.volume.handler;

import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class ItemHandler {
	private final ItemInventoryComponent component;

	private ItemHandler(ItemInventoryComponent component) {
		this.component = component;
	}

	public ItemStack getStack(int slot) {
		return component.getStack(slot);
	}

	public void setVolume(int slot, ItemStack stack) {
		component.setStack(slot, stack);
	}

	public ItemHandler withStack(int slot, Consumer<Optional<ItemStack>> consumer) {
		consumer.accept(Optional.ofNullable(component.getStack(slot)));

		return this;
	}

	public ItemStack getFirst() {
		return getStack(0);
	}

	public ItemStack getSecond() {
		return getStack(1);
	}

	public ItemStack getThird() {
		return getStack(2);
	}

	public ItemStack getFourth() {
		return getStack(3);
	}

	public ItemStack getFifth() {
		return getStack(4);
	}

	public ItemStack getSixth() {
		return getStack(5);
	}

	public ItemStack getSeventh() {
		return getStack(6);
	}

	public ItemStack getEight() {
		return getStack(7);
	}

	public void setFirst(ItemStack volume) {
		setVolume(0, volume);
	}

	public void setSecond(ItemStack volume) {
		setVolume(1, volume);
	}

	public void setThird(ItemStack volume) {
		setVolume(2, volume);
	}

	public void setFourth(ItemStack volume) {
		setVolume(3, volume);
	}

	public void setFifth(ItemStack volume) {
		setVolume(4, volume);
	}

	public void setSixth(ItemStack volume) {
		setVolume(5, volume);
	}

	public void setSeventh(ItemStack volume) {
		setVolume(6, volume);
	}

	public void setEight(ItemStack volume) {
		setVolume(7, volume);
	}

	public void setNinth(ItemStack volume) {
		setVolume(8, volume);
	}

	public ItemStack getNinth() {
		return getStack(8);
	}

	public ItemHandler forEach(Consumer<ItemStack> stack) {
		component.getContents().values().forEach(stack);

		return this;
	}

	public static ItemHandler of(Object object) {
		return ofOptional(object).get();
	}

	public static Optional<ItemHandler> ofOptional(Object object) {
		if (object instanceof ComponentProvider) {
			ComponentProvider provider = (ComponentProvider) object;

			if (provider.hasComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT)) {
				ItemInventoryComponent component = provider.getComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT);

				if (component != null) {
					return Optional.of(new ItemHandler(component));
				}
			}
		} else if (object instanceof ItemInventoryComponent) {
			return Optional.of(new ItemHandler((ItemInventoryComponent) object));
		}

		return Optional.empty();
	}
}