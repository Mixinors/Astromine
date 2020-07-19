package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.component.world.WorldNetworkComponent;
import com.github.chainmailstudios.astromine.common.network.*;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCableBlock extends Block implements NetworkMember {
	public static final BooleanProperty EAST = BooleanProperty.of("east");
	public static final BooleanProperty WEST = BooleanProperty.of("west");
	public static final BooleanProperty NORTH = BooleanProperty.of("north");
	public static final BooleanProperty SOUTH = BooleanProperty.of("south");
	public static final BooleanProperty UP = BooleanProperty.of("up");
	public static final BooleanProperty DOWN = BooleanProperty.of("down");

	public static final Map<Direction, BooleanProperty> PROPERTY_MAP = new HashMap<Direction, BooleanProperty>() {{
		put(Direction.EAST, EAST);
		put(Direction.WEST, WEST);
		put(Direction.NORTH, NORTH);
		put(Direction.SOUTH, SOUTH);
		put(Direction.UP, UP);
		put(Direction.DOWN, DOWN);
	}};

	public static final Map<BooleanProperty, VoxelShape> SHAPE_MAP = new HashMap<BooleanProperty, VoxelShape>() {{
		put(UP, Block.createCuboidShape(6D, 10D, 6D, 10D, 16D, 10D));
		put(DOWN, Block.createCuboidShape(6D, 0D, 6D, 10D, 6D, 10D));
		put(NORTH, Block.createCuboidShape(6D, 6D, 0D, 10D, 10D, 6D));
		put(SOUTH, Block.createCuboidShape(6D, 6D, 10D, 10D, 10D, 16D));
		put(EAST, Block.createCuboidShape(10D, 6D, 6D, 16D, 10D, 10D));
		put(WEST, Block.createCuboidShape(0D, 6D, 6D, 6D, 10D, 10D));
	}};

	protected static final Map<Integer, VoxelShape> SHAPE_CACHE = new HashMap<>();

	protected static final VoxelShape CENTER_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
	private Map<NetworkType, Collection<NetworkMemberType>> properties = ofTypes(getNetworkType(), NODE);

	public AbstractCableBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	public abstract <T extends NetworkType> T getNetworkType();

	@Override
	public @NotNull Map<NetworkType, Collection<NetworkMemberType>> getMemberProperties() {
		return properties;
	}

	@Override
	public void onPlaced(World world, BlockPos position, BlockState stateA, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, position, stateA, placer, itemStack);

		NetworkTracer.Tracer tracer = new NetworkTracer.Tracer();
		tracer.trace(getNetworkType(), position, world);

		NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
		modeller.scanNeighbours(getNetworkType(), position, world);

		world.setBlockState(position, modeller.applyToBlockState(stateA));

		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = position.offset(direction);
			Block offsetBlock = world.getBlockState(offsetPos).getBlock();

			if (!(offsetBlock instanceof AbstractCableBlock)) continue;
			if (((AbstractCableBlock) offsetBlock).acceptsType(getNetworkType())) continue;

			NetworkTracer.Modeller offsetModeller = new NetworkTracer.Modeller();
			offsetModeller.scanNeighbours(((AbstractCableBlock) offsetBlock).getNetworkType(), offsetPos, world);

			world.setBlockState(offsetPos, offsetModeller.applyToBlockState(world.getBlockState(offsetPos)));
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos position, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, position, newState, moved);

		if (state.getBlock() == newState.getBlock()) return;

		ComponentProvider provider = ComponentProvider.fromWorld(world);

		WorldNetworkComponent networkComponent = provider.getComponent(AstromineComponentTypes.WORLD_NETWORK_COMPONENT);

		networkComponent.removeInstance(networkComponent.getInstance(getNetworkType(), position));

		for (Direction directionA : Direction.values()) {
			BlockPos offsetPos = position.offset(directionA);
			Block offsetBlock = world.getBlockState(offsetPos).getBlock();

			if (!(offsetBlock instanceof AbstractCableBlock)) continue;
			if (((AbstractCableBlock) offsetBlock).getNetworkType() != getNetworkType()) continue;

			NetworkTracer.Tracer tracer = new NetworkTracer.Tracer();
			tracer.trace(getNetworkType(), offsetPos, world);

			NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
			modeller.scanNeighbours(getNetworkType(), offsetPos, world);

			world.setBlockState(offsetPos, modeller.applyToBlockState(world.getBlockState(offsetPos)));
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos position, Block block, BlockPos neighborPosition, boolean moved) {
		super.neighborUpdate(state, world, position, block, neighborPosition, moved);

		ComponentProvider provider = ComponentProvider.fromWorld(world);

		WorldNetworkComponent networkComponent = provider.getComponent(AstromineComponentTypes.WORLD_NETWORK_COMPONENT);

		networkComponent.getInstance(getNetworkType(), position).removeNode(NetworkNode.of(position));

		NetworkTracer.Tracer tracer = new NetworkTracer.Tracer();
		tracer.trace(getNetworkType(), position, world);

		NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
		modeller.scanNeighbours(getNetworkType(), position, world);

		world.setBlockState(position, modeller.applyToBlockState(world.getBlockState(position)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView world, BlockPos position, ShapeContext entityContext) {
		VoxelShape returnShape = CENTER_SHAPE;
		NetworkTracer.Modeller modeller = new NetworkTracer.Modeller();
		modeller.scanBlockState(blockState);
		returnShape = modeller.applyToVoxelShape(returnShape);
		return returnShape;
	}
}
