package com.github.chainmailstudios.astromine.common.component;

import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public interface ComponentProvider {
	<T extends Component> Collection<T> getComponents(@Nullable Direction direction);

	default <T extends Component> T getComponent(@Nullable Direction direction, @NotNull Class<T> clazz) {
		Optional<T> optional = (Optional<T>) getComponents(direction).stream().filter(component -> clazz.isInstance(component)).findFirst();
		return optional.orElse(null);
	}
}
