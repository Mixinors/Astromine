package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.body.BodyDimension;
import com.github.mixinors.astromine.common.registry.base.Registry;
import com.github.mixinors.astromine.common.util.ResourceUtil;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import com.github.mixinors.astromine.registry.common.AMRegistries;
import com.mojang.serialization.Codec;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class BodyManager {
	public static final Codec<Registry<Body>> REGISTRY_CODEC = Registry.createCodec(AMRegistries.BODY, Body.CODEC);
	
	private static final String BODIES = "bodies";
	
	public static void onWorldLoad(MinecraftServer server, ServerWorld world) {
		AMRegistries.BODY.getValues().forEach(Body::onLoad);
	}
	
	public static void onWorldUnload(MinecraftServer server, ServerWorld world) {
		AMRegistries.BODY.getValues().forEach(Body::onUnload);
	}
	
	public static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		var buf = PacketByteBufs.create();
		
		AMRegistries.BODY.writeToBuf(REGISTRY_CODEC, buf);
		
		ServerPlayNetworking.send(handler.player, AMNetworking.SYNC_BODIES, buf);
	}
	
	public static void onSync(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		AMRegistries.BODY.readFromBuf(REGISTRY_CODEC, buf);
	}
	
	public static void onRegistryReload(MinecraftServer server) {
		var result = REGISTRY_CODEC.encodeStart(NbtOps.INSTANCE, AMRegistries.BODY);
		
		var nbt = new NbtCompound();
		nbt.put(BODIES, result.result().get());
		
		var buf = PacketByteBufs.create();
		buf.writeNbt(nbt);
		
		for (var player : server.getPlayerManager().getPlayerList()) {
			ServerPlayNetworking.send(player, AMNetworking.SYNC_BODIES, PacketByteBufs.duplicate(buf));
		}
		
		AMRegistries.BODY.getValues().forEach(Body::onReload);
	}
	
	public static BodyDimension getBodyDimensionOfWorld(RegistryKey<World> worldKey) {
		for (Body body : AMRegistries.BODY.getValues()) {
			if (body.surfaceDimension() != null && worldKey.getValue().equals(body.surfaceDimension().worldKey().getValue())) {
				return body.surfaceDimension();
			}
			
			if (body.orbitDimension() != null && worldKey.getValue().equals(body.orbitDimension().worldKey().getValue())) {
				return body.orbitDimension();
			}
		}
		
		return null;
	}
	
	public static class ReloadListener implements SimpleSynchronousResourceReloadListener {
		private static final Identifier ID = AMCommon.id("body");
		
		@Override
		public Identifier getFabricId() {
			return ID;
		}
		
		@Override
		public void reload(ResourceManager manager) {
			AMRegistries.BODY.clear();
			
			ResourceUtil.load(manager, "bodies", Body.CODEC).forEach(body -> AMRegistries.BODY.register(body.id(), body));
			
			var server = InstanceUtil.getServer();
			
			if (server != null) {
				BodyManager.onRegistryReload(server);
			}
		}
	}
}
