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

package com.github.mixinors.astromine.common.widget.blade;

import java.util.Collections;
import java.util.List;

import com.github.mixinors.astromine.client.BaseRenderer;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.RedstoneControl;
import com.github.mixinors.astromine.common.util.NetworkingUtils;
import com.github.mixinors.astromine.registry.common.AMNetworks;
import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.client.texture.PartitionedTexture;
import dev.vini2003.hammer.common.widget.button.ButtonWidget;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class RedstoneControlWidget extends ButtonWidget {
    private ExtendedBlockEntity blockEntity;

    private static final ItemStack GLOWSTONE = new ItemStack(Items.GLOWSTONE_DUST);

    private static final ItemStack REDSTONE = new ItemStack(Items.REDSTONE);

    private static final ItemStack GUNPOWDER = new ItemStack(Items.GUNPOWDER);

    public static final PartitionedTexture TEXTURE = new PartitionedTexture(new Identifier("hammer", "textures/widget/panel.png"), 18.0F, 18.0F, 0.25F, 0.25F, 0.25F, 0.25F);
    
    @Override
    public void onMouseClicked(float x, float y, int button) {
        super.onMouseClicked(x, y, button);
    
        if (getFocused() && getHandled().getClient()) {
            var control = blockEntity.getRedstoneControl();

            RedstoneControl next;
    
            if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
                next = control.next();
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_2) {
                next = control.previous();
            } else {
                return;
            }
    
            var buf = NetworkingUtils.ofRedstoneControl(next, blockEntity.getPos());
    
            NetworkManager.sendToServer(AMNetworks.REDSTONE_CONTROL_UPDATE, buf);
        }
    }
    
    @NotNull
    @Override
    public List<Text> getTooltip() {
        return switch (blockEntity.getRedstoneControl()) {
            case WORK_WHEN_ON -> Collections.singletonList(new TranslatableText("tooltip.astromine.work_when_on").formatted(Formatting.GREEN));
            case WORK_WHEN_OFF -> Collections.singletonList(new TranslatableText("tooltip.astromine.work_when_off").formatted(Formatting.RED));
            case WORK_ALWAYS -> Collections.singletonList(new TranslatableText("tooltip.astromine.work_always").formatted(Formatting.YELLOW));
        };
    }
    
    @Override
    public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider, float delta) {
        if (getHidden()) {
            return;
        }
    
        var x = getPosition().getX();
        var y = getPosition().getY();
    
        var sX = getSize().getWidth();
        var sY = getSize().getHeight();

        TEXTURE.draw(matrices, provider, x, y, sX, sY);
    
        switch (blockEntity.getRedstoneControl()) {
            case WORK_WHEN_ON -> BaseRenderer.getDefaultItemRenderer().renderGuiItemIcon(REDSTONE, (int) x + (int) Math.max((sX - 16.0F) / 2.0F, 0.0F), (int) y + (int) Math.max((sY - 16.0F) / 2.0F, 0.0F));
            case WORK_WHEN_OFF -> BaseRenderer.getDefaultItemRenderer().renderGuiItemIcon(GUNPOWDER, (int) x + (int) Math.max((sX - 16.0F) / 2.0F, 0.0F), (int) y + (int) Math.max((sY - 16.0F) / 2.0F, 0.0F));
            case WORK_ALWAYS -> BaseRenderer.getDefaultItemRenderer().renderGuiItemIcon(GLOWSTONE, (int) x + (int) Math.max((sX - 16.0F) / 2.0F, 0.0F), (int) y + (int) Math.max((sY - 16.0F) / 2.0F, 0.0F));
        }
    }
    
    public ExtendedBlockEntity getBlockEntity() {
        return blockEntity;
    }
    
    public void setBlockEntity(ExtendedBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }
}
