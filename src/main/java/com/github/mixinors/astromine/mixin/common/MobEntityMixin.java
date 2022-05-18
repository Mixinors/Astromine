package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.access.MobEntityAccessor;
import com.github.mixinors.astromine.common.transfer.storage.EntityEquipmentStorage;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobEntity.class)
public class MobEntityMixin implements MobEntityAccessor {
	private final EntityEquipmentStorage astromine$equipmentStorage = new EntityEquipmentStorage((MobEntity) (Object) this);
	
	@Override
	public EntityEquipmentStorage astromine$getEquipmentStorage() {
		return astromine$equipmentStorage;
	}
}
