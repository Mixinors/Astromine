package com.github.chainmailstudios.astromine.common.component;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.level.LevelProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public interface ComponentProvider extends nerdhub.cardinal.components.api.component.ComponentProvider {
	<T extends Component> Collection<T> getSidedComponents(@Nullable Direction direction);

	default <T extends Component> T getSidedComponent(@Nullable Direction direction, @NotNull ComponentType<T> type) {
		Optional<T> optional = (Optional<T>) getSidedComponents(direction).stream().filter(component -> type.getComponentClass().isInstance(component)).findFirst();
		return optional.orElse(null);
	}

	/**
	 * Convenience method to retrieve ComponentProvider from a given {@link ItemStack}
	 * Requires the <tt>cardinal-components-item</tt> module.
	 */
	@SuppressWarnings("ConstantConditions")
	static ComponentProvider fromItemStack(ItemStack stack) {
		return (ComponentProvider) (Object) stack;
	}

	/**
	 * Convenience method to retrieve a ComponentProvider from a given {@link Entity}.
	 * Requires the <tt>cardinal-components-entity</tt> module.
	 */
	static ComponentProvider fromEntity(Entity entity) {
		return (ComponentProvider) entity;
	}

	/**
	 * Convenience method to retrieve a ComponentProvider from a given {@link World}.
	 * Requires the <tt>cardinal-components-world</tt> module.
	 */
	static ComponentProvider fromWorld(World world) {
		return (ComponentProvider) world;
	}

	/**
	 * Convenience method to retrieve a ComponentProvider from given {@link LevelProperties}.
	 * Requires the <tt>cardinal-components-level</tt> module.
	 */
	static ComponentProvider fromLevel(WorldProperties level) {
		return (ComponentProvider) level;
	}

	/**
	 * Convenience method to retrieve a ComponentProvider from given {@link Chunk}.
	 * Requires the <tt>cardinal-components-chunk</tt> module.
	 */
	static ComponentProvider fromChunk(Chunk chunk) {
		return (ComponentProvider) chunk;
	}

	static ComponentProvider fromBlockEntity(BlockEntity blockEntity) {
		return blockEntity instanceof ComponentProvider ? (ComponentProvider) blockEntity : null;
	}
}
