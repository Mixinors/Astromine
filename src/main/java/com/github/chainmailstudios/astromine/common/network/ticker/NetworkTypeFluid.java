package com.github.chainmailstudios.astromine.common.network.ticker;

import com.github.chainmailstudios.astromine.common.network.NetworkController;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.collection.AgnosticIndexedVolumeCollection;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class NetworkTypeFluid extends NetworkType {
    @Override
    public void simulate(NetworkController controller) {
        Map<BlockPos, FluidVolume> bufferMap = new HashMap<>();
        Map<BlockPos, FluidVolume> requesterMap = new HashMap<>();

        for (NetworkNode memberNode : controller.memberNodes) {
            BlockEntity blockEntity = controller.world.getBlockEntity(memberNode.position);
            if (blockEntity != null) {
                NetworkMember member = (NetworkMember) blockEntity;
                if (member.isBuffer()) {
                    bufferMap.put(blockEntity.getPos(), ((AgnosticIndexedVolumeCollection) blockEntity).get(FluidVolume.TYPE));
                } else if (member.isRequester()) {
                    requesterMap.put(blockEntity.getPos(), ((AgnosticIndexedVolumeCollection) blockEntity).get(FluidVolume.TYPE));
                }
            }
        }

        for (Map.Entry<BlockPos, FluidVolume> buffer : bufferMap.entrySet()) {
            for (Map.Entry<BlockPos, FluidVolume> requester : requesterMap.entrySet()) {
                if (!buffer.getValue().isEmpty() && !requester.getValue().isFull() && buffer.getValue().getFluid() == requester.getValue().getFluid()) {
                    buffer.getValue().push(requester.getValue(), requester.getValue().getSize());
                } else if (buffer.getValue().isEmpty()) {
                    break;
                }
            }
        }
    }
}
