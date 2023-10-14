/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.common.block.entity;

import com.github.mixinors.astromine.common.component.world.HoloBridgesComponent;
import com.github.mixinors.astromine.common.tick.Tickable;
import com.github.mixinors.astromine.common.util.LineUtils;
import com.github.mixinors.astromine.common.util.VectorUtils;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import dev.architectury.hooks.block.BlockEntityHooks;
import dev.vini2003.hammer.core.api.client.color.Color;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

public class HoloBridgeProjectorBlockEntity extends BlockEntity implements Tickable {
	public static final String CHILD_POSITION_KEY = "ChildPosition";
	public static final String PARENT_POSITION_KEY = "ParentPosition";
	public static final String COLOR_KEY = "Color";
	
	public static final String R_KEY = "R";
	public static final String G_KEY = "G";
	public static final String B_KEY = "B";
	public static final String A_KEY = "A";
	
	public static final Color DEFAULT_COLOR = new Color(0.5F, 0.79F, 0.83F, 0.5F);
	
	private HoloBridgeProjectorBlockEntity child = null;
	private HoloBridgeProjectorBlockEntity parent = null;
	
	private BlockPos childPosition = null;
	private BlockPos parentPosition = null;
	
	private boolean hasCheckedChild = false;
	private boolean hasCheckedParent = false;
	
	private boolean shouldInitialize = false;
	
	public List<Vector3f> segments = null;
	
	public Color color = DEFAULT_COLOR;
	
