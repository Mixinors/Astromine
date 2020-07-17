package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.world.WorldAtmosphereComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.AirBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class VentBlockEntity extends DefaultedEnergyFluidBlockEntity implements Tickable, NetworkMember {
	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	public VentBlockEntity() {
		super(AstromineBlockEntityTypes.VENT);

		setMaxStoredPower(16000);
		fluidComponent.getVolume(0).setSize(new Fraction(16, 1));
	}

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		return new SimpleFluidInventoryComponent(1);
	}

	@Override
	public void tick() {
		if (world.isClient()) return;
		if (fluidComponent.getVolume(0).hasStored(Fraction.of(1, 8))) {
			BlockPos position = getPos();

			Direction direction = world.getBlockState(position).get(FacingBlock.FACING);

			BlockPos output = position.offset(direction);

			if (asEnergy().use(50) && world.getBlockState(output).getBlock() instanceof AirBlock) {
				ComponentProvider componentProvider = ComponentProvider.fromWorld(world);

				WorldAtmosphereComponent atmosphereComponent = componentProvider.getComponent(AstromineComponentTypes.WORLD_ATMOSPHERE_COMPONENT);

				FluidVolume volume = atmosphereComponent.get(output);

				fluidComponent.getVolume(0).pushVolume(volume, Fraction.of(1, 8));

				atmosphereComponent.add(output, volume);

				isActive = true;
			} else {
				isActive = false;
			}
		} else {
			isActive = false;
		}

		if (activity.length - 1 >= 0) System.arraycopy(activity, 1, activity, 0, activity.length - 1);

		activity[4] = isActive;

		if (isActive && !activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, true));
		} else if (!isActive && activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, false));
		}
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID || type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public <T extends NetworkType> boolean isRequester(T type) {
		return type == AstromineNetworkTypes.FLUID || type == AstromineNetworkTypes.ENERGY;
	}
}
