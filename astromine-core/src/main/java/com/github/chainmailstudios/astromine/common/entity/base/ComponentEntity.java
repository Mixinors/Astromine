package com.github.chainmailstudios.astromine.common.entity.base;

import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public abstract class ComponentEntity extends Entity implements ComponentProvider {
	public ComponentEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public boolean hasComponent(ComponentType<?> componentType) {
		return ComponentProvider.fromEntity(this).hasComponent(componentType);
	}

	@Nullable
	@Override
	public <C extends Component> C getComponent(ComponentType<C> componentType) {
		return ComponentProvider.fromEntity(this).getComponent(componentType);
	}

	@Override
	public Set<ComponentType<?>> getComponentTypes() {
		return ComponentProvider.fromEntity(this).getComponentTypes();
	}
}
