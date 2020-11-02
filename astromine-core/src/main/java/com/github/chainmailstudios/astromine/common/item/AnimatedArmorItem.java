/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.texture.TextureTickListener;
import net.minecraft.client.texture.TextureUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import com.mojang.blaze3d.systems.RenderSystem;

import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.Optional;

public class AnimatedArmorItem extends ArmorItem {
	private final int frames;

	public AnimatedArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings, int frames) {
		super(material, slot, settings);
		this.frames = frames;
	}

	public int getFrames() {
		return frames;
	}

	@Environment(EnvType.CLIENT)
	public static class Texture extends RenderPhase.Texture {
		private final Optional<Identifier> id;

		public Texture(Identifier id, int frames) {
			beginAction = () -> {
				RenderSystem.enableTexture();
				TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
				AbstractTexture texture = textureManager.getTexture(id);
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

				texture.bindTexture();
			};
			endAction = () -> {};
			this.id = Optional.of(id);
		}

		public Texture() {
			this.id = Optional.empty();
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			} else if (object != null && this.getClass() == object.getClass()) {
				AnimatedArmorItem.Texture texture = (AnimatedArmorItem.Texture) object;
				return this.id.equals(texture.id);
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

		protected Optional<Identifier> getId() {
			return this.id;
		}

		private static final class AnimatedTexture extends AbstractTexture implements TextureTickListener {
			private final Identifier id;
			private final int frames;
			private int i;
			private NativeImage image;
			private NativeImage tmpTexture;

			public AnimatedTexture(Identifier id, int frames) {
				this.id = id;
				this.frames = frames;
			}

			@Override
			public void load(ResourceManager manager) throws IOException {
				close();

				try (Resource resource = manager.getResource(id)) {
					image = NativeImage.read(resource.getInputStream());
				}
				this.tmpTexture = new NativeImage(this.image.getFormat(), image.getWidth(), image.getHeight() / frames, false);
				TextureUtil.allocate(this.getGlId(), tmpTexture.getWidth(), tmpTexture.getHeight());
			}

			@Override
			public void close() {
				super.close();
				if (this.image != null) {
					this.image.close();
					this.clearGlId();
					this.image = null;
				}
				if (this.tmpTexture != null) {
					this.tmpTexture.close();
					this.tmpTexture = null;
				}
			}

			@Override
			public void tick() {
				if (!RenderSystem.isOnRenderThread()) {
					RenderSystem.recordRenderCall(this::_tick);
				} else {
					this._tick();
				}
			}

			private void _tick() {
				i++;
				bindTexture();
				int yOffset = (i % frames) * tmpTexture.getHeight();
				for (int x = 0; x < tmpTexture.getWidth(); x++) {
					for (int y = 0; y < tmpTexture.getHeight(); y++) {
						tmpTexture.setPixelColor(x, y, image.getPixelColor(x, y + yOffset));
					}
				}
				tmpTexture.upload(0, 0, 0, false);
			}
		}
	}
}
