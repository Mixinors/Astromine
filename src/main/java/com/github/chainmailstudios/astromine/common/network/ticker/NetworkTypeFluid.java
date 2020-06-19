package com.github.chainmailstudios.astromine.common.network.ticker;

import com.github.chainmailstudios.astromine.common.component.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkController;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class NetworkTypeFluid extends NetworkType {
    @Override
    public void simulate(NetworkController controller) {
        Multimap<BlockPos, FluidVolume> bufferMap = HashMultimap.create();
        Multimap<BlockPos, FluidVolume> requesterMap = HashMultimap.create();

        for (NetworkNode memberNode : controller.memberNodes) {
            BlockEntity blockEntity = controller.world.getBlockEntity(memberNode.position);
            if (blockEntity != null) {
                NetworkMember member = (NetworkMember) blockEntity;
                if (member.isBuffer()) {
                    ((FluidInventoryComponent) blockEntity).getContents().forEach((key, volume) -> {
                        bufferMap.put(blockEntity.getPos(), volume);
                    });
                } else if (member.isRequester()) {
                    ((FluidInventoryComponent) blockEntity).getContents().forEach((key, volume) -> {
                        requesterMap.put(blockEntity.getPos(), volume);
                    });
                }
            }
        }

        for (Map.Entry<BlockPos, FluidVolume> buffer : bufferMap.entries()) {
            for (Map.Entry<BlockPos, FluidVolume> requester : requesterMap.entries()) {
                if (!buffer.getValue().isEmpty() && !requester.getValue().isFull() && buffer.getValue().getFluid() == requester.getValue().getFluid()) {
                    buffer.getValue().push(requester.getValue(), requester.getValue().getSize());
                } else if (buffer.getValue().isEmpty()) {
                    break;
                }
            }
        }
    }
}
