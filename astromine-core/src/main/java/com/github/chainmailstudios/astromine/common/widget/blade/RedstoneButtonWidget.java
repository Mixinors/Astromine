package com.github.chainmailstudios.astromine.common.widget.blade;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.vini2003.blade.Blade;
import com.github.vini2003.blade.client.data.PartitionedTexture;
import com.github.vini2003.blade.common.widget.base.AbstractWidget;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * An {@link AbstractWidget} which represents
 * a redstone mode toggle for a {@link ComponentBlockEntity}.
 */
public class RedstoneButtonWidget extends ButtonWidget {
    private ComponentBlockEntity blockEntity;

    private final ItemStack GLOWSTONE = new ItemStack(Items.GLOWSTONE_DUST);

    private final ItemStack REDSTONE = new ItemStack(Items.REDSTONE);

    private final ItemStack GUNPOWDER = new ItemStack(Items.GUNPOWDER);

    public static final PartitionedTexture TEXTURE = new PartitionedTexture(Blade.identifier("textures/widget/panel.png"), 18F, 18F, 0.25F, 0.25F, 0.25F, 0.25F);

    public RedstoneButtonWidget() {
        setClickAction(() -> {
            if (!getHidden()) {
                blockEntity.getRedstoneComponent().setType(blockEntity.getRedstoneComponent().getType().next());
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
    public List<Component> getTooltip() {
        switch (blockEntity.getRedstoneComponent().getType()) {
            case WORK_WHEN_ON: {
                return Collections.singletonList(new TranslatableComponent("tooltip.astromine.work_when_on").withStyle(ChatFormatting.RED));
            }
            case WORK_WHEN_OFF: {
                return Collections.singletonList(new TranslatableComponent("tooltip.astromine.work_when_off").withStyle(ChatFormatting.GRAY));
            }
            case WORK_ALWAYS: {
                return Collections.singletonList(new TranslatableComponent("tooltip.astromine.work_always").withStyle(ChatFormatting.YELLOW));
            }
            default: {
                return Collections.emptyList();
            }
        }
    }

    /** Renders this widget. */
    @Override
    public void drawWidget(@NotNull PoseStack matrices, @NotNull MultiBufferSource provider) {
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
                BaseRenderer.getDefaultItemRenderer().renderGuiItem(REDSTONE, (int) x + (int) Math.max((sX - 16) / 2, 0), (int) y + (int) Math.max((sY - 16) / 2, 0));
                break;
            }

            case WORK_WHEN_OFF: {
                BaseRenderer.getDefaultItemRenderer().renderGuiItem(GUNPOWDER, (int) x + (int) Math.max((sX - 16) / 2, 0), (int) y + (int) Math.max((sY - 16) / 2, 0));
                break;
            }

            case WORK_ALWAYS: {
                BaseRenderer.getDefaultItemRenderer().renderGuiItem(GLOWSTONE, (int) x + (int) Math.max((sX - 16) / 2, 0), (int) y + (int) Math.max((sY - 16) / 2, 0));
                break;
            }
        }
    }
}
