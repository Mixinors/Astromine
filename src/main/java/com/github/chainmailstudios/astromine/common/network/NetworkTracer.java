package com.github.chainmailstudios.astromine.common.network;

import com.github.chainmailstudios.astromine.common.block.AbstractCableBlock;
import com.github.chainmailstudios.astromine.component.WorldNetworkComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NetworkTracer {
	public static Object getObjectAt(World world, BlockPos position) {
		Block newBlock = world.getBlockState(position).getBlock();
		BlockEntity newEntity = world.getBlockEntity(position);

		Object newObject;

		if (newEntity != null) {
			newObject = newEntity;
		} else {
			newObject = newBlock;
		}

		return newObject;
	}

	public static class Tracer {
		public void trace(NetworkType type, BlockPos initialPosition, World world) {
			ComponentProvider provider = ComponentProvider.fromWorld(world);

			WorldNetworkComponent networkComponent = provider.getComponent(AstromineComponentTypes.WORLD_NETWORK_COMPONENT);

			NetworkInstance instance = new NetworkInstance(world, type);

			Block block = world.getBlockState(initialPosition).getBlock();

			if (!(block instanceof NetworkMember) || !((NetworkMember) block).isNode()) {
				return;
			}

			Set<BlockPos> cache = new HashSet<>();
			cache.add(initialPosition);

			ArrayDeque<BlockPos> positions = new ArrayDeque<>();
			positions.add(initialPosition);

			if (networkComponent.containsInstance(type, initialPosition)) {
				return;
			} else {
				instance.addBlockPos(initialPosition);
			}

			while (!positions.isEmpty()) {
				BlockPos position = positions.getLast();
				positions.removeLast();
				MutableBoolean joined = new MutableBoolean(false);
				Object initialObject = getObjectAt(world, position);

				for (Direction direction : Direction.values()) {
					BlockPos offsetPosition = position.offset(direction);

					if (cache.contains(offsetPosition)) {
						continue;
					}

					Object offsetObject = getObjectAt(world, offsetPosition);

					NetworkInstance existingInstance = networkComponent.getInstance(type, offsetPosition);

					if (existingInstance != NetworkInstance.EMPTY) {
						existingInstance.join(instance);
						networkComponent.removeInstance(instance);
						networkComponent.addInstance(existingInstance);
						instance = existingInstance;
						joined.setTrue();
					} else if (offsetObject instanceof NetworkMember) {
						NetworkMember offsetMember = (NetworkMember) offsetObject;

						if ((offsetMember.isRequester() || offsetMember.isProvider() || offsetMember.isBuffer()) && offsetMember.getNetworkType() == type) {
							instance.addMember(NetworkNode.of(offsetPosition));
						}

						if (offsetMember.isNode()) {
							if (offsetMember.getNetworkType() == ((NetworkMember) initialObject).getNetworkType()) {
								positions.addLast(offsetPosition);
								instance.addNode(NetworkNode.of(offsetPosition));
							}
						}
					}

					cache.add(offsetPosition);
				}

				if (joined.isTrue()) {
					return;
				}
			}

			networkComponent.addInstance(instance);
		}
	}

	public static class Modeller {
		private final Set<Direction> directions = new HashSet<>();

		public void scanBlockState(BlockState blockState) {
			for (Map.Entry<Direction, BooleanProperty> property : AbstractCableBlock.PROPERTY_MAP.entrySet()) {
				if (blockState.get(property.getValue())) {
					directions.add(property.getKey());
				}
			}
		}

		public void scanNeighbours(NetworkType type, BlockPos initialPosition, World world) {
			Object initialObject = getObjectAt(world, initialPosition);

			for (Direction direction : Direction.values()) {
				Object offsetObject = getObjectAt(world, initialPosition.offset(direction));

				if (offsetObject instanceof NetworkMember && ((NetworkMember) offsetObject).getNetworkType() == type) {
					directions.add(direction);
				}
			}
		}

		public BlockState applyToBlockState(BlockState state) {
			if (!(state.getBlock() instanceof NetworkMember)) return state;
			for (Direction direction : Direction.values()) {
				state = state.with(AbstractCableBlock.PROPERTY_MAP.get(direction), directions.contains(direction));
			}
			return state;
		}

		public VoxelShape applyToVoxelShape(VoxelShape shape) {
			for (Direction direction : Direction.values()) {
				if (directions.contains(direction)) {
					shape = VoxelShapes.union(shape, AbstractCableBlock.SHAPE_MAP.get(AbstractCableBlock.PROPERTY_MAP.get(direction)));
				}
			}
			return shape;
		}
	}
}
