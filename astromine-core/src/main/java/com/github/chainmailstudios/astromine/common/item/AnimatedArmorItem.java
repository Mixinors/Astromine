package com.github.chainmailstudios.astromine.common.item;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.Optional;

public class AnimatedArmorItem extends ArmorItem {
	private int frames;

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

		public Texture(Identifier id, int count) {
			beginAction = () -> {
				RenderSystem.enableTexture();
				TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
				AbstractTexture _texture = textureManager.getTexture(id);
				if (!(_texture instanceof AnimatedTexture)) {
					if (_texture != null) {
						try {
							_texture.close();
						} catch (Exception e) {
							LogManager.getLogger().warn("Failed to close texture {}", id, e);
						}

						_texture.clearGlId();
					}

					_texture = new AnimatedTexture(id, count);
					textureManager.registerTexture(id, _texture);
				}
				
				_texture.bindTexture();
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
			private final int count;
			private int i;
			private NativeImage image;
			private NativeImage _image;

			public AnimatedTexture(Identifier id, int count) {
				this.id = id;
				this.count = count;
			}

			@Override
			public void load(ResourceManager manager) throws IOException {
				close();

				try (Resource resource = manager.getResource(id)) {
					image = NativeImage.read(resource.getInputStream());
				}
				this._image = new NativeImage(this.image.getFormat(), image.getWidth(), image.getHeight() / count, false);
				TextureUtil.allocate(this.getGlId(), _image.getWidth(), _image.getHeight());
			}

			@Override
			public void close() {
				super.close();
				if (this.image != null) {
					this.image.close();
					this.clearGlId();
					this.image = null;
				}
				if (this._image != null) {
					this._image.close();
					this._image = null;
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
				int yOffset = (i % count) * _image.getHeight();
				for (int x = 0; x < _image.getWidth(); x++) {
					for (int y = 0; y < count; y++) {
						_image.setPixelColor(x, y, image.getPixelColor(x, y + yOffset));
					}
				}
				_image.upload(0, 0, 0, false);
			}
		}
	}
}