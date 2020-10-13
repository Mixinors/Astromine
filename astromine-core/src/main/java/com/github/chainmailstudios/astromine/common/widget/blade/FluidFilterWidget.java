package com.github.chainmailstudios.astromine.common.widget.blade;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.chainmailstudios.astromine.client.render.sprite.SpriteRenderer;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.github.chainmailstudios.astromine.common.volume.handler.FluidHandler;
import com.github.vini2003.blade.client.utilities.Layers;
import com.github.vini2003.blade.common.widget.base.AbstractWidget;
import com.github.vini2003.blade.common.widget.base.ButtonWidget;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidFilterWidget extends ButtonWidget {
    private final Identifier FLUID_BACKGROUND = AstromineCommon.identifier("textures/widget/fluid_filter_background.png");

    private Supplier<Fluid> fluidSupplier = () -> Fluids.EMPTY;

    private Consumer<Fluid> fluidConsumer = fluid -> {};

    public Supplier<Fluid> getFluidSupplier() {
        return fluidSupplier;
    }

    public void setFluidSupplier(Supplier<Fluid> fluidSupplier) {
        this.fluidSupplier = fluidSupplier;
    }

    public Consumer<Fluid> getFluidConsumer() {
        return fluidConsumer;
    }

    public void setFluidConsumer(Consumer<Fluid> fluidConsumer) {
        this.fluidConsumer = fluidConsumer;
    }

    public Identifier getBackgroundTexture() {
        return FLUID_BACKGROUND;
    }

    @NotNull
    @Override
    public List<Text> getTooltip() {
        Identifier fluidId = Registry.FLUID.getId(fluidSupplier.get());

        return Collections.singletonList(new TranslatableText(String.format("block.%s.%s", fluidId.getNamespace(), fluidId.getPath())));
    }

    @Override
    public void onMouseClicked(float x, float y, int button) {
        super.onMouseClicked(x, y, button);

        ItemStack stack = getHandler().getPlayer().inventory.getCursorStack();

        if (isWithin(x, y)) {
            if (!stack.isEmpty()) {
                FluidHandler.ofOptional(stack).ifPresent(fluids -> {
                    fluidSupplier = () -> fluids.getFirst().getFluid();
                    fluidConsumer.accept(fluidSupplier.get());
                });
            } else if (button == 2) {
                fluidSupplier = () -> Fluids.EMPTY;
                fluidConsumer.accept(fluidSupplier.get());
            }
        }
    }

    @Override
    public void drawWidget(@NotNull MatrixStack matrices, @NotNull VertexConsumerProvider provider) {
        if (getHidden()) {
            return;
        }

        float x = getPosition().getX();
        float y = getPosition().getY();

        float sX = getSize().getWidth();
        float sY = getSize().getHeight();

        RenderLayer layer = Layers.get(getBackgroundTexture());

        BaseRenderer.drawTexturedQuad(matrices, provider, layer, x, y, getSize().getWidth(), getSize().getHeight(), getBackgroundTexture());

        if (fluidSupplier.get() != Fluids.EMPTY) {
            SpriteRenderer.beginPass().setup(provider, RenderLayer.getSolid()).sprite(FluidUtilities.texture(fluidSupplier.get())[0]).color(FluidUtilities.color(MinecraftClient.getInstance().player, fluidSupplier.get())).light(0x00f000f0).overlay(
                    OverlayTexture.DEFAULT_UV).alpha(0xff).normal(matrices.peek().getNormal(), 0, 0, 0).position(matrices.peek().getModel(), x + 1, y + 1, x + sX - 1, y + sY - 1, 0F).next(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
        }
    }
}
