package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.EnergyWireConnectorBlock;
import com.github.chainmailstudios.astromine.common.network.*;
import com.github.chainmailstudios.astromine.common.volume.BaseVolume;
import com.github.chainmailstudios.astromine.common.volume.collection.AgnosticIndexedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.collection.AgnosticSidedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.collection.IndexedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.collection.SimpleIndexedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.Vector;

public class EnergyWireConnectorBlockEntity extends WireConnectorBlockEntity implements Tickable, AgnosticIndexedVolumeCollection {
	private EnergyVolume energyVolume = new EnergyVolume();

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

		EnergyVolume ourVolume = get(EnergyVolume.TYPE);

		for (NetworkNode parentNode : getParents()) {
			EnergyWireConnectorBlockEntity parent = (EnergyWireConnectorBlockEntity) world.getBlockEntity(parentNode.getPosition());

			if (parent == null) continue;

			EnergyVolume parentVolume = parent.get(EnergyVolume.TYPE);

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
			if (attached instanceof AgnosticSidedVolumeCollection) {
				AgnosticSidedVolumeCollection collection = (AgnosticSidedVolumeCollection) attached;

				if (collection.contains(direction, EnergyVolume.TYPE)) {
					EnergyVolume volume = collection.get(direction, EnergyVolume.TYPE);

					ourVolume.push(volume, volume.getSize());
				}
			} else if (attached instanceof AgnosticIndexedVolumeCollection) {
				AgnosticIndexedVolumeCollection collection = (AgnosticIndexedVolumeCollection) attached;

				if (collection.contains(EnergyVolume.TYPE)) {
					EnergyVolume volume = collection.get(EnergyVolume.TYPE);

					ourVolume.push(volume, volume.getSize());
				}
			}
		}

		for (NetworkNode childNode : getChildren()) {
			EnergyWireConnectorBlockEntity child = (EnergyWireConnectorBlockEntity) world.getBlockEntity(childNode.getPosition());

			if (child == null) continue;

			EnergyVolume childVolume = child.get(EnergyVolume.TYPE);

			if (childVolume != null) {
				childVolume.push(ourVolume, ourVolume.getSize());
			}

			if (ourVolume.isEmpty()) {
				break;
			}
		}
	}

	@Override
	public boolean contains(int volumeType) {
		return volumeType == EnergyVolume.TYPE;
	}

	@Override
	public <T extends BaseVolume> T get(int volumeType) {
		return volumeType == EnergyVolume.TYPE ? (T) energyVolume : null;
	}
}
