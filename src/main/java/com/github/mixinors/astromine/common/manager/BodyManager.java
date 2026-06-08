package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.body.BodyDimension;
import com.github.mixinors.astromine.common.gravity.GravityManager;
import com.github.mixinors.astromine.common.registry.base.Registry;
import com.github.mixinors.astromine.common.util.ResourceUtil;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import com.github.mixinors.astromine.registry.common.AMRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import java.util.concurrent.atomic.AtomicBoolean;

public class BodyManager {
	public static final Codec<Registry<Body>> REGISTRY_CODEC = Registry.createCodec(AMRegistries.BODY, Body.CODEC);

	private static final String VALUES = "values";
	private static final AtomicBoolean PENDING_SYNC = new AtomicBoolean(false);

	public static void onWorldLoad(MinecraftServer server, ServerLevel world) {
		AMRegistries.BODY.getValues().forEach(Body::onLoad);
	}

	public static void onWorldUnload(MinecraftServer server, ServerLevel world) {
		GravityManager.reset(world.dimension());
	}

	public static void onPlayerJoin(MinecraftServer server) {
		sync(server);
	}

	public static void onSync(CompoundTag nbt) {
		var valuesTag = nbt.get(VALUES);

		if (valuesTag != null) {
			REGISTRY_CODEC.decode(NbtOps.INSTANCE, valuesTag).result();
		}
	}

	public static void onRegistryReload() {
		GravityManager.clear();
		AMRegistries.BODY.getValues().forEach(Body::onReload);
		PENDING_SYNC.set(true);
	}

	public static void flushPendingSync(MinecraftServer server) {
		if (PENDING_SYNC.compareAndSet(true, false)) {
			sync(server);
		}
	}

	public static void sync(MinecraftServer server) {
		var nbt = new CompoundTag();
		var result = REGISTRY_CODEC.encodeStart(NbtOps.INSTANCE, AMRegistries.BODY);

		result.result().ifPresent(tag -> nbt.put(VALUES, tag));

		for (var player : server.getPlayerList().getPlayers()) {
			PacketDistributor.sendToPlayer(player, new AMNetworking.SyncBodiesPayload(nbt.copy()));
		}
	}

	public static BodyDimension getBodyDimensionOfWorld(ResourceKey<Level> worldKey) {
		for (Body body : AMRegistries.BODY.getValues()) {
			if (body.surfaceDimension() != null && worldKey.location().equals(body.surfaceDimension().worldKey().location())) {
				return body.surfaceDimension();
			}

			if (body.orbitDimension() != null && worldKey.location().equals(body.orbitDimension().worldKey().location())) {
				return body.orbitDimension();
			}
		}

		return null;
	}

	public static class ReloadListener implements ResourceManagerReloadListener {
		@Override
		public void onResourceManagerReload(ResourceManager manager) {
			AMRegistries.BODY.clear();

			ResourceUtil.load(manager, "bodies", Body.CODEC).forEach(body -> AMRegistries.BODY.register(body.id(), body));
			BodyManager.onRegistryReload();
		}
	}
}
