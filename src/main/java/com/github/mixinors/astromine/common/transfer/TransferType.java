/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.common.transfer;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

/**
 * An enum representing a side's transfer information.
 */
public enum TransferType {
	NONE(AMCommon.id("textures/widget/none.png")),
	INPUT(AMCommon.id("textures/widget/insert.png")),
	OUTPUT(AMCommon.id("textures/widget/extract.png")),
	INPUT_OUTPUT(AMCommon.id("textures/widget/insert_extract.png"));
	
	public static final Codec<TransferType> CODEC = Codecs.createEnumCodec(TransferType.class);
	
	private final Identifier texture;
	
	/** Instantiates a {@link TransferType}. */
	TransferType(Identifier texture) {
		this.texture = texture;
	}
	
	/** Returns the next {@link TransferType} in relation to this one. */
	public TransferType next() {
		if (ordinal() + 1 == values().length) {
			return values()[0];
		}
		return values()[ordinal() + 1];
	}
	
	/** Returns the texture of this type. */
	public Identifier texture() {
		return texture;
	}
	
	/** Asserts whether this type accepts insertion or not. */
	public boolean canInsert() {
		return this == INPUT || this == INPUT_OUTPUT;
	}
	
	/** Asserts whether this type accepts extraction or not. */
	public boolean canExtract() {
		return this == OUTPUT || this == INPUT_OUTPUT;
	}
	
	/** Asserts whether this type is none or not. */
	public boolean isNone() {
		return this == NONE;
	}
}
