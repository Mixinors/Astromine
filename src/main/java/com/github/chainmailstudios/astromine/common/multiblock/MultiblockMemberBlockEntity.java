package com.github.chainmailstudios.astromine.common.multiblock;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.ComponentProvider;

import java.util.Set;

public abstract class MultiblockMemberBlockEntity extends BlockEntity implements ComponentProvider {
	private MultiblockControllerBlockEntity controller;

	private BlockPos relative;

	public MultiblockMemberBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	public MultiblockControllerBlockEntity getController() {
		return controller;
	}

	public void setController(MultiblockControllerBlockEntity controller) {
		this.controller = controller;
	}

	public BlockPos getRelative() {
		return relative;
	}

	public void setRelative(BlockPos relative) {
		this.relative = relative;
	}

	@Override
	public boolean hasComponent(ComponentType<?> componentType) {
		return controller.hasComponent(relative, componentType);
	}

	@Override
	public <C extends Component> C getComponent(ComponentType<C> componentType) {
		return controller.getComponent(relative, componentType);
	}

	@Override
	public Set<ComponentType<?>> getComponentTypes() {
		return controller.getComponentTypes(relative);
	}
}
