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

import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.mojang.serialization.Codec;

/**
 * A {@link StorageSiding} which dictates a machine's siding.
 */
public enum StorageSiding {
	/**
	 * <b>Insertion</b> is allowed, <b>extraction</b> is denied.
	 */
	INSERT,
	/**
	 * <b>Extraction</b> is allowed, <b>insertion</b> is denied.
	 */
	EXTRACT,
	/**
	 * <b>Insertion</b> and <b>extraction</b> are both allowed.
	 */
	INSERT_EXTRACT,
	/**
	 * <b>Insertion</b> and <b>extraction</b> are both denied.
	 */
	NONE;
	
	public static final Codec<StorageSiding> CODEC = Codecs.createEnumCodec(StorageSiding.class);
	
	public StorageSiding next() {
		return switch (this) {
			case INSERT -> EXTRACT;
			case EXTRACT -> INSERT_EXTRACT;
			case INSERT_EXTRACT -> NONE;
			case NONE -> INSERT;
		};
	}
	
	public StorageSiding previous() {
		return switch (this) {
			case INSERT -> NONE;
			case EXTRACT -> INSERT;
			case INSERT_EXTRACT -> EXTRACT;
			case NONE -> INSERT_EXTRACT;
		};
	}
}
