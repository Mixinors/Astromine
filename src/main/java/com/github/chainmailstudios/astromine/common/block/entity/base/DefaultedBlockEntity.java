package com.github.chainmailstudios.astromine.common.block.entity.base;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.network.PacketContext;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.packet.PacketConsumer;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class DefaultedBlockEntity extends BlockEntity implements ComponentProvider, PacketConsumer, BlockEntityClientSerializable {
	protected final BlockEntityTransferComponent transferComponent = new BlockEntityTransferComponent();

	protected final Map<ComponentType<?>, Component> allComponents = Maps.newHashMap();

	protected final Map<Identifier, BiConsumer<PacketByteBuf, PacketContext>> allHandlers = Maps.newHashMap();

	public static final Identifier TRANSFER_UPDATE_PACKET = AstromineCommon.identifier("transfer_update_packet");

	public DefaultedBlockEntity(BlockEntityType<?> type) {
		super(type);

		addConsumer(TRANSFER_UPDATE_PACKET, ((buffer, context) -> {
			Identifier packetIdentifier = buffer.readIdentifier();
			ComponentType<?> packetType = ComponentRegistry.INSTANCE.get(packetIdentifier);
			Direction packetDirection = buffer.readEnumConstant(Direction.class);
			TransferType packetTransferType = buffer.readEnumConstant(TransferType.class);

			transferComponent.get(packetType).set(packetDirection, packetTransferType);

			markDirty();
		}));
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
			return (Collection<T>) getComponentTypes()
					.stream()
					.map(type -> new Pair<>((ComponentType) type, (Component) getComponent(type)))
					.filter(pair -> transferComponent.get(pair.getLeft()).get(direction, getCachedState().get(HorizontalFacingBlock.FACING)) != TransferType.NONE)
					.map(Pair::getRight)
					.collect(Collectors.toList());
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
			component.fromTag(tag.getCompound(type.getId().toString()));
		});

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(null, compoundTag);
	}
}
