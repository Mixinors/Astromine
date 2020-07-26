/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.multiblock;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.Set;

public abstract class MultiblockControllerBlockEntity extends BlockEntity implements ComponentProvider, BlockEntityClientSerializable {
	private final MultiblockType multiblockType;

	private final ImmutableMap<ComponentType<?>, Component> components;

	private final Map<BlockPos, MultiblockMemberBlockEntity> members = Maps.newHashMap();

	public MultiblockControllerBlockEntity(BlockEntityType<?> blockEntityType, MultiblockType multiblockType) {
		super(blockEntityType);
		this.multiblockType = multiblockType;
		this.components = multiblockType.getSuppliers();
	}

	public boolean canBuild() {
		return multiblockType.getBlocks().entrySet().stream().allMatch((entry) -> world.getBlockState(entry.getKey()).getBlock() == entry.getValue());
	}

	public void assemble() {
		multiblockType.getBlocks().forEach((key, value) -> {
			MultiblockMemberBlockEntity blockEntity = (MultiblockMemberBlockEntity) world.getBlockEntity(key);

			blockEntity.setController(this);

			members.put(key, blockEntity);
		});
	}

	public void destroy() {
		multiblockType.getBlocks().forEach((key, value) -> {
			MultiblockMemberBlockEntity blockEntity = (MultiblockMemberBlockEntity) world.getBlockEntity(key);

			blockEntity.setController(null);

			members.remove(key, blockEntity);
		});
	}

	@Override
	public boolean hasComponent(ComponentType<?> componentType) {
		return components.containsKey(componentType);
	}

	@Override
	public <C extends Component> C getComponent(ComponentType<C> componentType) {
		return (C) components.get(componentType);
	}

	@Override
	public Set<ComponentType<?>> getComponentTypes() {
		return Sets.newHashSet(multiblockType.getComponents().values());
	}

	public boolean hasComponent(BlockPos blockPos, ComponentType<?> type) {
		return multiblockType.getComponents().get(blockPos).stream().anyMatch(mapType -> mapType == type);
	}

	public <C extends Component> C getComponent(BlockPos blockPos, ComponentType<?> componentType) {
		return hasComponent(blockPos, componentType) ? (C) components.get(componentType) : null;
	}

	public Set<ComponentType<?>> getComponentTypes(BlockPos blockPos) {
		return Sets.newHashSet(multiblockType.getComponents().get(blockPos));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		components.forEach((key, value) -> {
			tag.put(key.getId().toString(), value.toTag(new CompoundTag()));
		});

		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		components.forEach((key, value) -> {
			value.fromTag(tag.getCompound(key.getId().toString()));
		});

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		toTag(tag);
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(null, tag);
	}
}
