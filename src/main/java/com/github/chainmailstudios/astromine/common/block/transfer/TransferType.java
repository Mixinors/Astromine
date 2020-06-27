package com.github.chainmailstudios.astromine.common.block.transfer;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;

public enum TransferType {
	INPUT,
	OUTPUT,
	INPUT_OUTPUT,
	NONE;

	private static final Identifier INPUT_TEXTURE = AstromineCommon.identifier("textures/widget/input.png");
	private static final Identifier OUTPUT_TEXTURE = AstromineCommon.identifier("textures/widget/output.png");
	private static final Identifier INPUT_OUTPUT_TEXTURE = AstromineCommon.identifier("textures/widget/input_output.png");
	private static final Identifier NONE_TEXTURE = AstromineCommon.identifier("textures/widget/none.png");

	public TransferType next() {
		return this == INPUT ? OUTPUT : this == OUTPUT ? INPUT_OUTPUT : this == INPUT_OUTPUT ? NONE : this == NONE ? INPUT : null;
	}

	public Identifier texture() {
		return this == INPUT ? INPUT_TEXTURE : this == OUTPUT ? OUTPUT_TEXTURE : this == INPUT_OUTPUT ? INPUT_OUTPUT_TEXTURE : this == NONE ? NONE_TEXTURE : null;
	}

	public boolean canInsert() {
		return this == INPUT || this == INPUT_OUTPUT;
	}

	public boolean canExtract() {
		return this == OUTPUT || this == INPUT_OUTPUT;
	}
}
