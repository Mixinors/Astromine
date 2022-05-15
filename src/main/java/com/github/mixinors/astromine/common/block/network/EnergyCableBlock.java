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

package com.github.mixinors.astromine.common.block.network;

import com.github.mixinors.astromine.common.network.type.NetworkType;
import com.github.mixinors.astromine.registry.common.AMNetworkTypes;

public abstract class EnergyCableBlock extends CableBlock {
	public EnergyCableBlock(Settings settings) {
		super(settings);
	}
	
	public static final class Primitive extends EnergyCableBlock {
		public Primitive(Settings settings) {
			super(settings);
		}
		
		@Override
		public <T extends NetworkType> T getNetworkType() {
			return (T) AMNetworkTypes.PRIMITIVE_ENERGY;
		}
	}
	
	public static final class Basic extends EnergyCableBlock {
		public Basic(Settings settings) {
			super(settings);
		}
		
		@Override
		public <T extends NetworkType> T getNetworkType() {
			return (T) AMNetworkTypes.BASIC_ENERGY;
		}
	}
	
	public static final class Advanced extends EnergyCableBlock {
		public Advanced(Settings settings) {
			super(settings);
		}
		
		@Override
		public <T extends NetworkType> T getNetworkType() {
			return (T) AMNetworkTypes.ADVANCED_ENERGY;
		}
	}
	
	public static final class Elite extends EnergyCableBlock {
		public Elite(Settings settings) {
			super(settings);
		}
		
		@Override
		public <T extends NetworkType> T getNetworkType() {
			return (T) AMNetworkTypes.ELITE_ENERGY;
		}
	}
}
