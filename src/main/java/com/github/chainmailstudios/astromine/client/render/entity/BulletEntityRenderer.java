package com.github.chainmailstudios.astromine.client.render.entity;

import com.github.chainmailstudios.astromine.common.entity.projectile.BulletEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class BulletEntityRenderer extends ProjectileEntityRenderer<BulletEntity> {
    public BulletEntityRenderer(final EntityRenderDispatcher dispatcher, final EntityRendererRegistry.Context context) {
        super(dispatcher);
    }

    @Override
    public Identifier getTexture(BulletEntity entity) {
        return entity.getTexture();
    }
}
