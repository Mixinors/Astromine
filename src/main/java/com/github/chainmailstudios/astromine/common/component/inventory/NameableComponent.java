package com.github.chainmailstudios.astromine.common.component.inventory;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import nerdhub.cardinal.components.api.component.Component;

public interface NameableComponent extends Component {
	Identifier getSymbol();

	TranslatableText getName();
}
