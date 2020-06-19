package com.github.chainmailstudios.astromine.client.model;

import com.github.chainmailstudios.astromine.common.entity.RocketEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class RocketEntityModel extends EntityModel<RocketEntity> {
    
    private final ModelPart tip;
    private final ModelPart middle_overlay;
    private final ModelPart bottom_overlay;
    private final ModelPart s;
    private final ModelPart s2;
    private final ModelPart w;
    private final ModelPart s4;
    private final ModelPart n;
    private final ModelPart e;
    private final ModelPart windows;
    private final ModelPart base;

    public RocketEntityModel() {
        textureWidth = 256;
        textureHeight = 256;

        tip = new ModelPart(this);
        tip.setPivot(-8.0F, 24.0F, 0.0F);
        tip.setTextureOffset(0, 88).addCuboid(0.0F, -263.0F, -9.0F, 17.0F, 7.0F, 17.0F, 0.0F, false);
        tip.setTextureOffset(125, 0).addCuboid(1.0F, -266.0F, -8.0F, 15.0F, 3.0F, 15.0F, 0.0F, false);
        tip.setTextureOffset(68, 141).addCuboid(2.0F, -269.0F, -7.0F, 13.0F, 3.0F, 13.0F, 0.0F, false);
        tip.setTextureOffset(51, 0).addCuboid(4.0F, -271.0F, -5.0F, 9.0F, 2.0F, 9.0F, 0.0F, false);
        tip.setTextureOffset(119, 71).addCuboid(5.0F, -272.0F, -4.0F, 7.0F, 1.0F, 7.0F, 0.0F, false);
        tip.setTextureOffset(51, 11).addCuboid(6.0F, -273.0F, -3.0F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        middle_overlay = new ModelPart(this);
        middle_overlay.setPivot(9.5F, -174.0F, 2.0F);
        middle_overlay.setTextureOffset(68, 0).addCuboid(-18.5F, -1.0F, -12.0F, 19.0F, 27.0F, 19.0F, 0.0F, false);
        middle_overlay.setTextureOffset(0, 9).addCuboid(-5.5F, 26.0F, 6.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);
        middle_overlay.setTextureOffset(119, 79).addCuboid(-5.5F, 26.0F, -12.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);
        middle_overlay.setTextureOffset(68, 46).addCuboid(-18.5F, 26.0F, -11.0F, 1.0F, 4.0F, 5.0F, 0.0F, false);
        middle_overlay.setTextureOffset(0, 0).addCuboid(-0.5F, 26.0F, -11.0F, 1.0F, 4.0F, 5.0F, 0.0F, false);
        middle_overlay.setTextureOffset(68, 77).addCuboid(-17.5F, 26.0F, 6.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);
        middle_overlay.setTextureOffset(66, 11).addCuboid(-17.5F, 26.0F, -12.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);
        middle_overlay.setTextureOffset(68, 55).addCuboid(-18.5F, 26.0F, 1.0F, 1.0F, 4.0F, 5.0F, 0.0F, false);
        middle_overlay.setTextureOffset(68, 68).addCuboid(-0.5F, 26.0F, 1.0F, 1.0F, 4.0F, 5.0F, 0.0F, false);

        bottom_overlay = new ModelPart(this);
        bottom_overlay.setPivot(-8.0F, 24.0F, 1.0F);


        s = new ModelPart(this);
        s.setPivot(1.0F, -13.0F, 7.0F);
        bottom_overlay.addChild(s);
        s.setTextureOffset(60, 101).addCuboid(12.0F, -36.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        s.setTextureOffset(204, 69).addCuboid(11.0F, -33.0F, 0.0F, 5.0F, 33.0F, 1.0F, 0.0F, false);
        s.setTextureOffset(129, 49).addCuboid(9.0F, -9.0F, 0.0F, 2.0F, 9.0F, 1.0F, 0.0F, false);
        s.setTextureOffset(8, 101).addCuboid(0.0F, -36.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        s.setTextureOffset(120, 210).addCuboid(-1.0F, -33.0F, 0.0F, 5.0F, 33.0F, 1.0F, 0.0F, false);
        s.setTextureOffset(8, 125).addCuboid(6.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        s.setTextureOffset(119, 68).addCuboid(4.0F, -9.0F, 0.0F, 2.0F, 9.0F, 1.0F, 0.0F, false);
        s.setTextureOffset(125, 49).addCuboid(4.0F, -21.0F, 0.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        s.setTextureOffset(125, 0).addCuboid(10.0F, -21.0F, 0.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        s2 = new ModelPart(this);
        s2.setPivot(0.0F, 0.0F, 0.0F);
        s.addChild(s2);
        s2.setTextureOffset(59, 136).addCuboid(12.0F, -36.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        s2.setTextureOffset(84, 209).addCuboid(11.0F, -33.0F, 0.0F, 5.0F, 33.0F, 1.0F, 0.0F, false);
        s2.setTextureOffset(135, 49).addCuboid(9.0F, -9.0F, 0.0F, 2.0F, 9.0F, 1.0F, 0.0F, false);
        s2.setTextureOffset(136, 83).addCuboid(0.0F, -36.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        s2.setTextureOffset(96, 209).addCuboid(-1.0F, -33.0F, 0.0F, 5.0F, 33.0F, 1.0F, 0.0F, false);
        s2.setTextureOffset(136, 91).addCuboid(6.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        s2.setTextureOffset(0, 136).addCuboid(4.0F, -9.0F, 0.0F, 2.0F, 9.0F, 1.0F, 0.0F, false);
        s2.setTextureOffset(10, 88).addCuboid(4.0F, -21.0F, 0.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        s2.setTextureOffset(6, 88).addCuboid(10.0F, -21.0F, 0.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        w = new ModelPart(this);
        w.setPivot(1.0F, -13.0F, 7.0F);
        bottom_overlay.addChild(w);
        w.setTextureOffset(0, 99).addCuboid(16.0F, -36.0F, -16.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        w.setTextureOffset(199, 106).addCuboid(16.0F, -33.0F, -17.0F, 1.0F, 33.0F, 5.0F, 0.0F, false);
        w.setTextureOffset(0, 88).addCuboid(16.0F, -9.0F, -12.0F, 1.0F, 9.0F, 2.0F, 0.0F, false);
        w.setTextureOffset(52, 123).addCuboid(16.0F, -36.0F, -4.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        w.setTextureOffset(152, 196).addCuboid(16.0F, -33.0F, -5.0F, 1.0F, 33.0F, 5.0F, 0.0F, false);
        w.setTextureOffset(78, 0).addCuboid(16.0F, -3.0F, -10.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        w.setTextureOffset(61, 112).addCuboid(16.0F, -9.0F, -7.0F, 1.0F, 9.0F, 2.0F, 0.0F, false);
        w.setTextureOffset(10, 112).addCuboid(16.0F, -21.0F, -6.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        w.setTextureOffset(6, 112).addCuboid(16.0F, -21.0F, -12.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        s4 = new ModelPart(this);
        s4.setPivot(0.0F, 0.0F, 0.0F);
        w.addChild(s4);
        s4.setTextureOffset(52, 99).addCuboid(16.0F, -36.0F, -16.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        s4.setTextureOffset(140, 184).addCuboid(16.0F, -33.0F, -17.0F, 1.0F, 33.0F, 5.0F, 0.0F, false);
        s4.setTextureOffset(0, 112).addCuboid(16.0F, -9.0F, -12.0F, 1.0F, 9.0F, 2.0F, 0.0F, false);
        s4.setTextureOffset(0, 123).addCuboid(16.0F, -36.0F, -4.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        s4.setTextureOffset(164, 201).addCuboid(16.0F, -33.0F, -5.0F, 1.0F, 33.0F, 5.0F, 0.0F, false);
        s4.setTextureOffset(51, 0).addCuboid(16.0F, -3.0F, -10.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        s4.setTextureOffset(55, 88).addCuboid(16.0F, -9.0F, -7.0F, 1.0F, 9.0F, 2.0F, 0.0F, false);
        s4.setTextureOffset(12, 0).addCuboid(16.0F, -21.0F, -6.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        s4.setTextureOffset(80, 68).addCuboid(16.0F, -21.0F, -12.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        n = new ModelPart(this);
        n.setPivot(1.0F, -40.0F, -11.0F);
        bottom_overlay.addChild(n);
        n.setTextureOffset(134, 59).addCuboid(12.0F, -9.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        n.setTextureOffset(108, 210).addCuboid(11.0F, -6.0F, 0.0F, 5.0F, 33.0F, 1.0F, 0.0F, false);
        n.setTextureOffset(51, 88).addCuboid(10.0F, 6.0F, 0.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        n.setTextureOffset(6, 136).addCuboid(9.0F, 18.0F, 0.0F, 2.0F, 9.0F, 1.0F, 0.0F, false);
        n.setTextureOffset(131, 79).addCuboid(6.0F, 24.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        n.setTextureOffset(133, 0).addCuboid(4.0F, 18.0F, 0.0F, 2.0F, 9.0F, 1.0F, 0.0F, false);
        n.setTextureOffset(51, 112).addCuboid(4.0F, 6.0F, 0.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        n.setTextureOffset(206, 144).addCuboid(-1.0F, -6.0F, 0.0F, 5.0F, 33.0F, 1.0F, 0.0F, false);
        n.setTextureOffset(51, 136).addCuboid(0.0F, -9.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        e = new ModelPart(this);
        e.setPivot(0.0F, -40.0F, -9.0F);
        bottom_overlay.addChild(e);
        e.setTextureOffset(126, 59).addCuboid(-1.0F, -9.0F, 12.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        e.setTextureOffset(176, 201).addCuboid(-1.0F, -6.0F, 11.0F, 1.0F, 33.0F, 5.0F, 0.0F, false);
        e.setTextureOffset(61, 88).addCuboid(-1.0F, 6.0F, 10.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        e.setTextureOffset(80, 46).addCuboid(-1.0F, 18.0F, 9.0F, 1.0F, 9.0F, 2.0F, 0.0F, false);
        e.setTextureOffset(75, 13).addCuboid(-1.0F, 24.0F, 6.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        e.setTextureOffset(55, 112).addCuboid(-1.0F, 18.0F, 4.0F, 1.0F, 9.0F, 2.0F, 0.0F, false);
        e.setTextureOffset(129, 0).addCuboid(-1.0F, 6.0F, 4.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        e.setTextureOffset(206, 0).addCuboid(-1.0F, -6.0F, -1.0F, 1.0F, 33.0F, 5.0F, 0.0F, false);
        e.setTextureOffset(60, 123).addCuboid(-1.0F, -9.0F, 0.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);

        windows = new ModelPart(this);
        windows.setPivot(-24.0F, -190.0F, -8.0F);
        windows.setTextureOffset(191, 191).addCuboid(18.0F, 77.0F, -1.0F, 13.0F, 25.0F, 1.0F, 0.0F, false);
        windows.setTextureOffset(112, 184).addCuboid(18.0F, 105.0F, -1.0F, 13.0F, 25.0F, 1.0F, 0.0F, false);
        windows.setTextureOffset(195, 43).addCuboid(18.0F, 133.0F, -1.0F, 13.0F, 25.0F, 1.0F, 0.0F, false);
        windows.setTextureOffset(107, 146).addCuboid(16.0F, 77.0F, 1.0F, 1.0F, 25.0F, 13.0F, 0.0F, false);
        windows.setTextureOffset(0, 156).addCuboid(32.0F, 105.0F, 1.0F, 1.0F, 25.0F, 13.0F, 0.0F, false);
        windows.setTextureOffset(56, 157).addCuboid(32.0F, 133.0F, 1.0F, 1.0F, 25.0F, 13.0F, 0.0F, false);
        windows.setTextureOffset(84, 171).addCuboid(32.0F, 77.0F, 1.0F, 1.0F, 25.0F, 13.0F, 0.0F, false);
        windows.setTextureOffset(0, 194).addCuboid(18.0F, 77.0F, 15.0F, 13.0F, 25.0F, 1.0F, 0.0F, false);
        windows.setTextureOffset(170, 111).addCuboid(18.0F, 105.0F, 15.0F, 13.0F, 25.0F, 1.0F, 0.0F, false);
        windows.setTextureOffset(178, 146).addCuboid(18.0F, 133.0F, 15.0F, 13.0F, 25.0F, 1.0F, 0.0F, false);
        windows.setTextureOffset(178, 5).addCuboid(16.0F, 133.0F, 1.0F, 1.0F, 25.0F, 13.0F, 0.0F, false);
        windows.setTextureOffset(135, 146).addCuboid(16.0F, 105.0F, 1.0F, 1.0F, 25.0F, 13.0F, 0.0F, false);
        windows.setTextureOffset(28, 194).addCuboid(18.0F, -25.0F, -1.0F, 13.0F, 25.0F, 1.0F, 0.0F, false);
        windows.setTextureOffset(163, 163).addCuboid(32.0F, -25.0F, 1.0F, 1.0F, 25.0F, 13.0F, 0.0F, false);
        windows.setTextureOffset(56, 195).addCuboid(18.0F, -25.0F, 15.0F, 13.0F, 25.0F, 1.0F, 0.0F, false);
        windows.setTextureOffset(28, 156).addCuboid(16.0F, -25.0F, 1.0F, 1.0F, 25.0F, 13.0F, 0.0F, false);

        base = new ModelPart(this);
        base.setPivot(-8.0F, -167.0F, 7.0F);
        base.setTextureOffset(0, 0).addCuboid(0.0F, -20.0F, -16.0F, 17.0F, 71.0F, 17.0F, 0.0F, false);
        base.setTextureOffset(68, 68).addCuboid(0.0F, 135.0F, -16.0F, 17.0F, 56.0F, 17.0F, 0.0F, false);
        base.setTextureOffset(127, 51).addCuboid(0.0F, 107.0F, -16.0F, 17.0F, 3.0F, 17.0F, 0.0F, false);
        base.setTextureOffset(99, 157).addCuboid(0.0F, 82.0F, -16.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(84, 157).addCuboid(0.0F, 54.0F, -16.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(0, 220).addCuboid(0.0F, 110.0F, -16.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(219, 182).addCuboid(0.0F, -48.0F, -16.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(212, 217).addCuboid(15.0F, 110.0F, -16.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(218, 0).addCuboid(15.0F, 54.0F, -16.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(218, 155).addCuboid(15.0F, 82.0F, -16.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(219, 96).addCuboid(15.0F, -48.0F, -16.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(216, 69).addCuboid(0.0F, 82.0F, -1.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(163, 146).addCuboid(15.0F, -48.0F, -1.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(218, 128).addCuboid(15.0F, 110.0F, -1.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(132, 210).addCuboid(15.0F, 54.0F, -1.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(211, 103).addCuboid(15.0F, 82.0F, -1.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(188, 217).addCuboid(0.0F, 54.0F, -1.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(196, 217).addCuboid(0.0F, 110.0F, -1.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(204, 217).addCuboid(0.0F, -48.0F, -1.0F, 2.0F, 25.0F, 2.0F, 0.0F, false);
        base.setTextureOffset(136, 91).addCuboid(0.0F, 51.0F, -16.0F, 17.0F, 3.0F, 17.0F, 0.0F, false);
        base.setTextureOffset(127, 29).addCuboid(0.0F, 79.0F, -16.0F, 17.0F, 3.0F, 17.0F, 0.0F, false);
        base.setTextureOffset(136, 71).addCuboid(0.0F, -51.0F, -16.0F, 17.0F, 3.0F, 17.0F, 0.0F, false);
        base.setTextureOffset(0, 136).addCuboid(0.0F, -23.0F, -16.0F, 17.0F, 3.0F, 17.0F, 0.0F, false);
        base.setTextureOffset(0, 112).addCuboid(0.0F, -65.0F, -16.0F, 17.0F, 7.0F, 17.0F, 0.0F, false);
        base.setTextureOffset(119, 124).addCuboid(0.0F, -56.0F, -16.0F, 17.0F, 5.0F, 17.0F, 0.0F, false);
        base.setTextureOffset(68, 46).addCuboid(-1.0F, -58.0F, -17.0F, 19.0F, 3.0F, 19.0F, 0.0F, false);
    }

    @Override
    public void setAngles(RocketEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        
    }
    
    @Override
    public void render(MatrixStack matrixStack, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha){
        tip.render(matrixStack, vertices, light, overlay);
        middle_overlay.render(matrixStack, vertices, light, overlay);
        bottom_overlay.render(matrixStack, vertices, light, overlay);
        windows.render(matrixStack, vertices, light, overlay);
        base.render(matrixStack, vertices, light, overlay);
    }
}