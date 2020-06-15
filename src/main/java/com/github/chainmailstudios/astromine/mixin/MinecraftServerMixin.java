package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.world.AstromineDimensionType;
import com.github.chainmailstudios.astromine.world.generation.AstromineBiomeSource;
import com.github.chainmailstudios.astromine.world.generation.AstromineChunkGenerator;
import com.google.common.collect.ImmutableList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow
    protected @Final
    RegistryTracker.Modifiable dimensionTracker;
    @Shadow
    protected @Final
    SaveProperties saveProperties;
    @Shadow
    protected @Final
    LevelStorage.Session session;
    @Shadow
    private @Final
    Map<RegistryKey<DimensionType>, ServerWorld> worlds;
    @Shadow
    private @Final
    Executor workerExecutor;

    @Inject(method = "createWorlds",
            at = @At("HEAD"))
    protected void createWorlds(WorldGenerationProgressListener listener, CallbackInfo callback) {
        if (this.dimensionTracker.getDimensionTypeRegistry().get(AstromineDimensionType.REGISTRY_KEY) == null) {
            this.dimensionTracker.addDimensionType(AstromineDimensionType.REGISTRY_KEY, AstromineDimensionType.INSTANCE);
        }

        GeneratorOptions options = this.saveProperties.getGeneratorOptions(); // getGeneratorOptions
        long seed = options.getSeed();

        AstromineChunkGenerator generator = new AstromineChunkGenerator(new AstromineBiomeSource(seed), seed);
        UnmodifiableLevelProperties properties = new UnmodifiableLevelProperties(this.saveProperties, this.saveProperties.getMainWorldProperties());
        ServerWorld serverWorld = new ServerWorld((MinecraftServer) (Object) this, this.workerExecutor, this.session, properties, RegistryKey.of(Registry.DIMENSION, AstromineDimensionType.OPTIONS.getValue()), AstromineDimensionType.REGISTRY_KEY,
                AstromineDimensionType.INSTANCE, listener, generator, false, BiomeAccess.hashSeed(seed), ImmutableList.of(), false);

        WorldBorder worldBorder = serverWorld.getWorldBorder();

        worldBorder.addListener(new WorldBorderListener.WorldBorderSyncer(serverWorld.getWorldBorder()));
        this.worlds.put(AstromineDimensionType.REGISTRY_KEY, serverWorld);
    }
}