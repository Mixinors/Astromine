package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.client.render.skybox.Skybox;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.registry.base.Registry;
import net.minecraft.util.Identifier;

public class AMRegistries {
	public static final Registry<Identifier, Body> BODY = new Registry<>();
}
