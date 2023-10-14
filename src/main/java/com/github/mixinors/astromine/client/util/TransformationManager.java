package com.github.mixinors.astromine.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class TransformationManager {
	
	public static final MinecraftClient client = MinecraftClient.getInstance();
	
	public static boolean isIsometricView = false;
	public static float isometricViewLength = 50;
	
	private static void updateCamera(MinecraftClient client) {
		Camera camera = client.gameRenderer.getCamera();
		camera.update(
				client.world,
				client.player,
				!client.options.getPerspective().isFirstPerson(),
				client.options.getPerspective().isFrontView(),
				client.getTickDelta()
		);
	}
	
	// https://docs.microsoft.com/en-us/windows/win32/opengl/glortho
	public static Matrix4f getIsometricProjection() {
		int w = client.getWindow().getWidth();
		int h = client.getWindow().getHeight();
		
		float wView = (isometricViewLength / h) * w;
		
		float near = -2000;
		float far = 2000;
		
		float left = -wView / 2;
		float right = wView / 2;
		
		float top = isometricViewLength / 2;
		float bottom = -isometricViewLength / 2;
		
		float[] arr = new float[]{
				2.0f / (right - left), 0, 0, -(right + left) / (right - left),
				0, 2.0f / (top - bottom), 0, -(top + bottom) / (top - bottom),
				0, 0, -2.0f / (far - near), -(far + near) / (far - near),
				0, 0, 0, 1
		};
		Matrix4f m1 = new Matrix4f();
		m1.set(arr);
		
		return m1;
	}
	
	@Environment(EnvType.CLIENT)
	public static class RemoteCallables {
		public static void enableIsometricView(float viewLength) {
			isometricViewLength = viewLength;
			isIsometricView = true;
			
			client.chunkCullingEnabled = false;
		}
		
		public static void disableIsometricView() {
			isIsometricView = false;
			
			client.chunkCullingEnabled = true;
		}
	}
	
	// isometric is equivalent to the camera being in infinitely far place
	public static Vec3d getIsometricAdjustedCameraPos() {
		Camera camera = client.gameRenderer.getCamera();
		return getIsometricAdjustedCameraPos(camera);
	}
	
	public static Vec3d getIsometricAdjustedCameraPos(Camera camera) {
		Vec3d cameraPos = camera.getPos();
		
		if (!isIsometricView) {
			return cameraPos;
		}
		
		Quaternionf rotation = camera.getRotation();
		Vector3f vec = new Vector3f(0, 0, client.options.getClampedViewDistance() * -10);
		rotation.transform(vec);
		
		return cameraPos.add(vec.x(), vec.y(), vec.z());
	}
}
