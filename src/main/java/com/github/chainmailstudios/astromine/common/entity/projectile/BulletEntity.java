package com.github.chainmailstudios.astromine.common.entity.projectile;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.AstromineEntities;

public class BulletEntity extends PersistentProjectileEntity {
	public Identifier texture = AstromineCommon.identifier("textures/entity/projectiles/bullet.png");

	public BulletEntity(World world) {
		super(AstromineEntities.BULLET_ENTITY_TYPE, world);
	}

	public BulletEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public BulletEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
		super(type, x, y, z, world);
	}

	public BulletEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
		super(type, owner, world);
	}

	@Override
	protected void checkBlockCollision() {
		Box box = this.getBoundingBox();
		BlockPos positionA = new BlockPos(box.minX + 0.001D, box.minY + 0.001D, box.minZ + 0.001D);
		BlockPos positionB = new BlockPos(box.maxX - 0.001D, box.maxY - 0.001D, box.maxZ - 0.001D);
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		if (this.world.isRegionLoaded(positionA, positionB)) {
			for (int i = positionA.getX(); i <= positionB.getX(); ++i) {
				for (int j = positionA.getY(); j <= positionB.getY(); ++j) {
					for (int k = positionA.getZ(); k <= positionB.getZ(); ++k) {
						mutable.set(i, j, k);
						BlockState blockState = this.world.getBlockState(mutable);

						try {
							blockState.onEntityCollision(this.world, mutable, this);

							if (blockState.getBlock() instanceof AbstractGlassBlock) {
								this.world.breakBlock(mutable, true);
							}
						} catch (Throwable oops) {
							CrashReport crashReport = CrashReport.create(oops, "Colliding entity with block");
							CrashReportSection crashReportSection = crashReport.addElement("Block being collided with");
							CrashReportSection.addBlockInfo(crashReportSection, mutable, blockState);
							throw new CrashException(crashReport);
						}
					}
				}
			}
		}
	}

	@Override
	protected void onBlockCollision(BlockState state) {
		Block block = state.getBlock();

		if (block instanceof AbstractGlassBlock) {
			this.world.setBlockState(this.getBlockPos(), Blocks.AIR.getDefaultState());
		}
	}

	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}

	public Identifier getTexture() {
		return this.texture;
	}

	public void setTexture(Identifier texture) {
		this.texture = texture;
	}
}
