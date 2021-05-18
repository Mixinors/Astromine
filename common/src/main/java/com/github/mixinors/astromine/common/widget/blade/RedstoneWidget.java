/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.mixinors.astromine.client.BaseRenderer;
import com.github.mixinors.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.vini2003.blade.Blade;
import com.github.vini2003.blade.client.data.PartitionedTexture;
import com.github.vini2003.blade.common.widget.base.AbstractWidget;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * An {@link AbstractWidget} which represents
 * a redstone mode toggle for a {@link ComponentBlockEntity}.
 */
public class RedstoneWidget extends ButtonWidget {
    private ComponentBlockEntity blockEntity;

    private final ItemStack GLOWSTONE = new ItemStack(Items.GLOWSTONE_DUST);

    private final ItemStack REDSTONE = new ItemStack(Items.REDSTONE);

    private final ItemStack GUNPOWDER = new ItemStack(Items.GUNPOWDER);

    public static final PartitionedTexture TEXTURE = new PartitionedTexture(Blade.identifier("textures/widget/panel.png"), 18.0F, 18.0F, 0.25F, 0.25F, 0.25F, 0.25F);

    public RedstoneWidget() {
        setClickAction(() -> {
            if (!getHidden()) {
                blockEntity.getRedstoneComponent().setType(blockEntity.getRedstoneComponent().getType().getNext());
            }

            return null;
        });
    }

    /** Returns this widget's {@link ComponentBlockEntity}. */
    public ComponentBlockEntity getBlockEntity() {
        return blockEntity;
    }

    /** Sets this widget's {@link ComponentBlockEntity} to the specified value. */
    public void setBlockEntity(ComponentBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    /** Returns this widget's tooltip. */
    @NotNull
    @Override
    public List<Text> getTooltip() {
        return List.of(
                new TranslatableText("tooltip.astromine." + switch (blockEntity.getRedstoneComponent().getType()) {
                    case WORK_ALWAYS -> "work_always";
                    case WORK_WHEN_ON -> "work_when_on";
                    case WORK_WHEN_OFF -> "work_when_off";
                })
        );
    }

    /** Renders this widget. */
    @Override
    public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider) {
        if (getHidden())
            return;

        var x = getPosition().getX();
        var y = getPosition().getY();

        var sX = getSize().getWidth();
        var sY = getSize().getHeight();

        TEXTURE.draw(matrices, provider, x, y, sX, sY);
    
        BaseRenderer.getDefaultItemRenderer().renderGuiItemIcon(switch (blockEntity.getRedstoneComponent().getType()) {
            case WORK_ALWAYS -> GLOWSTONE;
            case WORK_WHEN_ON -> REDSTONE;
            case WORK_WHEN_OFF -> GUNPOWDER;
        }, (int) x + (int) Math.max((sX - 16.0F) / 2.0F, 0.0F), (int) y + (int) Math.max((sY - 16.0F) / 2.0F, 0.0F));
    }
}
