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

import dev.vini2003.hammer.client.texture.PartitionedTexture;
import dev.vini2003.hammer.common.widget.Widget;
import dev.vini2003.hammer.common.widget.button.ButtonWidget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.mixinors.astromine.client.BaseRenderer;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * An {@link Widget} which represents
 * a redstone mode toggle for a {@link ExtendedBlockEntity}.
 */
public class RedstoneWidget extends ButtonWidget
{
    private ExtendedBlockEntity blockEntity;

    private final ItemStack GLOWSTONE = new ItemStack(Items.GLOWSTONE_DUST);

    private final ItemStack REDSTONE = new ItemStack(Items.REDSTONE);

    private final ItemStack GUNPOWDER = new ItemStack(Items.GUNPOWDER);

    public static final PartitionedTexture TEXTURE = new PartitionedTexture(new Identifier("hammer", "textures/widget/panel.png"), 18F, 18F, 0.25F, 0.25F, 0.25F, 0.25F);

    public RedstoneWidget() {
        setClickAction(() -> {
            if (!getHidden()) {
                blockEntity.getRedstoneComponent().setType(blockEntity.getRedstoneComponent().getType().next());
            }

            return null;
        });
    }

    /** Returns this widget's {@link ExtendedBlockEntity}. */
    public ExtendedBlockEntity getBlockEntity() {
        return blockEntity;
    }

    /** Sets this widget's {@link ExtendedBlockEntity} to the specified value. */
    public void setBlockEntity(ExtendedBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    /** Returns this widget's tooltip. */
    @NotNull
    @Override
    public List<Text> getTooltip() {
        switch (blockEntity.getRedstoneComponent().getType()) {
            case WORK_WHEN_ON: {
                return Collections.singletonList(new TranslatableText("tooltip.astromine.work_when_on").formatted(Formatting.GREEN));
            }
            case WORK_WHEN_OFF: {
                return Collections.singletonList(new TranslatableText("tooltip.astromine.work_when_off").formatted(Formatting.RED));
            }
            case WORK_ALWAYS: {
                return Collections.singletonList(new TranslatableText("tooltip.astromine.work_always").formatted(Formatting.YELLOW));
            }
            default: {
                return Collections.emptyList();
            }
        }
    }

    /** Renders this widget. */
    @Override
    public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider, float delta) {
        if (getHidden()) {
            return;
        }

        float x = getPosition().getX();
        float y = getPosition().getY();

        float sX = getSize().getWidth();
        float sY = getSize().getHeight();

        TEXTURE.draw(matrices, provider, x, y, sX, sY);

        switch (blockEntity.getRedstoneComponent().getType()) {
            case WORK_WHEN_ON: {
                BaseRenderer.getDefaultItemRenderer().renderGuiItemIcon(REDSTONE, (int) x + (int) Math.max((sX - 16) / 2, 0), (int) y + (int) Math.max((sY - 16) / 2, 0));
                break;
            }

            case WORK_WHEN_OFF: {
                BaseRenderer.getDefaultItemRenderer().renderGuiItemIcon(GUNPOWDER, (int) x + (int) Math.max((sX - 16) / 2, 0), (int) y + (int) Math.max((sY - 16) / 2, 0));
                break;
            }

            case WORK_ALWAYS: {
                BaseRenderer.getDefaultItemRenderer().renderGuiItemIcon(GLOWSTONE, (int) x + (int) Math.max((sX - 16) / 2, 0), (int) y + (int) Math.max((sY - 16) / 2, 0));
                break;
            }
        }
    }
}
