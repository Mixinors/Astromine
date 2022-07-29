package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.client.render.skybox.Skybox;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.base.Registry;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.Identifier;

public class AMRegistries {
	public static final Registry<Body> BODY = new Registry<>();
	
	public static final Registry<NetworkType<?>> NETWORK_TYPE = new Registry<>();
}
