package com.github.chainmailstudios.astromine.client.render;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.entity.RocketEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;

public class RocketEntityRenderer extends EntityRenderer<RocketEntity> {

    public static final Identifier identifier = AstromineCommon.identifier("textures/entity/rocket/rocket.png");

    public RocketEntityRenderer(EntityRenderDispatcher dispatcher, final EntityRendererRegistry.Context context) {
        super(dispatcher);
    }

    @Override
    public Identifier getTexture(RocketEntity entity) {
        return identifier;
    }
}
