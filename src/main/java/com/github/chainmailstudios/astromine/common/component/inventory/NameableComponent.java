package com.github.chainmailstudios.astromine.common.component.inventory;

import net.minecraft.item.Item;
import net.minecraft.text.TranslatableText;

import nerdhub.cardinal.components.api.component.Component;

public interface NameableComponent extends Component {
	Item getSymbol();

	TranslatableText getName();
}
