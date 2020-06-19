package com.github.chainmailstudios.astromine.common.network.ticker;

import com.github.chainmailstudios.astromine.common.component.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkController;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class NetworkTypeEnergy extends NetworkType {
    @Override
    public void simulate(NetworkController controller) {
        Map<BlockPos, EnergyVolume> bufferMap = new HashMap<>();
        Map<BlockPos, EnergyVolume> requesterMap = new HashMap<>();

        for (NetworkNode memberNode : controller.memberNodes) {
            BlockEntity blockEntity = controller.world.getBlockEntity(memberNode.position);
            if (blockEntity != null) {
                NetworkMember member = (NetworkMember) blockEntity;
                if (member.isBuffer()) {
                    ((EnergyInventoryComponent) blockEntity).getContents().forEach((key, volume) -> {
                        bufferMap.put(blockEntity.getPos(), volume);
                    });
                } else if (member.isRequester()) {
                    ((EnergyInventoryComponent) blockEntity).getContents().forEach((key, volume) -> {
                        requesterMap.put(blockEntity.getPos(), volume);
                    });
                }
            }
        }

        for (Map.Entry<BlockPos, EnergyVolume> buffer : bufferMap.entrySet()) {
            for (Map.Entry<BlockPos, EnergyVolume> requester : requesterMap.entrySet()) {
                if (!buffer.getValue().isEmpty() && !requester.getValue().isFull()) {
                    buffer.getValue().push(requester.getValue(), requester.getValue().getSize());
                } else if (buffer.getValue().isEmpty()) {
                    break;
                }
            }
        }
    }
}
