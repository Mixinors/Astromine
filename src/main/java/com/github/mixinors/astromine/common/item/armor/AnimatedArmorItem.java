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

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureTickListener;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.Optional;

public class AnimatedArmorItem extends ArmorItem {
	private final int frames;
	
	public AnimatedArmorItem(ArmorMaterial material, ArmorItem.Type slot, Settings settings, int frames) {
		super(material, slot, settings);
		
		this.frames = frames;
	}
	
	public int getFrames() {
		return frames;
	}
	
	@Environment(EnvType.CLIENT)
	public static final class AnimatedTexturePhase extends RenderPhase.Texture {
		private final Optional<Identifier> id;
		
		public AnimatedTexturePhase(Identifier id, int frames) {
			super(id, false, false);
			
			beginAction = () -> {
				var textureManager = DrawingUtil.getTextureManager();
				
				var texture = textureManager.getTexture(id);
				
				if (!(texture instanceof AnimatedTexture)) {
					if (texture != null) {
						try {
							texture.close();
						} catch (Exception e) {
							LogManager.getLogger().warn("Failed to close texture {}", id, e);
						}
						
						texture.clearGlId();
					}
					
					texture = new AnimatedTexture(id, frames);
					
					textureManager.registerTexture(id, texture);
				}
				
				RenderSystem.setShaderTexture(0, texture.getGlId());
			};
			
			endAction = () -> {};
			
			this.id = Optional.of(id);
		}
		
		@Override
		protected Optional<Identifier> getId() {
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
		
		private static final class AnimatedTexture extends AbstractTexture implements TextureTickListener {
			private final Identifier id;
			
			private final int frames;
			
			private int tick;
			
			private NativeImage image;
			
			private NativeImage placeholderTexture;
			
			public AnimatedTexture(Identifier id, int frames) {
				this.id = id;
				this.frames = frames;
			}
			
			@Override
			public void load(ResourceManager manager) throws IOException {
				close();
				
				var resource = manager.getResource(id).orElse(null);
				if (resource == null) return;
				
				image = NativeImage.read(resource.getInputStream());
				
				this.placeholderTexture = new NativeImage(this.image.getFormat(), image.getWidth(), image.getHeight() / frames, false);
				
				TextureUtil.prepareImage(this.getGlId(), placeholderTexture.getWidth(), placeholderTexture.getHeight());
			}
			
			@Override
			public void close() {
				super.close();
				
				if (this.image != null) {
					this.image.close();
					
					this.clearGlId();
					
					this.image = null;
				}
				
				if (this.placeholderTexture != null) {
					this.placeholderTexture.close();
					
					this.placeholderTexture = null;
				}
			}
			
			@Override
			public void tick() {
				if (!RenderSystem.isOnRenderThread()) {
					RenderSystem.recordRenderCall(this::tickAnimation);
				} else {
					this.tickAnimation();
				}
			}

			private void tickAnimation() {
				++tick;
				
				bindTexture();
				
				var yOffset = (tick % frames) * placeholderTexture.getHeight();
				
				for (var x = 0; x < placeholderTexture.getWidth(); x++) {
					for (var y = 0; y < placeholderTexture.getHeight(); y++) {
						placeholderTexture.setColor(x, y, image.getColor(x, y + yOffset));
					}
				}
				
				placeholderTexture.upload(0, 0, 0, false);
			}
		}
	}
}
