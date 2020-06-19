package com.github.chainmailstudios.astromine.common.component;

import net.minecraft.util.math.Direction;

import java.util.Collection;
import java.util.Optional;

public interface ComponentProvider {
	<T extends Component> Collection<T> getComponents(Direction direction);

	default <T extends Component> T getComponent(Direction direction, Class<T> clazz) {
		Optional<T> optional = (Optional<T>) getComponents(direction).stream().filter(component -> clazz.isInstance(component)).findFirst();
		return optional.orElse(null);
	}
}
