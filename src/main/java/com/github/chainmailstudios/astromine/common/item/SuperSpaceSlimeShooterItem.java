package com.github.chainmailstudios.astromine.common.item;

import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SuperSpaceSlimeShooterItem extends Item {

    public SuperSpaceSlimeShooterItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20 * 5;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if(!world.isClient) {
            // rayTrace out to find end location of particle beam
            HitResult result = user.rayTrace(10, 0, false);
            Vec3d hitPos = result.getPos();
            Vec3d originPos = user.getPos().add(0, 1, 0);

            // calculate change in slope for particles
            Vec3d change = originPos.subtract(hitPos);
            double distance = Math.sqrt(Math.pow(change.x, 2) + Math.pow(change.y, 2) + Math.pow(change.z, 2));
            change = new Vec3d(change.x / distance, change.y / distance, change.z / distance);

            ServerWorld serverWorld = (ServerWorld) world;
            Vec3d increase = originPos;

            // spawn particles
            for(int i = 0; i < 5; i++) {
                serverWorld.spawnParticles(AstromineParticles.SPACE_SLIME, increase.getX(), increase.getY(), increase.getZ(), 1, 0, 0, 0, 0);
                increase = increase.subtract(change);
            }

            // damage hit entity, if we found one
            // todo: this doesn't actually work, because the rayTrace always returns air
            if(result instanceof EntityHitResult) {
                ((EntityHitResult) result).getEntity().damage(DamageSource.GENERIC, 1.0f);
            }
        }

        super.usageTick(world, user, stack, remainingUseTicks);
    }
}
