package openlyfay.ancientgateways.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import openlyfay.ancientgateways.AncientGateways;
import openlyfay.ancientgateways.block.GatewayBlock;
import openlyfay.ancientgateways.block.blockentity.GatewayBlockEntity;
import openlyfay.ancientgateways.item.RegisterItem;

import java.util.Iterator;
import java.util.List;

import static openlyfay.ancientgateways.entity.RegisterEntity.CHORUS_INK_ENTITY;


public class ChorusInkBottleEntity extends ThrownItemEntity {

    private final static int searchRange = AncientGateways.agConfig.chorusInkRadius;
    private final static boolean activatesGateways = AncientGateways.agConfig.chorusInkActivatesGateways;

    public ChorusInkBottleEntity(EntityType<? extends ChorusInkBottleEntity> entityType, World world) {
        super(entityType, world);
    }

    public ChorusInkBottleEntity(World world, LivingEntity owner){
        super(CHORUS_INK_ENTITY, owner, world);
    }

    @Environment(EnvType.CLIENT)
    public ChorusInkBottleEntity(World world, double x, double y, double z){
        super(CHORUS_INK_ENTITY, x, y, z, world);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        BlockEntity blockEntity = world.getBlockEntity(blockHitResult.getBlockPos());
        BlockState blockState = world.getBlockState(blockHitResult.getBlockPos());
        if (activatesGateways && blockEntity instanceof GatewayBlockEntity && blockState.getBlock() instanceof GatewayBlock){
            if(((GatewayBlock) blockState.getBlock()).GatewayStructureIntact(blockHitResult.getBlockPos(),blockState,world,null)){
                ((GatewayBlockEntity) blockEntity).activationCheck();
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        for(int i = 0; i < 128; ++i) {
            world.addParticle(ParticleTypes.PORTAL, getX() + random.nextDouble() * 5.0D - 2.5D,getY() + random.nextDouble() * 5.0D - 2.5D, getZ() + random.nextDouble() * 5.0D - 2.5D,random.nextGaussian(),random.nextGaussian(),random.nextGaussian());
        }
        if (!world.isClient && !this.isRemoved()){
            world.playSound(null,getBlockPos(),SoundEvents.BLOCK_GLASS_BREAK,SoundCategory.NEUTRAL,1.0f, 1.0f);
            LivingEntity target = world.getClosestPlayer(getX(),getY(),getZ(),searchRange,false);
            List<ArmorStandEntity> standList = world.getEntitiesByClass(ArmorStandEntity.class, new Box(getPos().add(searchRange, searchRange*0.5,searchRange),getPos().subtract(searchRange,searchRange*0.5,searchRange)),null);
            LivingEntity targetStand = world.getClosestEntity(standList, TargetPredicate.DEFAULT,null,getX(),getY(),getZ());
            if (target == null){
                target = targetStand;
            }
            List<Entity> payloadList = world.getNonSpectatingEntities(Entity.class, new Box(getPos().add(2.5D,2.5D,2.5D),getPos().add(-2.5D,-2.5D,-2.5D)));
            Iterator payloadIterator = payloadList.iterator();
            Entity payloadEntity;
            while (payloadIterator.hasNext()){
                payloadEntity = (Entity) payloadIterator.next();
                if (target == null || (payloadEntity instanceof PlayerEntity && targetStand == null)){
                    for (int i = 0;i < 16;i++){
                        double d = payloadEntity.getX() + random.nextInt(searchRange*2) - searchRange;
                        double e = Math.min(world.getHeight(), Math.max(payloadEntity.getY() + random.nextInt(searchRange) - searchRange*0.5,1));
                        double f = payloadEntity.getZ() + random.nextInt(searchRange*2) - searchRange;
                        if (!(world.getBlockState(new BlockPos(d,e,f)).isSolidBlock(world,new BlockPos(d,e,f)) || world.getBlockState(new BlockPos(d,e+1,f)).isSolidBlock(world,new BlockPos(d,e+1,f)))){
                            double g = payloadEntity.getX();
                            double h = payloadEntity.getY();
                            double j = payloadEntity.getZ();
                            payloadEntity.teleport(d,e,f);
                            world.playSound(null,g,h,j, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f);
                            break;
                        }
                    }
                }
                else {
                    for (int i = 0;i < 4;i++){
                        LivingEntity target1 = target;
                        if (payloadEntity instanceof PlayerEntity){
                            target1 = targetStand;
                        }
                        double d = target1.getX() + random.nextInt(2)-1;
                        double e = target1.getY() + random.nextInt(2)-1;
                        double f = target1.getZ() + random.nextInt(2)-1;
                        if (!(world.getBlockState(new BlockPos(d,e,f)).isSolidBlock(world,new BlockPos(d,e,f)) || world.getBlockState(new BlockPos(d,e+1,f)).isSolidBlock(world,new BlockPos(d,e+1,f)))){
                            double g = payloadEntity.getX();
                            double h = payloadEntity.getY();
                            double j = payloadEntity.getZ();
                            payloadEntity.teleport(d,e,f);
                            world.playSound(null,g,h,j, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f);
                            break;
                        }
                    }
                }
            }
        }
        this.remove(RemovalReason.DISCARDED);
    }

    protected Item getDefaultItem() {
        return RegisterItem.CHORUS_INK_ITEM;
    }
}
