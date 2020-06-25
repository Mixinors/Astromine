package com.github.chainmailstudios.astromine.common.component.inventory;

import com.github.chainmailstudios.astromine.AstromineCommon;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.item.Item;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public interface NameableComponent extends Component {
	Item getSymbol();

	TranslatableText getName();
}
