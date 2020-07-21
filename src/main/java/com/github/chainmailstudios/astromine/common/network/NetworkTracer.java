package com.github.chainmailstudios.astromine.common.network;

import com.github.chainmailstudios.astromine.common.block.AbstractCableBlock;
import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
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

import java.util.*;

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
		public static final Tracer INSTANCE = new Tracer();

		private Tracer() {}

		public void trace(NetworkType type, BlockPos initialPosition, World world) {
			ComponentProvider provider = ComponentProvider.fromWorld(world);
			WorldNetworkComponent networkComponent = provider.getComponent(AstromineComponentTypes.WORLD_NETWORK_COMPONENT);
			Block initialBlock = world.getBlockState(initialPosition).getBlock();

			if (!(initialBlock instanceof NetworkMember) || !((NetworkMember) initialBlock).isNode(type) || networkComponent.containsInstance(type, initialPosition)) {
				return;
			}

			LongSet tracedPositions = new LongOpenHashSet();
			tracedPositions.add(initialPosition.asLong());
			ArrayDeque<BlockPos> positionsToTrace = new ArrayDeque<>(Collections.singleton(initialPosition));

			NetworkInstance instance = new NetworkInstance(world, type);
			instance.addNode(NetworkNode.of(initialPosition));

			while (!positionsToTrace.isEmpty()) {
				BlockPos position = positionsToTrace.pop();
				boolean joined = false;
				Object initialObject = getObjectAt(world, position);

				for (Direction direction : Direction.values()) {
					BlockPos offsetPosition = position.offset(direction);
					long offsetPositionLong = offsetPosition.asLong();

					if (tracedPositions.contains(offsetPositionLong)) {
						continue;
					}

					Object offsetObject = getObjectAt(world, offsetPosition);

					NetworkInstance existingInstance = networkComponent.getInstance(type, offsetPosition);

					if (existingInstance != NetworkInstance.EMPTY) {
						existingInstance.join(instance);
						networkComponent.removeInstance(instance);
						networkComponent.addInstance(existingInstance);
						instance = existingInstance;
						joined = true;
					}

					if (offsetObject instanceof NetworkMember) {
						NetworkMember offsetMember = (NetworkMember) offsetObject;

						if (offsetMember.acceptsType(type)) {
							if (offsetMember.isRequester(type) || offsetMember.isProvider(type) || offsetMember.isBuffer(type)) {
								instance.addMember(NetworkMemberNode.of(offsetPosition, direction.getOpposite()));
							}

							if (offsetMember.isNode(type)) {
								positionsToTrace.addLast(offsetPosition);
								instance.addNode(NetworkNode.of(offsetPosition));
							}
						}
					}

					tracedPositions.add(offsetPositionLong);
				}

				if (joined) {
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
			for (Direction direction : Direction.values()) {
				Object offsetObject = getObjectAt(world, initialPosition.offset(direction));

				if (offsetObject instanceof NetworkMember && ((NetworkMember) offsetObject).acceptsType(type)) {
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
