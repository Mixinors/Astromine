package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.registry.base.Registry;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import com.github.mixinors.astromine.registry.common.AMRegistries;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.util.*;

public class BodyManager {
	public static final Codec<Registry<Identifier, Body>> REGISTRY_CODEC = Registry.createCodec(Identifier.CODEC, Body.CODEC);
	
	private static final String BODIES = "bodies";
	
	public static class JoinListener implements ServerPlayConnectionEvents.Join {
		public static final JoinListener INSTANCE = new JoinListener();
		
		protected JoinListener() {}
		
		@Override
		public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
			var result = REGISTRY_CODEC.encodeStart(NbtOps.INSTANCE, AMRegistries.BODY);
			
			var nbt = new NbtCompound();
			nbt.put(BODIES, result.result().get());
			
			var buf = PacketByteBufs.create();
			buf.writeNbt(nbt);
			
			for (var player : server.getPlayerManager().getPlayerList()) {
				ServerPlayNetworking.send(player, AMNetworking.SYNC_BODIES, PacketByteBufs.duplicate(buf));
			}
		}
	}
	
	public static class SyncListener implements ClientPlayNetworking.PlayChannelHandler {
		public static final SyncListener INSTANCE = new SyncListener();
		
		private SyncListener() {}
		
		@Override
		public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			var nbt = buf.readNbt();
			var bodiesNbt = nbt.get(BODIES);
			
			var bodies = REGISTRY_CODEC.decode(NbtOps.INSTANCE, bodiesNbt);
			var registry = bodies.result().map(Pair::getFirst).get();
			
			AMRegistries.BODY.setEntries(registry.getEntries());
		}
	}
	
	public static class ReloadListener implements SimpleSynchronousResourceReloadListener {
		public static final ReloadListener INSTANCE = new ReloadListener();
		
		private ReloadListener() {}
		
		private static final Identifier ID = AMCommon.id("body_reload_listener");
		
		@Override
		public Identifier getFabricId() {
			return ID;
		}
		
		private MinecraftServer getServer() {
			if (InstanceUtil.isServer()) return InstanceUtil.getServer(); else return getServerClient();
		}
		
		private MinecraftServer getServerClient() {
			return InstanceUtil.getClient().getServer();
		}
		
		@Override
		public void reload(ResourceManager manager) {
			AMRegistries.BODY.clear();
			
			manager.findResources("bodies", s -> s.endsWith(".json"))
					.stream()
					.map(id -> {
						try (var resource = manager.getResource(id)) {
							return resource;
						} catch (Exception e) {
							return null;
						}
					})
					.filter(Objects::nonNull)
					.map(Resource::getInputStream)
					.map(InputStreamReader::new)
					.map(reader -> {
						var json = AMCommon.GSON.fromJson(reader, JsonObject.class);
			
						try {
							reader.close();
						} catch (Exception ignored) {}
						
						return json;
					})
					.map(json -> Body.CODEC.decode(JsonOps.INSTANCE, json))
					.map(DataResult::result)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.map(Pair::getFirst)
					.forEach(body -> AMRegistries.BODY.register(body.getId(), body));
			
			var server = getServer();
			
			var result = REGISTRY_CODEC.encodeStart(NbtOps.INSTANCE, AMRegistries.BODY);
			
			var nbt = new NbtCompound();
			nbt.put(BODIES, result.result().get());
			
			var buf = PacketByteBufs.create();
			buf.writeNbt(nbt);
			
			for (var player : server.getPlayerManager().getPlayerList()) {
				ServerPlayNetworking.send(player, AMNetworking.SYNC_BODIES, PacketByteBufs.duplicate(buf));
			}
		}
	}
}
