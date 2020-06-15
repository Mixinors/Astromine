package com.github.chainmailstudios.astromine.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemRenderer<I extends Item> {
    public ItemRenderer(Item item) {
        // Unused.
    }

    public abstract void render(ItemStack stack, MatrixStack matrix, VertexConsumerProvider vertexes, int light, int overlay);
}
