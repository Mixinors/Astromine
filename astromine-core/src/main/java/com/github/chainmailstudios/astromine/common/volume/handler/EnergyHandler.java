package com.github.chainmailstudios.astromine.common.volume.handler;

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleEnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.energy.WrappedEnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergyStorage;

import java.util.Optional;
import java.util.function.Consumer;

public class EnergyHandler {
	private final EnergyInventoryComponent component;

	private EnergyHandler(EnergyInventoryComponent component) {
		this.component = component;
	}

	public EnergyVolume getVolume(int slot) {
		return component.getVolume(slot);
	}

	public void setVolume(int slot, EnergyVolume volume) {
		component.setVolume(slot, volume);
	}

	public EnergyHandler withVolume(int slot, Consumer<Optional<EnergyVolume>> consumer) {
		consumer.accept(Optional.ofNullable(component.getVolume(slot)));

		return this;
	}

	public EnergyHandler withFirstExtractable(@Nullable Direction diretion, Consumer<Optional<EnergyVolume>> consumer) {
		consumer.accept(Optional.ofNullable(component.getFirstExtractableVolume(diretion)));

		return this;
	}

	public EnergyHandler withFirstInsertable(@Nullable Direction direction, Consumer<Optional<EnergyVolume>> consumer) {
		consumer.accept(Optional.ofNullable(component.getFirstInsertableVolume(direction)));

		return this;
	}

	public EnergyVolume getFirst() {
		return getVolume(0);
	}

	public EnergyVolume getSecond() {
		return getVolume(1);
	}

	public EnergyVolume getThird() {
		return getVolume(2);
	}

	public EnergyVolume getFourth() {
		return getVolume(3);
	}

	public EnergyVolume getFifth() {
		return getVolume(4);
	}

	public EnergyVolume getSixth() {
		return getVolume(5);
	}

	public EnergyVolume getSeventh() {
		return getVolume(6);
	}

	public EnergyVolume getEight() {
		return getVolume(7);
	}

	public EnergyVolume getNinth() {
		return getVolume(8);
	}

	public void setFirst(EnergyVolume volume) {
		setVolume(0, volume);
	}

	public void setSecond(EnergyVolume volume) {
		setVolume(1, volume);
	}

	public void setThird(EnergyVolume volume) {
		setVolume(2, volume);
	}

	public void setFourth(EnergyVolume volume) {
		setVolume(3, volume);
	}

	public void setFifth(EnergyVolume volume) {
		setVolume(4, volume);
	}

	public void setSixth(EnergyVolume volume) {
		setVolume(5, volume);
	}

	public void setSeventh(EnergyVolume volume) {
		setVolume(6, volume);
	}

	public void setEight(EnergyVolume volume) {
		setVolume(7, volume);
	}

	public void setNinth(EnergyVolume volume) {
		setVolume(8, volume);
	}
	
	public boolean consume(int slot, double amount) {
		boolean[] consumed = { false };

			this.withVolume(slot, optionalVolume -> {
				optionalVolume.ifPresent(volume -> {
					volume.ifStored(amount, () -> {
						volume.from(amount);

						consumed[0] = true;
					});
				});
			});

		return consumed[0];
	}

	public static EnergyHandler of(Object object) {
		return ofOptional(object).get();
	}

	public static Optional<EnergyHandler> ofOptional(Object object) {
		return ofOptional(object, null);
	}

	public static Optional<EnergyHandler> ofOptional(Object object, @Nullable Direction direction) {
		if (object instanceof ComponentProvider) {
			ComponentProvider provider = (ComponentProvider) object;

			if (provider.hasComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT)) {
				EnergyInventoryComponent component = provider.getComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);

				if (component != null) {
					return Optional.of(new EnergyHandler(component));
				}
			}
		}

		if (object instanceof EnergyStorage && direction != null) {
			SimpleEnergyInventoryComponent energyComponent = new SimpleEnergyInventoryComponent(1);
			energyComponent.setVolume(0, WrappedEnergyVolume.of(((EnergyStorage) object), direction));

			return Optional.of(new EnergyHandler(energyComponent));
		}

		if (object instanceof EnergyInventoryComponent) {
			return Optional.of(new EnergyHandler((EnergyInventoryComponent) object));
		}

		return Optional.empty();
	}
}
