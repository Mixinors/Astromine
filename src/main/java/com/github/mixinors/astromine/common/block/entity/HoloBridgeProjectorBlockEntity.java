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
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import dev.architectury.hooks.block.BlockEntityHooks;
import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.*;
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
			var maxLoops = bridgePositions.size() * 16;
			var totalLoops = 0;
			
			for (var bridgePos : bridgePositions) {
				++totalLoops;
				
				if (totalLoops > maxLoops) {
					for (var player : world.getPlayers()) {
						player.sendMessage(Text.literal("Too many loops!").formatted(Formatting.RED), false);
					}
					
					break;
				}
				
				var y = bridgePos.y;
				var z = bridgePos.z;
				
				for (var x = bridgePos.x - 0.5F; x < bridgePos.x + 0.5F; x += 1.0F / 16.0F) {
					var bridgeBlockPos = new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
					
					var shape = bridgeVoxelShapes.getOrDefault(bridgeBlockPos, VoxelShapes.empty());
					
					var cX = x % (int) x;
					var cY = bridgePos.y % (int) bridgePos.y;
					var cZ = bridgePos.z % (int) bridgePos.z;
					
					if (cX < 0.0F) {
						cX += 1.0F;
					}
					if (cY < 0.0F) {
						cY += 1.0F;
					}
					if (cZ < 0.0F) {
						cZ += 1.0F;
					}
					
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
					
					bridgeVoxelShapes.put(bridgeBlockPos, shape);
				}
			}
		}
		
		// Span Z.
		if (facing == Direction.WEST || facing == Direction.EAST) {
			var maxLoops = bridgePositions.size() * 16;
			var totalLoops = 0;
			
			for (var bridgePos : bridgePositions) {
				++totalLoops;
				
				if (totalLoops > maxLoops) {
					for (var player : world.getPlayers()) {
						player.sendMessage(Text.literal("Too many loops!").formatted(Formatting.RED), false);
					}
					
					break;
				}
				
				var x = bridgePos.x;
				var y = bridgePos.y;
				
				for (var z = bridgePos.z - 0.5F; z < bridgePos.z + 0.5F; z += 1.0F / 16.0F) {
					var bridgeBlockPos = new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
					
					var shape = bridgeVoxelShapes.getOrDefault(bridgeBlockPos, VoxelShapes.empty());
					
					var cX = bridgePos.x % (int) bridgePos.x;
					var cY = bridgePos.y % (int) bridgePos.y;
					var cZ = z % (int) z;
					
					if (cX < 0.0F) {
						cX += 1.0F;
					}
					if (cY < 0.0F) {
						cY += 1.0F;
					}
					if (cZ < 0.0F) {
						cZ += 1.0F;
					}
					
					cX = MathHelper.clamp(cX, 0.0F, 1.0F - (1.0F / 16.0F));
					cY = MathHelper.clamp(cY, 0.0F, 1.0F - (1.0F / 16.0F));
					cZ = MathHelper.clamp(cZ, 0.0F, 1.0F - (1.0F / 16.0F));
					
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
					
					bridgeVoxelShapes.put(bridgeBlockPos, shape);
				}
			}
		}
		
		return bridgeVoxelShapes;
	}
	
	public Set<Vec3d> getBridgePositions() {
		if (!hasChild()) {
			return new HashSet<>();
		}
		
		var thisPos = this.getHookPosition();
		var childPos = child.getHookPosition();
		
		var distance = thisPos.distanceTo(childPos);
		
		var precision = 16.0F;
		
		var stepX = (childPos.getX() - thisPos.getX()) / (distance * precision);
		var stepY = (childPos.getY() - thisPos.getY()) / (distance * precision);
		var stepZ = (childPos.getZ() - thisPos.getZ()) / (distance * precision);
		
		var positions = new HashSet<Vec3d>();
		
		for (var i = 0; i < distance * precision; ++i) {
			positions.add(
					new Vec3d(
							thisPos.getX() + stepX * i,
							thisPos.getY() + stepY * i,
							thisPos.getZ() + stepZ * i
					)
			);
		}
		
		return positions;
	}
	
	public Vec3d getHookPosition() {
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
