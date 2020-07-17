package com.github.chainmailstudios.astromine.common.block.transfer;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.util.Identifier;

public enum TransferType {
	NONE(AstromineCommon.identifier("textures/widget/none.png")),
	INPUT(AstromineCommon.identifier("textures/widget/input.png")),
	OUTPUT(AstromineCommon.identifier("textures/widget/output.png")),
	INPUT_OUTPUT(AstromineCommon.identifier("textures/widget/input_output.png")),
	DISABLED(AstromineCommon.identifier("textures/widget/disabled.png"));

	private Identifier texture;

	TransferType(Identifier texture) {
		this.texture = texture;
	}

	public TransferType next() {
		if (ordinal() + 1 == values().length)
			return values()[0];
		return values()[ordinal() + 1];
	}

	public Identifier texture() {
		return texture;
	}

	public boolean canInsert() {
		return this == INPUT || this == INPUT_OUTPUT;
	}

	public boolean canExtract() {
		return this == OUTPUT || this == INPUT_OUTPUT;
	}

	public boolean isDisabled() {
		return this == NONE || this == DISABLED;
	}
}
