package com.github.chainmailstudios.astromine.common.component.inventory;

import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.item.Item;
import net.minecraft.text.TranslatableText;

import nerdhub.cardinal.components.api.component.Component;

public interface NameableComponent extends CopyableComponent<NameableComponent> {
	Item getSymbol();

	TranslatableText getName();
}
