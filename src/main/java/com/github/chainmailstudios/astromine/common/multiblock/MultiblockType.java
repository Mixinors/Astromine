package com.github.chainmailstudios.astromine.common.multiblock;

import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.google.common.collect.ImmutableMap;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import org.joml.Vector3i;

public class MultiblockType {
	private final ImmutableMap<Vector3i, Block> structure;
	private final ImmutableMap<Vector3i, ComponentType<?>> type;
	private final ImmutableMap<ComponentType<?>, Component> component;

	public MultiblockType(Builder builder) {
		this.structure = builder.structureBuilder.build();
		this.type = builder.typeBuilder.build();
		this.component = builder.componentBuilder.build();
	}

	public <T extends MultiblockInstance<?>> CompoundTag toTag(T instance, CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		int i = 0;

		for (MultiblockNode node : instance.getNodes()) {
			dataTag.put(String.valueOf(i), node.toTag(new CompoundTag()));
			++i;
		}

		tag.put("data", dataTag);

		return tag;
	}

	public <T extends MultiblockInstance<?>> void fromTag(T instance, CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			instance.getNodes().add(MultiblockNode.fromTag(dataTag.getCompound(key)));
		}
	}

	public ImmutableMap<Vector3i, Block> getStructure() {
		return structure;
	}

	public ImmutableMap<Vector3i, ComponentType<?>> getType() {
		return type;
	}

	public ImmutableMap<ComponentType<?>, Component> getComponent() {
		return component;
	}

	public Identifier getId();

	public static class Builder {
		private final ImmutableMap.Builder<Vector3i, Block> structureBuilder = ImmutableMap.builder();
		private final ImmutableMap.Builder<Vector3i, ComponentType<?>> typeBuilder = ImmutableMap.builder();
		private final ImmutableMap.Builder<ComponentType<?>, Component> componentBuilder = ImmutableMap.builder();

		private Identifier id;

		private Builder() {
		}

		public Builder builder() {
			return new Builder();
		}

		public Builder with(ComponentType<?> type, Component component) {
			componentBuilder.put(type, component);
			return this;
		}

		public Builder with(int oX, int oY, int oZ, ComponentType<?> type) {
			typeBuilder.put(new Vector3i(oX, oY, oZ), type);
			return this;
		}

		public Builder with(int oX, int oY, int oZ, Block block) {
			structureBuilder.put(new Vector3i(oX, oY, oZ), block);
			return this;
		}

		public Builder with(Identifier id) {
			this.id = id;
			return this;
		}

		public MultiblockType build() {
			return new MultiblockType(this);
		}
	}
}
