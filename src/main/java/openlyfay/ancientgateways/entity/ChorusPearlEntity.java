package openlyfay.ancientgateways.entity;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import openlyfay.ancientgateways.AncientGateways;

public class ChorusPearlEntity extends ThrownItemEntity implements Tickable {

    private final int breakChance = 20;
    private final ItemStack payload;
    private final Hand hand;

    public ChorusPearlEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType,world);
        payload = ItemStack.EMPTY;
        hand = Hand.MAIN_HAND;
    }

    public ChorusPearlEntity(World world, LivingEntity owner, ItemStack stack, Hand hand1){
        super(AncientGateways.CHORUS_PEARL_ENTITY, owner, world);
        payload = stack;
        hand = hand1;
    }

    @Environment(EnvType.CLIENT)
    public ChorusPearlEntity(World world, double x, double y, double z){
        super(AncientGateways.CHORUS_PEARL_ENTITY, x, y, z, world);
        payload = null;
        hand = null;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        removeEntity();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        activateBlock(blockHitResult.getBlockPos(),blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!world.isClient && !removed){
            entityHitResult.getEntity().interact((PlayerEntity) getOwner(),hand);
        }
    }

    protected Item getDefaultItem() {
        return AncientGateways.CHORUS_PEARL_ITEM;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isClient && !removed){
            BlockPos blockPos = new BlockPos(getX(),getY(),getZ());
            BlockState block = world.getBlockState(blockPos);
            if (!block.isAir() && !(block.getBlock() instanceof FluidBlock)){
                Direction direction = Direction.DOWN;
                activateBlock(blockPos, new BlockHitResult(getPos(),direction,blockPos,true));
                removeEntity();
            }
        }
    }

    @Override
    public boolean hasNoGravity() {
        return false;
    }

    private void removeEntity(){
        if (!world.isClient && !removed){
            if (random.nextInt(100) > breakChance){
                world.spawnEntity(new ItemEntity(world, getX(), getY(), getZ(), getStack()));
            }
        }
        this.remove();
    }

    private void activateBlock (BlockPos pos, BlockHitResult hitResult){
        if (!world.isClient && !removed){
            world.getBlockState(pos).onUse(world,(PlayerEntity) getOwner(),hand,hitResult);
        }
    }
}
