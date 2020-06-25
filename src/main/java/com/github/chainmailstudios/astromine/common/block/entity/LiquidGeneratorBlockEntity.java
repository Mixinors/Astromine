package com.github.chainmailstudios.astromine.common.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.recipe.LiquidGeneratingRecipe;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

import java.util.Optional;

public class LiquidGeneratorBlockEntity extends DefaultedEnergyFluidBlockEntity implements NetworkMember, Tickable {
	Optional<LiquidGeneratingRecipe> recipe = Optional.empty();
	
	public LiquidGeneratorBlockEntity() {
		super(AstromineBlockEntityTypes.LIQUID_GENERATOR);
		
		energyComponent.getVolume(0).setSize(new Fraction(32, 1));
		fluidComponent.getVolume(0).setSize(new Fraction(4, 1));
		
		fluidComponent.addListener(() -> {
			if (!this.world.isClient() && (!recipe.isPresent() || !recipe.get().tryCrafting(this, false)))
				recipe = (Optional) world.getRecipeManager().getAllOfType(LiquidGeneratingRecipe.Type.INSTANCE).values().stream()
						.filter(recipe -> recipe instanceof LiquidGeneratingRecipe)
						.filter(recipe -> ((LiquidGeneratingRecipe) recipe).tryCrafting(this, false))
						.findFirst();
		});
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
		
		for (Direction direction : Direction.values()) {
			BlockPos position = getPos().offset(direction);
			
			BlockEntity attached = world.getBlockEntity(position);
			
			if (attached instanceof ComponentProvider) {
				ComponentProvider provider = ComponentProvider.fromBlockEntity(attached);
				
				EnergyInventoryComponent inventory = provider.getSidedComponent(direction, AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);
				
				if (inventory != null && energyComponent.getVolume(0).hasStored(Fraction.BOTTLE)) {
					if (inventory.canInsert(energyComponent.getVolume(0))) {
						inventory.getVolume(0).pullVolume(energyComponent.getVolume(0), Fraction.BOTTLE);
					}
					
					if (attached instanceof BlockEntityClientSerializable && !world.isClient) {
						((BlockEntityClientSerializable) attached).sync();
					}
				}
			}
		}
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
