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

package com.github.mixinors.astromine.common.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

import java.util.Optional;

public class AnimatedArmorItem extends ArmorItem {
	private final int frames;
	
	public AnimatedArmorItem(Holder<ArmorMaterial> material, Type type, Properties settings, int frames) {
		super(material, type, settings);
		
		this.frames = frames;
	}
	
	public int getFrames() {
		return frames;
	}
	
	public static final class AnimatedTexturePhase extends RenderStateShard.TextureStateShard {
		private final Optional<ResourceLocation> id;
		
		public AnimatedTexturePhase(ResourceLocation id, int frames) {
			super(id, false, false);
			this.id = Optional.of(id);
		}
		
		@Override
		protected Optional<ResourceLocation> cutoutTexture() {
			return this.id;
		}
		
		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			} else if (object != null && this.getClass() == object.getClass()) {
				var animatedTexturePhase = (AnimatedTexturePhase) object;
				
				return this.id.equals(animatedTexturePhase.id);
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return this.id.hashCode();
		}
		
		@Override
		public String toString() {
			return this.name + '[' + this.id + "]";
		}
	}
}
