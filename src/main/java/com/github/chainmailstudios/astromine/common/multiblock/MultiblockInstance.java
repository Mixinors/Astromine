package com.github.chainmailstudios.astromine.common.multiblock;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import java.util.Set;

public class MultiblockInstance<T extends MultiblockType> implements ComponentProvider, ExtendedScreenHandlerFactory {
	private final Set<MultiblockNode> nodes = Sets.newHashSet();

	private final MultiblockType type;

	public MultiblockInstance(MultiblockType type) {
		this.type = type;
	}

	public Identifier getId() {
		return type.getId();
	}

	public Set<MultiblockNode> getNodes() {
		return nodes;
	}

	public boolean contains(MultiblockNode node) {
		return nodes.contains(node);
	}

	public CompoundTag toTag(CompoundTag tag) {
		return type.toTag(this, tag);
	}

	public void fromTag(CompoundTag tag) {
		type.fromTag(this, tag);
	}
}