	public HoloBridgeProjectorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.HOLOGRAPHIC_BRIDGE.get(), blockPos, blockState);
	}
	
	public boolean hasChild() {
		return this.child != null;
	}
	
	@Override
	public void tick() {
		if (world != null && world.isClient) {
			if (shouldInitialize) {
				this.destroyBridge();
				
				if (this.childPosition != null) {
					this.child = (HoloBridgeProjectorBlockEntity) this.world.getBlockEntity(this.childPosition);
				}
				
				this.buildBridge();
				
				shouldInitialize = false;
			}
		}
		
		if (this.world == null || this.world.isClient) {
			return;
		}
		
		if (!this.hasCheckedChild && this.childPosition != null) {
			var childEntity = this.world.getBlockEntity(this.childPosition);
			
			if (childEntity instanceof HoloBridgeProjectorBlockEntity holoChildEntity) {
				this.child = holoChildEntity;
				this.hasCheckedChild = true;
				
				this.buildBridge();
			} else if (childEntity != null) {
				this.hasCheckedChild = true;
			}
		}
		
		if (!this.hasCheckedParent && this.parentPosition != null) {
			var parentEntity = this.world.getBlockEntity(parentPosition);
			
			if (parentEntity instanceof HoloBridgeProjectorBlockEntity holoParentEntity) {
				this.parent = holoParentEntity;
				this.hasCheckedParent = true;
				
				this.buildBridge();
			} else if (parentEntity != null) {
				this.hasCheckedParent = true;
			}
		}
	}
	
	public void buildBridge() {
		var component = HoloBridgesComponent.get(world);
		if (component == null) {
			return;
		}
		
		var bridgeVoxelShapes = getBridgeVoxelShapes();
		
		for (var entry : bridgeVoxelShapes.entrySet()) {
			var pos = entry.getKey();
			var shape = entry.getValue();
			
			component.setShape(pos, shape);
			
			if (world.getBlockState(pos).isAir()) {
				world.setBlockState(pos, AMBlocks.HOLOGRAPHIC_BRIDGE_INVISIBLE_BLOCK.get().getDefaultState());
			}
		}
	}
	
	public Map<BlockPos, VoxelShape> getBridgeVoxelShapes() {
		var facing = getCachedState().get(HORIZONTAL_FACING);
		
		var bridgePositions = getBridgePositions();
		var bridgeVoxelShapes = new HashMap<BlockPos, VoxelShape>();
		
		// Span X.
		if (facing == Direction.NORTH || facing == Direction.SOUTH) {
			for (var bridgePosition : bridgePositions) {
				if (world.getBlockState(new BlockPos(
						(int) bridgePosition.x,
						(int) bridgePosition.y,
						(int) bridgePosition.z
				)).isAir()) {
					continue;
				}
				
				// We need to find all the blocks that this section spans.
				
				// To do that, we subtract 0.5F on the X, then step through
				// that until the point after adding 0.5F to it.
				
				// We step in increments of 1.0F / 16.0F; building the
				// shape cube by cube.
				
				// When the X changes, we save the position and voxel shape,
				// and restart it.
				
				var shape = VoxelShapes.empty();
				
				var prevX = bridgePosition.getX() - 0.5F;
				
				for (var x = bridgePosition.getX() - 0.5F; x < bridgePosition.getX() + 0.5F; x += (1.0F / 16.0F)) {
					var y = bridgePosition.getY();
					var z = bridgePosition.getZ();
					
					if ((int) x != (int) prevX) {
						// X has changed. Save the VoxelShape; move to the next.
						bridgeVoxelShapes.put(
								new BlockPos(
										(int) x,
										(int) y,
										(int) z
								),
								shape
						);
						
						// Start by placing the first cube in the next voxel shape.
						
						// The added cube is 1.0F / 16.0F in the X, Y and Z axis.
						// Its coordinates are local to the block; and as such we simply
						// do n % (int) n.
						shape = VoxelShapes.union(
								shape,
								VoxelShapes.cuboid(
										x % ((int) x),
										y % ((int) y),
										z % ((int) z),
										x % ((int) x) + (1.0F / 16.0F),
										y % ((int) y) + (1.0F / 16.0F),
										z % ((int) z) + (1.0F / 16.0F)
								
								)
						);
					} else {
						shape = VoxelShapes.union(
								shape,
								VoxelShapes.cuboid(
										x % ((int) x),
										y % ((int) y),
										z % ((int) z),
										x % ((int) x) + (1.0F / 16.0F),
										y % ((int) y) + (1.0F / 16.0F),
										z % ((int) z) + (1.0F / 16.0F)
								
								)
						);
					}
					
					prevX = x;
				}
			}
		}
		
		// Span Z.
		if (facing == Direction.WEST || facing == Direction.EAST) {
			var shape = VoxelShapes.empty();

			var prevX = -1.0F;
			var prevY = -1.0F;
			var prevZ = -1.0F;
			
			for (var bridgePosition : bridgePositions) {
				var bridgeBlockPos = new BlockPos(
						(int) bridgePosition.getX(),
						(int) bridgePosition.getY(),
						(int) bridgePosition.getZ()
				);
				
				if (
						(int) bridgePosition.getZ() != (int) prevZ ||
						(int) bridgePosition.getY() != (int) prevY ||
						(int) bridgePosition.getX() != (int) prevX
				
				) {
					if (bridgeVoxelShapes.containsKey(bridgeBlockPos)) {
						shape = bridgeVoxelShapes.get(bridgeBlockPos);
					} else {
						shape = VoxelShapes.empty();
					}
				}
				
				prevX = (float) bridgePosition.getX();
				prevY = (float) bridgePosition.getY();
				prevZ = (float) bridgePosition.getZ();
				
				for (var z = bridgePosition.getZ(); z < bridgePosition.getZ() + 1.0F; z += (1.0F / 16.0F)) {
					if (world instanceof ServerWorld serverWorld) {
						serverWorld.spawnParticles(
								ParticleTypes.FLAME,
								bridgePosition.getX() + 0.5F,
								bridgePosition.getY() + 0.5F,
								z,
								1,
								0.0F,
								0.0F,
								0.0F,
								0.0F
						);
					}
					
					var x = bridgePosition.getX();
					var y = bridgePosition.getY();
					
					var cX = x % ((int) x);
					var cY = y % ((int) y);
					var cZ = z % ((int) z);
					
					shape = VoxelShapes.union(
							shape,
							VoxelShapes.cuboid(
									cX,
									cY,
									cZ,
									cX + (1.0F / 16.0F),
									cY + (1.0F / 16.0F),
									cZ + (1.0F / 16.0F)
							)
					);
					
					bridgeVoxelShapes.put(
							new BlockPos(
									(int) x,
									(int) y,
									(int) z
							),
							shape
					);
				}
			}
		}
		
		return bridgeVoxelShapes;
	}
	
	public Set<Vec3d> getBridgePositions() {
		if (!hasChild()) {
			return new HashSet<>();
		}
		
		var thisPos = this.getPos();
		var childPos = child.getPositionInFront();
		
		var distance = Math.sqrt(thisPos.getSquaredDistance(childPos));
		
		var precision = 16.0F;
		
		var stepX = (childPos.getX() - thisPos.getX()) / (distance * precision);
		var stepY = (childPos.getY() - thisPos.getY()) / (distance * precision);
		var stepZ = (childPos.getZ() - thisPos.getZ()) / (distance * precision);
		
		var positions = new HashSet<Vec3d>();
		
		for (var i = 0; i < distance * precision; ++i) {
			positions.add(
					new Vec3d(
							thisPos.getX() + stepX * i,
							thisPos.getY() + stepY * i + 1.0F,
							thisPos.getZ() + stepZ * i
					)
			);
		}
		
		return positions;
	}
	
	public BlockPos getPositionInFront() {
		return getPos().offset(getCachedState().get(HORIZONTAL_FACING));
	}
	
	public Vec3d getBridgePositionInFront() {
		return switch (getCachedState().get(HORIZONTAL_FACING)) {
			case NORTH -> new Vec3d(getPos().getX() + 0.5F, getPos().getY() + 1.0F, getPos().getZ());
			case SOUTH -> new Vec3d(getPos().getX() + 0.5F, getPos().getY() + 1.0F, getPos().getZ() + 1.0F);
			case WEST -> new Vec3d(getPos().getX(), getPos().getY() + 1.0F, getPos().getZ() + 0.5F);
			case EAST -> new Vec3d(getPos().getX() + 1.0F, getPos().getY() + 1.0F, getPos().getZ() + 0.5F);
			
			default -> new Vec3d(getPos().getX() + 0.5F, getPos().getY() + 1.0F, getPos().getZ() + 0.5F);
		};
	}
	
	public HoloBridgeProjectorBlockEntity getChild() {
		return this.child;
	}
	
	public void setChild(HoloBridgeProjectorBlockEntity child) {
		this.child = child;
		
		if (this.child != null) {
			this.child.setParent(this);
			this.child.setChild(null);
		}
		
		this.childPosition = child != null ? child.getPos() : null;
	}
	
	public HoloBridgeProjectorBlockEntity getParent() {
		return parent;
	}
	
	public void setParent(HoloBridgeProjectorBlockEntity parent) {
		this.parent = parent;
		
		this.setChild(null);
		
		this.markDirty();
	}
	
	@Override
	public void markRemoved() {
		if (this.child != null) {
			this.destroyBridge();
			
			this.setChild(null);
			
			if (!world.isClient) {
				BlockEntityHooks.syncData(this);
			}
		}
		
		if (this.parent != null) {
			this.parent.destroyBridge();
			
			this.parent.setChild(null);
			
			if (!world.isClient) {
				BlockEntityHooks.syncData(this.parent);
			}
		}
		
		
		super.markRemoved();
	}
	
	public void destroyBridge() {
		if (this.segments != null && this.world != null) {
			var bridgeComponent = HoloBridgesComponent.get(world);
			
			for (var vec : this.segments) {
				var pos = new BlockPos((int) vec.x(), (int) vec.y(), (int) vec.z());
				
				bridgeComponent.setShape(pos, null);
				
				this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			this.segments.clear();
		}
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		if (nbt.contains(CHILD_POSITION_KEY)) {
			this.childPosition = BlockPos.fromLong(nbt.getLong(CHILD_POSITION_KEY));
		}
		
		if (nbt.contains(PARENT_POSITION_KEY)) {
			this.parentPosition = BlockPos.fromLong(nbt.getLong(PARENT_POSITION_KEY));
		}
		
		if (nbt.contains(COLOR_KEY)) {
			var colorTag = nbt.getCompound(COLOR_KEY);
			
			color = new Color(
					colorTag.getFloat(R_KEY),
					colorTag.getFloat(G_KEY),
					colorTag.getFloat(B_KEY),
					colorTag.getFloat(A_KEY)
			);
		}
		
		shouldInitialize = true;
		
		super.readNbt(nbt);
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		if (this.child != null) {
			nbt.putLong(CHILD_POSITION_KEY, this.child.getPos().asLong());
		} else if (this.childPosition != null) {
			nbt.putLong(CHILD_POSITION_KEY, this.childPosition.asLong());
		}
		
		if (this.parent != null) {
			nbt.putLong(PARENT_POSITION_KEY, this.parent.getPos().asLong());
		} else if (this.parentPosition != null) {
			nbt.putLong(PARENT_POSITION_KEY, this.parentPosition.asLong());
		}
		
		var colorTag = new NbtCompound();
		colorTag.putFloat(R_KEY, color.getR());
		colorTag.putFloat(G_KEY, color.getG());
		colorTag.putFloat(B_KEY, color.getB());
		colorTag.putFloat(A_KEY, color.getA());
		
		nbt.put(COLOR_KEY, colorTag);
		
		super.writeNbt(nbt);
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
}
