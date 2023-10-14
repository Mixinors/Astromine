package com.github.mixinors.astromine.client.util;

import com.github.mixinors.astromine.client.screen.IsometricCameraScreen;
import com.github.mixinors.astromine.common.entity.IsometricCameraEntity;
import com.github.mixinors.astromine.registry.client.AMKeyBindings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.text.Text;

public class IsometricCameraHandler {
    public static float speed = 2;
    public static int zoom = 50;
    public static int minZoom = 70;
    public static int maxZoom = 15;
    private static Perspective prevPerspective = null;
    public static IsometricCameraEntity cameraEntity = null;
    private static MinecraftClient client = MinecraftClient.getInstance();

    public static void startCamera() {
        if (cameraEntity == null) {
            IsometricCameraEntity entity = new IsometricCameraEntity(client.world, client.player);
            MinecraftClient.getInstance().setCameraEntity(entity);
            TransformationManager.RemoteCallables.enableIsometricView(50);
            cameraEntity = entity;

            prevPerspective = client.options.getPerspective();
            client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            client.world.spawnEntity(entity);
        }
    }

    public static void stopCamera() {
        if (cameraEntity != null) {
            TransformationManager.RemoteCallables.disableIsometricView();
            MinecraftClient.getInstance().setCameraEntity(client.player);
            cameraEntity.kill();
            cameraEntity = null;

            client.options.setPerspective(prevPerspective);
            prevPerspective = null;
        }
    }

    public static void tick() {
        while (AMKeyBindings.toggleCamera.wasPressed()) {
            client.player.sendMessage(Text.literal("Toggle Camera was pressed!"), false);
            client.setScreen(new IsometricCameraScreen());
        }

        if (client.currentScreen instanceof IsometricCameraScreen s) {
            s.worldTick();
        }

        if (cameraEntity != null) {
            cameraEntity.tick();
        }
    }
}