package com.github.chainmailstudios.astromine.client.render.skybox;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;

public class SpaceSkybox implements Skybox {
	public static final Identifier UP = new Identifier("skybox", "up");
	public static final Identifier DOWN = new Identifier("skybox", "down");
	public static final Identifier WEST = new Identifier("skybox", "west");
	public static final Identifier EAST = new Identifier("skybox", "east");
	public static final Identifier NORTH = new Identifier("skybox", "north");
	public static final Identifier SOUTH = new Identifier("skybox", "south");

	public static final Identifier PLANET = new Identifier("skybox", "planet");

	public ImmutableMap<Identifier, Identifier> textures;

	private SpaceSkybox(Builder builder) {
		this.textures = builder.textures.build();

		if (this.textures.size() != 7) {
			throw new UnsupportedOperationException("Skybox constructed without necessary information!");
		}
	}

	@Override
	public void render(MatrixStack matrices, float tickDelta) {
		MinecraftClient client = MinecraftClient.getInstance();

		TextureManager textureManager = client.getTextureManager();

		Tessellator tessellator = Tessellator.getInstance();

		BufferBuilder buffer = tessellator.getBuffer();

		World world = client.world;

		if (world == null) {
			return;
		}

		float rotation = (world.getTimeOfDay() / 12000f) * 360;

		int rawLight = (int) ((world.getTimeOfDay() / 12000) % 15);

		int vertexLight = 0x00f000f0 >> 2 | rawLight >> 3 | rawLight;

		for (int i = 0; i < 6; ++i) {
			matrices.push();

			switch (i) {
			case 0: {
				textureManager.bindTexture(this.textures.get(DOWN));

				matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));

				break;
			}
			case 1: {
				textureManager.bindTexture(this.textures.get(WEST));

				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));

				break;
			}
			case 2: {
				textureManager.bindTexture(this.textures.get(EAST));

				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
				matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(rotation));

				break;
			}
			case 3: {
				textureManager.bindTexture(this.textures.get(UP));

				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
				matrices.multiply(Vector3f.NEGATIVE_Z.getDegreesQuaternion(rotation));

				break;
			}
			case 4: {
				textureManager.bindTexture(this.textures.get(NORTH));

				matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
				matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));

				break;
			}
			case 5: {
				textureManager.bindTexture(this.textures.get(SOUTH));

				matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
				matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));

				break;
			}
			}


			buffer.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

			buffer.vertex(matrices.peek().getModel(), -100.0F, -100.0F, -100.0F).color(255, 255, 255, 255).texture(0.0F, 0.0F).light(vertexLight).next();
			buffer.vertex(matrices.peek().getModel(), -100.0F, -100.0F, 100.0F).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(vertexLight).next();
			buffer.vertex(matrices.peek().getModel(), 100.0F, -100.0F, 100.0F).color(255, 255, 255, 255).texture(1.0F, 1.0F).light(vertexLight).next();
			buffer.vertex(matrices.peek().getModel(), 100.0F, -100.0F, -100.0F).color(255, 255, 255, 255).texture(1.0F, 0.0F).light(vertexLight).next();

			tessellator.draw();

			matrices.pop();
		}

		textureManager.bindTexture(this.textures.get(PLANET));

		matrices.push();

		buffer.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

		buffer.vertex(matrices.peek().getModel(), -100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY() * 0.25f)), -100.0F).color(255, 255, 255, 255).texture(0.0F, 0.0F).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), -100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY() * 0.25f)), 100.0F).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), 100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY() * 0.25f)), 100.0F).color(255, 255, 255, 255).texture(1.0F, 1.0F).light(vertexLight).next();
		buffer.vertex(matrices.peek().getModel(), 100.0F, (float) (-64.0F - (client.player.getY() < 0 ? client.player.getY() : client.player.getY() * 0.25f)), -100.0F).color(255, 255, 255, 255).texture(1.0F, 0.0F).light(vertexLight).next();

		tessellator.draw();

		matrices.pop();
	}

	public static class Builder {
		ImmutableMap.Builder<Identifier, Identifier> textures = ImmutableMap.builder();

		public Builder() {
			// Unused.
		}

		public Builder up(Identifier up) {
			this.textures.put(UP, up);
			return this;
		}

		public Builder down(Identifier down) {
			this.textures.put(DOWN, down);
			return this;
		}

		public Builder east(Identifier east) {
			this.textures.put(EAST, east);
			return this;
		}

		public Builder west(Identifier west) {
			this.textures.put(WEST, west);
			return this;
		}

		public Builder north(Identifier north) {
			this.textures.put(NORTH, north);
			return this;
		}

		public Builder south(Identifier south) {
			this.textures.put(SOUTH, south);
			return this;
		}

		public Builder planet(Identifier planet) {
			this.textures.put(PLANET, planet);
			return this;
		}

		public SpaceSkybox build() {
			return new SpaceSkybox(this);
		}
	}
}
