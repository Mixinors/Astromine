package com.github.chainmailstudios.astromine.common.component.inventory;

import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.registry.AstromineItems;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.item.Item;
import net.minecraft.text.TranslatableText;

public interface NameableComponent extends CopyableComponent<NameableComponent> {
	Item getSymbol();

	TranslatableText getName();
}
