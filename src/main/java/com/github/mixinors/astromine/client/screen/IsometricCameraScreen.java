package com.github.mixinors.astromine.client.screen;

import com.github.mixinors.astromine.client.util.IsometricCameraHandler;
import com.github.mixinors.astromine.common.entity.IsometricCameraEntity;
import com.github.mixinors.astromine.registry.client.AMKeyBindings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class IsometricCameraScreen extends Screen {private boolean sneakPressed;
    private boolean forwardPressed;
    private boolean backPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    private BlockPos hitBlock;

    public IsometricCameraScreen() {
        super(Text.translatable("trucc.gui.road_builder"));
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        IsometricCameraEntity cameraEntity = IsometricCameraHandler.cameraEntity;
        MinecraftClient client = this.client;

        if (cameraEntity == null || client == null) {
            return false;
        }

        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (AMKeyBindings.TOGGLE_CAMERA.matchesKey(keyCode, scanCode)) {
            this.close();
            return true;
        } else if (AMKeyBindings.ROTATE_CAMERA_LEFT.matchesKey(keyCode, scanCode)) {
            if (cameraEntity.getYaw() > 45) {
                cameraEntity.setYaw(cameraEntity.getYaw() - 90);
            } else {
                cameraEntity.setYaw(315);
            }

            return true;
        } else if (AMKeyBindings.ROTATE_CAMERA_RIGHT.matchesKey(keyCode, scanCode)) {
            if (cameraEntity.getYaw() < 315) {
                cameraEntity.setYaw(cameraEntity.getYaw() + 90);
            } else {
                cameraEntity.setYaw(45);
            }

            return true;
        } else {
            return this.keyEvent(keyCode, scanCode, true);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (super.keyReleased(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            return this.keyEvent(keyCode, scanCode, false);
        }
    }

    private boolean keyEvent(int keyCode, int scanCode, boolean pressed) {
        MinecraftClient client = this.client;

        if (client == null) {
            return false;
        }

        if (client.options.sneakKey.matchesKey(keyCode, scanCode)) {
            this.sneakPressed = pressed;
        } else if (client.options.forwardKey.matchesKey(keyCode, scanCode)) {
            this.forwardPressed = pressed;
        } else if (client.options.backKey.matchesKey(keyCode, scanCode)) {
            this.backPressed = pressed;
        } else if (client.options.leftKey.matchesKey(keyCode, scanCode)) {
            this.leftPressed = pressed;
        } else if (client.options.rightKey.matchesKey(keyCode, scanCode)) {
            this.rightPressed = pressed;
        } else {
            return false;
        }

        return true;
    }

    private void updateEntitySpeed() {
        MinecraftClient client = this.client;
        IsometricCameraEntity cameraEntity = IsometricCameraHandler.cameraEntity;

        if (client == null || cameraEntity == null) {
            return;
        }

        float speed = IsometricCameraHandler.speed;

        if (this.leftPressed) {
            speed = 2;
            cameraEntity.sidewaysSpeed = 1;
        } else if (this.rightPressed) {
            speed = 2;
            cameraEntity.sidewaysSpeed = -1;
        }

        if (this.forwardPressed) {
            if (!this.rightPressed && !this.leftPressed)
                speed = 3.3F;
            cameraEntity.forwardSpeed = 1;
        } else if (this.backPressed) {
            if (!this.rightPressed && !this.leftPressed)
                speed = 3.3F;
            cameraEntity.forwardSpeed = -1;
        }

        if (this.sneakPressed) {
            speed *= 2;
        }

        IsometricCameraHandler.speed = speed;
    }

    public void worldTick() {
        this.updateEntitySpeed();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (super.mouseScrolled(mouseX, mouseY, amount)) {
            return true;
        }

        MinecraftClient client = this.client;

        if (client == null) {
            return false;
        }

        if (amount == 1) {
            IsometricCameraHandler.zoom -= this.sneakPressed ? 10 : 5;
        } else if (amount == -1) {
            IsometricCameraHandler.zoom += this.sneakPressed ? 10 : 5;
        }

        IsometricCameraHandler.zoom = Math.min(Math.max(IsometricCameraHandler.zoom, IsometricCameraHandler.maxZoom), IsometricCameraHandler.minZoom);
        return true;
    }

    @Override
    protected void init() {
        IsometricCameraHandler.startCamera();
        super.init();
    }

    @Override
    public void removed() {
        IsometricCameraHandler.stopCamera();
        super.removed();
    }
	
	@Override
    public boolean shouldPause() {
        return false;
    }
}