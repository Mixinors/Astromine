package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.packet.PacketConsumer;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class DefaultedBlockEntity extends BlockEntity implements ComponentProvider, PacketConsumer, BlockEntityClientSerializable, NetworkMember {
	protected final Lazy<Map<NetworkType, Collection<NetworkMemberType>>> networkMemberType = new Lazy<>(this::createMemberProperties);
	protected final BlockEntityTransferComponent transferComponent = new BlockEntityTransferComponent();

	protected final Map<ComponentType<?>, Component> allComponents = Maps.newHashMap();

	protected final Map<Identifier, BiConsumer<PacketByteBuf, PacketContext>> allHandlers = Maps.newHashMap();

	public static final Identifier TRANSFER_UPDATE_PACKET = AstromineCommon.identifier("transfer_update_packet");

	public DefaultedBlockEntity(BlockEntityType<?> type) {
		super(type);

		addConsumer(TRANSFER_UPDATE_PACKET, ((buffer, context) -> {
			Identifier packetIdentifier = buffer.readIdentifier();
			Direction packetDirection = buffer.readEnumConstant(Direction.class);
			TransferType packetTransferType = buffer.readEnumConstant(TransferType.class);

			transferComponent.get(packetIdentifier).set(packetDirection, packetTransferType);

			markDirty();
		}));
	}

	@NotNull
	protected Map<NetworkType, Collection<NetworkMemberType>> createMemberProperties() {
		return Collections.emptyMap();
	}

	@Override
	@NotNull
	public Map<NetworkType, Collection<NetworkMemberType>> getMemberProperties() {
		return networkMemberType.get();
	}

	public void addComponent(ComponentType<?> type, Component component) {
		allComponents.put(type, component);
		transferComponent.add(type);
	}

	public void addConsumer(Identifier identifier, BiConsumer<PacketByteBuf, PacketContext> consumer) {
		allHandlers.put(identifier, consumer);
	}

	@Override
	public void consumePacket(Identifier identifier, PacketByteBuf buffer, PacketContext context) {
		allHandlers.get(identifier).accept(buffer, context);
	}

	@Override
	public <T extends Component> Collection<T> getSidedComponents(Direction direction) {
		if (direction == null) {
			return (Collection<T>) allComponents.values();
		} else {
			if (getCachedState().contains(HorizontalFacingBlock.FACING)) {
				return (Collection<T>) getComponentTypes()
						.stream()
						.map(type -> new Pair<>((ComponentType) type, (Component) getComponent(type)))
						.filter(pair -> transferComponent.get(pair.getLeft()).get(direction, getCachedState().get(HorizontalFacingBlock.FACING)) != TransferType.NONE)
						.map(Pair::getRight)
						.collect(Collectors.toList());
			} else if (getCachedState().contains(FacingBlock.FACING)) {
				return (Collection<T>) getComponentTypes()
						.stream()
						.map(type -> new Pair<>((ComponentType) type, (Component) getComponent(type)))
						.filter(pair -> transferComponent.get(pair.getLeft()).get(direction, getCachedState().get(FacingBlock.FACING)) != TransferType.NONE)
						.map(Pair::getRight)
						.collect(Collectors.toList());
			} else {
				return Lists.newArrayList();
			}
		}
	}

	@Override
	public boolean hasComponent(ComponentType<?> componentType) {
		return allComponents.containsKey(componentType) || componentType == AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT;
	}

	@Override
	public <C extends Component> C getComponent(ComponentType<C> componentType) {
		return componentType == AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT ? (C) transferComponent : (C) allComponents.get(componentType);
	}

	@Override
	public Set<ComponentType<?>> getComponentTypes() {
		return allComponents.keySet();
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("transfer", transferComponent.toTag(new CompoundTag()));

		allComponents.forEach((type, component) -> {
			tag.put(type.getId().toString(), component.toTag(new CompoundTag()));
		});

		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		transferComponent.fromTag(tag.getCompound("transfer"));

		allComponents.forEach((type, component) -> {
			if (tag.contains(type.getId().toString())) {
				component.fromTag(tag.getCompound(type.getId().toString()));
			}
		});

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		compoundTag = toTag(compoundTag);
		compoundTag.remove(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT.getId().toString());
		return compoundTag;
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(null, compoundTag);
	}
}
