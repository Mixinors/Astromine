package com.github.mixinors.astromine.mixin.common.common;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPhase.class)
public interface RenderLayerAccessor {
	@Accessor
	static RenderPhase.Transparency getNO_TRANSPARENCY() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}
	
	@Accessor
	static RenderPhase.Transparency getADDITIVE_TRANSPARENCY() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}
	
	@Accessor
	static RenderPhase.Transparency getLIGHTNING_TRANSPARENCY() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}
	
	@Accessor
	static RenderPhase.Transparency getGLINT_TRANSPARENCY() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}
	
	@Accessor
	static RenderPhase.Transparency getCRUMBLING_TRANSPARENCY() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}
	
	@Accessor
	static RenderPhase.Transparency getTRANSLUCENT_TRANSPARENCY() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}
	
	@Accessor
	static RenderPhase.Alpha getZERO_ALPHA() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Alpha getONE_TENTH_ALPHA() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Alpha getHALF_ALPHA() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.ShadeModel getSHADE_MODEL() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.ShadeModel getSMOOTH_SHADE_MODEL() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Texture getMIPMAP_BLOCK_ATLAS_TEXTURE() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Texture getBLOCK_ATLAS_TEXTURE() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Texture getNO_TEXTURE() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Texturing getDEFAULT_TEXTURING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Texturing getOUTLINE_TEXTURING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Texturing getGLINT_TEXTURING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Texturing getENTITY_GLINT_TEXTURING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Lightmap getENABLE_LIGHTMAP() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Lightmap getDISABLE_LIGHTMAP() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Overlay getENABLE_OVERLAY_COLOR() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Overlay getDISABLE_OVERLAY_COLOR() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.DiffuseLighting getENABLE_DIFFUSE_LIGHTING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.DiffuseLighting getDISABLE_DIFFUSE_LIGHTING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Cull getENABLE_CULLING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Cull getDISABLE_CULLING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.DepthTest getALWAYS_DEPTH_TEST() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.DepthTest getEQUAL_DEPTH_TEST() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.DepthTest getLEQUAL_DEPTH_TEST() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.WriteMaskState getALL_MASK() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.WriteMaskState getCOLOR_MASK() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.WriteMaskState getDEPTH_MASK() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Layering getNO_LAYERING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Layering getPOLYGON_OFFSET_LAYERING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Layering getVIEW_OFFSET_Z_LAYERING() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Fog getNO_FOG() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Fog getFOG() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Fog getBLACK_FOG() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Target getMAIN_TARGET() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Target getOUTLINE_TARGET() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Target getTRANSLUCENT_TARGET() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Target getPARTICLES_TARGET() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Target getWEATHER_TARGET() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Target getCLOUDS_TARGET() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.Target getITEM_TARGET() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}

	@Accessor
	static RenderPhase.LineWidth getFULL_LINE_WIDTH() {
		throw new UnsupportedOperationException("Cannot invoke @Accessor method!");
	}
}
