package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.component.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.network.*;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class EnergyWireConnectorBlockEntity extends WireConnectorBlockEntity implements Tickable {
	@Override
	public float getCableOffset() {
		return 0f;
	}

	@Override
	public int getColor() {
		return 0x7e2fd3da;
	}

	@Override
	public void tick() {
		if (world == null) return;

		Vector3f velocity = new Vector3f(1, 1, 0);

		for (int i = 0; i < 360; ++i) {
			velocity.rotate(Vector3f.POSITIVE_Y.getDegreesQuaternion(1));

			world.addParticle(ParticleTypes.WHITE_ASH, getPos().getX(), getPos().getY(), getPos().getZ(), velocity.getX(), velocity.getY(), velocity.getZ());
		}

		EnergyVolume ourVolume = energyComponent.getVolume(0);

		for (NetworkNode parentNode : getParents()) {
			EnergyWireConnectorBlockEntity parent = (EnergyWireConnectorBlockEntity) world.getBlockEntity(parentNode.getPosition());

			if (parent == null) continue;

			EnergyVolume parentVolume = parent.energyComponent.getVolume(0);

			if (parentVolume != null) {
				parentVolume.push(ourVolume, parentVolume.getSize());
			}

			if (ourVolume.isFull()) {
				break;
			}
		}

		Direction direction = getCachedState().get(FacingBlock.FACING).getOpposite();

		BlockEntity attached = world.getBlockEntity(getPos().offset(direction));

		if (attached != null) {
			if (attached instanceof EnergyInventoryComponent) {
				EnergyInventoryComponent inventory = (EnergyInventoryComponent) attached;

				EnergyVolume volume = inventory.getVolume(1);

				ourVolume.push(volume, volume.getSize());
			}
		}

		for (NetworkNode childNode : getChildren()) {
			EnergyWireConnectorBlockEntity child = (EnergyWireConnectorBlockEntity) world.getBlockEntity(childNode.getPosition());

			if (child == null) continue;

			EnergyVolume childVolume = child.energyComponent.getVolume(0);

			if (childVolume != null) {
				childVolume.push(ourVolume, ourVolume.getSize());
			}

			if (ourVolume.isEmpty()) {
				break;
			}
		}
	}
}
