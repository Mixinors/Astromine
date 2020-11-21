package com.github.chainmailstudios.astromine.common.widget.blade;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
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

    public static final PartitionedTexture TEXTURE = new PartitionedTexture(Blade.identifier("textures/widget/panel.png"), 18F, 18F, 0.25F, 0.25F, 0.25F, 0.25F);

    public RedstoneWidget() {
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
    public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider) {
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
