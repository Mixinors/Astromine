package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.recipe.ElectrolyzingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.util.Tickable;

import java.util.Optional;

public class ElectrolyzerBlockEntity extends DefaultedEnergyFluidBlockEntity implements NetworkMember, Tickable {
	Optional<ElectrolyzingRecipe> recipe = Optional.empty();

	public ElectrolyzerBlockEntity() {
		super(AstromineBlockEntityTypes.ELECTROLYZER);

		fluidComponent = new SimpleFluidInventoryComponent(2);

		energyComponent.getVolume(0).setSize(new Fraction(32, 1));
		fluidComponent.getVolume(0).setSize(new Fraction(4, 1));
		fluidComponent.getVolume(1).setSize(new Fraction(4, 1));

		fluidComponent.addListener(() -> {
			if (!this.world.isClient() && (!recipe.isPresent() || !recipe.get().tryCrafting(this, false)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(ElectrolyzingRecipe.Type.INSTANCE).values().stream()
						.filter(recipe -> recipe instanceof ElectrolyzingRecipe)
						.filter(recipe -> ((ElectrolyzingRecipe) recipe).tryCrafting(this, false))
						.findFirst();
		});

		addComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT, fluidComponent);
	}

	@Override
	public void tick() {
		if (this.world.isClient()) return;
		fluidComponent.dispatchConsumers();
		this.recipe.ifPresent(recipe -> {
			if (!recipe.tryCrafting(this, true)) {
				this.recipe = Optional.empty();
			}
		});
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID || type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public <T extends NetworkType> boolean isBuffer(T type) {
		return true;
	}
}
