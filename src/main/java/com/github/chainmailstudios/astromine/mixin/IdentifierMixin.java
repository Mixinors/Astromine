package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.registry.IdentifierFixRegistry;

@Mixin(Identifier.class)
public class IdentifierMixin {
	@Mutable @Shadow @Final protected String path;
	@Shadow @Final protected String namespace;
	
	@Inject(method = "<init>([Ljava/lang/String;)V", at = @At("RETURN"))
	private void init(String[] strings, CallbackInfo ci) {
		if(namespace.equals(AstromineCommon.MOD_ID) && IdentifierFixRegistry.INSTANCE.containsKey(path)) {
			String oldPath = path;
			path = IdentifierFixRegistry.INSTANCE.get(path);
			AstromineCommon.LOGGER.info("Fixed identifier path from "+oldPath+" to "+path);
		}
	}
}
